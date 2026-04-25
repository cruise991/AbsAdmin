package com.abs.system.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import com.abs.system.api.IAbsAppInfoService;
import com.abs.system.domain.AbsAppInfo;
import com.abs.system.filter.ToToken;
import com.abs.system.util.AbsSessionHelper;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/appinfo")
public class AbsAppInfoController {
	
	
	private Logger logger=LoggerFactory.getLogger(AbsAppInfoController.class);
	
    @Autowired
    private IAbsAppInfoService appService;
	
	@ResponseBody
	@RequestMapping(value = "/addappinfo")
	public JSONObject addappinfo(@ToToken Params params,@RequestBody Map<String,String> reqMap) {
		logger.info("用户传入参数:{}",reqMap.toString());
		String token=params.getString("token");
		String userguid=AbsSessionHelper.getCurrentUserGuid(token);
		String appname=reqMap.get("appname");
		String introduction=reqMap.get("introduction");
		String tokentype=reqMap.get("tokentype");
		Calendar cc=Calendar.getInstance();
		AbsAppInfo appinfo=new AbsAppInfo();
		Date createDate=new Date();
		appinfo.setAddtime(createDate);
		appinfo.setAppkey("XBG"+StrUtil.getRanNumber(6));
		appinfo.setAppsecret(StrUtil.getRanChars(10));
		appinfo.setRowguid(UUID.randomUUID().toString());
		cc.setTime(createDate);
		cc.add(Calendar.YEAR, 1);
		appinfo.setExpiredtime(cc.getTime());
		appinfo.setIntroduction(introduction);
		appinfo.setAppname(appname);
		appinfo.setStatus("1");//0 未启用  1 已用  2.已到期
		appinfo.setUserguid(userguid);
		appinfo.setToken(UUID.randomUUID().toString().replace("-", ""));
		
		
		if("1".contentEquals(tokentype)) {
			Calendar ss=Calendar.getInstance();
			ss.setTime(createDate);
			ss.add(Calendar.DAY_OF_YEAR, 1);
			appinfo.setTokenexpired(ss.getTime());
		}else {
			appinfo.setTokenexpired(appinfo.getExpiredtime());
		}
		appinfo.setTokentype(tokentype);
		appService.addAppInfo(appinfo);
	    return BuildJsonOfObject.getJsonString("创建成功", MSG.SUCCESSCODE);
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/getapppagelist")
	public JSONObject getAppPageList(@RequestBody Map<String, Object> reqMap) {
		logger.info("用户传入参数为：{}",reqMap.toString());
		Params params=new Params(reqMap);
		List<Map<String,Object>> list_item= appService.queryAppInfoPageList(params);
		long totalcount= appService.queryAppInfoCount(params);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Map<String,Object> item : list_item) {
			Date addtime=(Date) item.get("addtime");
			item.put("addtime", sdf.format(addtime));
			Date expiredtime=(Date) item.get("expiredtime");
			item.put("expiredtime", sdf.format(expiredtime));
		}
		return BuildJsonOfObject.getJsonString(list_item, totalcount,MSG.SUCCESSCODE);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getapppagelistofuser")
	public JSONObject getAppPageList(@ToToken Params pp,@RequestBody Map<String, Object> reqMap) {
		logger.info("用户传入参数为：{}",reqMap.toString());
		Params params=new Params(reqMap);
		String token=pp.getString("token");
		String userguid=AbsSessionHelper.getCurrentUserGuid(token);
		params.put("userguid", userguid);
		List<Map<String,Object>> list_item= appService.queryAppInfoPageList(params);
		long totalcount= appService.queryAppInfoCount(params);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Map<String,Object> item : list_item) {
			Date addtime=(Date) item.get("addtime");
			item.put("addtime", sdf.format(addtime));
			Date expiredtime=(Date) item.get("expiredtime");
			item.put("expiredtime", sdf.format(expiredtime));
			
			Date tokenexpired=(Date) item.get("tokenexpired");
			item.put("tokenexpired", sdf.format(tokenexpired));
			
		
		}
		return BuildJsonOfObject.getJsonString(list_item, totalcount,MSG.SUCCESSCODE);
	}
	
	@ResponseBody
	@RequestMapping(value = "/startapp")
	public JSONObject startapp(@RequestBody Map<String, Object> reqMap) {
		logger.info("用户传入参数为：{}",reqMap.toString());
		String rowguid=reqMap.get("rowguid").toString();
		AbsAppInfo appinfo= appService.queryAppInfoByGuid(rowguid);
		appinfo.setStatus("1");
		appService.updateAppInfo(appinfo);
		return BuildJsonOfObject.getJsonString("延期成功",MSG.SUCCESSCODE);
	}
	
	@ResponseBody
	@RequestMapping(value = "/stopapp")
	public JSONObject stopapp(@RequestBody Map<String, Object> reqMap) {
		logger.info("用户传入参数为：{}",reqMap.toString());
		String rowguid=reqMap.get("rowguid").toString();
		AbsAppInfo appinfo= appService.queryAppInfoByGuid(rowguid);
		appinfo.setStatus("0");
		appService.updateAppInfo(appinfo);
		return BuildJsonOfObject.getJsonString("延期成功",MSG.SUCCESSCODE);
	}
	
	@ResponseBody
	@RequestMapping(value = "/ycapp")
	public JSONObject ycapp(@RequestBody Map<String, Object> reqMap) {
		logger.info("用户传入参数为：{}",reqMap.toString());
		String rowguid=reqMap.get("rowguid").toString();
		AbsAppInfo appinfo= appService.queryAppInfoByGuid(rowguid);
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		appinfo.setStatus("0");
		cal.add(Calendar.YEAR, 1);
		appinfo.setExpiredtime(cal.getTime());
		appService.updateAppInfo(appinfo);
		return BuildJsonOfObject.getJsonString("延期成功",MSG.SUCCESSCODE);
	}
	
	@ResponseBody
	@RequestMapping(value = "/resettoken")
	public JSONObject resettoken(@RequestBody Map<String, Object> reqMap) {
		logger.info("用户传入参数为：{}",reqMap.toString());
		String rowguid=reqMap.get("rowguid").toString();
		AbsAppInfo appinfo= appService.queryAppInfoByGuid(rowguid);
		appinfo.setToken(UUID.randomUUID().toString().replace("-", ""));
		if("1".contentEquals(appinfo.getTokentype()+"")) {
			Calendar ss=Calendar.getInstance();
			ss.setTime(new Date());
			ss.add(Calendar.DAY_OF_YEAR, 1);
			appinfo.setTokenexpired(ss.getTime());
		}else {
			appinfo.setTokenexpired(appinfo.getExpiredtime());
		}
		appService.updateAppInfo(appinfo);
		return BuildJsonOfObject.getJsonString("重置成功",MSG.SUCCESSCODE);
	}
	
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "/delbyguid")
	public JSONObject delAppInfoByGuid(@RequestBody Map<String, Object> map) {
		logger.info("delbyguid：{}", map.toString());
		String rowguids = map.get("rowguids").toString();
		String [] guids=rowguids.split(",");
		for(String guid:guids) {
			if(StrUtil.isNotBlank(guid)) {
				appService.delAppInfoByGuid(guid);
			}
		}
		return BuildJsonOfObject.getJsonString(MSG.DeleteSuccess, MSG.SUCCESSCODE);
	}
	
	
	
	
}
