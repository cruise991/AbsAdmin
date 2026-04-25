package com.abs.system.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsVcodeService;
import com.abs.system.domain.AbsVcodeInfo;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;



@Service
public class AbsVcodeServiceImpl implements IAbsVcodeService{

	@Override
	public void addVcodeEntity(AbsVcodeInfo code) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.addEntity(code);
	}

	@Override
	public boolean isExistVcode(String email, String vcode) {
		LocalDateTime now = LocalDateTime.now();
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("email", email);
		params.put("vcode", vcode);
		// 使用时间戳进行比较
		params.put("expiredtime", now.toString());
		long count=dbService.queryCountByById("vcode.isExistVcode",params);
		if(count>0) {
			return true;
		}
		return false;
	}

}
