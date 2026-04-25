package com.abs.system.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.RuntimeErrorException;
import jakarta.persistence.Table;

import org.springframework.beans.BeanUtils;

public class BasicServiceUtil {

	/**
	 * 生成insert sql
	 * 
	 * @param clazz
	 * @return
	 */

	public static String generateInsertSql(Class<?> clazz) {
		Table annotation = clazz.getAnnotation(Table.class);
		if (annotation == null || StrUtil.isStrBlank(annotation.name())) {
			throw new RuntimeErrorException(new Error(), MSG.EntityAnnotationIsNull);
		}
		String tablename = annotation.name();
		// 获取相关的,属性
		String sql_prex = "insert into " + tablename + "(";
		String sql_suffix = "values(";
		Field[] fs = clazz.getDeclaredFields();
		if (fs == null || fs.length == 0) {
			throw new RuntimeErrorException(new Error(), MSG.EntityPropertiesIsNull);
		}
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			sql_prex = sql_prex + f.getName() + ",";
			sql_suffix = sql_suffix + "?,";
		}
		sql_prex = sql_prex.substring(0, sql_prex.lastIndexOf(",")) + ")";
		sql_suffix = sql_suffix.substring(0, sql_suffix.lastIndexOf(",")) + ")";
		return sql_prex + " " + sql_suffix;
	}

	/**
	 * 生成update sql
	 * 
	 * @param clazz
	 * @return
	 */

	public static String generateUpdateSql(Class<?> clazz, String primekey) {
		Table annotation = clazz.getAnnotation(Table.class);
		if (annotation == null || StrUtil.isStrBlank(annotation.name())) {
			throw new RuntimeErrorException(new Error(), MSG.EntityAnnotationIsNull);
		}
		String tablename = annotation.name();
		// 获取相关的,属性
		String sql_prex = "insert into " + tablename + "(";
		String sql_suffix = "values(";
		Field[] fs = clazz.getDeclaredFields();
		if (fs == null || fs.length == 0) {
			throw new RuntimeErrorException(new Error(), MSG.EntityPropertiesIsNull);
		}
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			sql_prex = sql_prex + f.getName() + ",";
			sql_suffix = sql_suffix + "?,";
		}
		sql_prex = sql_prex.substring(0, sql_prex.lastIndexOf(",")) + ")";
		sql_suffix = sql_suffix.substring(0, sql_suffix.lastIndexOf(",")) + ")";
		return sql_prex + " " + sql_suffix;
	}

	/**
	 * 生成select sql
	 * 
	 * @param clazz
	 * @return
	 */

	public static String generateSelectSql(Class<?> clazz) {
		Table annotation = clazz.getAnnotation(Table.class);
		if (annotation == null || StrUtil.isStrBlank(annotation.name())) {
			throw new RuntimeErrorException(new Error(), MSG.EntityAnnotationIsNull);
		}
		String tablename = annotation.name();

		Field[] fields = clazz.getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		for (Field f : fields) {
			sb = sb.append(f.getName().toLowerCase()).append(",");
		}
		String sqlfileds = sb.toString().substring(0, sb.lastIndexOf(","));
		String sql = "select " + sqlfileds + " from " + tablename + " where 1=1";
		return sql;
	}

	/**
	 * 返回的map 转换为实体的list
	 * 
	 * @param <T>
	 * @param list
	 * @param clazz
	 * @return
	 */

	public static <T> List<T> generateEntityListByMapList(List<Map<String, Object>> list, Class<T> clazz) {
		if (list == null || list.size() == 0) {
			throw new RuntimeErrorException(new Error(), MSG.ListIsEmpty);
		}
		Field[] fields = clazz.getDeclaredFields();
		List<Object> listRtn = new ArrayList<Object>();
		try {
			Class<?> classType = Class.forName(clazz.getName());
			for (Map<String, Object> map : list) {
				Object obj = classType.newInstance();
				for (int i = 0; i < fields.length; i++) {
					String lname = fields[i].getName().toLowerCase();

					PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, fields[i].getName());
					Method setMethod = pd.getWriteMethod();// 获得set方法
					String fieldtypename = pd.getPropertyType().getCanonicalName();
					if (ConstantUtil.basicmap.containsKey(fieldtypename)) {
						if (StrUtil.isObjBlank(map.get(lname))) {
							// 默认的空值
							setMethod.invoke(obj, ConstantUtil.basicmap.get(fieldtypename));
						} else {
							String methodName = "parse" + fieldtypename.substring(0, 1).toUpperCase()
									+ fieldtypename.substring(1);
							Class<?> claz = (Class<?>) ConstantUtil.parsemap.get(fieldtypename);
							Method method = claz.getMethod(methodName, String.class);
							Object value = method.invoke(null, map.get(lname) + "");
							setMethod.invoke(obj, value);
						}
					} else {
						setMethod.invoke(obj, map.get(lname));
					}
				}
				listRtn.add(obj);
			}
		} catch (Exception e) {
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
		return (List<T>) listRtn;
	}

	/**
     **
     *
	 */




}
