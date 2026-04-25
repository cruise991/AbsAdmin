package com.abs.system.controller;

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

import com.abs.system.api.IAbsSysConfig;
import com.abs.system.domain.AbsSysConfig;
import com.abs.system.filter.ToToken;
import com.abs.system.util.AbsSessionHelper;
import com.abs.system.util.BeanUtil;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/sysconfig")
public class AbsSysConfigController {
	
	
	private Logger logger=LoggerFactory.getLogger(AbsSysConfigController.class);

	@Autowired
	private IAbsSysConfig sysConfigService;

	@ResponseBody
	@RequestMapping("/getglobalpagelist")
	public JSONObject getglobalpagelist(@RequestBody Map<String,Object> map) {
		try {
			logger.info("当前用户传入参数为：{}",map.toString());
			String configname = map.get("configname") == null ? "" : map.get("configname").toString();
			int pagesize = Integer.parseInt(map.get("pagesize") + "");
			int pagenum = Integer.parseInt(map.get("pagenum") + "");
			List<Map<String, Object>> list = sysConfigService.getGlobalConfigListByName(pagesize, pagenum, configname);
			long totalcount = sysConfigService.getGlobalTotalCountByName(configname);
			return BuildJsonOfObject.getJsonString(list, totalcount,MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(MSG.FAILCODE, e.getMessage());
		}
	}
	
	
	
	@ResponseBody
	@RequestMapping("/getconfiglistofuser")
	public JSONObject getConfigListOfUser(@ToToken Params params,@RequestBody Map<String,Object> map) {
		try {
			logger.info("当前用户传入参数为：{}",map.toString());
			String token=params.getString("token");
			String userguid=AbsSessionHelper.getCurrentUserGuid(token);
			Params param=new Params(map);
			param.put("userguid", userguid);
			List<Map<String, Object>> list = sysConfigService.getConfigListOfUser(param);
			long totalcount = sysConfigService.getTotalCountOfUser(param);
			return BuildJsonOfObject.getJsonString(list, totalcount,MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(MSG.FAILCODE, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("addconfig")
	public JSONObject addconfig(@RequestBody Map<String, Object> reqMap) {
		try {
			AbsSysConfig config = BeanUtil.mapToBean(reqMap, AbsSysConfig.class);
			config.setAddtime(new Date());
			config.setRowguid(UUID.randomUUID().toString());
			String configname = config.getConfigname();
			if (sysConfigService.isExistConfigName(configname)) {
				return BuildJsonOfObject.getJsonString(MSG.ExitSameItemName, MSG.FAILCODE);
			}
			sysConfigService.addConfig(config);
			return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}

	}
	
	@ResponseBody
	@RequestMapping("editconfig")
	public JSONObject editconfig(@ToToken Params params,@RequestBody Map<String, Object> reqMap) {
		try {
			
			logger.info("开始配置信息进行编辑:{}",reqMap.toString());
			String token=params.getString("token");
			String userguid=AbsSessionHelper.getCurrentUserGuid(token);
			
			AbsSysConfig config=BeanUtil.mapToBean(reqMap, AbsSysConfig.class);
			String configname=config.getConfigname();
			config.setAddtime(new Date());
			config.setUserguid(userguid);
			//config.setRowguid(UUID.randomUUID().toString());
			//判断是否存在
			if(sysConfigService.isExistConfigName(configname,config.getRowguid(),userguid)) {
				return BuildJsonOfObject.getJsonString(MSG.ExitSameItemName,MSG.FAILCODE);
			}
			sysConfigService.updateConfig(config);
			return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);

		}
	}
	
	
	
	@ResponseBody
	@RequestMapping("/userneedmodify")
	public JSONObject userneedmodify(@ToToken Params params,@RequestBody Map<String, Object> reqMap) {
		try {
			String rowguid = reqMap.get("rowguid").toString();
			AbsSysConfig config=sysConfigService.getAbsSysConfigByGuid(rowguid);
			
			String token=params.getString("token");
			String userguid=AbsSessionHelper.getCurrentUserGuid(token);

			AbsSysConfig uconfig=sysConfigService.getAbsSysConfigByConfigNameAndUserGuid(config.getConfigname(),userguid);
			if(uconfig==null) {
				config.setRowguid(UUID.randomUUID().toString());
				config.setUserguid(userguid);
				config.setAddtime(new Date());
				sysConfigService.addConfig(config);
				return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE);
			}else {
				return BuildJsonOfObject.getJsonString("系统参数已经引入", MSG.FAILCODE);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	} 
	
	@ResponseBody
	@RequestMapping("/delconfig")
	public JSONObject delconfig(@RequestBody Map<String, Object> reqMap) {
		try {
			String rowguid = reqMap.get("rowguid").toString();
			sysConfigService.deleteConfigByGuid(rowguid);
			return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	} 
	
	@ResponseBody
	@RequestMapping("/getconfigbyguid")
	public Map getConfigByGuid(@RequestBody Map<String, Object> reqMap) {
		try {
			String rowguid = reqMap.get("rowguid").toString();
			Map<String,Object> map=sysConfigService.getConfigMapByGuid(rowguid);
			return BuildJsonOfObject.getMapString(MSG.ok, MSG.SUCCESSCODE,map);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}
}
