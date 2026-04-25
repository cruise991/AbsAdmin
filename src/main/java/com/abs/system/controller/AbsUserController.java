package com.abs.system.controller;

import java.time.LocalDateTime;
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

import com.abs.system.api.IAbsOuInfo;
import com.abs.system.api.IAbsRoleService;
import com.abs.system.api.IAbsUserService;
import com.abs.system.domain.AbsUserInfo;
import com.abs.system.filter.ToToken;
import com.abs.system.util.AbsSessionHelper;
import com.abs.system.util.BeanUtil;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MD5Util;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.alibaba.fastjson.JSONObject;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class AbsUserController {

	private static Logger logger = LoggerFactory.getLogger(AbsUserController.class);

	@Autowired
	private IAbsUserService userService;

	@Autowired
	private IAbsRoleService roleService;


	@Autowired
	private IAbsOuInfo ouService;

	@ResponseBody
	@RequestMapping(value = "/userdetailpage")
	public JSONObject userdetailpage_init(@RequestBody Map<String, Object> reqMap) {
		String rowguid = reqMap.get("rowguid").toString();
		AbsUserInfo user = userService.getUserByGuid(rowguid);
		Params record = Params.getRecordByObject(user);
		return BuildJsonOfObject.getJsonString(record.toJSObject(), MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping(value = "/UserRegisterAction", produces = "application/json; charset=utf-8")
	public JSONObject UserRegisterAction(@RequestBody Map<String, Object> reqMap) {
		AbsUserInfo userinfo = BeanUtil.mapToBean(reqMap, AbsUserInfo.class);
		userinfo.setRowguid(UUID.randomUUID().toString());
		userinfo.setAddtime(LocalDateTime.now());
		userService.addUser(userinfo);
		return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping("/getpagelist")
	public JSONObject getPageList(@RequestBody Map<String, String> reqMap) {
		
		logger.info("user->getpagelist 用户传入参数：{}",reqMap.toString());
		try {
			String loginname = reqMap.get("loginname") == null ? "" : reqMap.get("loginname").toString();
			int pagenum = Integer.parseInt(reqMap.get("pagenum"));
			int pagesize = Integer.parseInt(reqMap.get("pagesize"));
			List<Map<String,Object>> list = userService.getUserListByLoginName(pagenum, pagesize,loginname);
			long count = userService.getTotalCountByLoginName(loginname);
			List<Map<String,Object>> list_ou=ouService.getOuInfoMapList();
			for (Map<String,Object> m : list) {
				String userrole = m.get("userrole")+"";
				String ouguid=m.get("ouguid")+"";
				String rolename = "";
				String ouname="";
				if (StrUtil.isNotBlank(userrole)) {
					for (String rowguid : userrole.split(";")) {
						Map<String,Object> map=roleService.getRoleNameByGuid(rowguid);
						rolename += map.get("rolename");
					}
				}
				if(list_ou!=null) {
					for(Map<String,Object> map:list_ou) {
						if(ouguid.equals(map.get("rowguid"))) {
							ouname=map.get("ouname").toString();
							break;
						}
					}
				}
				m.put("userrole", rolename);
				m.put("ouname", ouname);
			}
			return BuildJsonOfObject.getJsonString(list, count, "ok", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);
		}
	}

	@ResponseBody
	@RequestMapping("/adduser")
	public JSONObject addUser(@RequestBody Map<String, Object> reqMap) {

		logger.info("新增用户：user->adduser：{}", reqMap.toString());
		String username = reqMap.get("loginname").toString();
		if (userService.isExistUserName(username)) {
			return BuildJsonOfObject.getJsonString(MSG.ExitSameItemName, MSG.FAILCODE);
		}
		AbsUserInfo userinfo = new AbsUserInfo();
		userinfo.setRowguid(UUID.randomUUID().toString());
		userinfo = BeanUtil.mapToBean(reqMap, userinfo);
		userinfo.setPassword(MD5Util.MD5(userinfo.getPassword()==null?"123456":userinfo.getPassword()));
		userService.addUser(userinfo);
		return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping("/edituser")
	public JSONObject editUser(@RequestBody Map<String, Object> reqMap) {
		try {
			String rowguid = reqMap.get("rowguid").toString();
			AbsUserInfo userinfo = userService.getUserByGuid(rowguid);
			userinfo = BeanUtil.mapToBean(reqMap, userinfo);
			// String userrole = reqMap.get("rolesid").toString();
			// userinfo.setUserrole(userrole);
			userService.updateUser(userinfo);
			return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/resetpass")
	public JSONObject resetpass(@RequestBody Map<String, Object> reqMap) {
		try {
			String rowguid = reqMap.get("rowguid").toString();
			AbsUserInfo userinfo = userService.getUserByGuid(rowguid);
            userinfo.setPassword(MD5Util.MD5("123456"));        
			userService.updateUser(userinfo);
			return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}
	
	

	@ResponseBody
	@RequestMapping("/edituserinfo")
	public JSONObject editUserInfo(@RequestBody Map<String, Object> reqMap, HttpSession session) {
		try {
			String rowguid = session.getAttribute("userguid").toString();
			AbsUserInfo userinfo = userService.getUserByGuid(rowguid);
			userinfo = BeanUtil.mapToBean(reqMap, AbsUserInfo.class);
			userinfo.setRowguid(rowguid);
			userService.updateUser(userinfo);
			return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/edituserlogo")
	public JSONObject editUserLogo(@ToToken Params params,@RequestBody Map<String,Object> reqMap) {
		try {
			String token=params.getString("token");
			String userguid=AbsSessionHelper.getCurrentUserGuid(token);
			String logourl=reqMap.get("logourl")+"";
			userService.updateUserLogoByGuid(logourl, userguid);
			return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	@ResponseBody
	@RequestMapping("/deluser")
	public JSONObject delUser(@RequestBody Map<String, Object> map) {

		logger.info("当前用户传入参数为：{}", map.toString());
		try {
			String rowguid = map.get("rowguid").toString();
			AbsUserInfo user = userService.getUserByGuid(rowguid);
			user.setIsdelete("1");
			userService.updateUser(user);
			return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	@ResponseBody
	@RequestMapping("/getuserbyguid")
	public JSONObject getUserByGuid(@RequestBody Map<String, Object> map) {

		logger.info("当前用户传入参数为：{}", map.toString());
		try {
			String rowguid = map.get("rowguid").toString();
			AbsUserInfo user = userService.getUserByGuid(rowguid);
			return BuildJsonOfObject.getJsonString(Params.getRecordByObject(user).toJSObject(), MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	@ResponseBody
	@RequestMapping("/getuserinfobyguid")
	public JSONObject getUserInfoByGuid(@RequestBody Map<String, Object> map) {

		try {
			String rowguid = map.get("userguid").toString();
			AbsUserInfo user = userService.getUserByGuid(rowguid);
			Params r = new Params();
			r.setColumn("logourl", user.getLogourl());
			r.setColumn("username", user.getLoginname());
			r.setColumn("userguid", user.getRowguid());
			return BuildJsonOfObject.getJsonString(r.toJSObject(), MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}
	
	@ResponseBody
	@RequestMapping("/getuserinfo")
	public JSONObject getUserInfoByToken(@ToToken Params params) {
		try {
			
			logger.info("当前用户传入参数：{}",params.getFieldMap().toString());
			String token=params.getString("token");
			String userguid=AbsSessionHelper.getCurrentUserGuid(token);
			logger.info("当前登录人员：{}",userguid);
			AbsUserInfo user = userService.getUserByGuid(userguid);
			Map<String, Object> map_ou=ouService.getOuInfoByGuid(user.getOuguid());
			Map<String, Object> map_role=roleService.getRoleNameByGuid(user.getUserrole());
			Params r=new Params();
			r.setColumn("logourl",user.getLogourl());
			r.setColumn("username", user.getLoginname());
			r.setColumn("userguid", user.getRowguid());
			r.setColumn("ouname", map_ou!=null?map_ou.get("ouname"):"");
			r.setColumn("rolename",map_role!=null?map_role.get("rolename"):"");
			return BuildJsonOfObject.getJsonString(r.toJSObject(), MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

}
