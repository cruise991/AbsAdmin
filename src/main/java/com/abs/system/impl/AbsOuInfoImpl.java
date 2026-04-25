package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsOuInfo;
import com.abs.system.domain.AbsOuInfo;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;

@Service
public class AbsOuInfoImpl implements IAbsOuInfo {

	@Override
	public List<Map<String, Object>> getOuPageList(Params params) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return dbService.queryListForMapById("ouinfo.getOuPageListByOuName", params);
	}

	@Override
	public long queryTotalCount(Params params) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return dbService.queryCountByById("ouinfo.queryTotalCount", params);
	}

	@Override
	public void addOuInfo(AbsOuInfo ouinfo) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.addEntity(ouinfo);

	}

	@Override
	public AbsOuInfo queryOuInfoByGuid(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("rowguid", rowguid);
		return dbService.queryEntityById(AbsOuInfo.class, "ouinfo.queryOuInfoByGuid", params);
	}

	@Override
	public Map<String, Object> getOuInfoByGuid(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("rowguid", rowguid);
		return dbService.queryMapById("ouinfo.queryOuInfoByGuid", params);
	}

	@Override
	public void updateOuInfo(AbsOuInfo ouinfo) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.updateEntity(ouinfo, "rowguid");
	}

	@Override
	public void deleteOuInfoByGuid(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.delByGuid(rowguid, AbsOuInfo.class);
	}

	@Override
	public List<Map<String, Object>> queryOuList() {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return dbService.queryListForMapById("ouinfo.queryOuList");
	}

	@Override
	public List<Map<String, Object>> getOuInfoMapList() {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return dbService.queryListForMapById("ouinfo.queryOuList");
	}

}
