package com.abs.system.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsJiariService;
import com.abs.system.domain.AbsJiari;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;

@Service
public class IAbsJiariServiceImpl implements IAbsJiariService {

	@Override
	public boolean isExistGuid(String pmdate) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("rowguid", pmdate);
		long count = dbService.queryCountByById("jiari.isExistGuid", params);
		if (count > 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void addJiari(AbsJiari jiari) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.addEntity(jiari);
	}

	@Override
	public List<Map<String,Object>> queryJiaRiListByJiari(String jiari) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("jiari", jiari);
		return dbService.queryListForMapById("jiari.queryJiaRiListByJiari", params);
	}

	@Override
	public void delJiariByGuid(String pmdate) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", pmdate);
		dbService.execteSqlById("jiari.delJiariByGuid", params);
	}

	@Override
	public boolean isJiari(Calendar calendar) {
		int day=calendar.get(Calendar.DAY_OF_WEEK)-1;
		if (day == 6 || day == 0) {
		    return true;
		}
		AbsDbService dbService = AbsDbHelper.getDbService();
	    Date date=calendar.getTime();
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		Params params = new Params();
		params.put("rowguid",sdf.format(date));
		long count = dbService.queryCountByById("jiari.isExistGuid", params);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

}
