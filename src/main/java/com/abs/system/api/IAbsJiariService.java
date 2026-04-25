package com.abs.system.api;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.abs.system.domain.AbsJiari;

public interface IAbsJiariService {

	boolean isExistGuid(String pmdate);

	void addJiari(AbsJiari jiari);

	List<Map<String, Object>> queryJiaRiListByJiari(String jiari);

	void delJiariByGuid(String pmdate);
	

	boolean isJiari(Calendar calendar);

}
