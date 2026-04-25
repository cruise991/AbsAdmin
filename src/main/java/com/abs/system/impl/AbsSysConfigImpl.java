package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionStatus;

import com.abs.system.api.IAbsSysConfig;
import com.abs.system.domain.AbsSysConfig;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.alibaba.druid.sql.parser.Lexer.SavePoint;

@Service
public class AbsSysConfigImpl implements IAbsSysConfig {

	@Override
	public List<AbsSysConfig> getPageList(int pagesize, int pagenum, String sqlcondition) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return dbService.findPageListByCondition(pagesize, pagenum, AbsSysConfig.class, sqlcondition);
	}

	@Override
	public long getTotalCount(String sqlcondition) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		String sql = "select count(1) from abs_sysconfig where 1=1 " + sqlcondition;
		return dbService.count(sql);
	}

	@Override
	public AbsSysConfig getByName(String configname) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		DataSourceTransactionManager manager = dbService.getDataSourceTransactionManager();
		Params params = new Params();
		params.put("configname", configname);
		return dbService.queryEntityById(AbsSysConfig.class, "", params);
	}

	@Override
	public String getByConfigValueByName(String signName,String userguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("configname", signName);
		if(StrUtil.isStrBlank(userguid)) {
			Map<String, Object> map = dbService.queryMapById("sysconfig.getByConfigValueByName", params);
			if (map != null) {
				return map.get("configvalue") + "";
			} else {
				return null;
			}
			
		}else {
			params.put("userguid", userguid);
			Map<String, Object> map = dbService.queryMapById("sysconfig.getByConfigValueByNameOfUser", params);
			if (map != null) {
				return map.get("configvalue") + "";
			} else {
				return null;
			}
		}
	}

	@Override
	public List<Map<String, Object>> getConfigListByName(int pagesize, int pagenum, String configname) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("pagesize", pagesize);
		params.put("pagenum", pagenum);
		params.put("configname", configname);
		return dbService.queryListForMapById("sysconfig.findPageSysConfigList", params);
	}

	@Override
	public long getTotalCountByName(String configname) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("configname", configname);
		return dbService.queryCountByById("sysconfig.getTotalCountByName", params);
	}
	
	
	@Override
	public List<Map<String, Object>> getGlobalConfigListByName(int pagesize, int pagenum, String configname) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("pagesize", pagesize);
		params.put("pagenum", pagenum);
		params.put("configname", configname);
		return dbService.queryListForMapById("sysconfig.findGlobalSysConfigList", params);
	}

	@Override
	public long getGlobalTotalCountByName(String configname) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("configname", configname);
		return dbService.queryCountByById("sysconfig.getGlobalTotalCountByName", params);
	}

	@Override
	public boolean isExistConfigName(String configname,String ...rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("configname", configname);
		if(rowguid.length>0) {
			params.put("rowguid", rowguid[0]);
		}
		if(rowguid.length>1) {
			params.put("userguid", rowguid[1]);
		}
		long count = dbService.queryCountByById("sysconfig.isExistConfigName", params);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void addConfig(AbsSysConfig config) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.addEntity(config);
		
	}

	@Override
	public void updateConfig(AbsSysConfig config) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.updateEntity(config,"rowguid");
		
	}

	@Override
	public void deleteConfigByGuid(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.delByGuid(rowguid, AbsSysConfig.class);
	}

	@Override
	public Map<String, Object> getConfigMapByGuid(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", rowguid);
		return dbService.queryMapById("sysconfig.getConfigMapByGuid", params);
	}

	@Override
	public List<Map<String, Object>> getConfigListOfUser(Params param) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return dbService.queryPageListMapById("sysconfig.findPageSysConfigList", param);
	}

	@Override
	public long getTotalCountOfUser(Params param) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return dbService.queryCountByById("sysconfig.getTotalCountByName", param);
	}

	@Override
	public AbsSysConfig getAbsSysConfigByGuid(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid",rowguid);
		return dbService.queryEntityById(AbsSysConfig.class, "sysconfig.getAbsSysConfigByGuid", params);
	}

	@Override
	public AbsSysConfig getAbsSysConfigByConfigNameAndUserGuid(String configname, String userguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("configname",configname);
		params.put("userguid",userguid);
		return dbService.queryEntityById(AbsSysConfig.class, "sysconfig.getAbsSysConfigByConfigNameAndUserGuid", params);
	}



}
