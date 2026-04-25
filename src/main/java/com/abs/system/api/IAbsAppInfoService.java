package com.abs.system.api;

import java.util.List;
import java.util.Map;

import com.abs.system.domain.AbsAppInfo;
import com.abs.system.util.Params;

public interface IAbsAppInfoService {

	void addAppInfo(AbsAppInfo appinfo);

	List<Map<String, Object>> queryAppInfoPageList(Params params);

	long queryAppInfoCount(Params params);

	AbsAppInfo queryAppInfoByGuid(String rowguid);

	void updateAppInfo(AbsAppInfo appinfo);

	void delAppInfoByGuid(String guid);

}
