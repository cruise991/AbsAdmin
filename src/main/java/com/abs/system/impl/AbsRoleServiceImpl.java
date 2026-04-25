package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsRoleService;
import com.abs.system.domain.AbsRoleInfo;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;

@Service
public class AbsRoleServiceImpl implements IAbsRoleService {

	@Override
	public List<AbsRoleInfo> getRoleList(int pagesize, int pagenum, Class<AbsRoleInfo> clazz) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		return dbService.findPageList(pagesize, pagenum, clazz);
	}

	@Override
	public String getFieldByCondition(String sqlCondition, String colname) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		return dbService.getFieldByCondition(sqlCondition, colname, AbsRoleInfo.class);
	}

	@Override
	public List<AbsRoleInfo> getPageList(int pagesize, int pagenum, String sqlcondition) {
		AbsDbService dbService=AbsDbHelper.getDbService();

		return dbService.findPageListByCondition(pagesize, pagenum, AbsRoleInfo.class, sqlcondition);
	}

	@Override
	public AbsRoleInfo getOneByGuid(String rowguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();

		return dbService.findOneEntityByGuid(rowguid, AbsRoleInfo.class);
	}

	@Override
	public void delRole(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.delByGuid(rowguid, AbsRoleInfo.class);
	}

	@Override
	public void updateRole(AbsRoleInfo roleinfo) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.updateEntity(roleinfo, "rowguid");

	}

	@Override
	public void addRole(AbsRoleInfo roleinfo) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.addEntity(roleinfo);
	}

	@Override
	public long getTotalNumByRoleName(String rolename) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("rolename", rolename);
		return dbService.queryCountByById("roleinfo.getTotalNumByRoleName", params);
	}

	@Override
	public List<Map<String, Object>> getRolePageListByRoleName(int pagesize, int pagenum, String rolename) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("rolename", rolename);
		params.put("pagesize", pagesize);
		params.put("pagenum", pagenum);
		return dbService.queryListForMapById("roleinfo.getRolePageListByRoleName", params);
	}

	@Override
	public long isExistSameRoleName(String rolename, String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("rolename", rolename);
		params.put("rowguid", rowguid);
		return dbService.queryCountByById("roleinfo.isExistSameRoleName", params);
	}

	@Override
	public Map<String, Object> getRoleNameByGuid(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("rowguid", rowguid);
		return dbService.queryMapById("roleinfo.getRoleNameByGuid", params);
	}

	@Override
	public <T> void add(T entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void delete(T entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void delByGuid(Class<?> clazz, String rowguid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> void update(T entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, Object>> getRoleMapList() {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return dbService.queryListForMapById("roleinfo.getRoleMaplist", null);
	}



}
