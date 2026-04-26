package com.abs.system;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.abs.system.util.SqlHelper;

@Component
@Order(1)
public class SqlApplicationRunnerInit implements ApplicationRunner {

	private Logger logger = LoggerFactory.getLogger(SqlApplicationRunnerInit.class);

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			// 使用 Spring 的资源加载器来支持 JAR 包内的资源
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources("classpath:db/*_sql.xml");
			
			logger.info("找到 {} 个 SQL 配置文件", resources.length);
			
			for (Resource resource : resources) {
				try {
					SAXReader saxReader = new SAXReader();
					InputStream input = resource.getInputStream();
					Document read = saxReader.read(input);
					Element rootElement = read.getRootElement();
					Element sqlElement = rootElement.element("sql");
					String mid = sqlElement.attribute("ID").getValue().trim();
					List<Element> list_sql = sqlElement.elements();
					
					logger.info("加载 SQL 文件: {}，包含 {} 条 SQL 语句", resource.getFilename(), list_sql.size());
					
					for (Element sql : list_sql) {
						String subid = sql.attribute("ID").getValue().trim();
						String sqltext = sql.getStringValue().trim();
						SqlHelper.addSqlToCache(mid + "." + subid, sqltext);
						logger.debug("加载sql：{}.{}", mid, subid);
					}
					
					input.close();
				} catch (Exception e) {
					logger.error("加载 SQL 文件失败: {}", resource.getFilename(), e);
				}
			}
		} catch (Exception e) {
			logger.error("SQL 初始化失败", e);
		}
		
		logger.info("*****************sql语句加载结束*****************");
	}
}
