package com.abs.system.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus配置类
 */
@Configuration
@MapperScan("com.abs.system.mapper") // 扫描Mapper接口所在包
public class MyBatisPlusConfig {

}