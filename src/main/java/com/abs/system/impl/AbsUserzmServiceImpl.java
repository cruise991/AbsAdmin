package com.abs.system.impl;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsUserzmService;
import com.abs.system.domain.AbsUserZm;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;

@Service
public class AbsUserzmServiceImpl implements IAbsUserzmService {

	@Override
	public void addUserZm(AbsUserZm zm) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.addEntity(zm);
	}

	@Override
	public AbsUserZm getUserzmByUserguid(String userguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("userguid", userguid);
		return dbService.queryEntityById(AbsUserZm.class, "userzm.getUserzmByUserguid", params);
	}

	@Override
	public void updateUserZm(AbsUserZm userzm) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.updateEntity(userzm, "rowguid");
	}

}
