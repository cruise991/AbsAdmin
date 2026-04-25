package com.abs.system.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.abs.system.api.IAbsLoginInfoService;
import com.abs.system.api.IAbsOuInfo;
import com.abs.system.api.IAbsSysConfig;
import com.abs.system.api.IAbsUserService;
import com.abs.system.api.IAbsVcodeService;
import com.abs.system.domain.AbsLoginInfo;
import com.abs.system.domain.AbsUserInfo;
import com.abs.system.domain.AbsVcodeInfo;
import com.abs.system.filter.NoNeedLogin;
import com.abs.system.filter.ToToken;
import com.abs.system.util.AbsSessionHelper;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.EmailUtil;
import com.abs.system.util.MD5Util;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/login")
public class AbsLoginController {

	@Autowired
	private IAbsUserService userService;

	@Autowired
	private IAbsOuInfo ouService;

	@Autowired
	private IAbsVcodeService vcodeService;
	
	@Autowired
	private IAbsSysConfig configService;

	@Autowired
	private IAbsLoginInfoService loginService;

	private Logger logger = LoggerFactory.getLogger(AbsLoginController.class);

	
	@NoNeedLogin
	@ResponseBody
	@RequestMapping("/userlogin")
	public JSONObject userLogin(@RequestBody Map<String, String> map) {
		logger.info("[开始] 用户名和密码登录，参数: {}", map.toString());
		String username = map.get("username");
		String password = map.get("password");
		if (StrUtil.isStrBlank(password) || StrUtil.isStrBlank(username)) {
			return BuildJsonOfObject.getJsonString(MSG.UserNameOrPasswordCanNotNull, MSG.FAILCODE);
		}
		Map user_map = userService.getUserMapByUsernameAndPassowrd(username, MD5Util.MD5(password));
		if (user_map != null) {
			Params params = new Params();
			params.put("userguid", user_map.get("rowguid"));
			params.put("username", user_map.get("loginname"));
			params.put("ouguid", user_map.get("ouguid"));
			params.put("ouname", StrUtil.isObjBlank(user_map.get("ouguid"))?"":ouService.getOuInfoByGuid(user_map.get("ouguid").toString()).get("ouname"));
			String logourl = user_map.get("logourl") == null ? "" : user_map.get("logourl").toString();
			String token = AbsSessionHelper.getRandomString(8);
			AbsSessionHelper.setCurrentUserInfo(token, params.toJson());
			
			// 记录登录日志
			AbsLoginInfo loginInfo = new AbsLoginInfo();
			loginInfo.setRowguid(UUID.randomUUID().toString());
			loginInfo.setLogindate(LocalDateTime.now());
			loginInfo.setUserinfo(user_map.get("loginname").toString());
			loginInfo.setUserguid(user_map.get("rowguid").toString());
			loginInfo.setUsertoken(token);
			loginService.addLoginInfo(loginInfo);
			
			JSONObject jsonRtn= new JSONObject();
			jsonRtn.put("token", token);
			jsonRtn.put("logourl", logourl);
			jsonRtn.put("username", params.getString("username"));
			jsonRtn.put("ouname", params.getString("ouname"));
			jsonRtn.put("ouguid", params.getString("ouguid"));
			logger.info("[结束] 用户名和密码登录");
			return BuildJsonOfObject.getJsonString(jsonRtn, MSG.SUCCESSCODE);
		} else {
			
			JSONObject jsonRtn= BuildJsonOfObject.getJsonString(MSG.UserNameOrPasswordWrong, MSG.FAILCODE);
			logger.info("当前返回结果：{}",jsonRtn.toString());
			logger.info("[结束] 用户名和密码登录");
			return jsonRtn;

		}
	}

	
	@NoNeedLogin
	@ResponseBody
	@RequestMapping("/emaillogin")
	public JSONObject emailLogin(@RequestBody Map<String, String> map) {
		logger.info("[开始] 邮箱登录，参数: {}", map.toString());
		String password = map.get("password");
		String logintype = map.get("logintype");
		String email = map.get("email");
		String vcode = map.get("vcode");
		if (StrUtil.isStrBlank(email)) {
			return BuildJsonOfObject.getJsonString(MSG.EmailCanNotNull, MSG.FAILCODE);
		}
		if ("1".equals(logintype) && StrUtil.isStrBlank(password)) {
			return BuildJsonOfObject.getJsonString(MSG.PasswordCanNotNull, MSG.FAILCODE);
		} else if ("0".equals(logintype) && StrUtil.isStrBlank(password)) {
			return BuildJsonOfObject.getJsonString(MSG.VcodeCanNotNull, MSG.FAILCODE);
		}

		
		
		if ("1".equals(logintype)) {
			boolean isExist=userService.isExistEmail(email);
			if(!isExist) {
				return BuildJsonOfObject.getJsonString(MSG.EmailNotRegister,MSG.FAILCODE);
			}
			Map user_map = userService.getUserMapByEmailAndPassword(email, MD5Util.MD5(password));
			if (user_map != null) {
				Params params = new Params();
				params.put("userguid", user_map.get("rowguid"));
				params.put("username", user_map.get("loginname"));
				params.put("ouguid", user_map.get("ouguid"));
				params.put("ouname", user_map.get("ouguid")!=null?ouService.getOuInfoByGuid(user_map.get("ouguid").toString()).get("ouname"):"");
				String logourl = user_map.get("logourl") == null ? "" : user_map.get("logourl").toString();
				String token = AbsSessionHelper.getRandomString(8);
				AbsSessionHelper.setCurrentUserInfo(token, params.toJson());
				
				// 记录登录日志
				AbsLoginInfo loginInfo = new AbsLoginInfo();
				loginInfo.setRowguid(UUID.randomUUID().toString());
				loginInfo.setLogindate(LocalDateTime.now());
				loginInfo.setUserinfo(user_map.get("loginname").toString());
				loginInfo.setUserguid(user_map.get("rowguid").toString());
				loginInfo.setUsertoken(token);
				loginService.addLoginInfo(loginInfo);
				
				JSONObject jsonRtn= new JSONObject();
				jsonRtn.put("token", token);
				jsonRtn.put("logourl", logourl);
				jsonRtn.put("username", params.getString("username"));
				jsonRtn.put("ouname", params.getString("ouname"));
				jsonRtn.put("ouguid", params.getString("ouguid"));
				logger.info("[结束] 邮箱登录");
				return BuildJsonOfObject.getJsonString(jsonRtn,MSG.SUCCESSCODE);
			} else {
				return BuildJsonOfObject.getJsonString(MSG.EmailOrPasswordWrong, MSG.FAILCODE);
			}
		} else {
			if (!userService.isExistEmail(email)) {
				return BuildJsonOfObject.getJsonString(MSG.EmailNotRegister, MSG.FAILCODE);
			}
			boolean flag = vcodeService.isExistVcode(email, vcode);
			if (flag) {
				Map user_map = userService.getUserMapByEmail(email);
				Params params = new Params();
				params.put("userguid", user_map.get("rowguid"));
				params.put("username", user_map.get("loginname"));
				params.put("ouguid", user_map.get("ouguid"));
				params.put("ouname", user_map.get("ouguid")!=null?ouService.getOuInfoByGuid(user_map.get("ouguid").toString()).get("ouname"):"");
				String logourl = user_map.get("logourl") == null ? "" : user_map.get("logourl").toString();
				String token = AbsSessionHelper.getRandomString(8);
				AbsSessionHelper.setCurrentUserInfo(token, params.toJson());
				
				// 记录登录日志
				AbsLoginInfo loginInfo = new AbsLoginInfo();
				loginInfo.setRowguid(UUID.randomUUID().toString());
				loginInfo.setLogindate(LocalDateTime.now());
				loginInfo.setUserinfo(user_map.get("loginname").toString());
				loginInfo.setUserguid(user_map.get("rowguid").toString());
				loginInfo.setUsertoken(token);
				loginService.addLoginInfo(loginInfo);
				
				JSONObject jsonRtn= new JSONObject();
				jsonRtn.put("token", token);
				jsonRtn.put("logourl", logourl);
				jsonRtn.put("username", params.getString("username"));
				jsonRtn.put("ouname", params.getString("ouname"));
				jsonRtn.put("ouguid", params.getString("ouguid"));
				logger.info("[结束] 邮箱登录");
				return BuildJsonOfObject.getJsonString(jsonRtn,MSG.SUCCESSCODE);
			} else {
				return BuildJsonOfObject.getJsonString(MSG.YZMError, MSG.FAILCODE);
			}

		}

	}
	
	/**
	 * 通过邮箱注册
	 * @param map
	 * @return
	 */

	
	@NoNeedLogin
	@ResponseBody
	@RequestMapping("/emailregister")
	public JSONObject emailRegister(@RequestBody Map<String, String> map) {
		logger.info("[开始] 邮箱注册，参数: {}", map.toString());
		String password = map.get("password");
		String email = map.get("email");
		String vcode=map.get("vcode");
		if (StrUtil.isStrBlank(email)) {
			return BuildJsonOfObject.getJsonString(MSG.EmailCanNotNull, MSG.FAILCODE);
		}

		if (StrUtil.isStrBlank(password)) {
			return BuildJsonOfObject.getJsonString(MSG.PasswordCanNotNull, MSG.FAILCODE);
		}
		
		
		//判断邮箱是否已经存在
		if(userService.isExistEmail(email)) {
			logger.info("当前邮箱已经存在");
			return BuildJsonOfObject.getJsonString(MSG.EmailAlreadyRegister,MSG.AlreadyRegister);
		}
		
		boolean flag = vcodeService.isExistVcode(email,vcode);
		
		if(!flag) {
			return BuildJsonOfObject.getJsonString(MSG.EmailOrCodeWrong,MSG.FAILCODE);
		}
		
		String ouguid=configService.getByConfigValueByName("MROUGUID",null);
		String userrole=configService.getByConfigValueByName("MRROLE",null);

		AbsUserInfo userinfo = new AbsUserInfo();
		userinfo.setAddtime(LocalDateTime.now());
		userinfo.setEmail(email);
		userinfo.setLoginname(email);
		userinfo.setOuguid(ouguid);
		userinfo.setUserrole(userrole);
		userinfo.setPassword(MD5Util.MD5(password));
		userinfo.setRowguid(UUID.randomUUID().toString());
		userService.addUser(userinfo);
		logger.info("[结束] 邮箱注册");
			return BuildJsonOfObject.getJsonString(MSG.RegisterSuccess, MSG.SUCCESSCODE);

	}

	@ResponseBody
	@RequestMapping("/loginout")
	public JSONObject loginOut(@ToToken Params params) {
		String token = params.getString("token");
		logger.info("[开始] 退出登录，参数: {}", token);
		AbsSessionHelper.removeAccessTokenToCache(token);
		loginService.updateLogoutTime(token);
		logger.info("[结束] 退出登录");
			return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE);
	}

	
	@NoNeedLogin
	@ResponseBody
	@RequestMapping("/istokenvalid")
	public JSONObject isTokenValid(@ToToken Params params) {
		String token = params.getString("token");
		logger.info("[开始] 验证登录是否有效，参数: {}", token);
		String userguid = AbsSessionHelper.getCurrentUserGuid(token);
		if (StrUtil.isNotBlank(userguid)) {
			logger.info("[结束] 验证登录是否有效");
			return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE);
		}
		logger.info("[结束] 验证登录是否有效");
			return BuildJsonOfObject.getJsonString(MSG.TokenIsValid, MSG.FAILCODE);
	}
	
	
	@ResponseBody
	@RequestMapping("/resetpass")
	public JSONObject resetPass(@RequestBody Map<String,String> reqMap,@ToToken Params params) {
		String token = params.getString("token");
		logger.info("[开始] 重置密码，参数: {}，token: {}", reqMap.toString(), token);
		String userguid = AbsSessionHelper.getCurrentUserGuid(token);
		String oldpass=reqMap.get("oldpass");
		String newpass=reqMap.get("newpass");
		AbsUserInfo user=userService.getUserByGuid(userguid);
		if(user.getPassword().equals(MD5Util.MD5(oldpass))) {
			user.setPassword(MD5Util.MD5(newpass));
			userService.updateUser(user);
			logger.info("[结束] 重置密码");
			return BuildJsonOfObject.getJsonString(MSG.ok, MSG.SUCCESSCODE);
		}else {
			logger.info("[结束] 重置密码");
			return BuildJsonOfObject.getJsonString(MSG.YPasswordIsWrong, MSG.FAILCODE);
		}
	}

	@ResponseBody
	@RequestMapping("/sendvcode")
	@NoNeedLogin
	public JSONObject sendVcode(@RequestBody Map<String, String> map) {
		logger.info("[开始] 发送验证码，参数: {}", map.toString());
		String email = map.get("email").toString();
		// 生成验证码
		String vcode = StrUtil.vcode();
		AbsVcodeInfo code = new AbsVcodeInfo();
		code.setEmail(email);
		code.setRowguid(UUID.randomUUID().toString());
		code.setVcode(vcode);
		Date date = new Date();
		code.setExpiredtime(date.getTime() + 5 * 60 * 1000);

		JSONObject json = EmailUtil.publishEmail(email, "验证码", "你的验证码是：" + vcode + " 【小博股数据】");
		
		if ("0000".equals(json.getString("code"))) {
			logger.info("验证码发送成功");
			code.setSendstatus(1);
			vcodeService.addVcodeEntity(code);
		}else {
			logger.info("验证码发送失败{}",json.getString("msg"));
		}
		
		logger.info("[结束] 发送验证码");
			return BuildJsonOfObject.getJsonString(MSG.SendSuccess,vcode,MSG.SUCCESSCODE);
	}

}
