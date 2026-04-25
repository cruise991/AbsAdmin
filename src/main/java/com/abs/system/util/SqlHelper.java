package com.abs.system.util;

import java.time.Duration;
import java.util.function.Supplier;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.ExpiryPolicy;

public class SqlHelper {

	// Ehcache 3.x 缓存管理器实例
	private static CacheManager cacheManager;
	private static Cache<String, String> sqlCache;

	static {
		initCacheManager();
	}

	/**
	 * 初始化缓存管理器
	 */
	private static synchronized void initCacheManager() {
		if (cacheManager == null) {
			// 创建SQL缓存配置
			CacheConfiguration<String, String> sqlCacheConfig = 
        		    CacheConfigurationBuilder.newCacheConfigurationBuilder(
        		        String.class, // Key 类型
        		        String.class, // Value 类型
        		        ResourcePoolsBuilder.heap(1000) // 堆内存大小（最多存 1000 个缓存项）
        		    )
        		    .withExpiry(new ExpiryPolicy<String, String>() {
        		        @Override
        		        public Duration getExpiryForCreation(String key, String value) {
        		            // 创建后 24 小时过期
        		            return Duration.ofHours(24);
        		        }

        		        @Override
        		        public Duration getExpiryForAccess(String key, Supplier<? extends String> value) {
        		            // 访问后不更新过期时间（保持原有策略，返回 null 即可）
        		            return null;
        		        }

        		        @Override
        		        public Duration getExpiryForUpdate(String key, Supplier<? extends String> oldValue, String newValue) {
        		            // 更新后重新计算 24 小时过期
        		        	   return Duration.ofHours(24);
        		        }
        		    })
        		    .build();

			// 创建缓存管理器并添加多个缓存
			cacheManager = CacheManagerBuilder.newCacheManagerBuilder().withCache("sqlcache", sqlCacheConfig)
					.build(true); // true表示立即初始化

			// 获取SQL缓存实例
			sqlCache = cacheManager.getCache("sqlcache", String.class, String.class);
		}
	}

	/**
	 * 添加SQL到缓存
	 * 
	 * @param sqlid SQL的唯一标识
	 * @param sql   SQL语句
	 */
	public static void addSqlToCache(String sqlid, String sql) {
		if (sqlid == null || sql == null) {
			return;
		}
		sqlCache.put(sqlid, sql);
	}

	/**
	 * 根据SQL ID获取SQL语句
	 * 
	 * @param sqlid SQL的唯一标识
	 * @return SQL语句，如果不存在则返回null
	 */
	public static String getSqlBySqlid(String sqlid) {
		if (sqlid == null) {
			return null;
		}
		return sqlCache.get(sqlid);
	}

	/**
	 * 从缓存中移除SQL
	 * 
	 * @param sqlid SQL的唯一标识
	 */
	public static void removeSqlFromCache(String sqlid) {
		if (sqlid != null) {
			sqlCache.remove(sqlid);
		}
	}

	/**
	 * 检查SQL是否在缓存中
	 * 
	 * @param sqlid SQL的唯一标识
	 * @return 是否存在
	 */
	public static boolean containsSql(String sqlid) {
		return sqlCache.containsKey(sqlid);
	}

	/**
	 * 清空所有SQL缓存
	 */
	public static void clearAllSqlCache() {
		sqlCache.clear();
	}

	/**
	 * 获取SQL缓存统计信息
	 * 
	 * @return 缓存条目数
	 */
	public static long getSqlCacheSize() {
		// 注意：Ehcache 3.x 没有直接的size()方法
		// 需要自己维护计数或使用其他方式
		return -1; // 表示不支持直接获取大小
	}

	/**
	 * 关闭缓存管理器（应用关闭时调用）
	 */
	public static void shutdown() {
		if (cacheManager != null) {
			cacheManager.close();
		}
	}

	/**
	 * 使用XML配置文件初始化缓存（可选）
	 * 
	 * @param xmlConfigPath XML配置文件路径
	 */
	public static void initFromXmlConfig(String xmlConfigPath) {
		if (cacheManager != null) {
			cacheManager.close();
		}

		try {
			cacheManager = CacheManagerBuilder
					.newCacheManager(new org.ehcache.xml.XmlConfiguration(SqlHelper.class.getResource(xmlConfigPath)));
			cacheManager.init();

			sqlCache = cacheManager.getCache("sqlcache", String.class, String.class);
		} catch (Exception e) {
			// 如果XML配置失败，回退到默认配置
			System.err.println("XML配置加载失败，使用默认配置: " + e.getMessage());
			initCacheManager();
		}
	}
}