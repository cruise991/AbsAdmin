package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsViewRoleService;
import com.abs.system.domain.AbsRoleViewPerm;
import com.abs.system.domain.AbsViewRole;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;

@Service
public class ViewRoleServiceImpl implements IAbsViewRoleService {

	public List<AbsViewRole> getViewRoleList(String sqlCondition) {
		return null;
	}

	@Override
	public void deleteByViewGuid(String rowguid) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Params> getRoleListByViewGuid(String rowguid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addViewRoleList(List<AbsRoleViewPerm> list) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		if (list != null && list.size() > 0) {
          list.forEach(item->{
        	  dbService.addEntity(item);
          });
		}
	}

	@Override
	public void deleteByRoleguid(String roleguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
	    Params params=new Params();
	    params.put("roleguid", roleguid);
	    dbService.execteSqlById("roleinfo.deleteRoelViewPermByGuid", params);
	}

	@Override
	public List<Map<String, Object>> getViewListByRoleGuid(String roleguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
	    Params params=new Params();
	    params.put("roleguid", roleguid);
	    return dbService.queryListForMapById("roleinfo.getViewListByRoleGuid", params);
	}

}
