package com.abs.system.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.abs.system.api.IAbsRoleService;
import com.abs.system.api.IAbsUserService;
import com.abs.system.api.IAbsViewRoleService;
import com.abs.system.domain.AbsRoleInfo;
import com.abs.system.domain.AbsRoleViewPerm;
import com.abs.system.util.BeanUtil;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/role")
public class AbsRoleController {

	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Logger logger=LoggerFactory.getLogger(AbsRoleController.class);

	@Autowired
	private IAbsRoleService roleService;

	@Autowired
	private IAbsUserService userService;
	
	
	@Autowired
	private IAbsViewRoleService vrService;

	
	@ResponseBody
	@RequestMapping(value = "/getrolebyguid")
	public JSONObject getRoleByGuid(@RequestBody Map<String,String> map) {
		String rowguid=map.get("rowguid").toString();
		AbsRoleInfo role=roleService.getOneByGuid(rowguid);  
		return BuildJsonOfObject.getJsonString(Params.getRecordByObject(role).toJSObject(), MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping(value = "/addrole", produces = "application/json; charset=utf-8")
	public JSONObject addrole(@RequestBody Map<String, Object> map) {
		logger.info("用户正在调用addrole 添加角色信息：{}",map.toString());
		try {
			AbsRoleInfo roleinfo = BeanUtil.mapToBean(map, AbsRoleInfo.class);
			roleinfo.setAddtime(new Date());
			roleinfo.setRowguid(UUID.randomUUID().toString());
			String rolename=roleinfo.getRolename();
			long count = roleService.isExistSameRoleName(rolename,null);
			if (count > 0) {
				return BuildJsonOfObject.getJsonString(MSG.ExitSameItemName,MSG.SUCCESSCODE);
			}
			roleService.addRole(roleinfo);
			return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);
		}
	}
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "/editrole", produces = "application/json; charset=utf-8")
	public JSONObject editrole(@RequestBody Map<String, Object> map) {
		logger.info("当前调用接口editrole，用户传入参数:{}",map.toString());
		try {
			AbsRoleInfo roleinfo = BeanUtil.mapToBean(map, AbsRoleInfo.class);
			roleinfo.setAddtime(new Date());
			String rolename=roleinfo.getRolename();
			String rowguid=roleinfo.getRowguid();
			long count = roleService.isExistSameRoleName(rolename,rowguid);
			if (count > 0) {
				return BuildJsonOfObject.getJsonString(MSG.ExitSameItemName, MSG.FAILCODE);
			}
			roleService.updateRole(roleinfo);
			return BuildJsonOfObject.getJsonString(MSG.updateSuccess, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);
		}
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/delrole", produces = "application/json; charset=utf-8")
	public JSONObject delrole(@RequestBody Map<String, Object> map) {
		try {
			String rowguid=map.get("rowguid").toString();
			long count=userService.isUseingRole(rowguid);
			if (count > 0) {
				return BuildJsonOfObject.getJsonString("有用户使用该角色",MSG.FAILCODE);
			}
			roleService.delRole(rowguid);
			return BuildJsonOfObject.getJsonString(MSG.delSuccess,MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);
		}

	}
	
	@ResponseBody
	@RequestMapping(value = "/getlist")
	public JSONObject getlist(@RequestBody Map<String, Object> map) {
		logger.info("***************正在调用role/getlist************");
		try {
			String rolename = map.get("rolename") == null ? "" : map.get("rolename").toString();
			int pagesize = Integer.parseInt(map.get("pagesize")+"");
			int pagenum = Integer.parseInt(map.get("pagenum")+"");
			List<Map<String,Object>> list=roleService.getRolePageListByRoleName(pagesize,pagenum,rolename);
			logger.info("当前查询结果：{}",list.toString());
			DateTimeFormatter sdfs=DateTimeFormatter.ofPattern("yyyyMMdd");
			for(Map<String,Object> m:list) {
			   LocalDateTime  date=(LocalDateTime)m.get("addtime");
			   m.put("addtime", date.format(sdfs));
			}
			long count=roleService.getTotalNumByRoleName(rolename);
			return BuildJsonOfObject.getJsonString(list,count,MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getrolelist")
	public JSONObject getRoleList(@RequestBody Map<String, Object> map) {
		logger.info("***************正在调用role/getrolelist************");
		try {
			List<Map<String,Object>> list=roleService.getRoleMapList();	
		    if(list!=null && list.size()>0) {
		    	list.forEach(item->{
		    		item.put("text", item.get("rolename"));
		    		item.put("value", item.get("rowguid"));
		    	});
		    }
			return BuildJsonOfObject.getJsonString(list,MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/addroleperm")
	public JSONObject addRolePerm(@RequestBody Map<String, Object> map) {
		logger.info("***************正在调用role/addroleperm：{}************",map.toString());
		try {
			String roleguid=map.get("roleguid").toString();
			String viewsguid=map.get("viewsguid").toString();
			vrService.deleteByRoleguid(roleguid);
			if(StrUtil.isNotBlank(viewsguid)) {
                    String [] arr=StrUtil.getStringArrayByString(viewsguid);
					List<AbsRoleViewPerm> list=new ArrayList<AbsRoleViewPerm>();
					for(int i=0;i<arr.length;i++) {
						String viewguid=arr[i];
						logger.info(viewguid);
						AbsRoleViewPerm perm=new AbsRoleViewPerm();
						perm.setRoleguid(roleguid);
						perm.setRowguid(UUID.randomUUID().toString());
						perm.setViewguid(viewguid.trim());
						list.add(perm);
					}
					vrService.addViewRoleList(list);
				}
			logger.info("保存结束");
			return BuildJsonOfObject.getJsonString(MSG.saveSuccess,MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(),MSG.FAILCODE);
		}
	}
}
