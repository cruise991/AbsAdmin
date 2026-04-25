package com.abs.system.controller;

import java.time.LocalDateTime;
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

import com.abs.system.api.IAbsUserService;
import com.abs.system.api.IAbsViewRoleService;
import com.abs.system.api.IAbsViewService;
import com.abs.system.domain.AbsViewInfo;
import com.abs.system.domain.AbsViewRole;
import com.abs.system.filter.ToToken;
import com.abs.system.util.AbsSessionHelper;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/view")
public class AbsViewInfoController {

	@Autowired
	private IAbsViewService viewService;

	@Autowired
	private IAbsViewRoleService vrService;

	@Autowired
	private IAbsUserService userService;

	Logger logger = LoggerFactory.getLogger(AbsViewInfoController.class);

	@ResponseBody
	@RequestMapping("/getrootview")
	public JSONObject getRootView() {
		try {
			String isroot = "1";
			List<AbsViewInfo> list = viewService.getViewListByIsRoot(isroot);
			return BuildJsonOfObject.getJsonString(list, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	@ResponseBody
	@RequestMapping("/getrootviewmaplist")
	public JSONObject getrootviewmaplist() {
		try {
			String isroot = "1";
			List<Map<String, Object>> list = viewService.getViewMapListByIsRoot(isroot);
			return BuildJsonOfObject.getJsonString(list, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	@ResponseBody
	@RequestMapping("addview")
	public JSONObject addView(@RequestBody Map<String, Object> reqMap) {
		try {

			logger.info("新增模块，当前用户传入参数为：{}", reqMap.toString());
			AbsViewInfo view = new AbsViewInfo();
			String rowguid = UUID.randomUUID().toString();
			view.setRowguid(rowguid);
			view.setUrl(reqMap.get("url") == null ? "" : reqMap.get("url").toString());
			view.setSortnum(reqMap.get("sortnum").toString());
			view.setViewname(reqMap.get("viewname").toString());
			view.setAddtime(LocalDateTime.now());
			String isroot = reqMap.get("isroot").toString();
			view.setIsroot(isroot);
			if ("2".equals(isroot)) {
				view.setParentguid(reqMap.get("parentguid") + "");
				view.setParentname(reqMap.get("parentname") + "");
			}
			viewService.save(view);
			return BuildJsonOfObject.getJsonString("ok", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);

		}

	}

	@ResponseBody
	@RequestMapping("saveview")
	public JSONObject editView(@RequestBody Map<String, Object> reqMap) {
		try {

			logger.info("当前用户传入参数saveview:{}", reqMap.toString());
			String rowguid = reqMap.get("rowguid") + "";
			String viewname = reqMap.get("viewname").toString();
			String isroot = reqMap.get("isroot").toString();
			if ("1".equals(isroot) && viewService.isExistSameViewName(viewname, rowguid)) {
				return BuildJsonOfObject.getJsonString("存在相同的视图名称", MSG.FAILCODE);
			}
			boolean isupdate = true;
			AbsViewInfo view = null;
			if (StrUtil.isNotBlank(rowguid)) {
				view = viewService.findViewInfoByGuid(rowguid);
			} else {
				isupdate = false;
				view = new AbsViewInfo();
				view.setRowguid(UUID.randomUUID().toString());
				view.setAddtime(LocalDateTime.now());
			}
			
	
			view.setIsroot(isroot);
			if ("0".equals(isroot)) {
				view.setParentguid(reqMap.get("parentguid") + "");
				AbsViewInfo view1=viewService.findViewInfoByGuid(reqMap.get("parentguid").toString());
				view.setParentname(view1.getViewname());
			} else {
				view.setParentguid(null);
				view.setParentname(null);
			}
			
			view.setUrl(reqMap.get("url")==null?"":reqMap.get("url").toString());
			view.setSortnum(reqMap.get("sortnum")==null?"":reqMap.get("sortnum").toString());
			view.setViewname(reqMap.get("viewname")==null?"":reqMap.get("viewname").toString());
			view.setRemark(reqMap.get("remark")==null?"":reqMap.get("remark").toString());

			String rolesid = reqMap.get("rolesid") == null ? "" : reqMap.get("rolesid").toString();
			String[] roles = rolesid.split(";");
			List<AbsViewRole> list_role = new ArrayList<AbsViewRole>();
			for (String role : roles) {
				if (StrUtil.isNotBlank(role)) {
					AbsViewRole rolev = new AbsViewRole();
					rolev.setRoleguid(role);
					rolev.setRowguid(UUID.randomUUID().toString());
					rolev.setViewguid(rowguid);
					list_role.add(rolev);
				}
			}
			if (isupdate) {
				viewService.updateView(view);
			} else {
				viewService.save(view);
			}
			return BuildJsonOfObject.getJsonString("ok", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	@ResponseBody
	@RequestMapping("/getpagelist")
	public JSONObject getpageList(@RequestBody Map<String, Object> map) {
		try {
			logger.info("当前用户传入参数为getpagelist:{}", map.toString());
			Params params=new Params(map);
			List<Map<String, Object>> list = viewService.findPageMapByViewName(params);
			long totalcount = viewService.getViewCountByViewName(params);
			return BuildJsonOfObject.getJsonString(list, totalcount, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);

		}
	}

	@ResponseBody
	@RequestMapping("/delview")
	public JSONObject delview(@RequestBody Map<String, Object> reqMap) {
		logger.info("开始调用删除delview，用户传入参数为：{}", reqMap.toString());
		try {
			String rowguid = reqMap.get("rowguid").toString();
			viewService.deleteViewByGuid(rowguid);
			return BuildJsonOfObject.getJsonString("删除成功", MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	@ResponseBody
	@RequestMapping("getviewjson")
	public JSONObject getViewJson(@ToToken Params params) {
		try {
			String token = params.getString("token");
			logger.info("用户菜单页面:{}",token);
			String userguid = AbsSessionHelper.getCurrentUserGuid(token);
			logger.info("用户：{}",userguid);
			Map<String, Object> map = userService.getUserRoleByGuid(userguid);
			logger.info("************");
			String userrole = "";
			if (map != null) {
				userrole = map.get("userrole") == null ? "" : map.get("userrole").toString();
			}
			
			logger.info("用户角色：{}",userrole);
			
			String isroot = "1";
			List<String> list_view = new ArrayList<String>();
			if (StrUtil.isNotBlank(userrole)) {
				List<Map<String, Object>> list_v = vrService.getViewListByRoleGuid(userrole);
				if (list_v != null) {
					list_v.forEach(item -> {
						list_view.add(item.get("viewguid").toString().trim());
					});
				}
			}
			
			List<Map<String, Object>> list_root = viewService.getViewMapListByIsRoot(isroot);
			
			List<Map<String, Object>> list_o = new ArrayList<Map<String, Object>>();
			
			for (Map<String, Object> view : list_root) {
				JSONObject tree = new JSONObject();
				List<Map<String, Object>> sublist = viewService.getViewListMapByParentGuid(view.get("rowguid").toString().trim());
				List<Map<String, Object>> sublist_s = new ArrayList<Map<String, Object>>();
				if (sublist != null && sublist.size() > 0) {
					for (Map<String, Object> smap : sublist) {
						 for(String str:list_view) {
							if(str.trim().equals(smap.get("rowguid").toString().trim())) {
								sublist_s.add(smap);
								break;
							}
						}
					}
					if(sublist_s.size()>0) {
						tree.put("root", view);
						tree.put("havesub", 1);
						tree.put("subs", sublist_s);
						list_o.add(tree);
					}
				}
			}			
			logger.info(list_o.toString());
			return BuildJsonOfObject.getJsonString(list_o, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	@ResponseBody
	@RequestMapping("/initPage")
	public JSONObject getViewInfoByguid(@RequestBody Map<String, String> reqMap) {
		try {
			logger.info("***********************initPage初始化 视图修改页面{}*********************", reqMap.toString());
			String rowguid = reqMap.get("rowguid");
			AbsViewInfo view = viewService.findViewInfoByGuid(rowguid);
			// query current view who can see
			List<Params> list_role = vrService.getRoleListByViewGuid(rowguid);
			String rolesguid = "";
			if (list_role != null && list_role.size() > 0) {
				for (int i = 0; i < list_role.size(); i++) {
					rolesguid = rolesguid + list_role.get(i).getString("roleguid") + ";";
				}
				logger.info(list_role.toString());
			} else {
				logger.info("没有相关的视图设置..............");
			}
			JSONObject jsonRtn = Params.getRecordByObject(view).toJSObject();
			jsonRtn.put("hroleguids", rolesguid);
			return BuildJsonOfObject.getJsonString(jsonRtn, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

	@ResponseBody
	@RequestMapping("/getviewtree")
	public JSONObject getViewTree(@RequestBody Map<String, String> reqMap) {
		try {
			logger.info("getviewtree{}", reqMap.toString());
			String roleguid = reqMap.get("rowguid");
			List<Map<String, Object>> list_view = vrService.getViewListByRoleGuid(roleguid);
			String[] arrSelect = null;
			if (list_view != null) {
				arrSelect = new String[list_view.size()];
				for (int i = 0; i < list_view.size(); i++) {
					arrSelect[i] = list_view.get(i).get("viewguid").toString().trim();
				}
			}
			List<Map<String, Object>> list = viewService.getViewMapListByIsRoot("1");
			JSONArray arrTeee = new JSONArray();
			if (list != null) {
				for (Map<String, Object> map : list) {
					JSONObject o = new JSONObject();
					o.put("title", map.get("viewname"));
					o.put("key", map.get("rowguid"));
					List<Map<String, Object>> list_o = viewService.getViewListMapByParentGuid(map.get("rowguid").toString());
					if (list_o != null && list_o.size() > 0) {
						JSONArray arrSub = new JSONArray();
						for (Map<String, Object> mp : list_o) {
							JSONObject o_sub = new JSONObject();
							o_sub.put("title", mp.get("viewname"));
							o_sub.put("key", mp.get("rowguid"));
							arrSub.add(o_sub);
						}
						o.put("children", arrSub);
					}
					arrTeee.add(o);
				}
			}
			return BuildJsonOfObject.getJsonString(arrTeee, arrSelect == null ? new String[] {} : arrSelect,
					MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);
		}
	}

}
