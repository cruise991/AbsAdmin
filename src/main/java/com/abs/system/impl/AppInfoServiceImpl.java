package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsAppInfoService;
import com.abs.system.domain.AbsAppInfo;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;


@Service
public class AppInfoServiceImpl implements IAbsAppInfoService{

	@Override
	public void addAppInfo(AbsAppInfo appinfo) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.addEntity(appinfo);
		
	}

	@Override
	public List<Map<String, Object>> queryAppInfoPageList(Params params) {
		AbsDbService dbService=AbsDbHelper.getDbService();

		String sql="select rowguid,appname,introduction,appkey,appsecret,expiredtime,status,addtime,userguid,token,tokentype,tokenexpired from abs_appinfo where $like(appname,appname) and $equal(userguid,userguid) order by addtime desc";
		return dbService.queryPageListMapBySql(sql, params);
	}

	@Override
	public long queryAppInfoCount(Params params) {
		AbsDbService dbService=AbsDbHelper.getDbService();

		String sql="select count(1) from abs_appinfo where $like(appname,appname) and $equal(userguid,userguid) order by addtime desc";
		return dbService.queryCountBySql(sql, params);
	}

	@Override
	public AbsAppInfo queryAppInfoByGuid(String rowguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		String sql="select rowguid,appname,introduction,appkey,appsecret,expiredtime,status,addtime,userguid,token,tokentype from abs_appinfo where rowguid={rowguid}";
        Params params=new Params();
        params.put("rowguid", rowguid);
		return dbService.queryEntityBySql(AbsAppInfo.class, sql, params);
	}

	@Override
	public void updateAppInfo(AbsAppInfo appinfo) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.updateEntity(appinfo, "rowguid");
		
	}

	@Override
	public void delAppInfoByGuid(String guid) {
		String sql="delete from abs_appinfo where rowguid=\'"+guid+"\'";
		AbsDbService dbService=AbsDbHelper.getDbService();
        dbService.execteSql(sql);
		
		
	}
	
	
	

}
