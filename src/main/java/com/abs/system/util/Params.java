package com.abs.system.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

public class Params extends BaseBean {

	/**
	 * Record 的toString 方法
	 */
	
	public Params() {};
	
	
	public Params(Map<String,Object> map) {
		super(map);
		
	}

	@Override
	public String toString() {
		StringBuilder sbf = new StringBuilder();
		Map<String, Object> map = super.getFieldMap();
		for (String key : map.keySet()) {
			sbf.append(key + "=" + map.get(key) + ",");
		}
		String result = sbf.toString();
		if (StrUtil.isNotBlank(result)) {
			result = result.substring(0, result.lastIndexOf(","));
		}
		return "[" + result + "]";
	}

	public Set<String> getSet() {
		Map<String, Object> map = super.getFieldMap();
		return map.keySet();
	}

	/**
	 * Record 的tojson的方法
	 * 
	 * @return
	 */

	public String toJson() {
		StringBuilder sbf = new StringBuilder();
		Map<String, Object> map = super.getFieldMap();
		for (String key : map.keySet()) {
			sbf.append("\"" + key + "\":\"" + map.get(key) + "\",");
		}
		String result = sbf.toString();
		if (StrUtil.isNotBlank(result)) {
			result = result.substring(0, result.lastIndexOf(","));
		}
		return "{" + result + "}";
	}

	public static Params paseJson(String json) {
		Params params=new Params();
		if (StrUtil.isNotBlank(json)) {
			try {
				JSONObject jsonObject = JSONObject.parseObject(json);
				Set<String> set = jsonObject.keySet();
				for (String key : set) {
					params.setColumn(key, jsonObject.get(key));
				}
			} catch (Exception e) {
				// 当JSON解析失败时，返回空的Params对象
				// 例如传入的是"admin"这样的非JSON字符串
			}
		}
		return params;
	}

	/**
	 * Record 的toJSONObject方法
	 * 
	 * @return
	 */

	public JSONObject toJSObject() {
		JSONObject obj = new JSONObject();
		obj.fluentPutAll(super.getFieldMap());
		return obj;
	}

	/**
	 * 将实体类转换为弱类型
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public <T> T toObject(Class<T> clazz) {
		try {
			Field[] fields = clazz.getDeclaredFields();
			Object obj = clazz.newInstance();

			for (Field f : fields) {
				String methodname = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
				Method method = clazz.getMethod("set" + methodname, f.getType());
				Object value = super.get(methodname.toLowerCase()) == null ? null : super.get(methodname.toLowerCase());
				method.invoke(obj, f.getType().cast(value));
			}
			return (T) obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> Params getRecordByObject(T object) {

		try {
			Field[] fields = object.getClass().getDeclaredFields();
			Params r = new Params();
			for (Field f : fields) {
				String methodname = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
				Method method = object.getClass().getMethod("get" + methodname, null);
				Object value = method.invoke(object, null);
				r.setColumn(methodname.toLowerCase(), value);
			}
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Params getRecordByMap(Map<String, Object> map) {
		if (map != null) {
			Params r = new Params();
			for (String key : map.keySet()) {
				r.setColumn(key, map.get(key));
			}
			return r;
		}
		return null;
	}




	
	




}
