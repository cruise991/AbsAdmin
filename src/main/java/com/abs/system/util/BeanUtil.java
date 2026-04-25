package com.abs.system.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.management.RuntimeErrorException;

public class BeanUtil {

	public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
		try {
			T t = (T) clazz.newInstance();
			for (Field field : clazz.getDeclaredFields()) {
				if (map.containsKey(field.getName())) {
					boolean flag = field.isAccessible();
					field.setAccessible(true);
					Object object = map.get(field.getName());
					if (object != null && field.getType().isAssignableFrom(object.getClass())) {
						field.set(t, object);
					}

					if (ConstantUtil.basicmap.containsKey(field.getType().getName())) {
						String type = field.getType().getName();
						switch (type) {
						case "short":
							field.setShort(t, Short.parseShort(object + ""));
							break;
						case "int":
							field.setInt(t, Integer.parseInt(object + ""));
							break;
						case "long":
							field.setLong(t, Long.parseLong(object + ""));
							break;
						case "float":
							field.setFloat(t, Float.parseFloat(object + ""));
							break;
						case "double":
							field.setDouble(t, Double.parseDouble(object + ""));
							break;
						default:
							break;
						}
					}
					field.setAccessible(flag);
				}
			}
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), "BeanUtil:值与数据类型不匹配");
		}
	}

	public static Map<String, Object> beanToMap(Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Field field : object.getClass().getDeclaredFields()) {
			try {
				boolean flag = field.isAccessible();
				field.setAccessible(true);
				Object o = field.get(object);
				map.put(field.getName(), o);
				field.setAccessible(flag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public static <T> T mapToBean(Map<String, Object> map, T t) {
		try {
			Class<? extends Object> clazz = t.getClass();
			for (Field field : clazz.getDeclaredFields()) {
				if (map.containsKey(field.getName())) {
					boolean flag = field.isAccessible();
					field.setAccessible(true);
					Object object = map.get(field.getName());
					if (object != null && field.getType().isAssignableFrom(object.getClass())) {
						field.set(t, object);
					}
					field.setAccessible(flag);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;

	}
}
