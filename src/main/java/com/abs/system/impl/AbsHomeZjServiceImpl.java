package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsHomeZjService;
import com.abs.system.domain.AbsHomeUser;
import com.abs.system.domain.AbsHomeZj;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;


@Service
public class AbsHomeZjServiceImpl implements IAbsHomeZjService {

	@Override
	public boolean isExistSameCpath(String cpath, String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("cpath", cpath);
		params.put("rowguid", rowguid);
		long count = dbService.queryCountByById("homezj.isExistSameCpath", params);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void addHomezj(AbsHomeZj homezj) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.addEntity(homezj);
	}

	@Override
	public List<Map<String, Object>> getHomezjPageList(Params params) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return dbService.queryPageListMapById("homezj.getHomezjPageList", params);
	}

	@Override
	public long getHomezjCount(Params params) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return dbService.queryCountByById("homezj.getHomezjCount", params);
	}

	@Override
	public void delHomezjByGuid(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("rowguid", rowguid);
		dbService.execteSqlById("homezj.delHomezjByGuid", params);
	}

	@Override
	public List<Map<String, Object>> queryHomeZjListByUserguid(String userguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("userguid", userguid);
		return dbService.queryListForMapById("homezj.queryHomeZjListByUserguid", params);
	}

	@Override
	public Map<String, Object> queryHOmezjMapByGuid(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("rowguid", rowguid);
		return dbService.queryMapById("homezj.queryHOmezjMapByGuid", params);
	}

	@Override
	public AbsHomeZj queryHomeZjInfoByGuid(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("rowguid", rowguid);
		return dbService.queryEntityById(AbsHomeZj.class, "homezj.queryHOmezjMapByGuid", params);
	}

	@Override
	public void updateHomezj(AbsHomeZj homezj) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.updateEntity(homezj, "rowguid");

	}

	@Override
	public void addHomezjUser(AbsHomeUser homeuser) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.addEntity(homeuser);
	}

	@Override
	public boolean isExistSameHomeUser(String userguid, String zjguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("userguid", userguid);
		params.put("czjguid", zjguid);
		long count = dbService.queryCountByById("homezj.isExistSameHomeUser", params);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void delHomezjUserByUserGuidAndZjGuid(String userguid, String zjguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("userguid", userguid);
		params.put("czjguid", zjguid);
		dbService.execteSqlById("homezj.delHomezjUserByUserGuidAndZjGuid", params);

	}

	@Override
	public String queryCpathByZjGuid(String czjguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("rowguid", czjguid);
		Map<String,Object> zjmap=dbService.queryMapById("homezj.queryHOmezjMapByGuid", params);
		if(zjmap!=null) {
			return zjmap.get("cpath").toString();
		}else {
			return "";
		}
	}

}
