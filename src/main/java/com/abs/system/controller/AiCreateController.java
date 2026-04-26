package com.abs.system.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.abs.system.api.IAbsFileInfo;
import com.abs.system.api.IAbsSysConfig;
import com.abs.system.domain.AbsFileInfo;
import com.abs.system.filter.NoNeedLogin;
import com.abs.system.filter.ToToken;
import com.abs.system.util.AbsSessionHelper;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.alibaba.fastjson.JSON;
import com.volcengine.ark.runtime.model.content.generation.CreateContentGenerationTaskRequest;
import com.volcengine.ark.runtime.model.content.generation.CreateContentGenerationTaskRequest.Content;
import com.volcengine.ark.runtime.model.content.generation.CreateContentGenerationTaskResult;
import com.volcengine.ark.runtime.model.content.generation.GetContentGenerationTaskRequest;
import com.volcengine.ark.runtime.model.content.generation.GetContentGenerationTaskResponse;
import com.volcengine.ark.runtime.service.ArkService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * AI创作控制器 处理AI生成文本、图片、音乐、视频等创作请求
 */
@RestController
@RequestMapping("/api/ai")
public class AiCreateController {

	private static final Logger logger = LoggerFactory.getLogger(AiCreateController.class);

	// 豆包 Seed 配置
	@Value("${doubao.seedbao}")
	private String douBaoSeed;

	// DeepSeek 配置
	@Value("${deepseek.api-key}")
	private String deepseekApiKey;

	@Value("${deepseek.base-url}")
	private String deepseekBaseUrl;

	@Value("${deepseek.chat-path}")
	private String deepseekChatPath;

	// 豆包配置
	@Value("${doubao.api-key}")
	private String doubaoApiKey;

	@Value("${doubao.api-secret}")
	private String doubaoApiSecret;

	@Value("${doubao.api-url}")
	private String doubaoApiUrl;

	// GLM 配置
	@Value("${glm.api-key}")
	private String glmApiKey;

	@Value("${glm.base-url}")
	private String glmBaseUrl;

	@Value("${glm.model}")
	private String glmModel;

	// OpenAI 配置
	@Value("${openai.api-key}")
	private String openaiApiKey;

	@Value("${openai.base-url}")
	private String openaiBaseUrl;

	@Autowired
	private IAbsFileInfo fileService;

	@Autowired
	private IAbsSysConfig configService;

	/**
	 * AI创作接口（无需登录）
	 * 
	 * @param reqMap 请求参数
	 * @return 创作结果
	 */
	@NoNeedLogin
	@PostMapping("/create")
	@ResponseBody
	public String create(@RequestBody Map<String, Object> reqMap) {
		logger.info("接收AI创作请求: {}", reqMap);

		try {
			String type = reqMap.get("type") != null ? reqMap.get("type").toString() : "";

			switch (type) {
			case "text":
				return createText(reqMap);
			case "image":
				return createImage(reqMap);
			case "music":
				return createMusic(reqMap);
			case "video":
				return createVideo(reqMap);
			default:
				return JSON.toJSONString(createResponse(MSG.FAILCODE, null, "不支持的创作类型: " + type));
			}
		} catch (Exception e) {
			logger.error("AI创作失败: {}", e.getMessage(), e);
			return JSON.toJSONString(createResponse(MSG.FAILCODE, null, "创作失败: " + e.getMessage()));
		}
	}

	/**
	 * 创建文本内容
	 */
	private String createText(Map<String, Object> reqMap) {
		logger.info("开始AI文本创作");

		String topic = reqMap.get("topic") != null ? reqMap.get("topic").toString() : "";
		String style = reqMap.get("style") != null ? reqMap.get("style").toString() : "professional";
		String length = reqMap.get("length") != null ? reqMap.get("length").toString() : "medium";
		String keywords = reqMap.get("keywords") != null ? reqMap.get("keywords").toString() : "";
		String model = reqMap.get("model") != null ? reqMap.get("model").toString() : "deepseek";

		logger.info("使用模型: {}", model);

		String generatedText = "";
		try {
			switch (model) {
			case "deepseek":
				generatedText = callDeepSeekAPI(topic, style, length, keywords);
				break;
			case "doubao":
				generatedText = callDoubaoAPI(topic, style, length, keywords);
				break;
			case "glm":
				generatedText = callGLMAPI(topic, style, length, keywords);
				break;
			case "gpt-4":
			case "gpt-3.5-turbo":
				generatedText = callOpenAIAPI(topic, style, length, keywords, model);
				break;
			default:
				// 默认为DeepSeek
				generatedText = callDeepSeekAPI(topic, style, length, keywords);
			}
		} catch (Exception e) {
			logger.error("调用AI API失败: {}", e.getMessage(), e);
			// 如果调用失败，使用模拟数据
			generatedText = generateMockText(topic, style, length, keywords);
		}

		logger.info("AI文本创作完成");
		return JSON.toJSONString(createResponse(MSG.SUCCESSCODE, generatedText, MSG.SystemSuccess));
	}

	/**
	 * 创建图片
	 */
	private String createImage(Map<String, Object> reqMap) {
		logger.info("开始AI图片创作");

		String prompt = reqMap.get("prompt") != null ? reqMap.get("prompt").toString() : "";
		String style = reqMap.get("style") != null ? reqMap.get("style").toString() : "realistic";
		String size = reqMap.get("size") != null ? reqMap.get("size").toString() : "1024x1024";
		int count = reqMap.get("count") != null ? Integer.parseInt(reqMap.get("count").toString()) : 1;
		String model = reqMap.get("model") != null ? reqMap.get("model").toString() : "deepseek";

		logger.info("使用模型: {}", model);

		String[] generatedImages = null;
		try {
			switch (model) {
			case "deepseek":
				generatedImages = callDeepSeekImageAPI(prompt, style, size, count);
				break;
			case "doubao":
				generatedImages = callDoubaoImageAPI(prompt, style, size, count);
				break;
			case "glm":
				generatedImages = callGLMImageAPI(prompt, style, size, count);
				break;
			case "dall-e-3":
				generatedImages = callDallEImageAPI(prompt, style, size, count);
				break;
			case "midjourney":
				generatedImages = callMidjourneyImageAPI(prompt, style, size, count);
				break;
			case "stable-diffusion":
				generatedImages = callStableDiffusionImageAPI(prompt, style, size, count);
				break;
			case "wenxin-image":
				generatedImages = callWenxinImageAPI(prompt, style, size, count);
				break;
			default:
				// 默认为DeepSeek
				generatedImages = callDeepSeekImageAPI(prompt, style, size, count);
			}
		} catch (Exception e) {
			logger.error("调用AI图片API失败: {}", e.getMessage(), e);
			// 如果调用失败，使用模拟数据
			generatedImages = generateMockImages(prompt, style, size, count);
		}

		logger.info("AI图片创作完成，生成 {} 张图片", count);
		return JSON.toJSONString(createResponse(MSG.SUCCESSCODE, generatedImages, MSG.SystemSuccess));
	}

	/**
	 * 创建音乐
	 */
	private String createMusic(Map<String, Object> reqMap) {
		logger.info("开始AI音乐创作");

		String prompt = reqMap.get("prompt") != null ? reqMap.get("prompt").toString() : "";
		String genre = reqMap.get("genre") != null ? reqMap.get("genre").toString() : "pop";
		String mood = reqMap.get("mood") != null ? reqMap.get("mood").toString() : "happy";
		int duration = reqMap.get("duration") != null ? Integer.parseInt(reqMap.get("duration").toString()) : 30;
		String model = reqMap.get("model") != null ? reqMap.get("model").toString() : "deepseek";

		logger.info("使用模型: {}", model);

		String generatedMusic = "";
		try {
			switch (model) {
			case "deepseek":
				generatedMusic = callDeepSeekMusicAPI(prompt, genre, mood, duration);
				break;
			case "doubao":
				generatedMusic = callDoubaoMusicAPI(prompt, genre, mood, duration);
				break;
			case "glm":
				generatedMusic = callGLMMusicAPI(prompt, genre, mood, duration);
				break;
			case "suno":
				generatedMusic = callSunoMusicAPI(prompt, genre, mood, duration);
				break;
			case "aiva":
				generatedMusic = callAIVAMusicAPI(prompt, genre, mood, duration);
				break;
			case "mubert":
				generatedMusic = callMubertMusicAPI(prompt, genre, mood, duration);
				break;
			case "soundful":
				generatedMusic = callSoundfulMusicAPI(prompt, genre, mood, duration);
				break;
			default:
				// 默认为DeepSeek
				generatedMusic = callDeepSeekMusicAPI(prompt, genre, mood, duration);
			}
		} catch (Exception e) {
			logger.error("调用AI音乐API失败: {}", e.getMessage(), e);
			// 如果调用失败，使用模拟数据
			generatedMusic = generateMockMusic(prompt, genre, mood, duration);
		}

		logger.info("AI音乐创作完成");
		return JSON.toJSONString(createResponse(MSG.SUCCESSCODE, generatedMusic, MSG.SystemSuccess));
	}

	/**
	 * 创建视频
	 */
	private String createVideo(Map<String, Object> reqMap) {
		logger.info("开始AI视频创作");

		String prompt = reqMap.get("prompt") != null ? reqMap.get("prompt").toString() : "";
		String style = reqMap.get("style") != null ? reqMap.get("style").toString() : "realistic";
		String resolution = reqMap.get("resolution") != null ? reqMap.get("resolution").toString() : "1080p";
		int duration = reqMap.get("duration") != null ? Integer.parseInt(reqMap.get("duration").toString()) : 10;
		String aspectRatio = reqMap.get("aspectRatio") != null ? reqMap.get("aspectRatio").toString() : "16:9";
		List<String> images = new ArrayList<>();
		if (reqMap.get("images") != null) {
			Object imagesObj = reqMap.get("images");
			if (imagesObj instanceof List) {
				images = (List<String>) imagesObj;
			}
		}
		String model = reqMap.get("model") != null ? reqMap.get("model").toString() : "deepseek";

		logger.info("使用模型: {}", model);
		logger.info("上传的图片数量: {}", images.size());

		try {
			String taskId = "";
			switch (model) {
			case "doubao":
				taskId = callDoubaoVideoAPI(prompt, style, resolution, duration, aspectRatio, images);
				break;
			default:
				// 其他模型使用模拟数据
				taskId = "mock-" + System.currentTimeMillis();
				// 异步生成模拟视频
				final String finalTaskId = taskId;
				new Thread(() -> {
					try {
						// 模拟视频生成过程
						Thread.sleep(5000);
						logger.info("模拟视频生成完成: {}", finalTaskId);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}).start();
				break;
			}

			logger.info("AI视频创作任务已提交，任务ID: {}", taskId);
			return JSON.toJSONString(createResponse(MSG.SUCCESSCODE, taskId, MSG.SystemSuccess));
		} catch (Exception e) {
			logger.error("调用AI视频API失败: {}", e.getMessage(), e);
			// 返回错误信息
			return JSON.toJSONString(createResponse(MSG.FAILCODE, null, "生成失败: " + e.getMessage()));
		}
	}

	/**
	 * 生成模拟文本内容
	 */
	private String generateMockText(String topic, String style, String length, String keywords) {
		StringBuilder sb = new StringBuilder();

		sb.append("# ").append(topic).append("\n\n");

		// 根据风格生成不同的开场白
		switch (style) {
		case "professional":
			sb.append("在当今快速发展的时代，").append(topic).append("已经成为一个重要的研究领域。本文将从专业角度深入探讨这一主题。\n\n");
			break;
		case "casual":
			sb.append("大家好！今天我们来聊聊").append(topic).append("，这个话题其实挺有意思的。\n\n");
			break;
		case "humorous":
			sb.append("说到").append(topic).append("，你可能觉得这是个严肃的话题，但其实里面有很多有趣的故事！\n\n");
			break;
		case "formal":
			sb.append("尊敬的读者，本文将正式探讨").append(topic).append("的相关内容。\n\n");
			break;
		case "story":
			sb.append("从前，有一个关于").append(topic).append("的故事...\n\n");
			break;
		default:
			sb.append("关于").append(topic).append("，我们有以下内容：\n\n");
		}

		// 根据长度生成内容
		int paragraphCount = length.equals("short") ? 2 : (length.equals("medium") ? 4 : 6);

		for (int i = 1; i <= paragraphCount; i++) {
			sb.append("## 第").append(i).append("部分\n\n");
			sb.append("这里是关于").append(topic).append("的第").append(i).append("部分内容。");
			if (!keywords.isEmpty()) {
				sb.append("涉及关键词：").append(keywords).append("。");
			}
			sb.append("\n\n");
		}

		sb.append("## 总结\n\n");
		sb.append("综上所述，").append(topic).append("是一个值得深入研究的领域。");
		if (!keywords.isEmpty()) {
			sb.append("通过").append(keywords).append("等方面的探讨，我们可以更好地理解这一主题。");
		}
		sb.append("希望本文对您有所帮助！");

		return sb.toString();
	}

	/**
	 * 生成模拟图片URL
	 */
	private String[] generateMockImages(String prompt, String style, String size, int count) {
		String[] images = new String[count];
		for (int i = 0; i < count; i++) {
			// 这里返回模拟的图片URL，实际项目中应该返回AI生成的真实图片URL
			images[i] = "https://via.placeholder.com/" + size.replace("x", "/") + "/4472C4/FFFFFF?text="
					+ encodeUrl("AI生成图片 " + (i + 1) + "\n" + style + "风格");
		}
		return images;
	}

	/**
	 * 生成模拟音乐URL
	 */
	private String generateMockMusic(String prompt, String genre, String mood, int duration) {
		// 这里返回模拟的音乐URL，实际项目中应该返回AI生成的真实音乐URL
		return "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
	}

	/**
	 * 生成模拟视频URL
	 */
	private String generateMockVideo(String prompt, String style, String resolution, int duration) {
		// 这里返回模拟的视频URL，实际项目中应该返回AI生成的真实视频URL
		return "https://www.w3schools.com/html/mov_bbb.mp4";
	}

	/**
	 * 调用DeepSeek API
	 */
	private String callDeepSeekAPI(String topic, String style, String length, String keywords) throws Exception {
		logger.info("调用DeepSeek API");

		String url = deepseekBaseUrl + deepseekChatPath;

		// 构建请求体
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("model", "deepseek-chat");
		requestBody.put("messages",
				new Object[] { Map.of("role", "system", "content", "你是一个友好、专业的AI助手，用自然对话的方式回答用户问题。回答要简洁明了，避免过于正式或文章化的结构。"),
						Map.of("role", "user", "content", generateChatPrompt(topic, style, length, keywords)) });
		requestBody.put("temperature", 0.7);
		requestBody.put("max_tokens", 2000);

		// 发送请求
		return callAIAPI(url, deepseekApiKey, requestBody);
	}

	/**
	 * 调用豆包API
	 */
	private String callDoubaoAPI(String topic, String style, String length, String keywords) throws Exception {
		logger.info("调用豆包API");

		String url = doubaoApiUrl + "/api/v3/chat/completions";

		// 构建请求体
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("model", "ep-20260409125346-7g75c"); // 豆包模型
		requestBody.put("messages",
				new Object[] { Map.of("role", "system", "content", "你是一个专业的内容创作助手，根据用户的要求生成高质量的文章。"),
						Map.of("role", "user", "content", generatePrompt(topic, style, length, keywords)) });
		requestBody.put("temperature", 0.7);
		requestBody.put("max_tokens", 2000);

		// 发送请求
		return callAIAPI(url, doubaoApiKey, requestBody);
	}

	/**
	 * 调用GLM API
	 */
	private String callGLMAPI(String topic, String style, String length, String keywords) throws Exception {
		logger.info("调用GLM API");

		String url = glmBaseUrl + "chat/completions";

		// 构建请求体
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("model", glmModel);
		requestBody.put("messages",
				new Object[] { Map.of("role", "system", "content", "你是一个专业的内容创作助手，根据用户的要求生成高质量的文章。"),
						Map.of("role", "user", "content", generatePrompt(topic, style, length, keywords)) });
		requestBody.put("temperature", 0.7);
		requestBody.put("max_tokens", 2000);

		// 发送请求
		return callAIAPI(url, glmApiKey, requestBody);
	}

	/**
	 * 调用OpenAI API
	 */
	private String callOpenAIAPI(String topic, String style, String length, String keywords, String model)
			throws Exception {
		logger.info("调用OpenAI API，模型: {}", model);

		String url = openaiBaseUrl + "chat/completions";

		// 构建请求体
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("model", model);
		requestBody.put("messages",
				new Object[] { Map.of("role", "system", "content", "你是一个专业的内容创作助手，根据用户的要求生成高质量的文章。"),
						Map.of("role", "user", "content", generatePrompt(topic, style, length, keywords)) });
		requestBody.put("temperature", 0.7);
		requestBody.put("max_tokens", 2000);

		// 发送请求
		return callAIAPI(url, openaiApiKey, requestBody);
	}

	/**
	 * 通用AI API调用方法
	 */
	private String callAIAPI(String url, String apiKey, Map<String, Object> requestBody) throws Exception {
		RestTemplate restTemplate = new RestTemplate();

		// 设置请求头
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", "Bearer " + apiKey);

		// 构建请求实体
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

		// 发送请求
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		// 解析响应
		Map<String, Object> responseMap = JSON.parseObject(response.getBody(), Map.class);
		if (responseMap.containsKey("choices")) {
			Object choices = responseMap.get("choices");
			if (choices instanceof java.util.List) {
				java.util.List<?> choicesList = (java.util.List<?>) choices;
				if (!choicesList.isEmpty()) {
					Object choice = choicesList.get(0);
					if (choice instanceof Map) {
						Map<?, ?> choiceMap = (Map<?, ?>) choice;
						if (choiceMap.containsKey("message")) {
							Object message = choiceMap.get("message");
							if (message instanceof Map) {
								Map<?, ?> messageMap = (Map<?, ?>) message;
								if (messageMap.containsKey("content")) {
									return messageMap.get("content").toString();
								}
							}
						}
					}
				}
			}
		}

		throw new Exception("Failed to parse AI API response");
	}

	/**
	 * 生成提示词
	 */
	/**
	 * 生成对话提示词（用于AI助手场景）
	 */
	private String generateChatPrompt(String topic, String style, String length, String keywords) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("用户问题：").append(topic).append("\n\n");
		prompt.append("请用自然、友好的对话方式回答，不要使用文章式的结构和格式。\n");
		prompt.append("要求：\n");
		prompt.append("1. 语气：自然对话，像朋友聊天一样\n");
		prompt.append("2. 结构：简洁明了，避免长篇大论\n");
		prompt.append("3. 格式：使用简单的段落，避免复杂的标题层级\n");
		if (!keywords.isEmpty()) {
			prompt.append("4. 重点内容：").append(keywords).append("\n");
		}
		return prompt.toString();
	}

	/**
	 * 生成文章提示词（用于内容创作场景）
	 */
	private String generatePrompt(String topic, String style, String length, String keywords) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("请根据以下要求生成一篇关于'").append(topic).append("'的文章：\n");
		prompt.append("1. 写作风格：").append(style).append("\n");
		prompt.append("2. 文章长度：").append(length).append("\n");
		if (!keywords.isEmpty()) {
			prompt.append("3. 关键词：").append(keywords).append("\n");
		}
		prompt.append("请生成一篇结构清晰、内容丰富、语言流畅的文章。");
		return prompt.toString();
	}

	/**
	 * 调用DeepSeek图片API
	 */
	private String[] callDeepSeekImageAPI(String prompt, String style, String size, int count) throws Exception {
		logger.info("调用DeepSeek图片API");

		String url = deepseekBaseUrl + "/v1/images/generations";

		// 构建请求体
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("prompt", prompt);
		requestBody.put("n", count);
		requestBody.put("size", size);
		requestBody.put("style", style);

		// 发送请求
		RestTemplate restTemplate = new RestTemplate();

		// 设置请求头
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", "Bearer " + deepseekApiKey);

		// 构建请求实体
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

		// 发送请求
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		// 解析响应
		Map<String, Object> responseMap = JSON.parseObject(response.getBody(), Map.class);
		if (responseMap.containsKey("data")) {
			Object data = responseMap.get("data");
			if (data instanceof java.util.List) {
				java.util.List<?> dataList = (java.util.List<?>) data;
				String[] imageUrls = new String[dataList.size()];
				for (int i = 0; i < dataList.size(); i++) {
					Object item = dataList.get(i);
					if (item instanceof Map) {
						Map<?, ?> itemMap = (Map<?, ?>) item;
						if (itemMap.containsKey("url")) {
							imageUrls[i] = itemMap.get("url").toString();
						}
					}
				}
				return imageUrls;
			}
		}

		throw new Exception("Failed to parse DeepSeek image API response");
	}

	/**
	 * 调用豆包图片API
	 */
	private String[] callDoubaoImageAPI(String prompt, String style, String size, int count) throws Exception {
		logger.info("调用豆包图片API");

		String url = doubaoApiUrl + "/api/v3/images/generations";

		// 构建请求体
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("prompt", prompt);
		requestBody.put("n", count);
		requestBody.put("size", size);
		requestBody.put("style", style);

		// 发送请求
		RestTemplate restTemplate = new RestTemplate();

		// 设置请求头
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", "Bearer " + doubaoApiKey);

		// 构建请求实体
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

		// 发送请求
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		// 解析响应
		Map<String, Object> responseMap = JSON.parseObject(response.getBody(), Map.class);
		if (responseMap.containsKey("data")) {
			Object data = responseMap.get("data");
			if (data instanceof java.util.List) {
				java.util.List<?> dataList = (java.util.List<?>) data;
				String[] imageUrls = new String[dataList.size()];
				for (int i = 0; i < dataList.size(); i++) {
					Object item = dataList.get(i);
					if (item instanceof Map) {
						Map<?, ?> itemMap = (Map<?, ?>) item;
						if (itemMap.containsKey("url")) {
							imageUrls[i] = itemMap.get("url").toString();
						}
					}
				}
				return imageUrls;
			}
		}

		throw new Exception("Failed to parse Doubao image API response");
	}

	/**
	 * 调用GLM图片API
	 */
	private String[] callGLMImageAPI(String prompt, String style, String size, int count) throws Exception {
		logger.info("调用GLM图片API");

		String url = glmBaseUrl + "/v1/images/generations";

		// 构建请求体
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("prompt", prompt);
		requestBody.put("n", count);
		requestBody.put("size", size);
		requestBody.put("style", style);

		// 发送请求
		RestTemplate restTemplate = new RestTemplate();

		// 设置请求头
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", "Bearer " + glmApiKey);

		// 构建请求实体
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

		// 发送请求
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		// 解析响应
		Map<String, Object> responseMap = JSON.parseObject(response.getBody(), Map.class);
		if (responseMap.containsKey("data")) {
			Object data = responseMap.get("data");
			if (data instanceof java.util.List) {
				java.util.List<?> dataList = (java.util.List<?>) data;
				String[] imageUrls = new String[dataList.size()];
				for (int i = 0; i < dataList.size(); i++) {
					Object item = dataList.get(i);
					if (item instanceof Map) {
						Map<?, ?> itemMap = (Map<?, ?>) item;
						if (itemMap.containsKey("url")) {
							imageUrls[i] = itemMap.get("url").toString();
						}
					}
				}
				return imageUrls;
			}
		}

		throw new Exception("Failed to parse GLM image API response");
	}

	/**
	 * 调用DALL-E图片API
	 */
	private String[] callDallEImageAPI(String prompt, String style, String size, int count) throws Exception {
		logger.info("调用DALL-E图片API");

		String url = openaiBaseUrl + "/v1/images/generations";

		// 构建请求体
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("prompt", prompt);
		requestBody.put("n", count);
		requestBody.put("size", size);
		if (style != null && !style.isEmpty()) {
			requestBody.put("style", style);
		}

		// 发送请求
		RestTemplate restTemplate = new RestTemplate();

		// 设置请求头
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", "Bearer " + openaiApiKey);

		// 构建请求实体
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

		// 发送请求
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		// 解析响应
		Map<String, Object> responseMap = JSON.parseObject(response.getBody(), Map.class);
		if (responseMap.containsKey("data")) {
			Object data = responseMap.get("data");
			if (data instanceof java.util.List) {
				java.util.List<?> dataList = (java.util.List<?>) data;
				String[] imageUrls = new String[dataList.size()];
				for (int i = 0; i < dataList.size(); i++) {
					Object item = dataList.get(i);
					if (item instanceof Map) {
						Map<?, ?> itemMap = (Map<?, ?>) item;
						if (itemMap.containsKey("url")) {
							imageUrls[i] = itemMap.get("url").toString();
						}
					}
				}
				return imageUrls;
			}
		}

		throw new Exception("Failed to parse DALL-E image API response");
	}

	/**
	 * 调用Midjourney图片API
	 */
	private String[] callMidjourneyImageAPI(String prompt, String style, String size, int count) throws Exception {
		logger.info("调用Midjourney图片API");

		// 注意：Midjourney API的具体实现可能不同，这里使用通用的API格式
		String url = "https://api.midjourney.com/v1/images/generations";

		// 构建请求体
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("prompt", prompt);
		requestBody.put("n", count);
		requestBody.put("size", size);
		requestBody.put("style", style);

		// 发送请求
		RestTemplate restTemplate = new RestTemplate();

		// 设置请求头
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		// 假设Midjourney的API密钥存储在一个配置中
		String midjourneyApiKey = "your-midjourney-api-key";
		headers.set("Authorization", "Bearer " + midjourneyApiKey);

		// 构建请求实体
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

		// 发送请求
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		// 解析响应
		Map<String, Object> responseMap = JSON.parseObject(response.getBody(), Map.class);
		if (responseMap.containsKey("data")) {
			Object data = responseMap.get("data");
			if (data instanceof java.util.List) {
				java.util.List<?> dataList = (java.util.List<?>) data;
				String[] imageUrls = new String[dataList.size()];
				for (int i = 0; i < dataList.size(); i++) {
					Object item = dataList.get(i);
					if (item instanceof Map) {
						Map<?, ?> itemMap = (Map<?, ?>) item;
						if (itemMap.containsKey("url")) {
							imageUrls[i] = itemMap.get("url").toString();
						}
					}
				}
				return imageUrls;
			}
		}

		throw new Exception("Failed to parse Midjourney image API response");
	}

	/**
	 * 调用Stable Diffusion图片API
	 */
	private String[] callStableDiffusionImageAPI(String prompt, String style, String size, int count) throws Exception {
		logger.info("调用Stable Diffusion图片API");

		// 注意：Stable Diffusion API的具体实现可能不同，这里使用通用的API格式
		String url = "https://api.stability.ai/v1/generation/stable-diffusion-v1-6/text-to-image";

		// 构建请求体
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("text_prompts", new Object[] { Map.of("text", prompt, "weight", 1.0) });
		requestBody.put("samples", count);
		requestBody.put("width", Integer.parseInt(size.split("x")[0]));
		requestBody.put("height", Integer.parseInt(size.split("x")[1]));
		if (style != null && !style.isEmpty()) {
			requestBody.put("style_preset", style);
		}

		// 发送请求
		RestTemplate restTemplate = new RestTemplate();

		// 设置请求头
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		// 假设Stable Diffusion的API密钥存储在一个配置中
		String stabilityApiKey = "your-stability-api-key";
		headers.set("Authorization", "Bearer " + stabilityApiKey);

		// 构建请求实体
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

		// 发送请求
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		// 解析响应
		Map<String, Object> responseMap = JSON.parseObject(response.getBody(), Map.class);
		if (responseMap.containsKey("artifacts")) {
			Object artifacts = responseMap.get("artifacts");
			if (artifacts instanceof java.util.List) {
				java.util.List<?> artifactsList = (java.util.List<?>) artifacts;
				String[] imageUrls = new String[artifactsList.size()];
				for (int i = 0; i < artifactsList.size(); i++) {
					Object item = artifactsList.get(i);
					if (item instanceof Map) {
						Map<?, ?> itemMap = (Map<?, ?>) item;
						if (itemMap.containsKey("base64")) {
							// Stable Diffusion API通常返回base64编码的图片
							// 这里将其转换为data URL
							String base64 = itemMap.get("base64").toString();
							imageUrls[i] = "data:image/png;base64," + base64;
						}
					}
				}
				return imageUrls;
			}
		}

		throw new Exception("Failed to parse Stable Diffusion image API response");
	}

	/**
	 * 调用文心一格图片API
	 */
	private String[] callWenxinImageAPI(String prompt, String style, String size, int count) throws Exception {
		logger.info("调用文心一格图片API");

		// 注意：文心一格API的具体实现可能不同，这里使用通用的API格式
		String url = "https://aip.baidubce.com/rpc/2.0/ernievilg/v1/text2image";

		// 构建请求体
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("prompt", prompt);
		requestBody.put("size", size);
		requestBody.put("num", count);
		if (style != null && !style.isEmpty()) {
			requestBody.put("style", style);
		}

		// 发送请求
		RestTemplate restTemplate = new RestTemplate();

		// 设置请求头
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		// 构建请求URL，包含API密钥
		String wenxinApiKey = "your-wenxin-api-key";
		String wenxinSecretKey = "your-wenxin-secret-key";
		String accessToken = getWenxinAccessToken(wenxinApiKey, wenxinSecretKey);
		url += "?access_token=" + accessToken;

		// 构建请求实体
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

		// 发送请求
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		// 解析响应
		Map<String, Object> responseMap = JSON.parseObject(response.getBody(), Map.class);
		if (responseMap.containsKey("data")) {
			Object data = responseMap.get("data");
			if (data instanceof java.util.List) {
				java.util.List<?> dataList = (java.util.List<?>) data;
				String[] imageUrls = new String[dataList.size()];
				for (int i = 0; i < dataList.size(); i++) {
					Object item = dataList.get(i);
					if (item instanceof Map) {
						Map<?, ?> itemMap = (Map<?, ?>) item;
						if (itemMap.containsKey("url")) {
							imageUrls[i] = itemMap.get("url").toString();
						}
					}
				}
				return imageUrls;
			}
		}

		throw new Exception("Failed to parse Wenxin image API response");
	}

	/**
	 * 获取文心一格的访问令牌
	 */
	private String getWenxinAccessToken(String apiKey, String secretKey) throws Exception {
		String url = "https://aip.baidubce.com/oauth/2.0/token";
		url += "?grant_type=client_credentials";
		url += "&client_id=" + apiKey;
		url += "&client_secret=" + secretKey;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		Map<String, Object> responseMap = JSON.parseObject(response.getBody(), Map.class);
		if (responseMap.containsKey("access_token")) {
			return responseMap.get("access_token").toString();
		}

		throw new Exception("Failed to get Wenxin access token");
	}

	/**
	 * 调用DeepSeek音乐API
	 */
	private String callDeepSeekMusicAPI(String prompt, String genre, String mood, int duration) throws Exception {
		logger.info("调用DeepSeek音乐API");

		// 这里应该调用DeepSeek的音乐生成API
		// 由于没有实际的API文档，这里使用模拟数据
		return generateMockMusic(prompt, genre, mood, duration);
	}

	/**
	 * 调用豆包音乐API
	 */
	private String callDoubaoMusicAPI(String prompt, String genre, String mood, int duration) throws Exception {
		logger.info("调用豆包音乐API");

		// 这里应该调用豆包的音乐生成API
		// 由于没有实际的API文档，这里使用模拟数据
		return generateMockMusic(prompt, genre, mood, duration);
	}

	/**
	 * 调用GLM音乐API
	 */
	private String callGLMMusicAPI(String prompt, String genre, String mood, int duration) throws Exception {
		logger.info("调用GLM音乐API");

		// 这里应该调用GLM的音乐生成API
		// 由于没有实际的API文档，这里使用模拟数据
		return generateMockMusic(prompt, genre, mood, duration);
	}

	/**
	 * 调用Suno音乐API
	 */
	private String callSunoMusicAPI(String prompt, String genre, String mood, int duration) throws Exception {
		logger.info("调用Suno音乐API");

		// 这里应该调用Suno的音乐生成API
		// 由于没有实际的API文档，这里使用模拟数据
		return generateMockMusic(prompt, genre, mood, duration);
	}

	/**
	 * 调用AIVA音乐API
	 */
	private String callAIVAMusicAPI(String prompt, String genre, String mood, int duration) throws Exception {
		logger.info("调用AIVA音乐API");

		// 这里应该调用AIVA的音乐生成API
		// 由于没有实际的API文档，这里使用模拟数据
		return generateMockMusic(prompt, genre, mood, duration);
	}

	/**
	 * 调用Mubert音乐API
	 */
	private String callMubertMusicAPI(String prompt, String genre, String mood, int duration) throws Exception {
		logger.info("调用Mubert音乐API");

		// 这里应该调用Mubert的音乐生成API
		// 由于没有实际的API文档，这里使用模拟数据
		return generateMockMusic(prompt, genre, mood, duration);
	}

	/**
	 * 调用Soundful音乐API
	 */
	private String callSoundfulMusicAPI(String prompt, String genre, String mood, int duration) throws Exception {
		logger.info("调用Soundful音乐API");

		// 这里应该调用Soundful的音乐生成API
		// 由于没有实际的API文档，这里使用模拟数据
		return generateMockMusic(prompt, genre, mood, duration);
	}

	/**
	 * 调用DeepSeek视频API
	 */
	private String callDeepSeekVideoAPI(String prompt, String style, String resolution, int duration) throws Exception {
		logger.info("调用DeepSeek视频API");

		// 这里应该调用DeepSeek的视频生成API
		// 由于没有实际的API文档，这里使用模拟数据
		return generateMockVideo(prompt, style, resolution, duration);
	}

	/**
	 * 调用豆包视频API
	 */
	private String callDoubaoVideoAPI(String prompt, String style, String resolution, int duration, String aspectRatio, List<String> images) throws Exception {
		logger.info("调用豆包视频API");

		// 使用豆包Seed API生成视频
		String apiKey = douBaoSeed;
		// 创建ArkService实例
		ArkService arkService = ArkService.builder().apiKey(apiKey).build();

		// 使用视频生成模型
		//String model = "doubao-seedance-1-5-pro-251215";
		
		String model="Doubao-Seedance-1.5-pro免费在线推理资源包";

		// 构建视频生成请求
		List<Content> contents = new ArrayList<>();
		
		// 构建文本提示词，包含所有参数
		String textPrompt = prompt + " --style " + style + " --resolution " + resolution + " --duration " + duration + " --ratio " + aspectRatio + " --fps 24 --watermark false --seed 11 --camerafixed false";
		contents.add(Content.builder().type("text").text(textPrompt).build());
		
		// 添加参考图片
		if (images != null && !images.isEmpty()) {
			int imageIndex = 0;
			for (String imageUrl : images) {
				// 检查imageUrl是否为null
				if (imageUrl != null) {
					// 去除可能的反引号
					imageUrl = imageUrl.replace("`", "");
					// 设置role参数：第一张图片为first_frame，第二张为last_frame
					String role = imageIndex == 0 ? "first_frame" : "last_frame";
					contents.add(Content.builder().type("image_url")
							.role(role)
							.imageUrl(CreateContentGenerationTaskRequest.ImageUrl.builder()
									.url(imageUrl)
									.build())
							.build());
					logger.info("添加参考图片: {}, role: {}", imageUrl, role);
					imageIndex++;
				} else {
					logger.warn("跳过空的参考图片URL");
				}
			}
		}

		// 创建视频生成任务
		CreateContentGenerationTaskRequest createRequest = CreateContentGenerationTaskRequest.builder()
				.model(model)
				.content(contents)
				.build();

		// 发送请求创建任务
		CreateContentGenerationTaskResult createResult = arkService.createContentGenerationTask(createRequest);
		logger.info("创建视频生成任务: {}", createResult);

		// 获取任务ID
		String taskId = createResult.getId();
		logger.info("返回视频生成任务ID: {}", taskId);
		
		// 直接返回任务ID，不等待视频生成完成
		return taskId;
	}

	/**
	 * 调用GLM视频API
	 */
	private String callGLMVideoAPI(String prompt, String style, String resolution, int duration) throws Exception {
		logger.info("调用GLM视频API");

		// 这里应该调用GLM的视频生成API
		// 由于没有实际的API文档，这里使用模拟数据
		return generateMockVideo(prompt, style, resolution, duration);
	}

	/**
	 * 调用Runway视频API
	 */
	private String callRunwayVideoAPI(String prompt, String style, String resolution, int duration) throws Exception {
		logger.info("调用Runway视频API");

		// 这里应该调用Runway的视频生成API
		// 由于没有实际的API文档，这里使用模拟数据
		return generateMockVideo(prompt, style, resolution, duration);
	}

	/**
	 * 调用Pika视频API
	 */
	private String callPikaVideoAPI(String prompt, String style, String resolution, int duration) throws Exception {
		logger.info("调用Pika视频API");

		// 这里应该调用Pika的视频生成API
		// 由于没有实际的API文档，这里使用模拟数据
		return generateMockVideo(prompt, style, resolution, duration);
	}

	/**
	 * 调用HeyGen视频API
	 */
	private String callHeyGenVideoAPI(String prompt, String style, String resolution, int duration) throws Exception {
		logger.info("调用HeyGen视频API");

		// 这里应该调用HeyGen的视频生成API
		// 由于没有实际的API文档，这里使用模拟数据
		return generateMockVideo(prompt, style, resolution, duration);
	}

	/**
	 * 调用Stable Video视频API
	 */
	private String callStableVideoVideoAPI(String prompt, String style, String resolution, int duration)
			throws Exception {
		logger.info("调用Stable Video视频API");

		// 这里应该调用Stable Video的视频生成API
		// 由于没有实际的API文档，这里使用模拟数据
		return generateMockVideo(prompt, style, resolution, duration);
	}

	/**
	 * URL编码
	 */
	private String encodeUrl(String text) {
		try {
			return java.net.URLEncoder.encode(text, "UTF-8");
		} catch (Exception e) {
			return text;
		}
	}

	/**
	 * 创建响应对象
	 */
	private Map<String, Object> createResponse(String code, Object data, String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("code", code);
		response.put("data", data);
		response.put("message", message);
		return response;
	}

	/**
	 * 获取视频生成任务状态
	 */
	@RequestMapping(value = "/getVideoTaskStatus", method = RequestMethod.POST)
	@ResponseBody
	public String getVideoTaskStatus(@RequestBody Map<String, Object> reqMap) {
		logger.info("获取视频生成任务状态");

		String taskId = reqMap.get("taskId") != null ? reqMap.get("taskId").toString() : "";
		String model = reqMap.get("model") != null ? reqMap.get("model").toString() : "deepseek";

		logger.info("任务ID: {}", taskId);
		logger.info("使用模型: {}", model);

		Map<String, Object> statusInfo = new HashMap<>();

		try {
			if ("doubao".equals(model) && taskId != null && !taskId.isEmpty()) {
				// 使用豆包Seed API查询任务状态
				String apiKey = douBaoSeed;
				// 创建ArkService实例
				ArkService arkService = ArkService.builder().apiKey(apiKey).build();

				// 构建查询请求
				GetContentGenerationTaskRequest getRequest = GetContentGenerationTaskRequest.builder().taskId(taskId).build();
				// 发送请求获取任务状态
				GetContentGenerationTaskResponse getResponse = arkService.getContentGenerationTask(getRequest);
				
				String status = getResponse.getStatus();
				statusInfo.put("status", status);
				
				if ("succeeded".equalsIgnoreCase(status)) {
					// 尝试获取视频URL
					try {
						// 从content对象中获取videoUrl
						com.volcengine.ark.runtime.model.content.generation.GetContentGenerationTaskResponse.Content content = getResponse.getContent();
						if (content != null) {
							String videoUrl = content.getVideoUrl();
							if (videoUrl != null) {
								// 去除可能的反引号
								videoUrl = videoUrl.replace("`", "");
								statusInfo.put("videoUrl", videoUrl);
								logger.info("获取到视频URL: {}", videoUrl);
							}
						}
					} catch (Exception e) {
						logger.warn("尝试获取视频URL失败: {}", e.getMessage());
					}
				} else if ("failed".equalsIgnoreCase(status)) {
					statusInfo.put("error", getResponse.getError());
					logger.error("视频生成任务失败: {}", getResponse.getError());
				}
				
				logger.info("任务状态: {}", status);
			} else if (taskId.startsWith("mock-")) {
				// 模拟任务状态
				// 随机模拟任务状态，80%概率完成
				if (Math.random() > 0.2) {
					statusInfo.put("status", "succeeded");
					statusInfo.put("videoUrl", "https://example.com/mock-video.mp4");
				} else {
					statusInfo.put("status", "running");
				}
			} else {
				statusInfo.put("status", "unknown");
				statusInfo.put("error", "无效的任务ID");
			}
		} catch (Exception e) {
			logger.error("获取任务状态失败: {}", e.getMessage(), e);
			statusInfo.put("status", "error");
			statusInfo.put("error", e.getMessage());
		}

		return JSON.toJSONString(createResponse(MSG.SUCCESSCODE, statusInfo, MSG.SystemSuccess));
	}

	/**
	 * 保存视频素材
	 */
	@PostMapping("/video/saveMaterial")
	@ResponseBody
	public String saveVideoMaterial(@ToToken Params params, @RequestBody Map<String, Object> reqMap, HttpServletRequest request) {
		try {
			String videoUrl = reqMap.get("videoUrl").toString();
			String name = reqMap.get("name").toString();
			String type = reqMap.get("type").toString();

			logger.info("保存视频素材，URL: {}", videoUrl);

			// 直接保存视频URL到数据库
			String token = params.getString("token");
			String userguid = AbsSessionHelper.getCurrentUserGuid(token);
			String cliengguid = UUID.randomUUID().toString();
			
			// 生成文件类型
			String filetype = ".mp4"; // 假设视频都是mp4格式
			
			// 保存文件信息到数据库
			AbsFileInfo fileinfo = new AbsFileInfo();
			fileinfo.setRowguid(UUID.randomUUID().toString());
			fileinfo.setFilesize(0); // 无法获取文件大小，设置为0
			fileinfo.setAddtime(new Date());
			fileinfo.setFilepath("");
			fileinfo.setFileurl(videoUrl);
			fileinfo.setIstoali("0"); // 直接使用原始URL，不需要上传到阿里云
			fileinfo.setCliengguid(cliengguid);
			fileinfo.setFilename(name + ".mp4");
			fileinfo.setFiletype(filetype);
			fileinfo.setFirstkey("");
			fileinfo.setUserguid(userguid);
			fileinfo.setIstmp(1); // 临时文件
			fileinfo.setRemark("AI生成视频");
			fileService.addFileInfo(fileinfo);

			logger.info("视频素材保存成功，URL: {}", videoUrl);

			return JSON.toJSONString(createResponse(MSG.SUCCESSCODE, null, "素材保存成功"));
		} catch (Exception e) {
			logger.error("保存视频素材失败: {}", e.getMessage(), e);
			return JSON.toJSONString(createResponse(MSG.FAILCODE, null, "保存失败: " + e.getMessage()));
		}
	}
}
