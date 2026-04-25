package com.abs.system.api;

import java.util.List;
import java.util.Map;

import com.abs.system.domain.AbsRoleViewPerm;
import com.abs.system.domain.AbsViewRole;
import com.abs.system.util.Params;

public interface IAbsViewRoleService {

	List<AbsViewRole> getViewRoleList(String sqlCondition2);

	List<Params> getRoleListByViewGuid(String rowguid);

	void deleteByViewGuid(String rowguid);

	void addViewRoleList(List<AbsRoleViewPerm> list);

	void deleteByRoleguid(String roleguid);

	List<Map<String, Object>> getViewListByRoleGuid(String roleguid);

}
