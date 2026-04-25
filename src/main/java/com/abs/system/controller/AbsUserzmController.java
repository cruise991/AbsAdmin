package com.abs.system.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.abs.system.api.IAbsFileInfo;
import com.abs.system.api.IAbsSysConfig;
import com.abs.system.api.IAbsUserzmService;
import com.abs.system.domain.AbsFileInfo;
import com.abs.system.domain.AbsUserZm;
import com.abs.system.util.AbsSessionHelper;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.StrUtil;
import com.alibaba.fastjson.JSONObject;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/userzm")
public class AbsUserzmController {
	
	private Logger logger=LoggerFactory.getLogger(AbsUserzmController.class);
	
	@Autowired
	private IAbsSysConfig configService;
	
	
	@Autowired
	private IAbsUserzmService zmService;
	
	
	@Autowired
	private IAbsFileInfo fileService;
	
	
	@ResponseBody
	@RequestMapping("/zmbj")
	public JSONObject uploadZmbj(@RequestParam("file") MultipartFile multipartFile,HttpServletRequest request) {
		
		logger.info("************************开始保存桌面背景图片********************");
		try {
			String token=request.getHeader("Authorization");			
			logger.info("用户当前的token:{}",token);
			String uploadpath = configService.getByConfigValueByName("upload_path",null);
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
			clienguid = UUID.randomUUID().toString();
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

			String userguid=AbsSessionHelper.getCurrentUserGuid(token);
			//删除原来的
			AbsUserZm userzm=zmService.getUserzmByUserguid(userguid);
			if(userzm!=null) {
				String cliengguid=userzm.getCliengguid();
				fileService.delFileInfoByCliengGuid(cliengguid);
				userzm.setCliengguid(afile.getCliengguid());
				userzm.setAddtime(new Date());
				zmService.updateUserZm(userzm);
			}else {
				AbsUserZm zm=new AbsUserZm();
				zm.setCliengguid(afile.getCliengguid());
				zm.setRowguid(UUID.randomUUID().toString());
				zm.setAddtime(new Date());
				zm.setUserguid(userguid);
				zmService.addUserZm(zm);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		} finally {

		}
		return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE, MSG.ok);
	}
}
