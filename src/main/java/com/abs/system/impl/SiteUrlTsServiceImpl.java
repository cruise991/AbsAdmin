package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.SiteUrlTsService;
import com.abs.system.domain.SiteUrlTsInfo;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;


@Service
public class SiteUrlTsServiceImpl implements SiteUrlTsService {

	@Override
	public List<SiteUrlTsInfo> getSiteListNotTs(String sqlCondition) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void deletetsInfoByGuid(String rowguid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getCountBySql(String sql_count) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SiteUrlTsInfo getTsInfoByGuid(String rowguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", rowguid);
		return dbService.queryEntityById(SiteUrlTsInfo.class, "tsinfo.getTsInfoByGuid", params);
	}

	@Override
	public void updateTsInfo(SiteUrlTsInfo tsinfo) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.updateEntity(tsinfo, "rowguid");
	}

	@Override
	public void addTsInfo(SiteUrlTsInfo tsinfo) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		try {
			//dbService.beginTransaction();
			dbService.addEntity(tsinfo);
		//	dbService.commit();
			//dbService.endTransaction();
		}catch (Exception e) {
		//	dbService.rollback();
		}
	}

	@Override
	public boolean isAlreadyExitObject(String rowguid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Map<String, Object>> queryPageList(Map<String, Object> map) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params(map);
		List<Map<String, Object>> list=dbService.queryPageListMapById("tsinfo.queryPageList", params);
		return list;
		
	}


	@Override
	public long queryPageTotal(Map<String, Object> map) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params(map);
		long count=dbService.queryCountByById("tsinfo.queryPageTotal", params);
		return count;
	}


	@Override
	public void delByGuid(String rowguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.delByGuid(rowguid,SiteUrlTsInfo.class);
	}


	@Override
	public List<Map<String, Object>> findTsInfoMapList(String siteurl) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("siteurl", siteurl);
		return dbService.queryListForMapById("tsinfo.findTsInfoMapList", params);
	}



}
