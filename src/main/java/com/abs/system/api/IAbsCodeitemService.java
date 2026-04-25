package com.abs.system.api;

import java.util.List;
import java.util.Map;

import com.abs.system.domain.AbsCodeitem;
import com.abs.system.util.Params;


public interface IAbsCodeitemService {
	
	void addCodeitem(AbsCodeitem codeitem);

	void updateCodeitem(AbsCodeitem codeitem);

	void deleteCodeitem(AbsCodeitem codeitem);

	
	long getCountByItemname(String itemname, String itemguid);

	AbsCodeitem getItemInfoByGuid(String rowguid);


	List<AbsCodeitem> getCodelistByOrder(String itemorder);

	void deleteByRwoguid(String rowguid);

	void deleteByItemOrder(String itemorder);

	void updateCodeitemByItemOrder(String itemname, String itemorder, String olditemorder);

	boolean isExist(String itemorder, String itemname, String olditemorder);


	long getItemCountByItemName(String itemname);
	
	public AbsCodeitem getCodeItemByItemOrder(String itemorder);

	List<AbsCodeitem> findCodeItemListByItemOrder(String itemorder);

	long getItemCount(Params params);

	List<Map<String, Object>> findCodeItemMapByItemOrder(String itemorder);

	List<Map<String, Object>> findPageCodeitemList(Params params);

	String getCodeItemTextByOrder(String string, String goodstype);

}
