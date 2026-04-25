package com.abs.system.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.abs.system.api.IAbsHomeZjService;
import com.abs.system.domain.AbsHomeZj;
import com.abs.system.domain.AbsHomeUser;
import com.abs.system.filter.ToToken;
import com.abs.system.util.AbsSessionHelper;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/homezj")
public class AbsHomeZjController {

	private Logger logger = LoggerFactory.getLogger(AbsHomeZjController.class);

	@Autowired
	private IAbsHomeZjService zjService;

	@ResponseBody
	@RequestMapping(value = "/addhomezj")
	public JSONObject addOuInfo(@RequestBody Map<String, Object> map) {
		logger.info("addhomezj：{}", map.toString());
		String cname = map.get("cname").toString();
		String cpath = map.get("cpath").toString();
		String ordernum = map.get("ordernum").toString();
		boolean flag = zjService.isExistSameCpath(cpath, "");
		if (flag) {
			return BuildJsonOfObject.getJsonString("存在相同组件路径", MSG.FAILCODE);
		}

		AbsHomeZj homezj = new AbsHomeZj();
		homezj.setRowguid(UUID.randomUUID().toString());
		homezj.setCname(cname);
		homezj.setCpath(cpath);
		homezj.setOrdernum(ordernum);
		homezj.setAddtime(new Date());
		zjService.addHomezj(homezj);
		return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping(value = "/gethomezjpagelist")
	public JSONObject getHomeZjPageList(@ToToken Params param,@RequestBody Map<String, Object> map) {
		Params params = new Params(map);
		String token=param.getString("token");
		String userguid=AbsSessionHelper.getCurrentUserGuid(token);
		DateTimeFormatter sdf =DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<Map<String, Object>> list_ou = zjService.getHomezjPageList(params);
		long totalcount = zjService.getHomezjCount(params);
		if (list_ou != null && list_ou.size() > 0) {
			for (Map<String, Object> ou : list_ou) {
				LocalDateTime date = (LocalDateTime) ou.get("addtime");
				ou.put("addtime", date.format(sdf));
				String zjguid=ou.get("rowguid").toString();
				if(zjService.isExistSameHomeUser(userguid, zjguid)) {
					ou.put("issy", "1");
				}else {
					ou.put("issy", "0");
				}
			
			}
		}
		return BuildJsonOfObject.getJsonString(list_ou, totalcount, MSG.SUCCESSCODE);
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "/delbyguid")
	public JSONObject delHomezjByGuid(@RequestBody Map<String, Object> map) {
		logger.info("delbyguid：{}", map.toString());
		String rowguid = map.get("rowguid").toString();
		zjService.delHomezjByGuid(rowguid);
		return BuildJsonOfObject.getJsonString(MSG.DeleteSuccess, MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping(value = "/gethomezjbyguid")
	public JSONObject getHomeZjByGuid(@RequestBody Map<String, Object> map) {
		logger.info("gethomezjbyguid：{}", map.toString());
		String rowguid = map.get("rowguid").toString();
		Map<String, Object> homezj = zjService.queryHOmezjMapByGuid(rowguid);
		return BuildJsonOfObject.getJsonString(homezj, MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping(value = "/queryhomeuser")
	public JSONObject queryhomeuser(@ToToken Params params, @RequestBody Map<String, Object> map) {
		logger.info("queryhomeuser：{}", map.toString());
		String token = params.getString("token");
		String userguid = AbsSessionHelper.getCurrentUserGuid(token);
		List<Map<String, Object>> list = zjService.queryHomeZjListByUserguid(userguid);
		for(Map<String,Object> mm:list) {
			String czjguid=mm.get("czjguid").toString();
			String cpath=zjService.queryCpathByZjGuid(czjguid);
            mm.put("cpath", cpath);
		}
		return BuildJsonOfObject.getJsonString(list, MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping(value = "/edithomezj")
	public JSONObject edithomezj(@RequestBody Map<String, Object> map) {
		logger.info("edithomezj：{}", map.toString());
		String cname = map.get("cname").toString();
		String rowguid = map.get("rowguid").toString();
		String cpath = map.get("cpath").toString();
		String ordernum = map.get("ordernum").toString();
		boolean flag = zjService.isExistSameCpath(cpath, rowguid);
		AbsHomeZj homezj = zjService.queryHomeZjInfoByGuid(rowguid);
		if (flag) {
			return BuildJsonOfObject.getJsonString("存在相同组件路径", MSG.FAILCODE);
		}
		homezj.setCname(cname);
		homezj.setCpath(cpath);
		homezj.setOrdernum(ordernum);
		zjService.updateHomezj(homezj);
		return BuildJsonOfObject.getJsonString("修改成功", MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping(value = "/addtohome")
	public JSONObject addtohome(@ToToken Params params, @RequestBody Map<String, Object> map) {
		logger.info("addtohome：{}", map.toString());
		String token = params.getString("token");
		String userguid = AbsSessionHelper.getCurrentUserGuid(token);
		String zjguid = map.get("rowguid").toString();
		AbsHomeZj homezj = zjService.queryHomeZjInfoByGuid(zjguid);
		boolean flag = zjService.isExistSameHomeUser(userguid, zjguid);
		if (flag) {
			return BuildJsonOfObject.getJsonString("已经放到了首页", MSG.FAILCODE);
		}
		AbsHomeUser homeuser = new AbsHomeUser();
		homeuser.setRowguid(UUID.randomUUID().toString());
		homeuser.setOrdernumber(homezj.getOrdernum());
		homeuser.setUserguid(userguid);
		homeuser.setCzjguid(zjguid);
		homeuser.setZjname(homezj.getCname());
		zjService.addHomezjUser(homeuser);
		return BuildJsonOfObject.getJsonString("放置成功", MSG.SUCCESSCODE);
	}

	
	@ResponseBody
	@RequestMapping(value = "/removetohome")
	public JSONObject removetohome(@ToToken Params params, @RequestBody Map<String, Object> map) {
		logger.info("addtohome：{}", map.toString());
		String token = params.getString("token");
		String userguid = AbsSessionHelper.getCurrentUserGuid(token);
		String zjguid = map.get("rowguid").toString();
	    zjService.delHomezjUserByUserGuidAndZjGuid(userguid,zjguid);
		return BuildJsonOfObject.getJsonString("放置成功", MSG.SUCCESSCODE);
	}

}
