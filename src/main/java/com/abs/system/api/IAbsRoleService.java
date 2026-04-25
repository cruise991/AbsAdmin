package com.abs.system.api;

import java.util.List;
import java.util.Map;

import com.abs.system.domain.AbsRoleInfo;

public interface IAbsRoleService extends IAbsCommonService{

	List<AbsRoleInfo> getRoleList(int pagesize, int pagenum, Class<AbsRoleInfo> clazz);

	String getFieldByCondition(String sqlCondition, String string);

	List<AbsRoleInfo> getPageList(int pagesize, int pagenum, String sqlcondition);

	AbsRoleInfo getOneByGuid(String rowguid);

	void delRole(String rowguid);

	void updateRole(AbsRoleInfo roleinfo);

	void addRole(AbsRoleInfo roleinfo);

	long getTotalNumByRoleName(String rolename);

	List<Map<String, Object>> getRolePageListByRoleName(int pagesize, int pagenum, String rolename);

	long isExistSameRoleName(String rolename, String rowguid);

	Map<String,Object> getRoleNameByGuid(String rowguid);

	List<Map<String, Object>> getRoleMapList();


}
