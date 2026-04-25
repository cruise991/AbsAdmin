package com.abs.system.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abs.system.api.IAbsCodeitemService;
import com.abs.system.domain.AbsCodeitem;
import com.abs.system.util.AbsSessionHelper;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/codeitem")
public class AbsCodeItemController {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private Logger logger = null;

	@Autowired
	private IAbsCodeitemService codeService;

	public AbsCodeItemController() {
		logger = LoggerFactory.getLogger(AbsCodeItemController.class);
	}

	
	@ResponseBody
	@RequestMapping(value = "/initcodeitem")
	public JSONObject editcode_init(@RequestBody Map<String,String> reqMap) {
		logger.info("用户传入参数P:{}",reqMap.toString());
		String itemorder=reqMap.get("itemorder");
		AbsCodeitem code = codeService.getCodeItemByItemOrder(itemorder);
	    return BuildJsonOfObject.getJsonString(Params.getRecordByObject(code).toJSObject(), MSG.SUCCESSCODE);
	}



	@ResponseBody
	@RequestMapping(value = "/addcodeitem", produces = "application/json; charset=utf-8")
	public JSONObject addCodeItem(@RequestBody Map<String, String> reqMap, HttpServletRequest requset) {
		logger.info("************begin addcodeitem**************");
		String token = requset.getHeader("token");
		String userguid = AbsSessionHelper.getTokenFromCache(token);
		String itemname = reqMap.get("itemname");
		String itemorder = reqMap.get("itemorder");
		long num = 0;
		if (StrUtil.isNotBlank(itemname)) {
			num = codeService.getCountByItemname(itemname, itemorder);
		}
		if (num > 0) {
			return BuildJsonOfObject.getJsonString(MSG.ExitSameItemName,MSG.FAILCODE);
		} else {
			AbsCodeitem codeitem = new AbsCodeitem(); 
			codeitem = StrUtil.getObjFromParams(reqMap, codeitem);
			codeitem.setRowguid(UUID.randomUUID().toString());
			codeitem.setOperateguid(userguid);
			codeitem.setOperatedate(new Date());
			codeService.addCodeitem(codeitem);
			logger.info("************end addcodeitem success***************");
			return BuildJsonOfObject.getJsonString(MSG.saveSuccess,MSG.SUCCESSCODE);
		}

	}


	@ResponseBody
	@RequestMapping(value = "/getcodeitemlist", produces = "application/json; charset=utf-8")
	public JSONObject getCodeItemList(@RequestBody Map<String, Object> reqMap) {
		logger.info("用户传入参数为：{}",reqMap.toString());
		Params params=new Params(reqMap);
		List<Map<String,Object>> list_item = codeService.findPageCodeitemList(params);
		for (Map<String,Object> item : list_item) {
			if(item.get("operatedate")!=null) {
				Object operateDateObj = item.get("operatedate");
				Date date = null;
				if (operateDateObj instanceof Date) {
					date = (Date) operateDateObj;
				} else if (operateDateObj instanceof LocalDateTime) {
					// 将LocalDateTime转换为Date
					date = Date.from(((LocalDateTime) operateDateObj).atZone(ZoneId.systemDefault()).toInstant());
				}
				if (date != null) {
					item.put("operate", sdf.format(date));
				}
			}
			
		}
		long totalcount = codeService.getItemCount(params);
		return BuildJsonOfObject.getJsonString(list_item, totalcount,MSG.SUCCESSCODE);
	}

	
	@ResponseBody
	@RequestMapping(value = "/addsubcodeitem", produces = "application/json; charset=utf-8")
	public JSONObject addSubcodeitem(@RequestBody Map<String, String> reqMap) {
		
		logger.info("开始调用addsubcodeitem 当前用户传入参数为：{}",reqMap.toString());
		String itemorder = reqMap.get("itemorder")==null?reqMap.get("sub_itemorder"):reqMap.get("itemorder");
		String itemname = reqMap.get("itemname")==null?reqMap.get("sub_itemname"):reqMap.get("itemname");
		String rowguid = reqMap.get("rowguid")==null?reqMap.get("sub_rowguid"):reqMap.get("rowguid");
		AbsCodeitem item=null;
		if(StrUtil.isNotBlank(rowguid)) {
			item = codeService.getItemInfoByGuid(rowguid);
		}
	    if(item==null) {
	    	item=new AbsCodeitem();
			item.setItemtext(reqMap.get("itemtext")==null?reqMap.get("sub_itemtext"):reqMap.get("itemtext"));
			item.setItemvalue(reqMap.get("itemvalue")==null?reqMap.get("sub_itemvalue"):reqMap.get("itemvalue"));
			item.setOperatedate(new Date());
			item.setItemname(itemname);
			item.setItemorder(itemorder);
			item.setRowguid(UUID.randomUUID().toString());
			codeService.addCodeitem(item);
	    }else {
			item.setItemtext(reqMap.get("itemtext")==null?reqMap.get("sub_itemtext"):reqMap.get("itemtext"));
			item.setItemvalue(reqMap.get("itemvalue")==null?reqMap.get("sub_itemvalue"):reqMap.get("itemvalue"));
			item.setRemark(reqMap.get("remark")==null?reqMap.get("sub_remark"):reqMap.get("remark"));
			item.setOperatedate(new Date());
			codeService.updateCodeitem(item);
	    }
		return BuildJsonOfObject.getJsonString(MSG.saveSuccess,MSG.SUCCESSCODE);
	}


	@ResponseBody
	@RequestMapping(value = "/getcodeitembyorder")
	public JSONObject getCodeItemByItemOrder(@RequestBody Map<String, String> reqMap) {
		logger.info("用户传入参数为:{}",reqMap.toString());
		String itemorder = reqMap.get("itemorder");
		List<AbsCodeitem> list = codeService.getCodelistByOrder(itemorder);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<JSONObject> list_map = new ArrayList<JSONObject>();
		String itemname=null;
		for (AbsCodeitem it : list) {
			Params r = Params.getRecordByObject(it);
			r.setColumn("operate", sdf.format(it.getOperatedate()));
			itemname=it.getItemname();
			list_map.add(r.toJSObject());
		}
		return BuildJsonOfObject.getJsonString(list_map,MSG.SUCCESSCODE,itemname);

	}

	@ResponseBody
	@RequestMapping(value = "/getiteminfobyguid", produces = "application/json; charset=utf-8")
	public JSONObject getItemInfoByGuid(@RequestBody Map<String, String> reqMap) {
		logger.info("*******begin call function getiteminfobyguid *********");
		logger.info(reqMap.toString());
		String rowguid = reqMap.get("guid");
		AbsCodeitem item = codeService.getItemInfoByGuid(rowguid);
		JSONObject jsonRtn = new JSONObject();
		jsonRtn.put("itemname", item.getItemname());
		jsonRtn.put("itemvalue", item.getItemvalue());
		jsonRtn.put("itemtext", item.getItemtext());
		logger.info("*******end call function getiteminfobyguid *********");
		return BuildJsonOfObject.getJsonString(jsonRtn, MSG.SUCCESSCODE);

	}


	@ResponseBody
	@RequestMapping(value = "/delsubitem", produces = "application/json; charset=utf-8")
	public JSONObject delSubItem(@RequestBody Map<String, String> reqMap) {
		logger.info("************delsubitem{}***************",reqMap.toString());
		String rowguid = reqMap.get("guid");
		codeService.deleteByRwoguid(rowguid);
		logger.info("*************end delsunitem**************");
		return BuildJsonOfObject.getJsonString(MSG.delSuccess,MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping(value = "/delsubitembyitemorder", produces = "application/json; charset=utf-8")
	public JSONObject delSubItembyItemOrder(@RequestBody Map<String, String> reqMap) {
		String itemorder = reqMap.get("itemorder");
		codeService.deleteByItemOrder(itemorder);
		return BuildJsonOfObject.getJsonString(MSG.delSuccess, MSG.SUCCESSCODE);
	}


	@ResponseBody
	@RequestMapping(value = "/initcodeitem", produces = "application/json; charset=utf-8")
	public JSONObject initCodeItem(@RequestBody Map<String, String> reqMap) {
		String itemorder = reqMap.get("itemorder");
		AbsCodeitem item = codeService.getCodeItemByItemOrder(itemorder);
		JSONObject jsonRtn=Params.getRecordByObject(item).toJSObject();
		return BuildJsonOfObject.getJsonString(jsonRtn, MSG.SUCCESSCODE);
	}


	@ResponseBody
	@RequestMapping(value = "/editcodeitem", produces = "application/json; charset=utf-8")
	public JSONObject editCodeItem(@RequestBody Map<String, String> reqMap) {
		logger.info("当前传入参数:{}",reqMap.toString());
		String itemorder = reqMap.get("itemorder");
		String itemname = reqMap.get("itemname");
		String olditemorder = reqMap.get("olditemorder");
		if (codeService.isExist(itemorder, itemname, olditemorder)) {
			return BuildJsonOfObject.getJsonString(MSG.ExitSameItemName,MSG.FAILCODE);
		}
		codeService.updateCodeitemByItemOrder(itemname, itemorder, olditemorder);
		return BuildJsonOfObject.getJsonString(MSG.updateSuccess, MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping(value = "/getitemlist", produces = "application/json; charset=utf-8")
	public JSONObject getItemList(@RequestBody Map<String, String> reqMap) {
		String itemorder = reqMap.get("itemorder");
		List<AbsCodeitem> list = codeService.findCodeItemListByItemOrder(itemorder);
		return BuildJsonOfObject.getJsonString(list, MSG.SUCCESSCODE);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getitembyorder", produces = "application/json; charset=utf-8")
	public JSONObject getItemByOrder(@RequestBody Map<String, String> reqMap) {
		String itemorder = reqMap.get("itemorder");
		List<Map<String, Object>> list = codeService.findCodeItemMapByItemOrder(itemorder);
		JSONArray arr=new JSONArray();
		if(list!=null && list.size()>0) {
			for(Map map:list) {
				
				if(!StrUtil.isObjBlank(map.get("itemtext"))) {
					JSONObject o=new JSONObject();
					o.put("text", map.get("itemtext"));
					o.put("value", map.get("itemvalue"));
					arr.add(o);
				}
	
			}
		}
		return BuildJsonOfObject.getJsonString(arr, MSG.SUCCESSCODE);
	}
	
	
	public static void main(String[] args) {
		String sql="update abs_codeitem set itemorder={itemorder},itemname={itemname} where itemorder={olditemorder}";
		String sqlit_regex=" and | or |,| where ";
		for(String s:sql.split(sqlit_regex)) {
			System.out.println(s);
		}
		
		
		String sql2="select count(1) from abs_codeitem where (itemorder='3123123' or itemname='213123') and $unequal(itemorder,olditemorder)";
		for(String f:sql2.split(sqlit_regex)) {
			System.out.println(f);
		}
	}
	

}
