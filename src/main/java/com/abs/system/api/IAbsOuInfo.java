package com.abs.system.api;

import java.util.List;
import java.util.Map;

import com.abs.system.domain.AbsOuInfo;
import com.abs.system.util.Params;

public interface IAbsOuInfo {

	List<Map<String, Object>> getOuPageList(Params params);

	long queryTotalCount(Params params);

	void addOuInfo(AbsOuInfo ouinfo);

	AbsOuInfo queryOuInfoByGuid(String rowguid);

	Map<String, Object> getOuInfoByGuid(String rowguid);

	void updateOuInfo(AbsOuInfo ouinfo);

	void deleteOuInfoByGuid(String rowguid);

	List<Map<String, Object>> queryOuList();

	List<Map<String, Object>> getOuInfoMapList();

}
