package com.abs.system.util;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BuildJsonOfObject {

	/**
	 * *****方法1
	 * 
	 * @param msg
	 * @param code
	 * @return
	 */

	public static JSONObject getJsonString(String msg, String code) {
		JSONObject obj = new JSONObject();
		obj.put("code", code);
		obj.put("msg", msg);
		return obj;
	}

	/**
	 * *****方法1
	 * 
	 * @param msg
	 * @param code
	 * @return
	 */

	public static JSONObject getJsonString(List<Map<String, Object>> list, Map<String, Object> map, String code) {
		JSONObject obj = new JSONObject();
		obj.put("code", code);
		obj.put("data", list);
		obj.put("msg", map);
		return obj;
	}

	/**
	 ***** 方法2
	 * 
	 * @param data
	 * @param code
	 * @param msg
	 * @return
	 */

	public static JSONObject getJsonString(String data, String msg, String code) {
		JSONObject obj = new JSONObject();
		obj.put("data", data);
		obj.put("code", code);
		obj.put("msg", msg);
		return obj;
	}
	
	
	/**
	 ***** 方法2
	 * 
	 * @param data
	 * @param code
	 * @param msg
	 * @return
	 */

	public static JSONObject getJsonString(String data, String msg, String other,String code) {
		JSONObject obj = new JSONObject();
		obj.put("data", data);
		obj.put("other", other);
		obj.put("msg", msg);
		obj.put("code", code);
		return obj;
	}

	/**
	 ***** 方法2
	 * 
	 * @param data
	 * @param code
	 * @param msg
	 * @return
	 */

	public static <T> JSONObject getJsonString(List<T> list_data, String code) {
		JSONObject obj = new JSONObject();
		obj.put("data", list_data);
		obj.put("code", code);
		return obj;
	}

	/**
	 ***** 方法2
	 * 
	 * @param data
	 * @param code
	 * @param msg
	 * @return
	 */

	public static <T> JSONObject getJsonString(List<T> list_data, String code, String msg) {
		JSONObject obj = new JSONObject();
		obj.put("data", list_data);
		obj.put("code", code);
		obj.put("msg", msg);
		return obj;
	}

	/**
	 * **方法3 分页
	 * 
	 * @param <T>
	 * @param data
	 * @param totalcount
	 * @param code
	 * @return
	 */

	public static <T> JSONObject getJsonString(List<T> data, long totalcount, String code) {
		JSONObject obj = new JSONObject();
		obj.put("data", data);
		obj.put("totalcount", totalcount);
		obj.put("code", code);
		return obj;
	}
	
	public static <T> JSONObject getJsonString(List<T> data,JSONObject other, long totalcount, String code) {
		JSONObject obj = new JSONObject();
		obj.put("data", data);
		obj.put("totalcount", totalcount);
		obj.put("code", code);
		obj.put("other", other);
		return obj;
	}

	/**
	 * ***方法4 分页
	 * 
	 * @param <T>
	 * @param data
	 * @param totalcount
	 * @param msg
	 * @param code
	 * @return
	 */

	public static <T> JSONObject getJsonString(List<T> data, long totalcount, String msg, String code) {
		JSONObject obj = new JSONObject();
		obj.put("data", data);
		obj.put("totalcount", totalcount);
		obj.put("code", code);
		obj.put("msg", msg);
		return obj;
	}

	/**
	 * ***方法5
	 * 
	 * @param jsonRtn
	 * @param msg
	 * @param code
	 * @return
	 */

	public static JSONObject getJsonString(JSONObject jsonRtn, String msg, String code) {
		JSONObject obj = new JSONObject();
		obj.put("msg", msg);
		obj.put("code", code);
		obj.put("data", jsonRtn);
		return obj;
	}

	/**
	 * ***方法**6
	 * 
	 * @param jsonRtn
	 * @param code
	 * @return
	 */

	public static JSONObject getJsonString(JSONObject jsonRtn, String code) {
		JSONObject obj = new JSONObject();
		obj.put("code", code);
		obj.put("data", jsonRtn);
		return obj;
	}

	/**
	 * ****方法7
	 * 
	 * @param <T>
	 * @param list_data
	 * @param totalcount
	 * @param otherJson
	 * @param msg
	 * @param code
	 * @return
	 */

	public static <T> JSONObject getJsonString(List<T> list_data, long totalcount, JSONObject otherJson, String msg,
			String code) {
		JSONObject jsonRtn = new JSONObject();
		jsonRtn.put("data", list_data);
		jsonRtn.put("other", otherJson);
		jsonRtn.put("totalcount", totalcount);
		jsonRtn.put("msg", msg);
		jsonRtn.put("code", code);
		return jsonRtn;
	}

	public static Map getMapString(String msg, String code, Map<String, Object> map) {
		map.put("msg", msg);
		map.put("code", code);
		return map;
	}

	public static JSONObject getJsonString(List<Map<String, Object>> list_sum, List<Map<String, Object>> list_area,
			String code) {
		JSONObject obj = new JSONObject();
		obj.put("data", list_sum);
		obj.put("msg", list_area);
		obj.put("code", code);
		return obj;
	}

	public static JSONObject getJsonString(List<Map<String, Object>> list, long totalcount,
			List<Map<String, Object>> list_area, String code) {
		JSONObject jsonRtn = new JSONObject();
		jsonRtn.put("data", list);
		jsonRtn.put("other", list_area);
		jsonRtn.put("totalcount", totalcount);
		jsonRtn.put("code", code);
		return jsonRtn;
	}

	public static JSONObject getJsonString(JSONArray data, List<Map<String, Object>> list, String code) {
		JSONObject jsonRtn = new JSONObject();
		jsonRtn.put("data", data);
		jsonRtn.put("other", list);
		jsonRtn.put("code", code);
		return jsonRtn;
	}

	public static JSONObject getJsonString(JSONArray arrTree, String[] arrSelect, String code) {
		JSONObject jsonRtn = new JSONObject();
		jsonRtn.put("data", arrTree);
		jsonRtn.put("other", arrSelect);
		jsonRtn.put("code", code);
		return jsonRtn;
	}

	public static JSONObject getJsonString(Map<String, Object> map, String code) {
		JSONObject jsonRtn = new JSONObject();
		jsonRtn.put("data", map);
		jsonRtn.put("code", code);
		return jsonRtn;
	}

}
