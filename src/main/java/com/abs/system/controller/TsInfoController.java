package com.abs.system.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
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

import com.abs.system.api.SiteUrlTsService;
import com.abs.system.domain.SiteUrlTsInfo;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.JxlExcelUtils;
import com.abs.system.util.MSG;
import com.alibaba.fastjson.JSONObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/tsinfo")
public class TsInfoController {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static String posturl = "http://data.zz.baidu.com/urls?site=https://www.read8686.com&token=0ztDbQydZsvW1t0X";

	@Autowired
	private SiteUrlTsService tsService;

	private Logger logger = LoggerFactory.getLogger(TsInfoController.class);

	@RequestMapping("/tsinfo_init")
	public String pageload() {
		return "tsinfo/tslist";
	}

	@ResponseBody
	@RequestMapping("/addsiteurl")
	public JSONObject addsiteurl(@RequestBody Map<String, String> map) {
		logger.info("添加链接地址用户传入参数：{}", map.toString());
		String siteurl = map.get("siteurl");
		SiteUrlTsInfo tsinfo = new SiteUrlTsInfo();
		tsinfo.setAddtime(new Date());
		tsinfo.setRowguid(UUID.randomUUID().toString());
		tsinfo.setSiteurl(siteurl.trim());
		tsinfo.setTsstatus(MSG.TS_NO);
		tsService.addTsInfo(tsinfo);
		return BuildJsonOfObject.getJsonString(MSG.saveSuccess, MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping("/getpagelist")
	public JSONObject getpagelist(@RequestBody Map<String, Object> map) {
		logger.info("链接分页查询：{}", map.toString());
		List<Map<String, Object>> list = tsService.queryPageList(map);
		for (Map<String, Object> m : list) {
			m.put("addtime", sdf.format((Date) m.get("addtime")));
			m.put("tsdate", m.get("tsdate") != null ? sdf.format((Date) m.get("tsdate")) : "");
		}
		long count = tsService.queryPageTotal(map);
		return BuildJsonOfObject.getJsonString(list, count, MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping("/delbyguid")
	public JSONObject delByGuid(@RequestBody Map<String, Object> map) {
		logger.info("删除操作：{}", map.toString());
		String rowguid = map.get("rowguid").toString();
		tsService.delByGuid(rowguid);
		return BuildJsonOfObject.getJsonString(MSG.DeleteSuccess, MSG.SUCCESSCODE);
	}

	@ResponseBody
	@RequestMapping("/tstobaidu")
	public JSONObject tstobaidu(@RequestBody Map<String, String> map) {

		String msg = "";
		try {
			String rowguid = map.get("rowguid");
			SiteUrlTsInfo tsinfo = tsService.getTsInfoByGuid(rowguid);
			String[] param = { tsinfo.getSiteurl() };
			String json = Post(posturl, param);// 执行推送方法

			logger.info("推送返回结果：{}", json);
			JSONObject json_obj = JSONObject.parseObject(json);
			if ("1".equals(json_obj.getString("success"))) {
				tsinfo.setTsdate(new Date());
				tsinfo.setTsmsg("success");
				tsinfo.setTscode(json_obj.getString("remain"));
				tsinfo.setTsstatus(MSG.TS_Success);
				tsService.updateTsInfo(tsinfo);
				msg = "推送成功";
				return BuildJsonOfObject.getJsonString(msg, MSG.SUCCESSCODE);

			} else {
				tsinfo.setTsdate(new Date());
				tsinfo.setTsmsg(json_obj.getString("not_valid"));
				tsinfo.setTscode(json_obj.getString("remain"));
				tsinfo.setTsstatus(MSG.TS_Fail);
				tsService.updateTsInfo(tsinfo);
				msg = "推送失败:" + tsinfo.getTsmsg();
				return BuildJsonOfObject.getJsonString(msg, MSG.FAILCODE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
			return BuildJsonOfObject.getJsonString(msg, MSG.FAILCODE);
		}

	}

	
	@RequestMapping("/export")
	public void exportExcel(HttpServletRequest request,HttpServletResponse res) {
	    logger.info("导出文件测试");
	    String siteurl=request.getParameter("siteurl");
	    List<Map<String,Object>> list=tsService.findTsInfoMapList(siteurl);
	    List<String> list_columns=new ArrayList<>();
	    list_columns.add("链接地址");
	   
	    JxlExcelUtils.exportexcel(res,"推送数据",list,list_columns);
	}

	public static String Post(String PostUrl, String[] Parameters) {
		if (null == PostUrl || null == Parameters || Parameters.length == 0) {
			return null;
		}
		String result = "";
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			// 建立URL之间的连接
			URLConnection conn = new URL(PostUrl).openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("Host", "data.zz.baidu.com");
			conn.setRequestProperty("User-Agent", "curl/7.12.1");
			conn.setRequestProperty("Content-Length", "83");
			conn.setRequestProperty("Content-Type", "text/plain");

			// 发送POST请求必须设置如下两行
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// 获取conn对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			String param = "";
			for (String s : Parameters) {
				param += s + "\n";
			}
			out.print(param.trim());
			// 进行输出流的缓冲
			out.flush();
			// 通过BufferedReader输入流来读取Url的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}

		} catch (Exception e) {
			System.out.println("发送post请求出现异常！" + e);
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

}
