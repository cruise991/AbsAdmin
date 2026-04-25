package com.abs.system;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.volcengine.ark.runtime.model.content.generation.CreateContentGenerationTaskRequest;
import com.volcengine.ark.runtime.model.content.generation.CreateContentGenerationTaskRequest.Content;
import com.volcengine.ark.runtime.model.content.generation.CreateContentGenerationTaskResult;
import com.volcengine.ark.runtime.model.content.generation.GetContentGenerationTaskRequest;
import com.volcengine.ark.runtime.model.content.generation.GetContentGenerationTaskResponse;
import com.volcengine.ark.runtime.service.ArkService;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;

public class ContentGenerationTaskExample {
	// 请确保您已将 API Key 存储在环境变量 ARK_API_KEY 中
	// 初始化Ark客户端，从环境变量中读取您的API Key
	static String apiKey = System.getenv("ARK_API_KEY");
	static ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
	static Dispatcher dispatcher = new Dispatcher();
	static ArkService service = ArkService.builder().dispatcher(dispatcher).connectionPool(connectionPool)
			.apiKey(apiKey).build();

	public static void main(String[] args) {
		String model = "doubao-seedance-1-5-pro-251215"; // 模型 Model ID 已为您填入

		System.out.println("----- create request -----");
		List<Content> contents = new ArrayList<>();

		// 图生视频功能
		// 文本提示词与参数组合
		contents.add(Content.builder().type("text")
				.text("无人机以极快速度穿越复杂障碍或自然奇观，带来沉浸式飞行体验  --duration 5 --camerafixed false --watermark true").build());
		// 首帧图片 (若仅需使用文本生成视频功能，可将此部分内容进行注释处理。)
		contents.add(Content.builder().type("image_url")
				.imageUrl(CreateContentGenerationTaskRequest.ImageUrl.builder()
						.url("https://ark-project.tos-cn-beijing.volces.com/doc_image/seepro_i2v.png") // 请上传可以访问的图片URL
						.build())
				.build());

		// 创建视频生成任务
		CreateContentGenerationTaskRequest createRequest = CreateContentGenerationTaskRequest.builder().model(model)
				.content(contents).build();

		CreateContentGenerationTaskResult createResult = service.createContentGenerationTask(createRequest);
		System.out.println(createResult);

		// 获取任务详情
		String taskId = createResult.getId();
		GetContentGenerationTaskRequest getRequest = GetContentGenerationTaskRequest.builder().taskId(taskId).build();

		// 轮询查询部分
		System.out.println("----- polling task status -----");
		while (true) {
			try {
				GetContentGenerationTaskResponse getResponse = service.getContentGenerationTask(getRequest);
				String status = getResponse.getStatus();
				if ("succeeded".equalsIgnoreCase(status)) {
					System.out.println("----- task succeeded -----");
					System.out.println(getResponse);
					break;
				} else if ("failed".equalsIgnoreCase(status)) {
					System.out.println("----- task failed -----");
					System.out.println("Error: " + getResponse.getStatus());
					break;
				} else {
					System.out.printf("Current status: %s, Retrying in 3 seconds...", status);
					TimeUnit.SECONDS.sleep(3);
				}
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				System.err.println("Polling interrupted");
				break;
			}
		}
	}
}
