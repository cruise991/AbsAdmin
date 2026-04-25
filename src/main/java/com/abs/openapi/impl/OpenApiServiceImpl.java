package com.abs.openapi.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.abs.openapi.api.IOpenApiService;
import com.abs.system.util.AbsDbHelper;
import com.abs.system.util.AbsDbService;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.Params;
import com.abs.system.util.StrUtil;
import com.alibaba.fastjson.JSONObject;


@Service
public class OpenApiServiceImpl implements IOpenApiService{

	@Override
	public JSONObject queryStockList(JSONObject pjson) {
		String pagesize=pjson.getString("pagesize");
		String pagenum=pjson.getString("pagenum");
		if(StrUtil.isStrBlank(pagesize) && !StrUtil.isNumber(pagesize)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，pagesize不能为空而且整数为数字", MSG.FAILCODE);
		}
		
	    if(StrUtil.isStrBlank(pagenum) && !StrUtil.isNumber(pagenum)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，pagenum不能为空而且整数为数字", MSG.FAILCODE);
		}
		AbsDbService dbService=AbsDbHelper.getDbService();
		String sql_list="select ccode,cname,bkname,bkcode from stock_company where $like(cname,cname) and $equal(ccode,ccode) order by ccode";
		Params params=new Params(pjson);
		String sql_count="select count(1) from stock_company where $like(cname,cname) and $equal(ccode,ccode)";
		List<Map<String,Object>> list=dbService.queryPageListMapBySql(sql_list, params);
		long count =dbService.queryCountBySql(sql_count, params);
		JSONObject jsonRtn=new JSONObject();
		jsonRtn.put("msg", "查询成功");
		jsonRtn.put("data", list);
		jsonRtn.put("code", MSG.SUCCESSCODE);
		jsonRtn.put("total", count+"");
		return jsonRtn;
		

	}

	@Override
	public JSONObject queryBoardList(JSONObject pjson) {
		String pagesize=pjson.getString("pagesize");
		String pagenum=pjson.getString("pagenum");
		if(StrUtil.isStrBlank(pagesize) && !StrUtil.isNumber(pagesize)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，pagesize不能为空而且整数为数字", MSG.FAILCODE);
		}
	    if(StrUtil.isStrBlank(pagenum) && !StrUtil.isNumber(pagenum)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，pagenum不能为空而且整数为数字", MSG.FAILCODE);
		}
		AbsDbService dbService=AbsDbHelper.getDbService();
		String sql_list="select rowguid as bkcode,cplate as bkname from stock_board order by bkcode";
		Params params=new Params(pjson);
		String sql_count="select count(1) from stock_board";
		List<Map<String,Object>> list=dbService.queryPageListMapBySql(sql_list, params);
		long count =dbService.queryCountBySql(sql_count, params);
		JSONObject jsonRtn=new JSONObject();
		jsonRtn.put("msg", "查询成功");
		jsonRtn.put("data", list);
		jsonRtn.put("code", MSG.SUCCESSCODE);
		jsonRtn.put("total", count+"");
		return jsonRtn;
	}

	@Override
	public JSONObject queryConceptionList(JSONObject pjson) {
		String pagesize=pjson.getString("pagesize");
		String pagenum=pjson.getString("pagenum");
		if(StrUtil.isStrBlank(pagesize) && !StrUtil.isNumber(pagesize)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，pagesize不能为空而且整数为数字", MSG.FAILCODE);
		}
	    if(StrUtil.isStrBlank(pagenum) && !StrUtil.isNumber(pagenum)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，pagenum不能为空而且整数为数字", MSG.FAILCODE);
		}
		AbsDbService dbService=AbsDbHelper.getDbService();
		
		String sql_addtime="select addtime from stock_conception order by addtime desc limit 1";
		Map<String,Object> map=dbService.queryMapBySql(sql_addtime);
		Date addtime=(Date) map.get("addtime");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		
		String sql_list="select gnname,gncode from stock_conception where date_format(addtime,'%Y%m%d')=\'"+sdf.format(addtime)+"\' order by gncode";
		Params params=new Params(pjson);
		String sql_count="select count(1) from stock_conception where date_format(addtime,'%Y%m%d')=\'"+sdf.format(addtime)+"\' order by gncode";
		List<Map<String,Object>> list=dbService.queryPageListMapBySql(sql_list, params);
		long count =dbService.queryCountBySql(sql_count, params);
		JSONObject jsonRtn=new JSONObject();
		jsonRtn.put("msg", "查询成功");
		jsonRtn.put("data", list);
		jsonRtn.put("code", MSG.SUCCESSCODE);
		jsonRtn.put("total", count+"");
		return jsonRtn;
	}

	@Override
	public JSONObject queryTradeList(JSONObject pjson) {
		String pagesize=pjson.getString("pagesize");
		String pagenum=pjson.getString("pagenum");
		String ccode=pjson.getString("ccode");
		if(StrUtil.isStrBlank(pagesize) && !StrUtil.isNumber(pagesize)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，pagesize不能为空而且整数为数字", MSG.FAILCODE);
		}
	    if(StrUtil.isStrBlank(pagenum) && !StrUtil.isNumber(pagenum)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，pagenum不能为空而且整数为数字", MSG.FAILCODE);
		}
	    if(StrUtil.isStrBlank(ccode)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，股票代码ccode不能为空", MSG.FAILCODE);

	    }
		AbsDbService dbService=AbsDbHelper.getDbService();
		

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String sql_list="select ccode,closeprice,dealprice,dealje,liutong,buyname,buycode,sellname,sellcode,tradedate from stock_bigtrade where ccode={ccode} and tradedate is not null order by tradedate desc";
		Params params=new Params(pjson);
		String sql_count="select count(1) from stock_bigtrade where ccode={ccode} order by tradedate desc";
		List<Map<String,Object>> list=dbService.queryPageListMapBySql(sql_list, params);
		if(list!=null) {
			for(Map<String,Object> map:list) {
					Date tradedate=(Date) map.get("tradedate");
					map.put("tradedate",sdf.format(tradedate));
			}
		}
		long count =dbService.queryCountBySql(sql_count, params);
		JSONObject jsonRtn=new JSONObject();
		jsonRtn.put("msg", "查询成功");
		jsonRtn.put("data", list);
		jsonRtn.put("code", MSG.SUCCESSCODE);
		jsonRtn.put("total", count+"");
		return jsonRtn;
	}

	@Override
	public JSONObject queryPriceInfo(JSONObject pjson) {

		String ccode=pjson.getString("ccode");
		String date=pjson.getString("date");

	    if(StrUtil.isStrBlank(ccode)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，股票代码ccode不能为空", MSG.FAILCODE);

	    }
	    if(StrUtil.isStrBlank(date)) {
				return BuildJsonOfObject.getJsonString("内部参数有问题,日期date不能为空", MSG.FAILCODE);
		}
		AbsDbService dbService=AbsDbHelper.getDbService();
		

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String sql_info="select ccode,openprice,lowprice,topprice,cprice,changehandslv,cfluxslv,cflux,shijing,dealje,dealnum,updatetime from stock_info where ccode=\'"+ccode+"\' and DATE_FORMAT(updatetime,'%Y%m%d')=\'"+date+"\'";
		Map<String,Object> map=dbService.queryMapBySql(sql_info);
		if(map!=null && map.get("updatetime")!=null) {
			Date updatetime=(Date) map.get("updatetime");
			map.put("updatetime",sdf.format(updatetime));

		}

		JSONObject jsonRtn=new JSONObject();
		jsonRtn.put("msg", "查询成功");
		jsonRtn.put("data", map);
		jsonRtn.put("code", MSG.SUCCESSCODE);
		return jsonRtn;
	}

	@Override
	public JSONObject queryBoardInfo(JSONObject pjson) {
		String bkcode=pjson.getString("bkcode");
		String date=pjson.getString("date");

	    if(StrUtil.isStrBlank(bkcode)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，板块代码bkcode不能为空", MSG.FAILCODE);

	    }
	    if(StrUtil.isStrBlank(date)) {
				return BuildJsonOfObject.getJsonString("内部参数有问题,日期date不能为空", MSG.FAILCODE);
		}
		AbsDbService dbService=AbsDbHelper.getDbService();
		

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String sql_info="select bkcode,bkname,syncdate,cflux,changeslv,upnum,downnum from stock_board_jemxjemx where bkcode=\'"+bkcode+"\' and DATE_FORMAT(syncdate,'%Y%m%d')=\'"+date+"\'";
		Map<String,Object> map=dbService.queryMapBySql(sql_info);
		if(map!=null && map.get("syncdate")!=null) {
			Date syncdate=(Date) map.get("syncdate");
			map.put("syncdate",sdf.format(syncdate));

		}

		JSONObject jsonRtn=new JSONObject();
		jsonRtn.put("msg", "查询成功");
		jsonRtn.put("data", map);
		jsonRtn.put("code", MSG.SUCCESSCODE);
		return jsonRtn;
	}

	@Override
	public JSONObject queryProfitList(JSONObject pjson) {
		String ccode=pjson.getString("ccode");

	    if(StrUtil.isStrBlank(ccode)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，股票代码ccode不能为空", MSG.FAILCODE);

	    }

		AbsDbService dbService=AbsDbHelper.getDbService();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String sql_info="select ccode,pershare,total_income,total_increase,total_quarter,profit_income,profit_income,profit_increase,profit_quarter,noticetime from stock_profit where ccode=\'"+ccode+"\' order by noticetime desc";
		List<Map<String,Object>> list_map=dbService.queryListForMapBySql(sql_info);
		if(list_map!=null && list_map.size()>0) {
			for(Map<String,Object> map:list_map) {
				Date noticetime=(Date) map.get("noticetime");
				map.put("noticetime",sdf.format(noticetime));
			}
		}

		JSONObject jsonRtn=new JSONObject();
		jsonRtn.put("msg", "查询成功");
		jsonRtn.put("data", list_map);
		jsonRtn.put("code", MSG.SUCCESSCODE);
		return jsonRtn;
	}

	@Override
	public JSONObject queryXSJJList(JSONObject pjson) {
		String ccode=pjson.getString("ccode");
	    if(StrUtil.isStrBlank(ccode)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，股票代码ccode不能为空", MSG.FAILCODE);
	    }
		AbsDbService dbService=AbsDbHelper.getDbService();
		String sql_info="select limitdate,limitnumber,limitslv from stock_holder_limit where ccode=\'"+ccode+"\'";
		List<Map<String,Object>> list_map=dbService.queryListForMapBySql(sql_info);
		if(list_map==null) {
			list_map=new ArrayList<>();
		}
		JSONObject jsonRtn=new JSONObject();
		jsonRtn.put("msg", "查询成功");
		jsonRtn.put("data", list_map);
		jsonRtn.put("code", MSG.SUCCESSCODE);
		return jsonRtn;
	}

	@Override
	public JSONObject queryBoardJemxList(JSONObject pjson) {
		String pagesize=pjson.getString("pagesize");
		String pagenum=pjson.getString("pagenum");
		String bkcode=pjson.getString("bkcode");
		if(StrUtil.isStrBlank(pagesize) && !StrUtil.isNumber(pagesize)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，pagesize不能为空而且整数为数字", MSG.FAILCODE);
		}
	    if(StrUtil.isStrBlank(pagenum) && !StrUtil.isNumber(pagenum)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，pagenum不能为空而且整数为数字", MSG.FAILCODE);
		}
	    if(StrUtil.isStrBlank(bkcode)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，板块代码bkcode不能为空", MSG.FAILCODE);

	    }
		AbsDbService dbService=AbsDbHelper.getDbService();
		

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String sql_list="select bkcode,bkname,mainje,mainslv,modje,modslv,smallje,smallslv,mxdate from stock_board_jemx where bkcode=\'"+bkcode+"\' order by mxdate desc";
		Params params=new Params(pjson);
		String sql_count="select count(1) from stock_board_jemx where bkcode=\'"+bkcode+"\' order by mxdate desc";
		List<Map<String,Object>> list=dbService.queryPageListMapBySql(sql_list, params);
		if(list!=null) {
			for(Map<String,Object> map:list) {
					Date mxdate=(Date) map.get("mxdate");
					map.put("mxdate",sdf.format(mxdate));
			}
		}
		long count =dbService.queryCountBySql(sql_count, params);
		JSONObject jsonRtn=new JSONObject();
		jsonRtn.put("msg", "查询成功");
		jsonRtn.put("data", list);
		jsonRtn.put("code", MSG.SUCCESSCODE);
		jsonRtn.put("total", count+"");
		return jsonRtn;

	}

	@Override
	public JSONObject queryStockJemxList(JSONObject pjson) {
		String pagesize=pjson.getString("pagesize");
		String pagenum=pjson.getString("pagenum");
		String ccode=pjson.getString("ccode");
		if(StrUtil.isStrBlank(pagesize) && !StrUtil.isNumber(pagesize)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，pagesize不能为空而且整数为数字", MSG.FAILCODE);
		}
	    if(StrUtil.isStrBlank(pagenum) && !StrUtil.isNumber(pagenum)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，pagenum不能为空而且整数为数字", MSG.FAILCODE);
		}
	    if(StrUtil.isStrBlank(ccode)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，股票代码ccode不能为空", MSG.FAILCODE);

	    }
		AbsDbService dbService=AbsDbHelper.getDbService();
		

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String sql_list="select mainje,mainslv,superje,superslv,bigje,bigslv,closeprice,fluslv,modje,modslv,smallje,smallslv,mxdate from stock_jemx where ccode=\'"+ccode+"\' order by mxdate desc";
		Params params=new Params(pjson);
		String sql_count="select count(1) from stock_jemx where ccode=\'"+ccode+"\'";
		List<Map<String,Object>> list=dbService.queryPageListMapBySql(sql_list, params);
		if(list!=null) {
			for(Map<String,Object> map:list) {
					Date mxdate=(Date) map.get("mxdate");
					map.put("mxdate",sdf.format(mxdate));
			}
		}
		long count =dbService.queryCountBySql(sql_count, params);
		JSONObject jsonRtn=new JSONObject();
		jsonRtn.put("msg", "查询成功");
		jsonRtn.put("data", list);
		jsonRtn.put("code", MSG.SUCCESSCODE);
		jsonRtn.put("total", count+"");
		return jsonRtn;
	}

	@Override
	public JSONObject queryConceptionStockList(JSONObject pjson) {
		String pagesize=pjson.getString("pagesize");
		String pagenum=pjson.getString("pagenum");
		String gncode=pjson.getString("gncode");
		if(StrUtil.isStrBlank(pagesize) && !StrUtil.isNumber(pagesize)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，pagesize不能为空而且整数为数字", MSG.FAILCODE);
		}
	    if(StrUtil.isStrBlank(pagenum) && !StrUtil.isNumber(pagenum)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，pagenum不能为空而且整数为数字", MSG.FAILCODE);
		}
	    if(StrUtil.isStrBlank(gncode)) {
			return BuildJsonOfObject.getJsonString("内部参数有问题，概念代码gncode不能为空", MSG.FAILCODE);

	    }
		AbsDbService dbService=AbsDbHelper.getDbService();
		

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String sql_list="select gncode,gnname,gnslv,ccode,cname,price,fluslv from stock_conception_mx where gncode=\'"+gncode+"\' order by fluslv desc";
		Params params=new Params(pjson);
		String sql_count="select count(1) from stock_conception_mx where gncode=\'"+gncode+"\'";
		List<Map<String,Object>> list=dbService.queryPageListMapBySql(sql_list, params);
		long count =dbService.queryCountBySql(sql_count, params);
		JSONObject jsonRtn=new JSONObject();
		jsonRtn.put("msg", "查询成功");
		jsonRtn.put("data", list);
		jsonRtn.put("code", MSG.SUCCESSCODE);
		jsonRtn.put("total", count+"");
		return jsonRtn;
	}



}
