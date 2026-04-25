package com.abs.system.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;

/**
 * @category 字符串验证 v1.1
 * @author wdg
 *
 */
public class StrUtil {

	public static boolean isStrBlank(String str) {
		if (str == null || str.length() < 1 || "null".equals(str) || str.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isObjBlank(Object obj) {
		if (obj == null) {
			return true;
		} else if ((obj + "").length() < 1) {
			return true;
		} else if ("null".equals(obj)) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean isNotBlank(String str) {
		return !isStrBlank(str);
	}

	public static String getStringSplitByList(List<String> list) {
		StringBuilder sb = new StringBuilder();
		if (list != null && list.size() > 0) {
			for (String str : list) {
				sb.append(str);
				sb.append(";");
			}
		}
		if (isNotBlank(sb.toString())) {
			return sb.toString().substring(0, sb.toString().length() - 1);
		} else {
			return null;
		}
	}

	/**
	 * 将字符串按照指定分隔符分割成List<String>
	 * @param str 要分割的字符串
	 * @param separator 分隔符
	 * @return 分割后的List<String>
	 */
	public static List<String> splitToList(String str, String separator) {
		List<String> list = new ArrayList<>();
		if (isStrBlank(str) || isStrBlank(separator)) {
			return list;
		}
		String[] array = str.split(separator);
		for (String s : array) {
			if (isNotBlank(s)) {
				list.add(s.trim());
			}
		}
		return list;
	}

	public static JSONArray getJSONArrayByList(List<?> list) {
		JSONArray jsonArray = new JSONArray();
		if (list == null || list.isEmpty()) {
			return jsonArray;// nerver return null
		}

		for (Object object : list) {
			jsonArray.add(object);
		}
		return jsonArray;
	}

	public static <T> T getObjFromParams(Map<String, String> reqMap, T obj) {
		for (String fieldname : reqMap.keySet()) {
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field f : fields) {
				if (f.getName().equalsIgnoreCase(fieldname)) {
					try {
						Class<?> classType = Class.forName(obj.getClass().getName());
						String methodname = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
						Method method = classType.getDeclaredMethod("set" + methodname, f.getType());
						if ("int".equals(f.getType().toGenericString())) {
							int num = 0;
							if (reqMap.get(fieldname) != null) {
								num = Integer.parseInt(reqMap.get(fieldname) + "");
							} else {
								num = 0;
							}
							method.invoke(obj, num);
						}
						// 处理LocalDateTime类型
						if (f.getType().toGenericString().indexOf("LocalDateTime") > 0) {
							String d = (String) reqMap.get(fieldname);
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
							if (StrUtil.isNotBlank(d)) {
								// 支持日期格式和日期时间格式
								if (d.length() == 10) {
									// 纯日期格式：yyyy-MM-dd
									LocalDate date = LocalDate.parse(d, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
									LocalDateTime dateTime = date.atStartOfDay();
									method.invoke(obj, dateTime);
								} else {
									// 日期时间格式：yyyy-MM-dd HH:mm:ss 或 yyyy-MM-dd HH:mm
									try {
										LocalDateTime dateTime = LocalDateTime.parse(d, formatter);
										method.invoke(obj, dateTime);
									} catch (Exception e) {
										// 尝试短时间格式
										DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
										LocalDateTime dateTime = LocalDateTime.parse(d, shortFormatter);
										method.invoke(obj, dateTime);
									}
								}
							} else {
								LoggerFactory.getLogger(StrUtil.class).info("####日期时间格式错误######");
							}
						} else {
							method.invoke(obj, reqMap.get(fieldname));
						}

					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			}
		}
		return obj;
	}

	public static String[] getStringArrayByString(String viewsguid) {
		return viewsguid.replace("[", "").replace("]", "").split(",");
	}

	public static String vcode() {
		String vode = "";
		Random rand = new Random();
		for (int i = 0; i < 6; i++) {
			vode = vode.concat(rand.nextInt(10) + "");
		}
		return vode;
	}

	public static String getRanChars(){
		String str="";
		for(int i=0;i<4;i++) {
			int ranNum=(int)(Math.random()*25);
			char ch = (char)(ranNum+65);
			str=str+ch;
		}
		return str;	
	}
	
	public static String getRanChars(int len){
		String str="";
		for(int i=0;i<len;i++) {
			int ranNum=(int)(Math.random()*25);
			char ch = (char)(ranNum+65);
			str=str+ch;
		}
		return str;	
	}
	
	
	public static String getRanNumber(int len){
		String str="";
		for(int i=0;i<len;i++) {
			int ranNum=(int)(Math.random()*10);
			str=str+ranNum;
		}
		return str;	
	}

	public static String getRanName() {
		String str="";
		for(int i=0;i<6;i++) {
			int ranNum=(int)(Math.random()*25);
			char ch = (char)(ranNum+65);
			str=str+ch;
		}
		return "KC"+str.toLowerCase();	
	}

	public static boolean isNumber(String str) {
		final String number = "-0123456789.";
		for (int i = 0; i < str.length(); i++) {
			if (number.indexOf(str.charAt(i)) == -1) {
				return false;
			}
		}
		if("-".contentEquals(str) || StrUtil.isStrBlank(str)) {
			return false;
		}
		return true;
	}
}
