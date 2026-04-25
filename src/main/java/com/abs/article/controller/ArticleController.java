package com.abs.article.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.abs.article.api.IArticleInfo;
import com.abs.article.domain.AbsArticle;
import com.abs.system.api.IAbsFileInfo;
import com.abs.system.api.IAbsSysConfig;
import com.abs.system.api.IAbsUserService;
import com.abs.system.api.SiteUrlTsService;
import com.abs.system.domain.AbsFileInfo;
import com.abs.system.domain.AbsUserInfo;
import com.abs.system.domain.SiteUrlTsInfo;
import com.abs.system.filter.NoNeedLogin;
import com.abs.system.filter.ToToken;
import com.abs.system.util.AbsSessionHelper;
import com.abs.system.util.BeanUtil;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.abs.system.util.XMLUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/article")
public class ArticleController {

	private static SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final String Article_Init = "0";//初始化
	public static final String Article_Draf = "1";//草稿
	public static final String Article_WaitPass = "2";//待审核
	public static final String Article_NotPass = "3";//审核不通过
	public static final String Article_Pass = "4";//审核通过
	
	private Logger logger=LoggerFactory.getLogger(ArticleController.class);

	@Autowired
	private IArticleInfo articleService;

	@Autowired
	private IAbsFileInfo fileServie;

	@Autowired
	private IAbsUserService userService;

	@Autowired
	private SiteUrlTsService tsService;
	
	@Autowired
	private IAbsSysConfig configService;


	
	@ResponseBody
	@RequestMapping("articleadd_init")
	public JSONObject articleadd_init(@ToToken Params params) {
		try {
			String token=params.getString("token");
			String userguid=AbsSessionHelper.getCurrentUserGuid(token);
			AbsArticle article = new AbsArticle();
			article.setRowguid(UUID.randomUUID().toString());
			article.setAddtime(new Date());
			article.setAuthor(userguid);
			article.setStatus(Article_Init);
			articleService.addArticle(article);
		    JSONObject jsonRtn=new JSONObject();
		    jsonRtn.put("rowguid", article.getRowguid());
		    jsonRtn.put("code", MSG.SUCCESSCODE);
		    jsonRtn.put("msg","初始化成功");
			return jsonRtn;
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}
	
	
	
	@ResponseBody
	@RequestMapping("getarticlelist")
	public JSONObject getArticleList(@ToToken Params params,@RequestBody Map<String,Object> reqMap) {
		Params param=new Params(reqMap);
		List<Map<String,Object>> list= articleService.getArticlePageList(param);
		long count= articleService.getArticleCount(param);
	    return BuildJsonOfObject.getJsonString(list, count, MSG.SUCCESSCODE);
	}

	
	@ResponseBody
	@RequestMapping("view_init")
	public JSONObject view_init(@RequestBody Map<String,Object> reqMap) {
		
		logger.info("查看文章:{}",reqMap.toString());
		String rowguid=reqMap.get("articleguid").toString();
		AbsArticle absArticle = articleService.getArtilceInfoByGuid(rowguid);
        Params p=Params.getRecordByObject(absArticle);
        JSONObject jsonRtn=new JSONObject();
        jsonRtn=p.toJSObject();
        jsonRtn.put("code", MSG.SUCCESSCODE);
        jsonRtn.put("msg", "文章查询成功");
		
		return jsonRtn;
	}

	
	
	@ResponseBody
	@RequestMapping("editinit")
	public String editinit(String rowguid, Model model) {
		AbsArticle AbsArticle = articleService.getArtilceInfoByGuid(rowguid);
		AbsArticle.setStatus("0");
		articleService.updateArticle(AbsArticle);
		model.addAttribute("title", AbsArticle.getTitle());
		if (StrUtil.isNotBlank(AbsArticle.getFmguid())) {
			model.addAttribute("fmpic", fileServie.getFileUrlByGuid(AbsArticle.getFmguid()));
		} else {
			model.addAttribute("fmpic", "../img/default.jpg");
		}
		model.addAttribute("content", dealContent(AbsArticle.getContent()));
		model.addAttribute("addtime", sdff.format(AbsArticle.getAddtime()));
		model.addAttribute("belongtype", AbsArticle.getBtype());
		String userguid = AbsArticle.getAuthor();
		AbsUserInfo userinfo = userService.getUserByGuid(userguid);
		if (userinfo != null) {
			model.addAttribute("author", userinfo.getRealname());
		} else {
			model.addAttribute("author", "佚名");
		}

		model.addAttribute("rowguid", AbsArticle.getRowguid());
		return "article/articleedit";
	}
	
	
	
	
	

	/**
	 *
	 * 
	 * @param reqMap
	 * @return
	 */

	@ResponseBody
	@RequestMapping("savearticle")
	public JSONObject savearticle(@ToToken Params params,@RequestBody Map<String, Object> reqMap) {
		logger.info("当前用户传入参数：{}",reqMap.toString());
		
		
		try {
			String rowguid = reqMap.get("rowguid").toString();
			String fmurl = reqMap.get("fmurl").toString();
			String btype = reqMap.get("btype").toString();
			String status = reqMap.get("status").toString();
			String content=reqMap.get("content").toString();
			String title=reqMap.get("title").toString();


			AbsArticle article = articleService.getArtilceInfoByGuid(rowguid);
			article.setAddtime(new Date());
			article.setContent(content);
			article.setTitle(title);
			try {
				article.setSummary(delHtmlTag(content.substring(0, 50)));
			} catch (StringIndexOutOfBoundsException e) {
				throw new RuntimeException(MSG.WZCDBG);
			}
			if (StrUtil.isObjBlank(article.getTitle())) {
				return BuildJsonOfObject.getJsonString(MSG.WZBTBUWK,MSG.FAILCODE);
			}
			article.setContent(clearnHtmlTag(content));
			article.setFmurl(fmurl);
			article.setBtype(btype);
			article.setStatus(status);//保存或者草稿状态
			articleService.updateArticle(article);
			return BuildJsonOfObject.getJsonString(MSG.saveSuccess,MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);

		}

	}
	
	
	@ResponseBody
	@RequestMapping("delbyguid")
	public JSONObject delbyguid(@RequestBody Map<String, Object> reqMap) {
		try {
			String rowguids=reqMap.get("rowguids").toString();
			String [] guids=rowguids.split(",");
			for(String guid:guids) {
				articleService.delArticleByGuid(guid);
			}
			return BuildJsonOfObject.getJsonString("删除成功", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);
		}

	}
	
	

	@ResponseBody
	@RequestMapping("editarticle")
	public JSONObject editarticle(@RequestBody Map<String, Object> reqMap, HttpSession session) {
		try {
			AbsArticle article = BeanUtil.mapToBean(reqMap, AbsArticle.class);
			article.setContent(clearnHtmlTag(article.getContent()));
			article.setAddtime(new Date());
			try {
				article.setSummary(delHtmlTag(article.getContent()).substring(0, 50));
			} catch (StringIndexOutOfBoundsException e) {
				throw new RuntimeException("文章长度不足");
			}
			try {
				article.setAuthor(session.getAttribute("userguid").toString());
			} catch (NullPointerException e) {
				throw new RuntimeException("未登陆");
			}
			article.setStatus("1");
			articleService.updateArticle(article);
			JSONObject json_obj = new JSONObject();
			json_obj.put("articleguid", article.getRowguid());
			return BuildJsonOfObject.getJsonString(json_obj,MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);
		}

	}
	
	
	@NoNeedLogin
	@ResponseBody
	@RequestMapping("querybyid")
	public JSONObject querybyid(@RequestBody Map<String,Object> reqMap) {
		logger.info("正在调用querybyid的接口,用户传入参数：{}",reqMap.toString());
		String aid=reqMap.get("aid")+"";;
		try {
			if(StrUtil.isStrBlank(aid)) {
				aid="13";
			}
			Map<String,Object> map=articleService.queryArticleById(aid);
		
			if(map!=null) {
				String author=map.get("author").toString();
				Map<String,Object> user=userService.queryUserMapByGuid(author);
				map.put("author", user.get("realname"));
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date addtime=(Date) map.get("addtime");
			map.put("addtime", sdf.format(addtime));
			String content=map.get("content").toString();
			map.put("content", this.dealContent(content));
			
			JSONObject json_obj = new JSONObject(map);
			
			String btype=map.get("btype")+"";
			List<Map<String,Object>> list=articleService.queryArticleByType(btype);
			
			
			
			
			return BuildJsonOfObject.getJsonString(json_obj,MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);
		}
	}

	public static String delHtmlTag(String str) {
		String newstr = "";
		newstr = str.replaceAll("<[.[^>]]*>", "");
		newstr = newstr.replaceAll(" ", "");
		newstr = newstr.replace("\'", "&quot;");
		newstr = newstr.replace("\"", "&quot;");
		return newstr;
	}

	public static String clearnHtmlTag(String str) {
		String newstr = "";
		newstr = str.replaceAll(" ", "&nbsp;");
		newstr = newstr.replace("\'", "&lsquo;");
		newstr = newstr.replace("\"", "&quot;");
		return newstr;
	}

	private String dealContent(String content) {
		String con = content.replaceAll("&quot;", "\"");
		con = con.replaceAll("&lsquo;", "\"");
		con = con.replaceAll("&nbsp;", " ");
		return con;
	}

	/**
	 *
	 * 
	 * @param reqMap
	 * @return
	 */

	@ResponseBody
	@RequestMapping("/uploadfm")
	public JSONObject uploadfm(@ToToken Params params,HttpServletRequest request) {
		
		logger.info("开始上传封面信息.....");
		try {
			String articleguid = request.getParameter("articleguid");
			String endpoint = configService.getByConfigValueByName("endpoint",null);
			String accessKeyId = configService.getByConfigValueByName("accessKeyId",null);
			String accessKeySecret = configService.getByConfigValueByName("accessKeySecret",null);
			String firstKey = UUID.randomUUID().toString();
			String bucketName = "read8686img";
			OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			String imgurl="";
			try {

				if (ossClient.doesBucketExist(bucketName)) {
					System.out.println("您已经创建Bucket：" + bucketName + "。");
				} else {
					System.out.println("您的Bucket不存在，创建Bucket：" + bucketName + "。");
					ossClient.createBucket(bucketName);
				}

				MultipartHttpServletRequest fileUpload = (MultipartHttpServletRequest) request;
				MultipartFile multifile = fileUpload.getFile("file");

				InputStream is = new ByteArrayInputStream(multifile.getBytes());
				ossClient.putObject(bucketName, firstKey, is);
				System.out.println("Object：" + firstKey + "存入OSS成功。");

				OSSObject ob = ossClient.getObject(bucketName, firstKey);
				imgurl = ob.getResponse().getUri();

				AbsFileInfo fileinfo = new AbsFileInfo();
				fileinfo.setRowguid(UUID.randomUUID().toString());
				fileinfo.setFilesize(request.getContentLength());
				fileinfo.setAddtime(new Date());
				fileinfo.setFilepath("");
				fileinfo.setFileurl(imgurl);
				fileinfo.setIstoali("1");
				fileinfo.setCliengguid(UUID.randomUUID().toString());
				fileServie.addFileInfo(fileinfo);
				AbsArticle article = articleService.getArtilceInfoByGuid(articleguid);
				article.setFmguid(fileinfo.getRowguid());
				articleService.updateArticle(article);

			} catch (OSSException oe) {
				oe.printStackTrace();
			} catch (ClientException ce) {
				ce.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ossClient.shutdown();
			}

			return BuildJsonOfObject.getJsonString(imgurl, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);

		}
	}

	

	
	

	@ResponseBody
	@RequestMapping("/delarticle")
	public JSONObject delarticle(@RequestBody Map<String, Object> map) {
		try {
			String rowguid = map.get("rowguid") == null ? "" : map.get("rowguid").toString();
			AbsArticle article = articleService.getArtilceInfoByGuid(rowguid);
			String fmguid = article.getFmguid();
			fileServie.delFileInfoByRowGuid(fmguid);
			articleService.delArticleByGuid(rowguid);
			return BuildJsonOfObject.getJsonString("ok", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);
		}

	}
	
	@ResponseBody
	@RequestMapping("/delarticleinit")
	public JSONObject delArticleinit(@ToToken Params params, @RequestBody Map<String, Object> map) {
		try {
			
			String token=params.getString("token");
			String userguid=AbsSessionHelper.getCurrentUserGuid(token);
			articleService.delArticleByUserGuidAnsStatus(userguid,Article_Init);
			
			return BuildJsonOfObject.getJsonString("ok", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);
		}

	}
	
	

	/**
	 * 审核通过
	 * 
	 * @param map
	 * @return
	 */

	@ResponseBody
	@RequestMapping("/shenhepass")
	public JSONObject shenhepass(@RequestBody Map<String, Object> map) {
		try {
			String rowguid = map.get("rowguid") == null ? "" : map.get("rowguid").toString();
			AbsArticle article = articleService.getArtilceInfoByGuid(rowguid);
			article.setStatus(MSG.Article_Pass);
			articleService.updateArticle(article);
			if (!tsService.isAlreadyExitObject(article.getRowguid())) {
				SiteUrlTsInfo tsinfo = new SiteUrlTsInfo();
				tsinfo.setRowguid(UUID.randomUUID().toString());
				String siteurl =XMLUtils.getValueByName("articleurl");
				tsinfo.setSiteurl(siteurl.replace("#id#", article.getId() + ""));
				tsinfo.setAddtime(new Date());
				tsinfo.setTsstatus("0");
				tsinfo.setObjectguid(article.getRowguid());
				tsService.addTsInfo(tsinfo);
			}
			return BuildJsonOfObject.getJsonString("ok", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);

		}
	}

	/**
	 * 审核不通过
	 * 
	 * @param map
	 * @return
	 */

	@ResponseBody
	@RequestMapping("/shenhenopass")
	public JSONObject shenhenopass(@RequestBody Map<String, Object> map) {
		try {
			String rowguid = map.get("rowguid") == null ? "" : map.get("rowguid").toString();
			AbsArticle article = articleService.getArtilceInfoByGuid(rowguid);
			article.setStatus(MSG.Article_NotPass);
			articleService.updateArticle(article);
			return BuildJsonOfObject.getJsonString("审核不通过", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);
		}
	}

	@ResponseBody
	@RequestMapping("/getarticlebyguid")
	public JSONObject getarticlebyguid(@RequestBody Map<String, Object> map) {
		try {
			String rowguid = map.get("rowguid") == null ? "" : map.get("rowguid").toString();
			AbsArticle article = articleService.getArtilceInfoByGuid(rowguid);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			JSONObject jsonRtn = new JSONObject();
			jsonRtn.put("content", dealContent(article.getContent()));
			AbsUserInfo userinfo = userService.getUserByGuid(article.getAuthor());
			jsonRtn.put("author", userinfo != null ? userinfo.getRealname() : "");
			jsonRtn.put("addtime", sdf.format(article.getAddtime()));
			return BuildJsonOfObject.getJsonString(jsonRtn, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);

		}

	}
}
