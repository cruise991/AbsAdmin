package com.abs.system.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.RuntimeErrorException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
 * ** v 1.2 实现自己定义数据源
 * 
 * 
 */

public class AbsDbService {

	private static Logger logger = LoggerFactory.getLogger(AbsDbService.class);

	private JdbcTemplate jdbcTemplate;

	public AbsDbService(DataSource source) {
		this.jdbcTemplate = new JdbcTemplate(source);
	}

	public AbsDbService(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	public DataSource getDataSource() {
		DataSource dataSource = jdbcTemplate.getDataSource();
		return dataSource;
	}

	public void beginTransaction() {
		Connection conn;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			conn.setAutoCommit(false);
			logger.info("conn catalog：{}", conn.getCatalog());
			logger.info("commit: conn.toString(){}", conn.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Savepoint setSavepoint(String name) {
		Connection conn = null;
		Savepoint savePoint = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			savePoint = conn.setSavepoint(name);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return savePoint;
	}

	public void setAutoCommit(boolean flag) {
		Connection conn;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			conn.setAutoCommit(false);
			logger.info("conn catalog：{}", conn.getCatalog());
			logger.info("commit: conn.toString(){}", conn.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Savepoint getSavePoint(String... pointname) {
		Connection conn;
		Savepoint point = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			if (pointname != null && pointname.length > 0) {
				point = conn.setSavepoint(pointname[0]);
			} else {
				point = conn.setSavepoint();
			}
			return point;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void commit() {
		Connection conn;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void rollback(Savepoint... point) {
		Connection conn;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			if (point == null && point.length > 0) {
				conn.rollback(point[0]);
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void endTransaction() {
		Connection conn;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public DataSourceTransactionManager getDataSourceTransactionManager() {
		DataSource dataSource = jdbcTemplate.getDataSource();
		DataSourceTransactionManager manager = new DataSourceTransactionManager(dataSource);
		return manager;
	}

	/**
	 * 新增v1.1 date 2021-03-15 在这个地方新增回滚机制
	 * 
	 * @param entity 要添加的实体
	 * @return 新增成功 返回true 新增失败 返回false
	 */

	public <T> void addEntity(T entity) {
		Connection conn = null;
		Savepoint point = null;
		try {
			logger.debug("****添加实体类addEntity*******");
			conn = jdbcTemplate.getDataSource().getConnection();
			conn.setAutoCommit(false);
			if (entity == null) {
				throw new RuntimeErrorException(new Error(), MSG.EntityCanNotNull);
			}
			logger.debug("生成插入sql");
			point = conn.setSavepoint();
			String sql = BasicServiceUtil.generateInsertSql(entity.getClass());
			Field[] fields = entity.getClass().getDeclaredFields();
			PreparedStatementSetter setter = new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					for (int j = 0; j < fields.length; j++) {
						// 获取方法
						PropertyDescriptor pd = null;

						pd = BeanUtils.getPropertyDescriptor(entity.getClass(), fields[j].getName());
						Method getMethod = pd.getReadMethod();
						try {
							
							ps.setObject(j + 1, getMethod.invoke(entity));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			};
			jdbcTemplate.update(sql, setter);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback(point);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new RuntimeErrorException(new Error(), e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		logger.debug("****结束实体类addEntity*******");

	}

	/**
	 * **@param 要删除的对象 ****删除对象v1.0
	 */

	public <T> void delEntity(T obj) {
		logger.debug("*****删除对象del方法正在执行******");
		try {
			String tablename = "";
			Table table = obj.getClass().getAnnotation(Table.class);
			if (table != null && StrUtil.isNotBlank(table.name())) {
				tablename = table.name();
			}
			Method method = obj.getClass().getDeclaredMethod("getRowguid");
			String rowguid = (String) method.invoke(obj);
			String sql = "delete from " + tablename + " where rowguid=\'" + rowguid + "\'";
			logger.info("delEntity=>{}", sql);
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
		logger.info("删除成功");
	}

	/**
	 **** 分页查询 v1.0
	 *** 
	 * @param pagesize 每页的个数 pagenum 当前页码 clazz 实体类类型 sqlCondition 条件
	 * @return 返回查询list
	 */
	public <T> List<T> findPageListByCondition(int pagesize, int pagenum, Class<T> clazz, String sqlCondition) {
		logger.debug("******开始调用findPageList******");
		String sql = BasicServiceUtil.generateSelectSql(clazz);
		if (StrUtil.isNotBlank(sqlCondition)) {
			sql = sql + sqlCondition + " limit " + pagesize * pagenum + "," + pagesize;
		} else {
			sql = sql + " limit " + pagesize * pagenum + "," + pagesize;
		}
		logger.debug("findPageListByCondition=>{}", sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		List<T> list_t = BasicServiceUtil.generateEntityListByMapList(list, clazz);
		return list_t;
	}

	/**
	 **** 分页有限字段
	 */

	public <T> List<Map<String, Object>> findPageListLimitFiled(int pagesize, int pagenum, Class<T> clazz,
			String sqlCondition, String[] fields) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Callable myCallable = new Callable() {
			@Override
			public List<Map<String, Object>> call() throws Exception {
				String tablename = "";
				Table table = clazz.getAnnotation(Table.class);
				if (table != null && StrUtil.isNotBlank(table.name())) {
					tablename = table.name();
				}
				StringBuffer sb = new StringBuffer();
				for (String f : fields) {
					sb = sb.append(f).append(",");
				}
				String sqlfileds = sb.toString().substring(0, sb.lastIndexOf(","));
				String sql = null;
				if (StrUtil.isNotBlank(sqlCondition)) {
					sql = "select " + sqlfileds + " from " + tablename + " where 1=1 " + sqlCondition + " limit "
							+ pagesize * pagenum + "," + pagesize;
				} else {
					sql = "select " + sqlfileds + " from " + tablename + " limit " + pagesize * pagenum + ","
							+ pagesize;
				}
				logger.debug("findPageListLimitFiled=>:{}", sql);
				List<Object> list = new ArrayList<Object>();
				List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql);
				return list1;
			}
		};

		try {
			Future future = executor.submit(myCallable);
			list = (List<Map<String, Object>>) future.get();
			future.cancel(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public <T> List<T> findPageList(int pagesize, int pagenum, Class<T> clazz) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		List<T> list = new ArrayList<T>();
		Callable myCallable = new Callable() {
			@Override
			public List<T> call() throws Exception {
				String tablename = "";
				Table table = clazz.getAnnotation(Table.class);
				if (table != null && StrUtil.isNotBlank(table.name())) {
					tablename = table.name();
				}
				Field[] fields = clazz.getDeclaredFields();
				StringBuffer sb = new StringBuffer();
				for (Field f : fields) {
					sb = sb.append(f.getName().toLowerCase()).append(",");
				}
				String sqlfileds = sb.toString().substring(0, sb.lastIndexOf(","));
				String sql = "select " + sqlfileds + " from " + tablename + " limit " + pagesize * pagenum + ","
						+ pagesize;
				logger.debug("findPageList=>:{}", sql);
				List<Object> list = new ArrayList<Object>();
				List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql);
				try {
					for (int i = 0; i < list1.size(); i++) {
						Map<String, Object> map = list1.get(i);
						Class<?> classType = Class.forName(clazz.getName());
						Object obj = classType.newInstance();
						for (Field field : fields) {
							String methodname = field.getName().substring(0, 1).toUpperCase()+ field.getName().substring(1);
							PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, field.getName());
							Method setMethod = pd.getWriteMethod();// 获得set方法
							String fieldtypename = pd.getPropertyType().getCanonicalName();
							if (ConstantUtil.basicmap.containsKey(fieldtypename)) {
								if (StrUtil.isObjBlank(map.get(methodname))) {
									// 默认的空值
									setMethod.invoke(obj, ConstantUtil.basicmap.get(fieldtypename));
								} else {
									String methodName = "parse" + fieldtypename.substring(0, 1).toUpperCase()+ fieldtypename.substring(1);
									Class<?> claz = (Class<?>) ConstantUtil.parsemap.get(fieldtypename);
									Method method = claz.getMethod(methodName, String.class);
									Object value = method.invoke(null, map.get(methodname));
									setMethod.invoke(obj, value);
								}
							} else {
								setMethod.invoke(obj, map.get(methodname));
							}
						}
						list.add(obj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return (List<T>) list;
			}
		};

		try {
			Future future = executor.submit(myCallable);
			list = (List<T>) future.get();
			future.cancel(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public <T> void updateEntity(T record, String primekey) {
		String tablename = "";
		String primekey_value = "";
		Table table = record.getClass().getAnnotation(Table.class);
		if (table != null && StrUtil.isNotBlank(table.name())) {
			tablename = table.name();
		}
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		sb.append("update " + tablename.toLowerCase() + " set ");
		Field[] fields = record.getClass().getDeclaredFields();
		for (Field f : fields) {
			String methodname = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
			try {
				if (primekey.equals(methodname.toLowerCase())) {
					Method method = record.getClass().getMethod("get" + methodname, null);
					Object value = method.invoke(record, null);
					primekey_value = value + "";
					continue;
				} else {
					sb1.append(f.getName());
					sb1.append("=");

					// logger.info(methodname);

					Method method = record.getClass().getMethod("get" + methodname, null);
					Object value = method.invoke(record, null);
					if (value == null) {
						sb1.append(value);
					} else {
						if (value.getClass().toString().indexOf("util.Date") > 0) {
							Date d = (Date) value;
							java.sql.Timestamp sdate = new java.sql.Timestamp(d.getTime());
							value = sdate;
						}
						sb1.append("\'" + value + "\'");
					}
					sb1.append(",");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String sql = sb1.toString().substring(0, sb1.toString().lastIndexOf(','));
		sql = sb.toString() + sql + " where " + primekey + "=\'" + primekey_value + "\'";
		logger.info("updateEntity=>{}", sql);
		jdbcTemplate.execute(sql);

	}

	public long count(String sql) {
		try {
			Connection conn = jdbcTemplate.getDataSource().getConnection();
			conn.setAutoCommit(false);
			ExecutorService executor = Executors.newFixedThreadPool(2);
			Callable myCallable = new Callable() {
				@Override
				public Object call() throws Exception {
					Map<String, Object> map = jdbcTemplate.queryForMap(sql);
					Set<String> set = map.keySet();
					String str = "";
					for (String s : set) {
						str = s;
					}
					if (StrUtil.isStrBlank(map.get(str) + "")) {
						return 0;
					} else {
						long value = Long.parseLong((map.get(str) + "").toString());
						return value;
					}
				}
			};

			Future future = executor.submit(myCallable);
			long value = (long) future.get();
			future.cancel(true);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public <T> T getFieldByCondition(String sqlCondition, String colname, Class<?> clazz) {
		// TODO Auto-generated method stub
		try {
			String tablename = "";
			Table table = clazz.getAnnotation(Table.class);
			if (table != null && StrUtil.isNotBlank(table.name())) {
				tablename = table.name();
			}
			String sql = "select " + colname + " from " + tablename + " where 1=1 " + sqlCondition;
			logger.debug("getFieldByCondition=>{}", sql);
			Map<String, Object> map = jdbcTemplate.queryForMap(sql);
			if (map == null) {
				return null;
			} else {
				Object obj = map.get(colname);
				return (T) obj;
			}
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public <T> List<String> getListFieldByCondition(String sqlCondition, String colname, Class<?> clazz) {
		// TODO Auto-generated method stub
		try {

			List<String> list_t = new ArrayList<String>();
			String tablename = "";
			Table table = clazz.getAnnotation(Table.class);
			if (table != null && StrUtil.isNotBlank(table.name())) {
				tablename = table.name();
			}
			String sql = "select " + colname + " from " + tablename + " where 1=1 " + sqlCondition;
			logger.debug("getListFieldByCondition=>{}", sql);
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			if (list == null) {
				return null;
			} else {
				for (Map<String, Object> map : list) {
					Object obj = map.get(colname);
					list_t.add(obj.toString());
				}
				return list_t;
			}
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public <T> T findOjectById(Class<?> clazz, Object value) {
		String tablename = "";
		Table table = clazz.getAnnotation(Table.class);
		if (table != null && StrUtil.isNotBlank(table.name())) {
			tablename = table.name();
		}
		String sql_suffix = "";
		String sql_mod = "";
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			if (f.getAnnotation(Id.class) != null) {
				sql_suffix = " where " + f.getName().toLowerCase() + "\'" + value + "\'";
			}
			sql_mod += f.getName().toLowerCase() + ",";
		}
		sql_mod = sql_mod.substring(0, sql_mod.toString().length() - 1);
		String sql = "select " + sql_mod + " from " + tablename + sql_suffix;
		logger.debug("findOjectById=>{}", sql);
		Map<String, Object> map = jdbcTemplate.queryForMap(sql);
		try {
			if (map != null) {
				Object obj = clazz.newInstance();
				for (Field f : fields) {
					// PropertyDescriptor pd = new PropertyDescriptor(f.getName(), clazz);
					PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, f.getName());

					Method wM = pd.getWriteMethod();
					wM.invoke(obj, map.get(f.getName().toLowerCase()));
				}
				return (T) obj;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public <T> T findOneEntityByGuid(String rowguid, Class<T> clazz) {
		String tablename = "";
		Table table = clazz.getAnnotation(Table.class);
		if (table != null && StrUtil.isNotBlank(table.name())) {
			tablename = table.name();
		}
		String sql_suffix = "";
		String sql_mod = "";
		Field[] fields = clazz.getDeclaredFields();
		sql_suffix = " where rowguid= " + "\'" + rowguid + "\'";
		for (Field f : fields) {
			sql_mod += f.getName().toLowerCase() + ",";
		}
		sql_mod = sql_mod.substring(0, sql_mod.toString().length() - 1);
		String sql = "select " + sql_mod + " from " + tablename + sql_suffix;
		logger.info("当前执行sql=>>>>>{}", sql);
		try {
			Map<String, Object> map = jdbcTemplate.queryForMap(sql);
			if (map != null) {
				Object obj = clazz.newInstance();
				for (Field field : fields) {
					String methodname = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
					// PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
					PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, field.getName());
					Method setMethod = pd.getWriteMethod();// 获得set方法
					String fieldtypename = pd.getPropertyType().getCanonicalName();
					if (ConstantUtil.basicmap.containsKey(fieldtypename)) {
						if (StrUtil.isObjBlank(map.get(methodname))) {
							// 默认的空值
							setMethod.invoke(obj, ConstantUtil.basicmap.get(fieldtypename));
						} else {
							String methodName = "parse" + fieldtypename.substring(0, 1).toUpperCase()
									+ fieldtypename.substring(1);
							Class<?> claz = (Class<?>) ConstantUtil.parsemap.get(fieldtypename);
							Method method = claz.getMethod(methodName, String.class);
							Object value = method.invoke(null, map.get(methodname) + "");
							setMethod.invoke(obj, value);
						}
					} else {
						setMethod.invoke(obj, map.get(methodname));
					}
				}
				return (T) obj;
			} else {
				return null;
			}
		} catch (EmptyResultDataAccessException e) {
			logger.info("未找到相关信息");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/****
	 *** 优化查询v1.1
	 */
	public <T> List<T> getListByCondition(String sqlCondition, Class<T> clazz) {
		String tablename = "";
		Table table = clazz.getAnnotation(Table.class);
		if (table != null && StrUtil.isNotBlank(table.name())) {
			tablename = table.name();
		}
		Field[] fields = clazz.getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		for (Field f : fields) {
			sb = sb.append(f.getName()).append(",");
		}
		String fields_str = sb.toString().substring(0, sb.toString().lastIndexOf(","));
		String sql = "select " + fields_str + " from " + tablename + " where 1=1";
		if (StrUtil.isNotBlank(sqlCondition)) {
			sql += sqlCondition;
		}
		logger.info("获取list:sql->{}", sql);
		List<Object> list = new ArrayList<Object>();
		List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql);
		try {
			for (int i = 0; i < list1.size(); i++) {
				Map<String, Object> map = list1.get(i);
				Class<?> classType = Class.forName(clazz.getName());
				Object obj = classType.newInstance();
				for (Field field : fields) {
					String methodname = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
					// PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
					PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, field.getName());
					Method setMethod = pd.getWriteMethod();// 获得set方法
					String fieldtypename = pd.getPropertyType().getCanonicalName();
					if (ConstantUtil.basicmap.containsKey(fieldtypename)) {
						if (StrUtil.isObjBlank(map.get(methodname))) {
							// 默认的空值
							setMethod.invoke(obj, ConstantUtil.basicmap.get(fieldtypename));
						} else {
							String methodName = "parse" + fieldtypename.substring(0, 1).toUpperCase()
									+ fieldtypename.substring(1);
							Class<?> claz = (Class<?>) ConstantUtil.parsemap.get(fieldtypename);
							Method method = claz.getMethod(methodName, String.class);
							Object value = method.invoke(null, map.get(methodname) + "");
							setMethod.invoke(obj, value);
						}
					} else {
						setMethod.invoke(obj, map.get(methodname));
					}
				}
				list.add(obj);
			}
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (List<T>) list;
	}

	public <T> void delByGuid(String rowguid, Class<T> clazz) {
		try {
			String tablename = "";
			Table table = clazz.getAnnotation(Table.class);
			if (table != null && StrUtil.isNotBlank(table.name())) {
				tablename = table.name();
			}
			if (StrUtil.isStrBlank(tablename)) {
				throw new RuntimeErrorException(new Error(), MSG.EntityAnnotationIsNull);
			}
			String sql = "delete from " + tablename + " where rowguid=\'" + rowguid + "\'";
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}

	}

	public void delByGuid(String rowguid, String tablename) {
		String sql = "delete from " + tablename + " where rowguid=\'" + rowguid + "\'";
		jdbcTemplate.execute(sql);
	}

	public List<Params> findListParamsBySql(String sql) {
		logger.info("list_record:{}", sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		List<Params> list_r = new ArrayList<Params>();
		if (list != null) {
			for (Map<String, Object> map : list) {
				Params r = Params.getRecordByMap(map);
				list_r.add(r);
			}
		}
		return list_r;
	}

	/**
	 * **查询个体优化v1.1 ** 优化了java基本的数据类型默认赋值
	 */

	public <T> T findOneByCondition(String sqlcondition, Class<T> clazz) {
		String tablename = "";
		Table table = clazz.getAnnotation(Table.class);
		if (table != null && StrUtil.isNotBlank(table.name())) {
			tablename = table.name();
		}
		Field[] fields = clazz.getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		for (Field field : fields) {
			sb = sb.append(field.getName()).append(",");
		}
		String fields_str = sb.toString().substring(0, sb.toString().lastIndexOf(","));
		String sql = "select " + fields_str + " from " + tablename + " where 1=1" + sqlcondition + " limit 1";
		logger.info("findOneByCondition===>{}", sql);
		try {
			Map<String, Object> map = jdbcTemplate.queryForMap(sql);
			if (map == null) {
				return null;
			}
			Class<?> classType = Class.forName(clazz.getName());
			Object obj = classType.newInstance();
			for (Field field : fields) {
				String methodname = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
				// PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
				PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, field.getName());
				Method setMethod = pd.getWriteMethod();// 获得set方法
				String fieldtypename = pd.getPropertyType().getCanonicalName();
				if (ConstantUtil.basicmap.containsKey(fieldtypename)) {
					if (StrUtil.isObjBlank(map.get(methodname))) {
						// 默认的空值
						setMethod.invoke(obj, ConstantUtil.basicmap.get(fieldtypename));
					} else {
						String methodName = "parse" + fieldtypename.substring(0, 1).toUpperCase()
								+ fieldtypename.substring(1);
						Class<?> claz = (Class<?>) ConstantUtil.parsemap.get(fieldtypename);
						Method method = claz.getMethod(methodName, String.class);
						Object value = method.invoke(null, map.get(methodname) + "");
						setMethod.invoke(obj, value);
					}
				} else {
					setMethod.invoke(obj, map.get(methodname));
				}
			}
			return (T) obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}

	public <T> T queryEntityById(Class<T> clazz, String sqlid, Params record) {
		try {
			if (StrUtil.isStrBlank(sqlid)) {
				throw new RuntimeErrorException(new Error(), "sqlid不能为空");
			}
			String sql = SqlHelper.getSqlBySqlid(sqlid);
			if (StrUtil.isStrBlank(sql)) {
				throw new RuntimeErrorException(new Error(), "不存在的sqlid:" + sqlid);
			}
			if (!StrUtil.isObjBlank(record)) {
				sql = BuildSqlBySqlId(sqlid, record);
			}
			if (sql.contains("*")) {
				Field[] fields = clazz.getDeclaredFields();
				StringBuffer sb = new StringBuffer();
				for (Field field : fields) {
					sb = sb.append(field.getName()).append(",");
				}
				String fields_str = sb.toString().substring(0, sb.toString().lastIndexOf(","));
				sql = sql.replace("*", fields_str);
			}
			logger.debug("queryEntityById===>{}", sql);
			Map<String, Object> map = jdbcTemplate.queryForMap(sql);
			if (map == null) {
				return null;
			}
			Field[] fields = clazz.getDeclaredFields();
			Class<?> classType = Class.forName(clazz.getName());
			Object obj = classType.newInstance();
			for (Field field : fields) {
				PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, field.getName());
				Method setMethod = pd.getWriteMethod();// 获得set方法
				String fieldtypename = pd.getPropertyType().getCanonicalName();
				String fieldname = field.getName().toLowerCase();
				if (ConstantUtil.basicmap.containsKey(fieldtypename)) {
					if (StrUtil.isObjBlank(map.get(fieldname))) {
						// 默认的空值
						setMethod.invoke(obj, ConstantUtil.basicmap.get(fieldtypename));
					} else {
						String methodName = "parse" + fieldtypename.substring(0, 1).toUpperCase()
								+ fieldtypename.substring(1);
						Class<?> claz = (Class<?>) ConstantUtil.parsemap.get(fieldtypename);
						Method method = claz.getMethod(methodName, String.class);
						Object value = method.invoke(null, map.get(fieldname) + "");
						setMethod.invoke(obj, value);
					}
				} else {
					setMethod.invoke(obj, map.get(fieldname));
				}
			}
			return (T) obj;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}

	public <T> T queryEntityBySql(Class<T> clazz, String sql, Params params) {
		try {

			sql = BuildSqlBySqlId(null, params, sql);
			if (sql.contains("*")) {
				Field[] fields = clazz.getDeclaredFields();
				StringBuffer sb = new StringBuffer();
				for (Field field : fields) {
					sb = sb.append(field.getName()).append(",");
				}
				String fields_str = sb.toString().substring(0, sb.toString().lastIndexOf(","));
				sql = sql.replace("*", fields_str);
			}
			logger.debug("queryEntityBySql===>{}", sql);
			Map<String, Object> map = jdbcTemplate.queryForMap(sql);
			if (map == null) {
				return null;
			}
			Field[] fields = clazz.getDeclaredFields();
			Class<?> classType = Class.forName(clazz.getName());
			Object obj = classType.newInstance();
			for (Field field : fields) {
				PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, field.getName());
				Method setMethod = pd.getWriteMethod();// 获得set方法
				String fieldtypename = pd.getPropertyType().getCanonicalName();
				String fieldname = field.getName().toLowerCase();
				if (ConstantUtil.basicmap.containsKey(fieldtypename)) {
					if (StrUtil.isObjBlank(map.get(fieldname))) {
						// 默认的空值
						setMethod.invoke(obj, ConstantUtil.basicmap.get(fieldtypename));
					} else {
						String methodName = "parse" + fieldtypename.substring(0, 1).toUpperCase()+ fieldtypename.substring(1);
						Class<?> claz = (Class<?>) ConstantUtil.parsemap.get(fieldtypename);
						Method method = claz.getMethod(methodName, String.class);
						Object value = method.invoke(null, map.get(fieldname) + "");
						setMethod.invoke(obj, value);
					}
				} else {
			
					setMethod.invoke(obj, map.get(fieldname));
				}
			}
			return (T) obj;
		} catch (EmptyResultDataAccessException e2) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}

	/**
	 * count
	 * 
	 * @param <T>
	 * @param clazz
	 * @param sqlcondition
	 * @return
	 */

	public <T> long queryCountBySql(String sql) {
		try {
			Map map = jdbcTemplate.queryForMap(sql);
			Set<String> key = map.keySet();
			long count = 0;
			for (String k : key) {
				count = Long.parseLong(map.get(k) + "");
			}
			return count;
		}catch(EmptyResultDataAccessException e) {
			return 0;
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}

	/**
	 * count
	 * 
	 * @param <T>
	 * @param clazz
	 * @param sqlcondition
	 * @return
	 */

	public <T> long queryCountByById(String sqlid, Params params) {
		try {
			if (StrUtil.isStrBlank(sqlid)) {
				throw new RuntimeErrorException(new Error(), "sqlid不能为空");
			}
			String sql = SqlHelper.getSqlBySqlid(sqlid);
			if (StrUtil.isStrBlank(sql)) {
				throw new RuntimeErrorException(new Error(), "不存在的sqlid:" + sqlid);
			}
			sql = BuildSqlBySqlId(sqlid, params);
			logger.debug("queryCountByById=>{}", sql);
			Map map = jdbcTemplate.queryForMap(sql);
			Set<String> key = map.keySet();
			for (String k : key) {
				long count = Long.parseLong(map.get(k) + "");
				return count;
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}

	public <T> long queryCountByById(String sqlid) {
		try {
			if (StrUtil.isStrBlank(sqlid)) {
				throw new RuntimeErrorException(new Error(), "sqlid不能为空");
			}
			String sql = BuildSqlBySqlId(sqlid, null);
			Map map = jdbcTemplate.queryForMap(sql);
			Set<String> key = map.keySet();
			for (String k : key) {
				long count = Long.parseLong(map.get(k) + "");
				return count;
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}

	/**
	 * pageList 查询List 都使用id
	 * 
	 * @param sqlid
	 * @param record
	 * @return
	 */

	public List<Params> findPageListParamsById(String sqlid, Params record) {
		String pageSize = record.getString("pageSize");
		String pageNum = record.getString("pageNum");
		if (StrUtil.isStrBlank(pageSize)) {
			throw new RuntimeErrorException(new Error(), "缺少pageSize参数");
		}
		if (StrUtil.isStrBlank(pageNum)) {
			throw new RuntimeErrorException(new Error(), "缺少pageNum参数");
		}
		int pagesize = Integer.parseInt(pageSize);
		int pagenum = Integer.parseInt(pageNum);
		String SqlCondition = " limit " + pagesize * pagenum + "," + pagesize;

		if (StrUtil.isStrBlank(sqlid)) {
			throw new RuntimeErrorException(new Error(), "sqlid不能为空");
		}
		String sql = SqlHelper.getSqlBySqlid(sqlid);
		if (StrUtil.isStrBlank(sql)) {
			throw new RuntimeErrorException(new Error(), "不存在的sqlid:" + sqlid);
		}

		Set<String> keyset = record.getFieldMap().keySet();
		for (String key : keyset) {
			sql = sql.replaceAll("{" + key + "}", "\'" + record.get(key) + "\'");
		}
		sql = sql + SqlCondition;

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null && list.size() > 0) {
			List<Params> list_r = new ArrayList<Params>();
			if (list != null) {
				for (Map<String, Object> map : list) {
					Params r = Params.getRecordByMap(map);
					list_r.add(r);
				}
			}
			return list_r;
		} else {
			return new ArrayList<Params>();
		}

	}

	/**
	 * 分页查询 V1.2
	 * 
	 * @param sqlid
	 * @param params
	 * @return
	 */

	public List<Map<String, Object>> queryPageListMapBySql(String sql, Params params) {
		sql = BuildSqlBySqlId(null, params, sql);
		logger.info("queryPageListMapBySql=>{}", sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null) {
			return list;
		} else {
			return new ArrayList<Map<String, Object>>();
		}

	}

	public List<Map<String, Object>> queryPageListMapById(String sqlid, Params params) {

		if (StrUtil.isStrBlank(sqlid)) {
			throw new RuntimeErrorException(new Error(), "sqlid不能为空");
		}
		String sql = SqlHelper.getSqlBySqlid(sqlid);
		if (StrUtil.isStrBlank(sql)) {
			throw new RuntimeErrorException(new Error(), "不存在的sqlid:" + sqlid);
		}
		sql = BuildSqlBySqlId(null, params, sql);
		logger.info("queryPageListMapById=>{}", sql);
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		if (list != null) {
			return list;
		} else {
			return new ArrayList<Map<String, Object>>();
		}

	}

	/**
	 * V1.1 批量更新 ** date 2021-03-15
	 */

	public <T> void batchAddObjectList(List<T> list) {
		logger.debug("*************开始执行批量增加**************");
		if (list == null || list.size() == 0) {
			throw new RuntimeErrorException(new Error(), MSG.ListIsEmpty);
		}
		T obj_1 = list.get(0);
		String sql = BasicServiceUtil.generateInsertSql(obj_1.getClass());
		try {
			BatchPreparedStatementSetter setter = new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					T obj = list.get(i);
					Field[] fields = obj.getClass().getDeclaredFields();
					for (int j = 0; j < fields.length; j++) {
						// 获取方法
						PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(obj.getClass(), fields[j].getName());
						Method getMethod = pd.getReadMethod();
						try {
							ps.setObject(j + 1, getMethod.invoke(obj));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}

				@Override
				public int getBatchSize() {
					return list.size();
				}
			};
			jdbcTemplate.batchUpdate(sql, setter);
			logger.debug("****************结束调用批量更新****************");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}

	}

	/**
	 * 执行sql 语句
	 * 
	 * @param sql
	 */

	public void execteSql(String sql) {
		try {
			logger.debug("执行sql:{}", sql);
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}

	public int execteSqlById(String sqlid, Params params) {
		try {
			if (StrUtil.isStrBlank(sqlid)) {
				throw new RuntimeErrorException(new Error(), "sqlid不能为空");
			}
			String sql = BuildSqlBySqlId(sqlid, params);

			logger.info("execteSqlById====>{}", sql);
			jdbcTemplate.execute(sql);
			return 2;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}
	
	public void execteSql(String sql, Params params) {
		try {

			sql = BuildSqlBySqlId(null,params,sql);
			logger.info("execteSql====>{}", sql);
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}

	public void execteSqlById(String sqlid) {
		try {
			if (StrUtil.isStrBlank(sqlid)) {
				throw new RuntimeErrorException(new Error(), "sqlid不能为空");
			}
			String sql = BuildSqlBySqlId(sqlid, null);
			logger.debug("execteSqlById====>{}", sql);
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}

	/**
	 **** 查询list
	 * 
	 * @param sqlid
	 * @param record
	 * @return
	 */

	public List<Map<String, Object>> queryListForMapById(String sqlid, Params params) {
		if (StrUtil.isStrBlank(sqlid)) {
			throw new RuntimeErrorException(new Error(), "sqlid不能为空");
		}
		String sql = BuildSqlBySqlId(sqlid, params);
		logger.debug("queryListForMapById=>{}", sql);
		return jdbcTemplate.queryForList(sql);
	}

	public List<Map<String, Object>> queryListForMapById(String sqlid) {
		if (StrUtil.isStrBlank(sqlid)) {
			throw new RuntimeErrorException(new Error(), "sqlid不能为空");
		}
		String sql = BuildSqlBySqlId(sqlid, null);
		logger.debug("queryListForMapById=>{}", sql);
		return jdbcTemplate.queryForList(sql);
	}

	public List<Map<String, Object>> queryListForMapBySql(String sql, Params params) {
		if (StrUtil.isStrBlank(sql)) {
			throw new RuntimeErrorException(new Error(), "sql:不能为空");
		}
		sql = BuildSqlBySqlId(null, params, sql);
		logger.debug("queryListForMapBySql===>{}", sql);
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 查询map
	 * 
	 * @param sqlid
	 * @param params
	 * @return
	 */
	public Map<String, Object> queryMapById(String sqlid, Params params) {
		try {
			if (StrUtil.isStrBlank(sqlid)) {
				throw new RuntimeErrorException(new Error(), "sqlid不能为空");
			}
			String sql = SqlHelper.getSqlBySqlid(sqlid);
			if (StrUtil.isStrBlank(sql)) {
				throw new RuntimeErrorException(new Error(), "不存在的sqlid:" + sqlid);
			}
			if (!StrUtil.isObjBlank(params)) {
				sql = BuildSqlBySqlId(sqlid, params);
			}
			logger.debug("queryMapById=>:{}", sql);
			Map<String, Object> map = jdbcTemplate.queryForMap(sql);
			return map;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	/**
	 * 查询map
	 * 
	 * @param sqlid
	 * @param params
	 * @return
	 */
	public Map<String, Object> queryMapById(String sqlid) {
		try {
			if (StrUtil.isStrBlank(sqlid)) {
				throw new RuntimeErrorException(new Error(), "sqlid不能为空");
			}
			String sql = SqlHelper.getSqlBySqlid(sqlid);
			if (StrUtil.isStrBlank(sql)) {
				throw new RuntimeErrorException(new Error(), "不存在的sqlid:" + sqlid);
			}

			sql = BuildSqlBySqlId(sqlid, null);

			logger.debug("queryMapById=>:{}", sql);
			Map<String, Object> map = jdbcTemplate.queryForMap(sql);
			return map;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	public Map<String, Object> queryMapBySql(String sql, Params params) {
		try {
			sql = BuildSqlBySqlId(null, params, sql);
			logger.info("queryMapBySql=>{}", sql);
			Map<String, Object> map = jdbcTemplate.queryForMap(sql);
			return map;
		} catch (Exception e) {
			return null;
		}
	}

	public Map<String, Object> queryMapBySql(String sql) {
		try {
			logger.debug("queryMapBySql=>{}", sql);
			Map<String, Object> map = jdbcTemplate.queryForMap(sql);
			return map;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据sqlid 查询相关的List 集合
	 * 
	 * @param <T>
	 * @param clazz
	 * @param sqlid
	 * @param params
	 * @return
	 */

	public <T> List<T> queryListForEntityBySqlId(Class<?> clazz, String sqlid, Params params) {
		try {
			if (StrUtil.isStrBlank(sqlid)) {
				throw new RuntimeErrorException(new Error(), "sqlid不能为空");
			}
			String sql = SqlHelper.getSqlBySqlid(sqlid);
			if (StrUtil.isStrBlank(sql)) {
				throw new RuntimeErrorException(new Error(), "不存在的sqlid:" + sqlid);
			}
			if (!StrUtil.isObjBlank(params)) {
				sql = BuildSqlBySqlId(sqlid, params);
			}
			logger.info("queryListForEntityBySqlId=>{}", sql);
			return queryListForEntityBySql(clazz, sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}

	public <T> List<T> queryListForEntityBySqlId(Class<?> clazz, String sqlid) {
		try {
			if (StrUtil.isStrBlank(sqlid)) {
				throw new RuntimeErrorException(new Error(), "sqlid不能为空");
			}
			String sql = SqlHelper.getSqlBySqlid(sqlid);
			if (StrUtil.isStrBlank(sql)) {
				throw new RuntimeErrorException(new Error(), "不存在的sqlid:" + sqlid);
			}
			logger.info("queryListForEntityBySqlId=>{}", sql);
			return queryListForEntityBySql(clazz, sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}

	private static String BuildSqlBySqlId(String sqlid, Params params, String... sqls) {
		String sql = "";
		if (StrUtil.isNotBlank(sqlid)) {
			sql = SqlHelper.getSqlBySqlid(sqlid);
		} else {
			sql = sqls[0];
		}
		if (params == null) {
			return sql;
		}
		Set<String> set = params.getFieldMap().keySet();

		logger.debug("当前正在执行：{}", sql);
		// 处理if标签
		sql = processIfTags(sql, params);
		
		// 替换参数占位符
		for (String key : set) {
			// 支持自定义的{key}格式
			sql = sql.replaceAll("\\{" + key + "\\}", "\'" + params.getString(key) + "\'");
			// 支持MyBatis的#{key}格式
			sql = sql.replaceAll("#\\{" + key + "}", "\'" + params.getString(key) + "\'");
			// 支持MyBatis的${key}格式
			sql = sql.replaceAll("\\$\\{" + key + "}", "\'" + params.getString(key) + "\'");
		}

		if (StrUtil.isNotBlank(sql)) {
			String sqlit_regex = " and | or |},| where ";
			String[] strs = sql.split(sqlit_regex);
			for (String substr : strs) {

				String regex_like = "\\$like\\(.*.\\,[a-z]+\\)".trim();
				Pattern r = Pattern.compile(regex_like);
				Matcher m = r.matcher(substr);
				if (m.find()) {
					String group = m.group();
					String field = group.substring(6, group.indexOf(","));
					if (StrUtil.isStrBlank(params.getString(field))) {
						sql = sql.replace(group, "1=1");
					} else {
						sql = sql.replace(group, field + " like \'%" + params.getString(field) + "%\'");
					}

				}

				String regex_like_suffix = "\\$like_suffix\\(.*.\\,[a-z]+\\)".trim();
				r = Pattern.compile(regex_like_suffix);
				m = r.matcher(substr);
				if (m.find()) {
					String group = m.group();
					String field = group.substring(13, group.indexOf(","));
					if (StrUtil.isStrBlank(params.getString(field))) {
						sql = sql.replace(group, "1=1");
					} else {
						sql = sql.replace(group, field + " like \'" + params.getString(field) + "%\'");
					}
				}
				
				
		

				String regex_equal2 = "\\$equal\\(.*.\\,.*.\\)".trim();
				r = Pattern.compile(regex_equal2);
				m = r.matcher(substr);
				if (m.find()) {
					String group = m.group();
					String field = group.substring(7, group.lastIndexOf(","));
					String cloname = group.substring(group.lastIndexOf(",") + 1, group.lastIndexOf(")"));
					if (StrUtil.isStrBlank(params.getString(cloname))) {
						sql = sql.replace(group, "1=1");
					} else {
						sql = sql.replace(group, field + "=\'" + params.getString(cloname) + "\'");
					}
				}

				String regex_in = "\\$in\\(.*.\\,.*.\\)".trim();
				r = Pattern.compile(regex_in);
				m = r.matcher(substr);
				if (m.find()) {
					String group = m.group();
					String field = group.substring(4, group.lastIndexOf(","));
					String cloname = group.substring(group.lastIndexOf(",") + 1, group.lastIndexOf(")"));
					if (StrUtil.isStrBlank(params.getString(cloname))) {
						sql = sql.replace(group, "1=1");
					} else {
						sql = sql.replace(group, field + " in (" + params.getString(cloname) + ")");
					}
				}
				
				
				
				String regex_bigger = "\\$bigger\\(.*.\\,[a-z0-9]+\\)".trim();
				r = Pattern.compile(regex_bigger);
				m = r.matcher(substr);
				if (m.find()) {
					String group = m.group();
					String field = group.substring(8, group.lastIndexOf(","));
					String cloname = group.substring(group.lastIndexOf(",") + 1, group.lastIndexOf(")"));
					if (StrUtil.isStrBlank(params.getString(cloname))) {
						sql = sql.replace(group, "1=1");
					} else {
						sql = sql.replace(group, field + ">\'" + params.getString(cloname) + "\'");
					}
				}

				String regex_unequal = "\\$unequal\\(.*.\\,[a-z]+\\)".trim();
				r = Pattern.compile(regex_unequal);
				m = r.matcher(substr);
				if (m.find()) {
					String group = m.group();
					String field = group.substring(9, group.lastIndexOf(","));
					String cloname = group.substring(group.lastIndexOf(",") + 1, group.lastIndexOf(")"));
					if (StrUtil.isStrBlank(params.getString(cloname))) {
						sql = sql.replace(group, "1=1");
					} else {
						sql = sql.replace(group, field + "!=\'" + params.getString(cloname) + "\'");
					}
				}
				String regex_between = "\\$between\\(.*.\\,[a-z0-9]+\\){1}".trim();
				Pattern rb = Pattern.compile(regex_between);
				Matcher mb = rb.matcher(sql);
				if (mb.find()) {
					String group = mb.group();
					String cloname1 = group.substring(9, group.indexOf(","));
					String cloname2 = group.substring(group.indexOf(",") + 1, group.indexOf(")"));
					String group_n = group.replace("$between", "between");
					group_n = group_n.replace(cloname1, "\'" + params.getString(cloname1) + "\'");
					group_n = group_n.replace(cloname2, "\'" + params.getString(cloname2) + "\'");
					group_n = group_n.replaceFirst(",", " and ");
					group_n = group_n.replaceFirst("\\(", " ");
					group_n = group_n.replaceFirst("\\)", " ");
					sql = sql.replace(group, group_n);
				}

			}
			if (!sql.contains("count(1)") || sql.indexOf("count(1)") > 10) {
				if (StrUtil.isNotBlank(params.getString("pagesize")) && StrUtil.isNotBlank(params.getString("pagesize"))) {
					int pagesize = Integer.parseInt(params.getString("pagesize"));
					int pagenum = Integer.parseInt(params.getString("pagenum")) - 1;
					String page_prex = " limit " + pagenum * pagesize + "," + pagesize;
					sql = sql + page_prex;
				}
			}
		} else {
			throw new RuntimeErrorException(new Error(), "不存在的sqlid：" + sqlid + " || sql 语句不能为空");
		}
		return sql;
	}

	/**
	 * 处理SQL语句中的if标签
	 * @param sql 包含if标签的SQL语句
	 * @param params 参数
	 * @return 处理后的SQL语句
	 */
	private static String processIfTags(String sql, Params params) {
		if (params == null) {
			return sql;
		}
		
		// 正则表达式匹配if标签
		Pattern ifPattern = Pattern.compile("<if test=\"([^\"]+)\">([\\s\\S]*?)</if>", Pattern.CASE_INSENSITIVE);
		Matcher ifMatcher = ifPattern.matcher(sql);
		
		StringBuffer result = new StringBuffer();
		while (ifMatcher.find()) {
			String testExpression = ifMatcher.group(1);
			String content = ifMatcher.group(2);
			
			// 解析并评估test表达式
			boolean condition = evaluateCondition(testExpression, params);
			
			// 根据条件决定是否保留内容
			if (condition) {
				ifMatcher.appendReplacement(result, content);
			} else {
				ifMatcher.appendReplacement(result, "");
			}
		}
		ifMatcher.appendTail(result);
		
		return result.toString();
	}

	/**
	 * 评估条件表达式
	 * @param expression 条件表达式
	 * @param params 参数
	 * @return 条件是否成立
	 */
	private static boolean evaluateCondition(String expression, Params params) {
		// 简单实现，支持常见的条件表达式
		// 1. 处理 != null 条件
		if (expression.contains("!= null")) {
			String key = expression.trim().split("\s+")[0];
			return params.getFieldMap().containsKey(key);
		}
		
		// 2. 处理 == null 条件
		if (expression.contains("== null")) {
			String key = expression.trim().split("\s+")[0];
			return !params.getFieldMap().containsKey(key);
		}
		
		// 3. 处理 != '' 条件（非空字符串）
		if (expression.contains("!= ''")) {
			String key = expression.trim().split("\s+")[0];
			return  params.getFieldMap().containsKey(key) && StrUtil.isNotBlank(params.getString(key));
		}
		
		// 4. 处理 == '' 条件（空字符串）
		if (expression.contains("== ''")) {
			String key = expression.trim().split("\s+")[0];
			return ! params.getFieldMap().containsKey(key) || StrUtil.isStrBlank(params.getString(key));
		}
		
		// 5. 处理组合条件（使用 && 连接）
		if (expression.contains("&&")) {
			String[] conditions = expression.split("&&");
			for (String condition : conditions) {
				if (!evaluateCondition(condition.trim(), params)) {
					return false;
				}
			}
			return true;
		}
		
		// 6. 处理组合条件（使用 || 连接）
		if (expression.contains("||")) {
			String[] conditions = expression.split("||");
			for (String condition : conditions) {
				if (evaluateCondition(condition.trim(), params)) {
					return true;
				}
			}
			return false;
		}
		
		// 默认返回true
		return true;
	}

	public <T> List<T> queryListForEntityBySql(Class<?> clazz, String sql) {
		try {
			if (sql.contains("*")) {
				Field[] fields = clazz.getDeclaredFields();
				StringBuffer sb = new StringBuffer();
				for (Field field : fields) {
					sb = sb.append(field.getName()).append(",");
				}
				String fields_str = sb.toString().substring(0, sb.toString().lastIndexOf(","));
				sql = sql.replace("*", fields_str);
			}
			logger.debug("queryListForEntityBySql=>{}", sql);
			List<Object> list_t = new ArrayList<Object>();
			List<Map<String, Object>> list_map = jdbcTemplate.queryForList(sql);
			if (list_map == null || list_map.size() == 0) {
				return new ArrayList<T>();
			}
			Field[] fields = clazz.getDeclaredFields();
			for (Map<String, Object> map : list_map) {
				Class<?> classType = Class.forName(clazz.getName());
				Object obj = classType.newInstance();
				for (Field field : fields) {
					PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, field.getName());
					Method setMethod = pd.getWriteMethod();// 获得set方法
					String fieldtypename = pd.getPropertyType().getCanonicalName();
					String fieldname = field.getName().toLowerCase();
					if (ConstantUtil.basicmap.containsKey(fieldtypename)) {
						if (StrUtil.isObjBlank(map.get(fieldname))) {
							setMethod.invoke(obj, ConstantUtil.basicmap.get(fieldtypename));
						} else {
							String methodName = "parse" + fieldtypename.substring(0, 1).toUpperCase()+ fieldtypename.substring(1);
							Class<?> claz = (Class<?>) ConstantUtil.parsemap.get(fieldtypename);
							Method method = claz.getMethod(methodName, String.class);
							Object value = method.invoke(null, map.get(fieldname) + "");
							setMethod.invoke(obj, value);
						}
					} else {
						setMethod.invoke(obj, map.get(fieldname));
					}
				}
				list_t.add(obj);
			}
			return (List<T>) list_t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}
	
	
	public <T> List<T> queryListForEntityBySql(Class<?> clazz, String sql, Params params) {
		try {
			if (sql.contains("*")) {
				Field[] fields = clazz.getDeclaredFields();
				StringBuffer sb = new StringBuffer();
				for (Field field : fields) {
					sb = sb.append(field.getName()).append(",");
				}
				String fields_str = sb.toString().substring(0, sb.toString().lastIndexOf(","));
				sql = sql.replace("*", fields_str);
			}
			logger.debug("queryListForEntityBySql=>{}", sql);
			
			sql = BuildSqlBySqlId(null, params, sql);
			List<Object> list_t = new ArrayList<Object>();
			List<Map<String, Object>> list_map = jdbcTemplate.queryForList(sql);
			if (list_map == null || list_map.size() == 0) {
				return new ArrayList<T>();
			}
			Field[] fields = clazz.getDeclaredFields();
			for (Map<String, Object> map : list_map) {
				Class<?> classType = Class.forName(clazz.getName());
				Object obj = classType.newInstance();
				for (Field field : fields) {
					PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, field.getName());
					Method setMethod = pd.getWriteMethod();// 获得set方法
					String fieldtypename = pd.getPropertyType().getCanonicalName();
					String fieldname = field.getName().toLowerCase();
					if (ConstantUtil.basicmap.containsKey(fieldtypename)) {
						if (StrUtil.isObjBlank(map.get(fieldname))) {
							setMethod.invoke(obj, ConstantUtil.basicmap.get(fieldtypename));
						} else {
							String methodName = "parse" + fieldtypename.substring(0, 1).toUpperCase()
									+ fieldtypename.substring(1);
							Class<?> claz = (Class<?>) ConstantUtil.parsemap.get(fieldtypename);
							Method method = claz.getMethod(methodName, String.class);
							Object value = method.invoke(null, map.get(fieldname) + "");
							setMethod.invoke(obj, value);
						}
					} else {
						setMethod.invoke(obj, map.get(fieldname));
					}
				}
				list_t.add(obj);
			}
			return (List<T>) list_t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}

	public <T> long queryCountBySql(String sql, Params params) {
		try {

			sql = BuildSqlBySqlId(null, params, sql);
			logger.info("queryCountBySql=>{}", sql);
			Map map = jdbcTemplate.queryForMap(sql);
			Set<String> key = map.keySet();
			for (String k : key) {
				long count = Long.parseLong(map.get(k) + "");
				return count;
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}

	public List<Map<String, Object>> queryListForMapBySql(String sql) {
		if (StrUtil.isStrBlank(sql)) {
			throw new RuntimeErrorException(new Error(), "sql:不能为空");
		}
		logger.debug("queryListForMapBySql===>{}", sql);
		return jdbcTemplate.queryForList(sql);
	}


	
	
	public <T> List<T> queryPageListEntityBySql(Class<?> clazz, String sql, Params params) {
		try {
			if (sql.contains("*")) {
				Field[] fields = clazz.getDeclaredFields();
				StringBuffer sb = new StringBuffer();
				for (Field field : fields) {
					sb = sb.append(field.getName()).append(",");
				}
				String fields_str = sb.toString().substring(0, sb.toString().lastIndexOf(","));
				sql = sql.replace("*", fields_str);
			}
			logger.debug("queryListForEntityBySql=>{}", sql);
			
			sql = BuildSqlBySqlId(null, params, sql);
			List<Object> list_t = new ArrayList<Object>();
			List<Map<String, Object>> list_map = jdbcTemplate.queryForList(sql);
			if (list_map == null || list_map.size() == 0) {
				return new ArrayList<T>();
			}
			Field[] fields = clazz.getDeclaredFields();
			for (Map<String, Object> map : list_map) {
				Class<?> classType = Class.forName(clazz.getName());
				Object obj = classType.newInstance();
				for (Field field : fields) {
					PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, field.getName());
					Method setMethod = pd.getWriteMethod();// 获得set方法
					String fieldtypename = pd.getPropertyType().getCanonicalName();
					String fieldname = field.getName().toLowerCase();
					if (ConstantUtil.basicmap.containsKey(fieldtypename)) {
						if (StrUtil.isObjBlank(map.get(fieldname))) {
							setMethod.invoke(obj, ConstantUtil.basicmap.get(fieldtypename));
						} else {
							String methodName = "parse" + fieldtypename.substring(0, 1).toUpperCase()
									+ fieldtypename.substring(1);
							Class<?> claz = (Class<?>) ConstantUtil.parsemap.get(fieldtypename);
							Method method = claz.getMethod(methodName, String.class);
							Object value = method.invoke(null, map.get(fieldname) + "");
							setMethod.invoke(obj, value);
						}
					} else {
						setMethod.invoke(obj, map.get(fieldname));
					}
				}
				list_t.add(obj);
			}
			return (List<T>) list_t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeErrorException(new Error(), e.getMessage());
		}
	}



}
