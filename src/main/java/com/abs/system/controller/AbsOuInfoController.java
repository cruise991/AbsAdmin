package com.abs.system.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.abs.system.api.IAbsOuInfo;
import com.abs.system.domain.AbsOuInfo;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/ouinfo")
public class AbsOuInfoController {

	private Logger logger = LoggerFactory.getLogger(AbsOuInfoController.class);

	@Autowired
	private IAbsOuInfo ouService;;

	/**
	 * 列表
	 * 
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getpagelist")
	public JSONObject getOuPageList(@RequestBody Map<String, Object> map) {
		Params params = new Params(map);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> list_ou = ouService.getOuPageList(params);
		long totalcount = ouService.queryTotalCount(params);
		if (list_ou != null && list_ou.size() > 0) {
			for (Map<String, Object> ou : list_ou) {
				Object addtimeObj = ou.get("addtime");
				Date date = null;
				if (addtimeObj instanceof Date) {
					date = (Date) addtimeObj;
				} else if (addtimeObj instanceof LocalDateTime) {
					// 将LocalDateTime转换为Date
					date = Date.from(((LocalDateTime) addtimeObj).atZone(ZoneId.systemDefault()).toInstant());
				}
				if (date != null) {
					ou.put("addtime", sdf.format(date));
				}
			}
		}
		return BuildJsonOfObject.getJsonString(list_ou, totalcount, MSG.SUCCESSCODE);
	}

	/**
	 * 新增
	 */

	@ResponseBody
	@RequestMapping(value = "/addouinfo")
	public JSONObject addOuInfo(@RequestBody Map<String, Object> map) {
		logger.info("addouinfo：{}", map.toString());
		String ouname = map.get("ouname").toString();
		String oucode = map.get("oucode").toString();
		String ouaddresstel = map.get("ouaddresstel")==null?"":map.get("ouaddresstel").toString();
		String oubankaccount = map.get("oubankaccount")==null?"":map.get("oubankaccount").toString();
		String remark = map.get("remark")==null?"":map.get("remark").toString();
		AbsOuInfo ouinfo = new AbsOuInfo();
		ouinfo.setAddtime(new Date());
		ouinfo.setOuaddresstel(ouaddresstel);
		ouinfo.setOubankaccount(oubankaccount);
		ouinfo.setOucode(oucode);
		ouinfo.setOuname(ouname);
		ouinfo.setRemark(remark);
		ouinfo.setRowguid(UUID.randomUUID().toString());
		ouService.addOuInfo(ouinfo);
		return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping(value = "/editouinfo")
	public JSONObject editOuInfo(@RequestBody Map<String, Object> map) {
		logger.info("addouinfo：{}", map.toString());
		String ouname = map.get("ouname").toString();
		String oucode = map.get("oucode").toString();
		String ouaddresstel = map.get("ouaddresstel")==null?"":map.get("ouaddresstel").toString();
		String oubankaccount = map.get("oubankaccount")==null?"":map.get("oubankaccount").toString();
		String remark = map.get("remark")==null?"":map.get("remark").toString();
		String rowguid = map.get("rowguid").toString();
		AbsOuInfo ouinfo = ouService.queryOuInfoByGuid(rowguid);
		ouinfo.setOuaddresstel(ouaddresstel);
		ouinfo.setOubankaccount(oubankaccount);
		ouinfo.setOucode(oucode);
		ouinfo.setOuname(ouname);
		ouinfo.setRemark(remark);
		ouinfo.setRowguid(rowguid);
		ouService.updateOuInfo(ouinfo);
		return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
	}

	/**
	 * 获取到ouinfo
	 */
	@ResponseBody
	@RequestMapping(value = "/getouinfobyguid")
	public JSONObject getOuInfoByGuid(@RequestBody Map<String, Object> map) {
		logger.info("getouinfobyguid：{}", map.toString());
		String rowguid = map.get("rowguid").toString();
		Map<String, Object> ou_map = ouService.getOuInfoByGuid(rowguid);
		return BuildJsonOfObject.getJsonString(ou_map, MSG.SUCCESSCODE);
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "/delbyguid")
	public JSONObject delOuInfoByGuid(@RequestBody Map<String, Object> map) {
		logger.info("delbyguid：{}", map.toString());
		String rowguid = map.get("rowguid").toString();
		ouService.deleteOuInfoByGuid(rowguid);
		return BuildJsonOfObject.getJsonString(MSG.DeleteSuccess, MSG.SUCCESSCODE);
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "/getoulist")
	public JSONObject getOuList(@RequestBody Map<String, Object> map) {
		List<Map<String, Object>> list_ou = ouService.queryOuList();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Map<String, Object> ou : list_ou) {
			Map<String, String> m = new HashMap<>();
			m.put("label", ou.get("ouname").toString());
			m.put("value", ou.get("rowguid").toString());
            list.add(m);
		}
		return BuildJsonOfObject.getJsonString(list, MSG.SUCCESSCODE);
	}
}
