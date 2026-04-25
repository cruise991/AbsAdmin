package com.abs.system.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.file.AccessDeniedException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.abs.system.domain.AbsSysUpdate;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.abs.system.util.UnzipJarUtil;
import com.abs.system.util.XMLUtils;
import com.alibaba.fastjson.JSONObject;

/**
 * 在线更新
 * 
 * @author wdg
 *
 */
@Controller
@RequestMapping("/uponline")
public class AbsUpdateOnLineController {

	private Logger logger = LoggerFactory.getLogger(AbsUpdateOnLineController.class);

	/**
	 * 文件备份
	 * 
	 * @param reqMap
	 * @return
	 */

	@ResponseBody
	@RequestMapping("/bffile")
	public JSONObject bffile(@RequestBody Map<String, String> reqMap) {
		try {
			logger.info("**********begin back-up the file**********");
			logger.info(reqMap.toString());
			String updatetype = reqMap.get("updatetype");
			String bfpath = reqMap.get("bfpath");
			if (StrUtil.isStrBlank(bfpath)) {
				throw new RuntimeErrorException(new Error(), "备份路径不能为空");
			}
			String serverbf_path = XMLUtils.getValueByName("serverbf_path");
			if (StrUtil.isStrBlank(serverbf_path)) {
				throw new RuntimeErrorException(new Error(), "服务器备份文件存储路径不能为空");
			}

			
			File srcFile = new File(bfpath);
			
			
			logger.info("***********备份路径准备{}*********", serverbf_path);
			File bffile = new File(serverbf_path);
			if (!bffile.exists()) {
				bffile.mkdir();
			}
			if (bffile.isDirectory()) {
				String[] files = bffile.list();
				if (files.length > 0) {
					for (File f : bffile.listFiles()) {
						if (f.isDirectory() && !f.getName().equals(srcFile.getName())) {
							FileSystemUtils.deleteRecursively(f);
						} else {
							f.delete();
						}
					}
					logger.info("备份文件夹下面文件已经清除");
				} else {
					logger.info("备份目录为空目录");
				}
			}

			logger.info("备份文件路径:{}", bfpath);

		
			File destFile = new File(serverbf_path + srcFile.getName());
			FileSystemUtils.copyRecursively(srcFile, destFile);
			AbsSysUpdate update = new AbsSysUpdate();
			update.setBfpath(bfpath);
			update.setAddtime(new Date());
			update.setIshy(0);
			update.setRowguid(UUID.randomUUID().toString());
			update.setUpdatetype(updatetype);
			AbsDbService dbService = AbsDbHelper.getDbService();
			dbService.addEntity(update);
			logger.info("*****备份完成******");
			return BuildJsonOfObject.getJsonString("ok", MSG.SUCCESSCODE);
		} catch (AccessDeniedException access) {
			access.printStackTrace();
			return BuildJsonOfObject.getJsonString("文件夹：" + access.getMessage() + "无访问权限", MSG.FAILCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	/**
	 * 添加更新备注
	 * 
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addremark")
	public JSONObject addremark(@RequestBody Map<String, String> reqMap) {
		try {
			logger.info("**********begin back-up the file**********");
			String remark = reqMap.get("remark");
			AbsSysUpdate update = new AbsSysUpdate();
			update.setAddtime(new Date());
			update.setRowguid(UUID.randomUUID().toString());
			update.setRemark(remark);
			AbsDbService dbService = AbsDbHelper.getDbService();
			dbService.addEntity(update);
			logger.info("*****over back-up the file******");
			return BuildJsonOfObject.getJsonString("ok", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	@ResponseBody
	@RequestMapping("/gobefore")
	public JSONObject goBefore(@RequestBody Map<String, String> params) {
		logger.info("开始还原到上次记录，调用gobefore接口:{}", params.toString());

		try {
			String cguid = params.get("rowguid");
			String sql = "select * from abs_sysupdate where rowguid={rowguid}";

			Params p = new Params();
			p.put("rowguid", cguid);
			AbsSysUpdate u = AbsDbHelper.getDbService().queryEntityBySql(AbsSysUpdate.class, sql, p);
			if (u == null) {
				throw new RuntimeErrorException(new Error(), "还原失败,未找到更新记录");
			}
			String updatepath = u.getBfpath();// 那里做了备份
			File destFile = new File(updatepath);
			
			logger.info("获取上次备份路径");
			String bf_path = XMLUtils.getValueByName("serverbf_path");
			if (StrUtil.isStrBlank(bf_path)) {
				throw new RuntimeErrorException(new Error(), "还原失败,请配置备份文件路径 config.xml =>serverbf_path");
			}
			logger.info("备份文件存储路径为：{}", bf_path);
			bf_path=bf_path.trim();
			File file = new File(bf_path);
			if (!file.exists()) {
				throw new RuntimeErrorException(new Error(), "还原失败,未找到备份文件");
			}
			if (!file.isDirectory()) {
				throw new RuntimeErrorException(new Error(), "还原失败,备份文件异常");
			}
			logger.info("还原路径：{}",bf_path+destFile.getName());
			File srcFile = new File(bf_path + destFile.getName());
			if (srcFile.exists()) {
				for (File f : srcFile.listFiles()) {
					if (f.getName().indexOf(".") == 0) {
						f.delete();
					} else {
						logger.info(f.getName() + "****" + f.getCanonicalPath());
					}
				}
			}else {
				throw new RuntimeErrorException(new Error(), "还原失败,未找到备份文件："+srcFile.getAbsolutePath());
			}

			
			logger.info("删除文件:{}", destFile.getAbsoluteFile());
			FileSystemUtils.deleteRecursively(destFile);

			logger.info("开始还原");

			FileSystemUtils.copyRecursively(srcFile, destFile);
			u.setIshy(1);
			AbsDbHelper.getDbService().updateEntity(u, "rowguid");

			logger.info("还原结束............");
			return BuildJsonOfObject.getJsonString("ok", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	/**
	 * 上传jar包文件
	 * 
	 * @param multipartFile
	 * @param map
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	@ResponseBody
	@RequestMapping("/addfile")
	public JSONObject uploadjar(@RequestParam("file") MultipartFile multipartFile, Map<String, Object> map)
			throws IOException {
		logger.info("************************上传更新文件********************");
		try {
			System.out.println(map.get("remark"));
			// System.out.println(file.);
			/* 获取到文件上传地址 */
			String uploadpath = XMLUtils.getValueByName("upload_path");
			String date_dir = "update/";
			File dir = new File(uploadpath + date_dir);

			if (dir.isDirectory()) {
				File[] files = dir.listFiles();
				for (File f : files) {
					f.delete();
				}
			}
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String storePath = uploadpath + date_dir + "/yun.jar";
			File file = new File(storePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			multipartFile.transferTo(file);
			logger.info("**************解析更新文件**********");
			// 解压文件
			UnzipJarUtil.unzipJar(uploadpath + date_dir, storePath);

			logger.info("将更新文件转移到更新路径");
			// 检索文件并且赋值到项目路径下： 实现com 和resource
			String classPath = this.getClass().getResource("/").getPath();
			File[] filess = dir.listFiles();
			for (File ff : filess) {
				String fname = ff.getName();
				if (!fname.equals("META-INF") && !fname.equals("yun.jar")) {
					if (fname.equals("resources")) {
						File[] res = ff.listFiles();
						for (File r : res) {
							File destFile = new File(classPath + r.getName());
							FileSystemUtils.copyRecursively(r, destFile);
						}
					} else {
						File destFile = new File(classPath + fname);
						FileSystemUtils.copyRecursively(ff, destFile);
					}

				}
			}
			logger.info("******更新完成***********");
			return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString("fail", MSG.FAILCODE, e.getMessage());
		}
	}

	/**
	 * 重新加载项目
	 * 
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/reload")
	public Map reload(@RequestBody Map<String, String> reqMap) {
		logger.info("...开始重启tomcat.....");
		try {
			String configname = reqMap.get("configname");
			if (StrUtil.isStrBlank(configname)) {
				throw new RuntimeErrorException(new Error(), "参数配置名称不能位空");
			}
			String sql = "select configvalue from abs_sysconfig where configname={configname}";
			AbsDbService dbService = AbsDbHelper.getDbService();
			Params params = new Params();
			params.put("configname", configname);
			Map<String, Object> map = dbService.queryMapBySql(sql, params);
			if (map == null || StrUtil.isObjBlank(map.get("configvalue"))) {
				throw new RuntimeErrorException(new Error(), "请配置重启命令");
			}
			String command = map.get("configvalue").toString();
			logger.info("待执行的命令为：{}", command);
			Process process = Runtime.getRuntime().exec(command);
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line = "";
			String result = "";
			while ((line = input.readLine()) != null) {
				result += line;
			}
			logger.info("命令执行结果：{}", result);
			return BuildJsonOfObject.getJsonString(result, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	/**
	 * 获取到备份操作列表
	 * 
	 * @param reqMap
	 * @return
	 */

	@ResponseBody
	@RequestMapping("/getbflist")
	public Map getbflist(@RequestBody Map<String, String> reqMap) {
		logger.info("...getbflist...");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sql = "select updatetype,rowguid,remark,bfpath,ishy,addtime from abs_sysupdate GROUP BY updatetype having remark is null order by addtime desc";
			AbsDbService dbService = AbsDbHelper.getDbService();
			List<Map<String, Object>> list = dbService.queryListForMapBySql(sql, null);
			for (Map<String, Object> map : list) {
				map.put("addtime", sdf.format(map.get("addtime")));
			}
			return BuildJsonOfObject.getJsonString(list, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	/**
	 * 获取到备份操作列表
	 * 
	 * @param reqMap
	 * @return
	 */

	@ResponseBody
	@RequestMapping("/getuplist")
	public Map getuplist(@RequestBody Map<String, String> reqMap) {
		logger.info("...getuplist...");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sql = "select remark,addtime from abs_sysupdate where remark is not null order by addtime desc limit 5";
			AbsDbService dbService = AbsDbHelper.getDbService();
			List<Map<String, Object>> list = dbService.queryListForMapBySql(sql, null);
			for (Map<String, Object> map : list) {
				map.put("addtime", sdf.format(map.get("addtime")));
			}
			return BuildJsonOfObject.getJsonString(list, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

}
