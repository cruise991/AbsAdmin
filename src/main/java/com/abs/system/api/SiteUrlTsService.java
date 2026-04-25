package com.abs.system.api;

import java.util.List;
import java.util.Map;

import com.abs.system.domain.SiteUrlTsInfo;

public interface SiteUrlTsService {

	List<SiteUrlTsInfo> getSiteListNotTs(String sqlCondition);

	void deletetsInfoByGuid(String rowguid);

	long getCountBySql(String sql_count);

	SiteUrlTsInfo getTsInfoByGuid(String rowguid);

	void updateTsInfo(SiteUrlTsInfo tsinfo);

	void addTsInfo(SiteUrlTsInfo tsinfo);

	boolean isAlreadyExitObject(String rowguid);

	List<Map<String, Object>> queryPageList(Map<String, Object> map);

	long queryPageTotal(Map<String, Object> map);

	void delByGuid(String rowguid);

	List<Map<String, Object>> findTsInfoMapList(String siteurl);

}
