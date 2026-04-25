package com.abs.system.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.abs.system.api.IAbsFileInfo;
import com.abs.system.api.IAbsSysConfig;
import com.abs.system.api.IAbsUserService;
import com.abs.system.domain.AbsFileInfo;
import com.abs.system.filter.NoNeedLogin;
import com.abs.system.filter.ToToken;
import com.abs.system.util.AbsSessionHelper;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.abs.system.util.XMLUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/absfile")
public class AbsFileInfoController {

	private Logger logger = LoggerFactory.getLogger(AbsFileInfoController.class);

	@Autowired
	private IAbsFileInfo fileService;

	@Autowired
	private IAbsSysConfig configService;

	@Autowired
	private IAbsUserService userService;

	@NoNeedLogin
	@ResponseBody
	@RequestMapping("/addfiletolocal")
	public JSONObject addFile(@RequestParam("file") MultipartFile multipartFile, Map<String, Object> map)
			throws IOException {
		logger.info("************************开始保存签名********************");
		try {
			// System.out.println(file.);
			// 文件上传地址
			String uploadpath = configService.getByConfigValueByName("upload_path", null);
			if (StrUtil.isStrBlank(uploadpath)) {
				return BuildJsonOfObject.getJsonString(MSG.UploadPathNoConfig, MSG.FAILCODE);
			}
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("YYYYMM");
			String date_dir = sdf.format(date);
			File dir = new File(uploadpath + "//" + date_dir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			String clienguid = null;
			String rowguid = UUID.randomUUID().toString();
			if (!StrUtil.isObjBlank(map.get("cliengguid"))) {
				clienguid = map.get("cliengguid").toString();
			} else {
				clienguid = UUID.randomUUID().toString();
			}
			String originalFilename = multipartFile.getOriginalFilename();
			int index = originalFilename.lastIndexOf(".");

			String filename = rowguid + "_" + originalFilename;
			String filetype = originalFilename.substring(index);
			String storePath = dir + "/" + filename;
			File file = new File(storePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			long filesize = multipartFile.getSize();
			multipartFile.transferTo(file);

			AbsFileInfo afile = new AbsFileInfo();
			afile.setAddtime(new Date());
			afile.setCliengguid(clienguid);
			afile.setRowguid(rowguid);
			afile.setFileurl(date_dir + "/" + filename);
			afile.setFilepath(storePath);
			afile.setIstoali(MSG.ToAliNO);
			afile.setFilename(originalFilename);
			afile.setFiletype(filetype);
			afile.setFilesize(filesize);
			fileService.addFileInfo(afile);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		} finally {

		}
		return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE, MSG.ok);
	}

	@NoNeedLogin
	@ResponseBody
	@RequestMapping("/adduserlogo")
	public JSONObject addUserLogo(@ToToken Params Params, @RequestParam("file") MultipartFile multipartFile,
			HttpServletRequest request) throws IOException {
		logger.info("***********************用户上传头像********************");
		try {
			String token = request.getHeader("token");
			String userguid = AbsSessionHelper.getCurrentUserGuid(token);
			String originalFilename = multipartFile.getOriginalFilename();
			int index = originalFilename.lastIndexOf(".");
			String filetype = originalFilename.substring(index);
			if (MSG.ToAliYes.equals(configService.getByConfigValueByName("isToAli", null))) {
				String endpoint = configService.getByConfigValueByName("endpoint", null);
				String accessKeyId = configService.getByConfigValueByName("accessKeyId", null);
				String accessKeySecret = configService.getByConfigValueByName("accessKeySecret", null);
				String bucketName = "read8686img";
				String firstKey = "logo/" + UUID.randomUUID().toString().replace("-", "") + filetype;
				OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
				String msg = "";
				String code = MSG.SUCCESSCODE;
				String url = "";
				try {
					if (ossClient.doesBucketExist(bucketName)) {
						logger.info("您已经创建Bucket：" + bucketName + "。");
					} else {
						logger.info("您的Bucket不存在，创建Bucket：" + bucketName + "。");
						ossClient.createBucket(bucketName);
					}
					MultipartHttpServletRequest fileUpload = (MultipartHttpServletRequest) request;
					MultipartFile multifile = fileUpload.getFile("file");
					InputStream is = new ByteArrayInputStream(multifile.getBytes());
					ossClient.putObject(bucketName, firstKey, is);
					logger.info("文件上传到阿里云，上传成功");
					OSSObject ob = ossClient.getObject(bucketName, firstKey);
					url = ob.getResponse().getUri();
					AbsFileInfo fileinfo = new AbsFileInfo();
					fileinfo.setRowguid(UUID.randomUUID().toString());
					fileinfo.setFilesize(request.getContentLength());
					fileinfo.setAddtime(new Date());
					fileinfo.setFilepath("");
					fileinfo.setFileurl(url);
					fileinfo.setIstoali(MSG.ToAliYes);
					fileinfo.setCliengguid(userguid);
					fileinfo.setFilename(originalFilename);
					fileinfo.setFiletype(filetype);
					fileinfo.setFirstkey(firstKey);

					fileinfo.setRemark("logo");
					fileinfo.setUserguid(userguid);
					fileService.delFileInfoByCliengGuid(userguid);
					fileService.addFileInfo(fileinfo);
					userService.updateUserLogoByGuid(url, userguid);
				} catch (Exception e) {
					e.printStackTrace();
					msg = e.getMessage();
					code = MSG.FAILCODE;
				} finally {
					ossClient.shutdown();
				}
				return BuildJsonOfObject.getJsonString(url, msg, code);
			} else {
				String uploadpath = configService.getByConfigValueByName("upload_path", null);
				if (StrUtil.isStrBlank(uploadpath)) {
					return BuildJsonOfObject.getJsonString(MSG.UploadPathNoConfig, MSG.FAILCODE);
				}
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("YYYYMM");
				String date_dir = sdf.format(date);
				File dir = new File(uploadpath + "//" + date_dir);
				if (!dir.exists()) {
					dir.mkdir();
				}
				String rowguid = UUID.randomUUID().toString();
				String filename = rowguid + "_" + originalFilename;
				String storePath = dir + "/" + filename;
				File file = new File(storePath);
				if (!file.exists()) {
					file.createNewFile();
				}
				long filesize = multipartFile.getSize();
				multipartFile.transferTo(file);
				AbsFileInfo afile = new AbsFileInfo();
				afile.setAddtime(new Date());
				afile.setCliengguid(userguid);
				afile.setRowguid(rowguid);
				afile.setFileurl(date_dir + "/" + filename);
				afile.setFilepath(storePath);
				afile.setIstoali(MSG.ToAliNO);
				afile.setFilename(originalFilename);
				afile.setFiletype(filetype);
				afile.setFilesize(filesize);
				afile.setRemark("logo");
				fileService.delFileInfoByCliengGuid(userguid);
				fileService.addFileInfo(afile);
				userService.updateUserLogoByGuid(storePath, userguid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
		return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE);
	}

	@NoNeedLogin
	@ResponseBody
	@RequestMapping("/addfiletoali")
	public JSONObject addFileToAli(@ToToken Params params, @RequestParam("file") MultipartFile multipartFile,
			HttpServletRequest request) throws IOException {
		logger.info("************************上传图片********************");
		try {
			String cliengguid = request.getParameter("cliengguid");
			String token = params.getString("token");
			String remark = request.getParameter("remark");
			String userguid = AbsSessionHelper.getCurrentUserGuid(token);
			if (StrUtil.isStrBlank(cliengguid)) {
				cliengguid = UUID.randomUUID().toString();
			} else {
				fileService.delFileInfoByCliengGuid(cliengguid);
			}
			String originalFilename = multipartFile.getOriginalFilename();
			int index = originalFilename.lastIndexOf(".");
			String filetype = originalFilename.substring(index);
			String endpoint = configService.getByConfigValueByName("endpoint", null);
			String accessKeyId = configService.getByConfigValueByName("accessKeyId", null);
			String accessKeySecret = configService.getByConfigValueByName("accessKeySecret", null);
			String bucketName = "read8686img";

			// configService.getByConfigValueByName("bucketName");
			String firstKey = "mall/" + UUID.randomUUID().toString().replace("-", "") + filetype;
			OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
			String msg = cliengguid;
			String code = MSG.SUCCESSCODE;
			String url = "";
			try {
				if (ossClient.doesBucketExist(bucketName)) {
					logger.info("您已经创建Bucket：" + bucketName + "。");
				} else {
					logger.info("您的Bucket不存在，创建Bucket：" + bucketName + "。");
					ossClient.createBucket(bucketName);
				}
				MultipartHttpServletRequest fileUpload = (MultipartHttpServletRequest) request;
				MultipartFile multifile = fileUpload.getFile("file");
				InputStream is = new ByteArrayInputStream(multifile.getBytes());
				ossClient.putObject(bucketName, firstKey, is);
				logger.info("文件上传到阿里云，上传成功");
				OSSObject ob = ossClient.getObject(bucketName, firstKey);
				url = ob.getResponse().getUri();
				AbsFileInfo fileinfo = new AbsFileInfo();
				fileinfo.setRowguid(UUID.randomUUID().toString());
				fileinfo.setFilesize(request.getContentLength());
				fileinfo.setAddtime(new Date());
				fileinfo.setFilepath("");
				fileinfo.setFileurl(url);
				fileinfo.setIstoali(MSG.ToAliYes);
				fileinfo.setCliengguid(cliengguid);
				fileinfo.setFilename(originalFilename);
				fileinfo.setFiletype(filetype);
				fileinfo.setFirstkey(firstKey);
				fileinfo.setUserguid(userguid);
				fileinfo.setIstmp(1);// 临时文件
				fileinfo.setRemark(remark);
				fileService.delFileInfoByCliengGuid(cliengguid);
				fileService.addFileInfo(fileinfo);
			} catch (Exception e) {
				e.printStackTrace();
				msg = e.getMessage();
				code = MSG.FAILCODE;
			} finally {
				ossClient.shutdown();
			}
			return BuildJsonOfObject.getJsonString(url, msg, code);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}
	
	@NoNeedLogin
	@ResponseBody
	@RequestMapping("/addfiletoali2")
	public JSONObject addFileToAli2(@ToToken Params params, @RequestParam("file") MultipartFile multipartFile,
			HttpServletRequest request) throws IOException {
		logger.info("************************上传图片********************");
		try {
			String cliengguid = request.getParameter("cliengguid");
			String token = params.getString("token");
			String remark = request.getParameter("remark");
			String userguid = AbsSessionHelper.getCurrentUserGuid(token);
			if (StrUtil.isStrBlank(cliengguid)) {
				cliengguid = UUID.randomUUID().toString();
			} else {
				fileService.delFileInfoByCliengGuid(cliengguid);
			}
			String originalFilename = multipartFile.getOriginalFilename();
			int index = originalFilename.lastIndexOf(".");
			String filetype = originalFilename.substring(index);
			String endpoint = configService.getByConfigValueByName("endpoint", null);
			String accessKeyId = configService.getByConfigValueByName("accessKeyId", null);
			String accessKeySecret = configService.getByConfigValueByName("accessKeySecret", null);
			String bucketName = "read8686";

			// configService.getByConfigValueByName("bucketName");
			String firstKey = "aicreate/" + UUID.randomUUID().toString().replace("-", "") + filetype;
			OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
			String msg = cliengguid;
			String code = MSG.SUCCESSCODE;
			String url = "";
			try {
				if (ossClient.doesBucketExist(bucketName)) {
					logger.info("您已经创建Bucket：" + bucketName + "。");
				} else {
					logger.info("您的Bucket不存在，创建Bucket：" + bucketName + "。");
					ossClient.createBucket(bucketName);
				}
				MultipartHttpServletRequest fileUpload = (MultipartHttpServletRequest) request;
				MultipartFile multifile = fileUpload.getFile("file");
				InputStream is = new ByteArrayInputStream(multifile.getBytes());
				ossClient.putObject(bucketName, firstKey, is);
				logger.info("文件上传到阿里云，上传成功");
				OSSObject ob = ossClient.getObject(bucketName, firstKey);
				url = ob.getResponse().getUri();
				AbsFileInfo fileinfo = new AbsFileInfo();
				fileinfo.setRowguid(UUID.randomUUID().toString());
				fileinfo.setFilesize(request.getContentLength());
				fileinfo.setAddtime(new Date());
				fileinfo.setFilepath("");
				fileinfo.setFileurl(url);
				fileinfo.setIstoali(MSG.ToAliYes);
				fileinfo.setCliengguid(cliengguid);
				fileinfo.setFilename(originalFilename);
				fileinfo.setFiletype(filetype);
				fileinfo.setFirstkey(firstKey);
				fileinfo.setUserguid(userguid);
				fileinfo.setIstmp(1);// 临时文件
				fileinfo.setRemark(remark);
				fileService.delFileInfoByCliengGuid(cliengguid);
				fileService.addFileInfo(fileinfo);
			} catch (Exception e) {
				e.printStackTrace();
				msg = e.getMessage();
				code = MSG.FAILCODE;
			} finally {
				ossClient.shutdown();
			}
			return BuildJsonOfObject.getJsonString(url, msg, code);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	@NoNeedLogin
	@ResponseBody
	@RequestMapping("/addgoodarticlepic")
	public JSONObject addGoodsArticlePic(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request)
			throws IOException {
		logger.info("************************商品描述上传图片********************");
		try {
			String cliengguid = request.getParameter("cliengguid");
			logger.info("当前的cliengguid:{}", cliengguid);

			if (StrUtil.isStrBlank(cliengguid)) {
				cliengguid = UUID.randomUUID().toString();
			}

			String originalFilename = multipartFile.getOriginalFilename();
			int index = originalFilename.lastIndexOf(".");
			String filetype = originalFilename.substring(index);
			if (MSG.ToAliYes.equals(configService.getByConfigValueByName("isToAli", null))) {
				String endpoint = configService.getByConfigValueByName("endpoint", null);
				String accessKeyId = configService.getByConfigValueByName("accessKeyId", null);
				String accessKeySecret = configService.getByConfigValueByName("accessKeySecret", null);
				String bucketName = "read8686img";
				String firstKey = "article/" + UUID.randomUUID().toString().replace("-", "") + filetype;
				OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
				String msg = cliengguid;
				String code = MSG.SUCCESSCODE;
				String url = "";
				try {
					if (ossClient.doesBucketExist(bucketName)) {
						logger.info("您已经创建Bucket：" + bucketName + "。");
					} else {
						logger.info("您的Bucket不存在，创建Bucket：" + bucketName + "。");
						ossClient.createBucket(bucketName);
					}
					MultipartHttpServletRequest fileUpload = (MultipartHttpServletRequest) request;
					MultipartFile multifile = fileUpload.getFile("file");
					InputStream is = new ByteArrayInputStream(multifile.getBytes());
					ossClient.putObject(bucketName, firstKey, is);
					logger.info("文件上传到阿里云，上传成功");
					OSSObject ob = ossClient.getObject(bucketName, firstKey);
					url = ob.getResponse().getUri();
					AbsFileInfo fileinfo = new AbsFileInfo();
					fileinfo.setRowguid(UUID.randomUUID().toString());
					fileinfo.setFilesize(request.getContentLength());
					fileinfo.setAddtime(new Date());
					fileinfo.setFilepath("");
					fileinfo.setFileurl(url);
					fileinfo.setIstoali(MSG.ToAliYes);
					fileinfo.setCliengguid(cliengguid);
					fileinfo.setFilename(originalFilename);
					fileinfo.setFiletype(filetype);
					fileinfo.setFirstkey(firstKey);
					fileinfo.setIstmp(1);// 临时文件
					fileinfo.setRemark("agoods");
					fileService.addFileInfo(fileinfo);
				} catch (MaxUploadSizeExceededException e) {
					msg = "图片大小超限制>2M";
					code = MSG.FAILCODE;

				} catch (Exception e) {
					e.printStackTrace();
					msg = e.getMessage();
					code = MSG.FAILCODE;
				} finally {
					ossClient.shutdown();
				}
				JSONArray jsonArray = new JSONArray();
				jsonArray.add(url);
				JSONObject jsonRtn = new JSONObject();
				jsonRtn.put("data", jsonArray);
				jsonRtn.put("errno", 0);
				jsonRtn.put("code", code);
				jsonRtn.put("msg", msg);
				logger.info("文件上传成功，返回参数：{}", jsonRtn.toString());
				return jsonRtn;
			} else {
				String uploadpath = configService.getByConfigValueByName("upload_path", null);
				if (StrUtil.isStrBlank(uploadpath)) {
					return BuildJsonOfObject.getJsonString(MSG.UploadPathNoConfig, MSG.FAILCODE);
				}
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("YYYYMM");
				String date_dir = sdf.format(date);
				File dir = new File(uploadpath + "//" + date_dir);
				if (!dir.exists()) {
					dir.mkdir();
				}
				String rowguid = UUID.randomUUID().toString();
				String filename = rowguid + "_" + originalFilename;
				String storePath = dir + "/" + filename;
				File file = new File(storePath);
				if (!file.exists()) {
					file.createNewFile();
				}
				long filesize = multipartFile.getSize();
				multipartFile.transferTo(file);
				AbsFileInfo afile = new AbsFileInfo();
				afile.setAddtime(new Date());
				afile.setCliengguid(cliengguid);
				afile.setRowguid(rowguid);
				afile.setFileurl(date_dir + "/" + filename);
				afile.setFilepath(storePath);
				afile.setIstoali(MSG.ToAliNO);
				afile.setFilename(originalFilename);
				afile.setFiletype(filetype);
				afile.setFilesize(filesize);
				afile.setIstmp(1);// 临时文件
				afile.setRemark("goods");
				fileService.delFileInfoByCliengGuid(cliengguid);
				fileService.addFileInfo(afile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
		return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping("/deletebycliengguid")
	public JSONObject deletebycliengguid(@RequestBody Map<String, Object> map) {
		logger.info("************************删除文件********************");
		try {
			logger.info("用户传入参数{}", map.toString());
			String cliengguid = map.get("cliengguid").toString();

			fileService.delFileInfoByCliengGuid(cliengguid);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
		return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping("/deletebyguid")
	public JSONObject deletebyguid(@RequestBody Map<String, Object> map) {
		logger.info("************************删除文件********************");
		try {
			logger.info("用户传入参数{}", map.toString());
			String rowguid = map.get("rowguid").toString();
			fileService.delFileInfoByRowGuid(rowguid);
			return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}

	}

	@NoNeedLogin
	@ResponseBody
	@RequestMapping("/addfiletoaliqm")
	public JSONObject addFileToAliQm(@RequestParam("file") MultipartFile multipartFile, Map<String, Object> map)
			throws IOException {
		logger.info("************************开始保存签名********************");
		try {
			String uploadpath = XMLUtils.getValueByName("upload_path");
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("YYYYMM");
			String date_dir = sdf.format(date);
			File dir = new File(uploadpath + date_dir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			String clienguid = null;
			String rowguid = UUID.randomUUID().toString();
			if (!StrUtil.isObjBlank(map.get("cliengguid"))) {
				clienguid = map.get("cliengguid").toString();
			} else {
				clienguid = UUID.randomUUID().toString();
			}
			String originalFilename = multipartFile.getOriginalFilename();
			int index = originalFilename.lastIndexOf(".");

			String filename = rowguid + "_" + originalFilename;
			String filetype = originalFilename.substring(index);
			String storePath = dir + "/" + filename;
			File file = new File(storePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			long filesize = multipartFile.getSize();
			multipartFile.transferTo(file);

			AbsFileInfo afile = new AbsFileInfo();
			afile.setAddtime(new Date());
			afile.setCliengguid(clienguid);
			afile.setRowguid(rowguid);
			afile.setFileurl(date_dir + "/" + filename);
			afile.setFilepath(storePath);
			afile.setIstoali(MSG.ToAliYes);
			afile.setFilename(originalFilename);
			afile.setFiletype(filetype);
			afile.setFilesize(filesize);
			fileService.addFileInfo(afile);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString("fail", MSG.FAILCODE, e.getMessage());
		} finally {

		}
		return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE, MSG.ok);
	}

	@ResponseBody
	@RequestMapping(value = "/getpicfilelist")
	public JSONObject getPicFilelist(@RequestBody Map<String, Object> reqMap) {
		logger.info("用户传入参数getpicfilelist:{}", reqMap.toString());
		try {
			Params params = new Params(reqMap);
			List<Map<String, Object>> list = fileService.queryPicFileList(params);

			if (list != null && list.size() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (Map<String, Object> map : list) {
					map.put("addtime", sdf.format((Date) map.get("addtime")));
				}

			}
			long totalCount = fileService.queryPicFileCount(params);
			return BuildJsonOfObject.getJsonString(list, totalCount, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getpicfilelistbyuser")
	public JSONObject getPicFileListByUser(@ToToken Params param, @RequestBody Map<String, Object> reqMap) {
		logger.info("用户传入参数getpicfilelistbyuser:{}", reqMap.toString());
		try {

			String token = param.getString("token");
			String userguid = AbsSessionHelper.getCurrentUserGuid(token);
			Params params = new Params(reqMap);
			params.put("userguid", userguid);
			List<Map<String, Object>> list = fileService.queryPicFileList(params);
			if (list != null && list.size() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (Map<String, Object> map : list) {
					map.put("addtime", sdf.format((Date) map.get("addtime")));
				}

			}
			long totalCount = fileService.queryPicFileCount(params);
			return BuildJsonOfObject.getJsonString(list, totalCount, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

}
