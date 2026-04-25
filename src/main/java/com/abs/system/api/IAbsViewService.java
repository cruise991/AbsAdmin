package com.abs.system.api;

import java.util.List;
import java.util.Map;

import com.abs.system.domain.AbsViewInfo;
import com.abs.system.util.Params;

public interface IAbsViewService {

	List<AbsViewInfo> getViewListByIsRoot(String isroot);

	boolean isExistSameViewName(String sqlCondition, String rowguid);


	void save(AbsViewInfo view);

	AbsViewInfo findViewInfoByGuid(String rowguid);

	void updateView(AbsViewInfo view);

	void deleteViewByGuid(String rowguid);

	List<Map<String, Object>> findPageMapByViewName(Params params);
	

	long getViewCountByViewName(Params params);

	List<AbsViewInfo> getViewListByParentGuid(String parentguid);

	List<Map<String,Object>> getViewMapListByIsRoot(String isroot);

	List<Map<String, Object>> getViewListMapByParentGuid(String string);


}
