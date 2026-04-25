package com.abs.system.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsFileInfo;
import com.abs.system.api.IAbsSysConfig;
import com.abs.system.domain.AbsFileInfo;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

@Service
public class AbsFileInfoImpl implements IAbsFileInfo {
	
	
	@Autowired
	private IAbsSysConfig configService;


	public void addFileInfo(AbsFileInfo fileinfo) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.addEntity(fileinfo);
	}

	public String getFileUrlByGuid(String fmguid) {
		String sqlCondition = " and rowguid=\'" + fmguid + "\'";
		AbsDbService dbService=AbsDbHelper.getDbService();
		return dbService.getFieldByCondition(sqlCondition, "fileurl", AbsFileInfo.class);
	}

	public AbsFileInfo getFileUrlByClinegGuidLimit1(String cliengguid) {
		String sqlcondition = " and cliengguid=\'" + cliengguid + "\' limit 1";
		AbsDbService dbService=AbsDbHelper.getDbService();
		return dbService.getFieldByCondition(sqlcondition, "fileurl", AbsFileInfo.class);
	}

	@Override
	public List<AbsFileInfo> getFileInfoByCliengguid(String cliengguid) {
		String sqlcondition = " and cliengguid=\'" + cliengguid + "\'";
		AbsDbService dbService=AbsDbHelper.getDbService();
		return dbService.getListByCondition(sqlcondition, AbsFileInfo.class);
	}

	@Override
	public void delFileInfoByCliengGuid(String cliengguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("cliengguid",cliengguid);
		List<Map<String,Object>> list_file=dbService.queryListForMapById("fileinfo.getFileInfoMapListByCliengguid", params);
		if(list_file!=null) {
			String endpoint =configService.getByConfigValueByName("endpoint",null);
			String accessKeyId =configService.getByConfigValueByName("accessKeyId",null);
			String accessKeySecret = configService.getByConfigValueByName("accessKeySecret",null);
			String bucketName =configService.getByConfigValueByName("bucketName",null);
			OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
			for(Map<String,Object> map:list_file) {
				String istoali=map.get("istoali").toString();
				if(MSG.ToAliYes.equals(istoali)) {
					String firstkey=map.get("firstkey")+"";
					ossClient.deleteObject(bucketName, firstkey);
				}else {
					String fullPath = map.get("filepath").toString();
					File fileobj = new File(fullPath);
					if (!fileobj.isDirectory()) {
						fileobj.delete();
					}
				}
				delFileInfoByRowGuid(map.get("rowguid").toString());
			}
			ossClient.shutdown();
		}
	}
	
	
	
	
	@Override
	public void delFileInfoByRowGuid(String rowguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid",rowguid);
		Map<String,Object> file=dbService.queryMapById("fileinfo.getFileMapByRowGuid", params);
		if(file!=null) {
			System.out.println("找到了文件，开始删除....");
			String endpoint =configService.getByConfigValueByName("endpoint",null);
			String accessKeyId =configService.getByConfigValueByName("accessKeyId",null);
			String accessKeySecret = configService.getByConfigValueByName("accessKeySecret",null);
			String bucketName =configService.getByConfigValueByName("bucketName",null);
			OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		    String istoali=file.get("istoali").toString();
			if(MSG.ToAliYes.equals(istoali)) {
				String firstkey=file.get("firstkey")+"";
				ossClient.deleteObject(bucketName, firstkey);
			}else {
				String fullPath = file.get("filepath").toString();
				File fileobj = new File(fullPath);
				if (!fileobj.isDirectory()) {
					fileobj.delete();
				}
			}
		    dbService.execteSqlById("fileinfo.delFileInfoByRowGuid", params);
			ossClient.shutdown();
		}
	}


	@Override
	public List<Map<String, Object>> getFileMapListByCliengguid(String cliengguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("cliengguid", cliengguid);
		return dbService.queryListForMapById("fileinfo.getFileMapListByCliengguid", params);
	}

	@Override
	public Map<String, Object> getFileMapByCliengguid(String cliengguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("cliengguid", cliengguid);
		return dbService.queryMapById("fileinfo.getFileMapListByCliengguid", params);
	}

	@Override
	public List<Map<String, Object>> queryPicFileList(Params params) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		return dbService.queryPageListMapById("fileinfo.queryPicFileList", params);
	}

	@Override
	public long queryPicFileCount(Params params) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		return dbService.queryCountByById("fileinfo.queryPicFileCount", params);
	}
	

}
