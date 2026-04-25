package com.abs.system.util;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;

@Component
public class AbsDbHelper {

	public static Map<String, AbsDbService> map_service = new HashMap<>();

	public static Map<String, DataSource> map_datasource = new HashMap<>();

	// 静态字段用于存储配置值
	private static String url;
	private static String username;
	private static String password;
	private static String driverClassName;

	// 实例字段用于接收@Value注入
	@Value("${spring.datasource.url}")
	private String injectedUrl;

	@Value("${spring.datasource.username}")
	private String injectedUsername;

	@Value("${spring.datasource.password}")
	private String injectedPassword;

	@Value("${spring.datasource.driver-class-name}")
	private String injectedDriverClassName;

	// 在Bean初始化后将实例字段的值设置到静态字段
	@javax.annotation.PostConstruct
	public void init() {
		AbsDbHelper.url = this.injectedUrl;
		AbsDbHelper.username = this.injectedUsername;
		AbsDbHelper.password = this.injectedPassword;
		AbsDbHelper.driverClassName = this.injectedDriverClassName;
	}

	public static DataSource getDataSource(String... args) {
		if (StrUtil.isObjBlank(args) || args.length == 0) {
			if (map_datasource.get("defaults") != null) {
				return map_datasource.get("defaults");
			}
			// 使用Spring配置的数据库连接信息
			DruidDataSource dataSource = new DruidDataSource();
			dataSource.setDriverClassName(driverClassName);
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			map_datasource.put("defaults", dataSource);
			return dataSource;
		} else {
			String Ids = args[0];
			if (map_datasource.get(Ids) != null) {
				return map_datasource.get(Ids);
			}
			// 对于指定ID的数据源，暂时使用默认配置
			// 可以根据需要扩展，从Spring配置中获取不同的数据源配置
			DruidDataSource dataSource = new DruidDataSource();
			dataSource.setDriverClassName(driverClassName);
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			map_datasource.put(Ids, dataSource);
			return dataSource;
		}

	}

	public static AbsDbService getDbService(String... args) {
		DataSource source = getDataSource(args);
		AbsDbService dbService = new AbsDbService(source);
		return dbService;

	}

}
