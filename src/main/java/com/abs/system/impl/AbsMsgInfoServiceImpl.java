package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsMsgInfoService;
import com.abs.system.domain.AbsMsgInfo;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;


@Service
public class AbsMsgInfoServiceImpl implements IAbsMsgInfoService{

	@Override
	public void addMsgInfo(AbsMsgInfo msginfo) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.addEntity(msginfo);
		
	}

	@Override
	public List<Map<String, Object>> queryMsgListByUserGuidOfAdmin(Map<String,Object> reqMap) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params(reqMap);
		String sql="select rowguid,isread,msgtitle,content,userguid,datasource,addtime from abs_msginfo where $equal(userguid,userguid) order by addtime desc";
		return dbService.queryPageListMapBySql(sql, params);
	}

	@Override
	public long queryCountByUserGuidOfAdmin(Map<String, Object> reqMap) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params(reqMap);
		String sql="select count(1) from abs_msginfo where $equal(userguid,userguid)";
		return dbService.queryCountBySql(sql, params);
	}

	@Override
	public void updateMsgReadStatus(String rowguid, String isread) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", rowguid);
		params.put("isread", isread);
		String sql_update="update abs_msginfo set isread={isread} where rowguid={rowguid}";
		dbService.execteSql(sql_update,params);
	}

}
