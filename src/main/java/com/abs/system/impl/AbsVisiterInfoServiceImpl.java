package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsVisiterInfoService;
import com.abs.system.domain.AbsVisiterInfo;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;

@Service
public class AbsVisiterInfoServiceImpl implements IAbsVisiterInfoService {

	@Override
	public void addVisterInfo(AbsVisiterInfo visiter) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.addEntity(visiter);

	}

	@Override
	public List<Map<String, Object>> queryPageList(Params params) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		String sql = "select rowguid,visiterip,visittime,position from abs_visiterinfo order by visittime desc";
		return dbService.queryPageListMapBySql(sql, params);
	}

	@Override
	public long queryTotalCount(Params params) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		String sql = "select count(1) from abs_visiterinfo";
		return dbService.queryCountBySql(sql);
	}

}
