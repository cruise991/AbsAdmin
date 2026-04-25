package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsViewService;
import com.abs.system.domain.AbsViewInfo;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;

@Service
public class AbsViewServiceImpl implements IAbsViewService {

	@Override
	public List<AbsViewInfo> getViewListByIsRoot(String isroot) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("isroot", isroot);
		return dbService.queryListForEntityBySqlId(AbsViewInfo.class, "viewinfo.getViewList", params);
	}



	@Override
	public boolean isExistSameViewName(String viewname, String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("viewname", viewname);
		params.put("rowguid", rowguid);
		long count = dbService.queryCountByById("viewinfo.isExistSameViewName", params);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public long getViewCountByViewName(Params params) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return  dbService.queryCountByById("viewinfo.getViewCountByViewName", params);

	}
	
	@Override
	public List<Map<String, Object>> findPageMapByViewName(Params params) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		return dbService.queryListForMapById("viewinfo.findPageMapByViewName", params);

	}

	@Override
	public void save(AbsViewInfo view) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.addEntity(view);
	}

	@Override
	public AbsViewInfo findViewInfoByGuid(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", rowguid);
		return dbService.queryEntityById(AbsViewInfo.class,"viewinfo.findViewInfoByGuid", params);
	}

	@Override
	public void updateView(AbsViewInfo view) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		dbService.updateEntity(view, "rowguid");

	}

	@Override
	public void deleteViewByGuid(String rowguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("rowguid", rowguid);
		 dbService.execteSqlById("viewinfo.deleteViewByGuid", params);
	}

	@Override
	public List<AbsViewInfo> getViewListByParentGuid(String parentguid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("parentguid", parentguid);
		return dbService.queryListForEntityBySqlId(AbsViewInfo.class, "viewinfo.getViewListByParentGuid", params);
	}

	@Override
	public List<Map<String,Object>> getViewMapListByIsRoot(String isroot) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("isroot",isroot);
		return dbService.queryListForMapById("viewinfo.getViewMapListByIsRoot", params);
	}

	@Override
	public List<Map<String, Object>> getViewListMapByParentGuid(String guid) {
		AbsDbService dbService = AbsDbHelper.getDbService();
		Params params = new Params();
		params.put("parentguid", guid);
		return dbService.queryListForMapById("viewinfo.getViewListMapByParentGuid", params);
	}

}
