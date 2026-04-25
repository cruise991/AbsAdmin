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

import com.abs.system.api.IAbsMsgInfoService;
import com.abs.system.api.IAbsUserService;
import com.abs.system.domain.AbsMsgInfo;
import com.abs.system.filter.NoNeedLogin;
import com.abs.system.filter.ToToken;
import com.abs.system.util.AbsSessionHelper;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/msginfo")
public class AbsMsgInfoController {

	    
	    private Logger logger=LoggerFactory.getLogger(AbsMsgInfoController.class);
	
	
	    @Autowired
	    private IAbsMsgInfoService msgService;
	    
	    @Autowired
	    private IAbsUserService userService;
		
	    
	    
	    @NoNeedLogin
		@ResponseBody
		@RequestMapping(value = "/addmsginfo")
		public JSONObject addmsginfo(@RequestBody Map<String,String> reqMap) {
			logger.info("用户传入参数:{}",reqMap.toString());
			String message=reqMap.get("message");
			
			AbsMsgInfo msginfo=new AbsMsgInfo();
			if(StrUtil.isNotBlank(message)) {
				msginfo.setAddtime(new Date());
				msginfo.setRowguid(UUID.randomUUID().toString());
				msginfo.setIsread("0");
				msginfo.setContent(message);
				msginfo.setDatasource("【用户留言】");
				if(message.length()>20) {
					msginfo.setMsgtitle(message.substring(0, 20)+"....");
				}else {
					msginfo.setMsgtitle(message);
				}
				msginfo.setUserguid(null);
				msgService.addMsgInfo(msginfo);
				return BuildJsonOfObject.getJsonString("保存成功", MSG.SUCCESSCODE);
			}else {
				return BuildJsonOfObject.getJsonString("消息内容不能为空", MSG.SUCCESSCODE);

			}
		 
		}
	    
		@ResponseBody
		@RequestMapping(value = "/querymsglist")
		public JSONObject queryMsglist(@ToToken Params params,@RequestBody Map<String,Object> reqMap) {
			logger.info("用户传入参数:{}",reqMap.toString());
			String token=params.getString("token");
			String userguid=AbsSessionHelper.getCurrentUserGuid(token);
			
			Map<String, Object> map_role=userService.getUserRoleByGuid(userguid);
			if(map_role!=null) {
				reqMap.put("roleguid", map_role.get("userrole"));
			}
			//reqMap.put("userguid", userguid);
			List<Map<String,Object>> list=msgService.queryMsgListByUserGuidOfAdmin(reqMap);
			SimpleDateFormat sdf=new SimpleDateFormat("MM/dd HH:mm");
			if(list!=null) {
				for(Map<String,Object> mm:list) {
					Date addtime=(Date) mm.get("addtime");
					mm.put("addtime", sdf.format(addtime));
				}
			}
			long count=msgService.queryCountByUserGuidOfAdmin(reqMap);
		    return BuildJsonOfObject.getJsonString(list, count, MSG.SUCCESSCODE);
		 
		}
	    
	    
		@ResponseBody
		@RequestMapping(value = "/readmsg")
		public JSONObject readMsg(@RequestBody Map<String,Object> reqMap) {
			logger.info("用户传入参数:{}",reqMap.toString());
			String rowguid=reqMap.get("rowguid").toString();
			String isread="1";
			msgService.updateMsgReadStatus(rowguid,isread);
			return BuildJsonOfObject.getJsonString("状态更新成功", MSG.SUCCESSCODE);
		 
		}
	
	
	

}
