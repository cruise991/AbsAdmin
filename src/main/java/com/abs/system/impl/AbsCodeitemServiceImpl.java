package com.abs.system.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.system.api.IAbsCodeitemService;
import com.abs.system.domain.AbsCodeitem;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.Params;



@Service
public class AbsCodeitemServiceImpl implements IAbsCodeitemService {
	
	@Override
	public void addCodeitem(AbsCodeitem codeitem) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.addEntity(codeitem);
	}

	@Override
	public void updateCodeitem(AbsCodeitem codeitem) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.updateEntity(codeitem,"rowguid");
	}

	@Override
	public void deleteCodeitem(AbsCodeitem codeitem) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		dbService.delEntity(codeitem);
	}

	@Override
	public List<Map<String, Object>> findPageCodeitemList(Params params){
		AbsDbService dbService=AbsDbHelper.getDbService();
		List<Map<String, Object>> list=dbService.queryListForMapById("codeitem.findPageCodeitemList", params);
		return list;
	}


	/**
	 * is have the same itemname
	 */
	@Override
	public long getCountByItemname(String itemname, String itemorder) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("itemname", itemname);
		params.put("itemorder", itemorder);
		long count=dbService.queryCountByById("codeitem.getItemCountByItemName", params);
		return count;
	}

	@Override
	public long getItemCountByItemName(String itemname) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put(itemname, itemname);
		return dbService.queryCountByById("codeitem.getItemCountByItemName", params);
	}

	@Override
	public AbsCodeitem getItemInfoByGuid(String rowguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", rowguid);
		return dbService.queryEntityById(AbsCodeitem.class, "codeitem.getItemInfoByGuid", params);
	}


	@Override
	public List<AbsCodeitem> getCodelistByOrder(String itemorder) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("itemorder", itemorder);
		return dbService.queryListForEntityBySqlId(AbsCodeitem.class,"codeitem.getCodelistByOrder",params);

	}

	@Override
	public void deleteByRwoguid(String rowguid) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("rowguid", rowguid);
		dbService.execteSqlById("codeitem.delCodeByGuid", params);
	}

	@Override
	public void deleteByItemOrder(String itemorder) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("itemorder", itemorder);
		dbService.execteSqlById("codeitem.delCodeByItemOrder", params);
	}

	@Override
	public AbsCodeitem getCodeItemByItemOrder(String itemorder) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("itemorder", itemorder);
		return dbService.queryEntityById(AbsCodeitem.class, "codeitem.getCodeItemByItemOrder", params);
	}

	@Override
	public void updateCodeitemByItemOrder(String itemname, String itemorder,String olditemorder) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("itemorder", itemorder);
		params.put("itemname", itemname);
		params.put("olditemorder", olditemorder);
		dbService.execteSqlById("codeitem.updateCodeitemByItemOrder", params);

	}


	@Override
	public boolean isExist(String itemorder, String itemname,String olditemorder) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("itemname", itemname);
		params.put("itemorder", itemorder);
		params.put("olditemorder", olditemorder);
		long count=dbService.queryCountByById("codeitem.isExist", params);
		if(count>0) {
			return true;
		}else {
			return false;
		}
	
	}

	@Override
	public List<AbsCodeitem> findCodeItemListByItemOrder(String itemorder) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("itemorder",itemorder);
		return dbService.queryListForEntityBySqlId(AbsCodeitem.class, "codeitem.getCodelistByOrder", params);
	}

	@Override
	public long getItemCount(Params params) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		long count=dbService.queryCountByById("codeitem.getItemCount",params);
		return count;
	}

	@Override
	public List<Map<String, Object>> findCodeItemMapByItemOrder(String itemorder) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("itemorder",itemorder);
		return dbService.queryListForMapById("codeitem.findCodeItemMapByItemOrder", params);
	}

	@Override
	public String getCodeItemTextByOrder(String itemorder, String goodstype) {
		AbsDbService dbService=AbsDbHelper.getDbService();
		Params params=new Params();
		params.put("itemorder",itemorder);
		params.put("itemvalue",goodstype);
		Map<String,Object> map=dbService.queryMapById("codeitem.getCodeItemTextByOrder", params);
		if(map!=null) {
			return map.get("itemtext")+"";
		}else {
			return "";
		}
	}



}
