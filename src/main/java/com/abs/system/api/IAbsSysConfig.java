package com.abs.system.api;

import java.util.List;
import java.util.Map;

import com.abs.system.domain.AbsSysConfig;
import com.abs.system.util.Params;

public interface IAbsSysConfig {

	List<AbsSysConfig> getPageList(int pagesize, int i, String sqlcondition);

	long getTotalCount(String sqlcondition);

	AbsSysConfig getByName(String configname);

	String getByConfigValueByName(String signName, String userguid);

	List<Map<String, Object>> getConfigListByName(int pagesize, int pagenum, String configname);

	long getTotalCountByName(String configname);

	boolean isExistConfigName(String configname, String ...rowguid);

	void addConfig(AbsSysConfig config);

	void updateConfig(AbsSysConfig config);

	void deleteConfigByGuid(String rowguid);

	Map<String, Object> getConfigMapByGuid(String rowguid);

	List<Map<String, Object>> getConfigListOfUser(Params param);

	long getTotalCountOfUser(Params param);

	AbsSysConfig getAbsSysConfigByGuid(String rowguid);

	AbsSysConfig getAbsSysConfigByConfigNameAndUserGuid(String configname, String userguid);

	List<Map<String, Object>> getGlobalConfigListByName(int pagesize, int pagenum, String configname);

	long getGlobalTotalCountByName(String configname);

}
