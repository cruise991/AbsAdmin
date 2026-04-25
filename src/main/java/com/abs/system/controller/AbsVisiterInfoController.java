package com.abs.system.controller;

import java.text.SimpleDateFormat;
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

import com.abs.system.api.IAbsVisiterInfoService;
import com.abs.system.domain.AbsVisiterInfo;
import com.abs.system.filter.NoNeedLogin;
import com.abs.system.util.AddressUtils;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/visiterinfo")
public class AbsVisiterInfoController {
	
	
	@Autowired
	private IAbsVisiterInfoService visiterService;
	
	private Logger logger=LoggerFactory.getLogger(AbsVisiterInfoController.class);
	
	
	@NoNeedLogin
	@ResponseBody
	@RequestMapping(value = "/userstatics")
	public JSONObject userStatics(@RequestBody Map<String, Object> map) {
		logger.info("开始获取用户ip：{}",map.toString());
		String ip=map.get("ip").toString();
		String address= AddressUtils.getAddresses(ip);
		AbsVisiterInfo visiter=new AbsVisiterInfo();
		visiter.setRowguid(UUID.randomUUID().toString());
		visiter.setVisiterip(ip);
		visiter.setVisittime(new Date());
		visiter.setPosition(address);
		visiterService.addVisterInfo(visiter);
		logger.info("结束统计用户ip信息");
		return BuildJsonOfObject.getJsonString("", MSG.SUCCESSCODE);
	}

	
	
	@ResponseBody
	@RequestMapping("/getpagelist")
	public JSONObject getPageList(@RequestBody Map<String, Object> reqMap) {
		
		logger.info("getpagelist 用户传入参数：{}",reqMap.toString());
		try {
			Params params=new Params(reqMap);
			List<Map<String,Object>> list=visiterService.queryPageList(params);
			if(list!=null) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for(Map<String,Object> map:list) {
					Date date=(Date)map.get("visittime");
					map.put("visittime", sdf.format(date));
				}
			}
			long totalcount=visiterService.queryTotalCount(params);
			return BuildJsonOfObject.getJsonString(list, totalcount, "ok", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);
		}
	}
}
	

	

