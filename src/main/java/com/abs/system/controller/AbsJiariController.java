package com.abs.system.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abs.system.api.IAbsJiariService;
import com.abs.system.domain.AbsJiari;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/jiari")
public class AbsJiariController {

	@Autowired
	private IAbsJiariService jiariService;

	private Logger logger = LoggerFactory.getLogger(AbsJiariController.class);

	@ResponseBody
	@RequestMapping("/addjiari")
	public JSONObject addJiaRi(@RequestBody Map<String, String> map) {
		logger.info("当前用户传入参数：{}",map.toString());
		String pmdate = map.get("jiari");
		
		boolean isjia = jiariService.isExistGuid(pmdate);
		if (!isjia) {
			logger.info("不存在的节假日.....");
			AbsJiari jiari = new AbsJiari();
			jiari.setIsjia(1);
			jiari.setRowguid(pmdate);
			jiariService.addJiari(jiari);
		}else {
			logger.info("存在的节假日.....");
			jiariService.delJiariByGuid(pmdate);
		}
		return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
	}
	
	@ResponseBody
	@RequestMapping("/getjiarilist")
	public JSONObject getjiarilist(@RequestBody Map<String, String> map) {
		logger.info("获取节假日.....");
		String jiari = map.get("jiari");
		List<Map<String,Object>> list=jiariService.queryJiaRiListByJiari(jiari);
		return BuildJsonOfObject.getJsonString(list,MSG.SUCCESSCODE);
	}

}
