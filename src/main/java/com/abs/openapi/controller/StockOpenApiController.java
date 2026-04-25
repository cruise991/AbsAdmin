package com.abs.openapi.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import com.abs.openapi.api.IOpenApiService;
import com.abs.system.api.IAbsAppInfoService;
import com.abs.system.domain.AbsAppInfo;
import com.abs.system.filter.NoNeedLogin;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/openapi")
public class StockOpenApiController {
	
	
	public Logger logger=LoggerFactory.getLogger(StockOpenApiController.class);
	
	
	@Autowired
	private IAbsAppInfoService appService;
	
	@Autowired
	private IOpenApiService apiService;
	
	@NoNeedLogin
	@ResponseBody
	@RequestMapping(value="/gettoken",produces = "application/json; charset=utf-8")
	public JSONObject getToken(@RequestBody Map<String,Object> map) {

		logger.info("当前用户传入参数:{}",map.toString());
		AbsDbService dbService = AbsDbHelper.getDbService();
        String appkey=map.get("appkey")+"";
        String appsecret=map.get("appsecret")+"";
        JSONObject jsonRtn=new JSONObject();
        if(StrUtil.isStrBlank(appkey) || StrUtil.isStrBlank(appsecret)) {
        	jsonRtn.put("msg", "appkey,appsecret 为必填参数不能为空");
        	jsonRtn.put("code",MSG.FAILCODE);
        	return jsonRtn;
        }
        
        
        Params params=new Params();
        params.put("appkey", appkey);
        params.put("appsecret", appsecret);

        String sql="select rowguid,appname,introduction,appkey,appsecret,expiredtime, status,addtime,userguid,token,tokentype,tokenexpired from abs_appinfo where appkey={appkey} and appsecret={appsecret} and status='1' order by addtime desc limit 1";
		AbsAppInfo appinfo=dbService.queryEntityBySql(AbsAppInfo.class,sql,params);
		if(appinfo==null) {
			jsonRtn.put("msg", "appkey,appsecret 错误或已停用");
        	jsonRtn.put("code",MSG.FAILCODE);
        	return jsonRtn;
		}
		String token=UUID.randomUUID().toString();
		appinfo.setToken(token);
		String tokentype=appinfo.getTokentype();
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		int day=cal.get(Calendar.DAY_OF_MONTH);
		if(appinfo.getTokennum()>50) {
			jsonRtn.put("msg", "本月申请token次数已达上限,请联系管理员重置申请次数！");
        	jsonRtn.put("code",MSG.FAILCODE);
        	return jsonRtn;
		}
		if(day==0 || day==1) {
			appinfo.setTokennum(1);
		}else {
			appinfo.setTokennum(appinfo.getTokennum()+1);
		}
		
		if("1".contentEquals(tokentype)) {
			cal.add(Calendar.HOUR_OF_DAY, 24);
		}else {
			cal.setTime(appinfo.getExpiredtime());
		}
		appinfo.setTokenexpired(cal.getTime());
		appService.updateAppInfo(appinfo);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		jsonRtn.put("msg", "获取成功");
    	jsonRtn.put("code",MSG.SUCCESSCODE);
    	jsonRtn.put("token",appinfo.getToken());
    	jsonRtn.put("expired",sdf.format(appinfo.getExpiredtime()));
    	return jsonRtn;
	}
	
	@NoNeedLogin
	@ResponseBody
	@RequestMapping(value="/query",produces = "application/json; charset=utf-8")
	public JSONObject query(@RequestBody Map<String,Object> map) {

		logger.info("当前用户传入参数:{}",map.toString());
		AbsDbService dbService = AbsDbHelper.getDbService();
        String appkey=map.get("appkey")+"";
        String appsecret=map.get("appsecret")+"";
        String token=map.get("token")+"";
        String method=map.get("method")+"";
        String param=map.get("params")+"";
        JSONObject jsonRtn=new JSONObject();
        if(StrUtil.isStrBlank(appkey) || StrUtil.isStrBlank(appsecret) || StrUtil.isStrBlank(token) || StrUtil.isStrBlank(param) || StrUtil.isStrBlank(method)) {
        	jsonRtn.put("msg", "必填参数不能为空,请检查参数是否正确");
        	jsonRtn.put("code",MSG.FAILCODE);
        	return jsonRtn;
        }
        
        Params params=new Params();
        params.put("appkey", appkey);
        params.put("appsecret", appsecret);
        String sql="select rowguid,appname,introduction,appkey,appsecret,expiredtime,status,addtime,userguid,token,tokentype,tokenexpired from abs_appinfo where appkey={appkey} and appsecret={appsecret} and status='1' order by addtime desc limit 1";
		AbsAppInfo appinfo=dbService.queryEntityBySql(AbsAppInfo.class,sql,params);
		if(appinfo==null) {
			jsonRtn.put("msg", "appkey,appsecret 错误或已停用");
        	jsonRtn.put("code",MSG.FAILCODE);
        	return jsonRtn;
		}
		System.out.println(appinfo.getToken());
		System.out.println(appinfo.getTokenexpired());
		if(!token.contentEquals(appinfo.getToken()) || appinfo.getTokenexpired().getTime()<new Date().getTime()) {
			jsonRtn.put("msg", "token错误或者已过期");
        	jsonRtn.put("code",MSG.FAILCODE);
        	return jsonRtn;
		}
		
		JSONObject pjson=new JSONObject();
		try {
			pjson=JSONObject.parseObject(param);
		}catch (Exception e) {
			jsonRtn.put("msg", "params 内部参数有问题无法解析，请传入正确的json格式");
        	jsonRtn.put("code",MSG.FAILCODE);
        	return jsonRtn;
		}
		switch(method) {
	 	    case "xbg_stock_list":
	 	    	return apiService.queryStockList(pjson);
	 	    case "xbg_board_list":
	 	    	return apiService.queryBoardList(pjson);
	 	    case "xbg_conception_list":
	 	     	return apiService.queryConceptionList(pjson);
	 	    case "xbg_trade_list":
	 	     	return apiService.queryTradeList(pjson);
	 	    case "xbg_price_info":
	 	     	return apiService.queryPriceInfo(pjson);	
	 	    case "xbg_stock_jemx":
	 	     	return apiService.queryStockJemxList(pjson);
	 	    case "xbg_conception_stock":
	 	        return apiService.queryConceptionStockList(pjson);
	 	    case "xbg_board_info":
	 	     	return apiService.queryBoardInfo(pjson);
	 	    case "xbg_board_jemx":
	 	     	return apiService.queryBoardJemxList(pjson);
	 	    case "xbg_profit_list":
	 	     	return apiService.queryProfitList(pjson);
	 	   case "xbg_xsjj_list":
	 	     	return apiService.queryXSJJList(pjson);
		    default:
		    	 jsonRtn.put("msg", "method 参数有误或者方法未授权");
		         jsonRtn.put("code",MSG.FAILCODE);
		         return jsonRtn;
		}
	}

}
