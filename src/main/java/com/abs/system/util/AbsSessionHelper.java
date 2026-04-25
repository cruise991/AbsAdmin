package com.abs.system.util;

import java.time.Duration;
import java.util.Random;
import java.util.function.Supplier;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.ExpiryPolicy;

/**
 * 签章保存token到缓存中
 * 
 * @author lxn
 *
 */
public class AbsSessionHelper {

    // Ehcache 3.x 缓存管理器实例
    private static CacheManager cacheManager;
    private static Cache<String, String> tokenCache;
    
    static {
        initCacheManager();
    }
    
    /**
     * 初始化缓存管理器（单例模式）
     */
    private static synchronized void initCacheManager() {
        if (cacheManager == null) {
            // 创建缓存配置
        	CacheConfiguration<String, String> cacheConfig = 
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
            
            // 创建缓存管理器
            cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("usertoken", cacheConfig)
                .build(true); // true 表示立即初始化
            
            // 获取缓存实例
            tokenCache = cacheManager.getCache("usertoken", 
                String.class, String.class);
        }
    }

    /**
     * 添加键值对到缓存
     */
    public static void addKeyToCache(String key, String keyvalue) {
        tokenCache.put(key, keyvalue);
    }

    /**
     * 从缓存获取token
     */
    public static String getTokenFromCache(String key) {
        return tokenCache.get(key);
    }

    /**
     * 从缓存移除token
     */
    public static void removeAccessTokenToCache(String key) {
        tokenCache.remove(key);
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
     * 获取当前用户GUID
     */
    public static String getCurrentUserGuid(String token) {
        String json = getTokenFromCache(token);
        if (StrUtil.isNotBlank(json)) {
            Params params = Params.paseJson(json);
            return params.getString("userguid");
        }
        return null;
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUserName(String token) {
        String json = getTokenFromCache(token);
        if (StrUtil.isNotBlank(json)) {
            Params params = Params.paseJson(json);
            return params.getString("username");
        }
        return null;
    }
    
    /**
     * 获取当前组织GUID
     */
    public static String getCurrentOuGuid(String token) {
        String json = getTokenFromCache(token);
        if (StrUtil.isNotBlank(json)) {
            Params params = Params.paseJson(json);
            return params.getString("ouguid");
        }
        return null;
    }
    
    /**
     * 获取当前组织名
     */
    public static String getCurrentOuName(String token) {
        String json = getTokenFromCache(token);
        if (StrUtil.isNotBlank(json)) {
            Params params = Params.paseJson(json);
            return params.getString("ouname");
        }
        return null;
    }
    
    /**
     * 获取当前用户token（原始JSON）
     */
    public static String getCurrentUserToken(String token) {
        return getTokenFromCache(token);
    }

    /**
     * 生成随机字符串
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 设置当前用户信息
     */
    public static void setCurrentUserInfo(String token, String user_json) {
        addKeyToCache(token, user_json);
    }
    
    /**
     * 可选的：从XML配置文件初始化（如果需要在外部配置）
     */
    public static void initFromXmlConfig(String xmlConfigPath) {
        if (cacheManager != null) {
            cacheManager.close();
        }
        
        cacheManager = CacheManagerBuilder.newCacheManager(
            new org.ehcache.xml.XmlConfiguration(
                AbsSessionHelper.class.getResource(xmlConfigPath)
            )
        );
        cacheManager.init();
        
        tokenCache = cacheManager.getCache("usertoken", 
            String.class, String.class);
    }
}