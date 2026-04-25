package com.abs.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.abs.system.util.SqlHelper;

@Component
@Order(1)
public class SqlApplicationRunnerInit implements ApplicationRunner {

	private Logger logger = LoggerFactory.getLogger(SqlApplicationRunnerInit.class);

	@Override
	public void run(ApplicationArguments args) throws Exception {
		String path=SqlApplicationRunnerInit.class.getResource("/").getPath() + "db/";
		File file = new File(path);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
			    SAXReader saxReader = new SAXReader();
				InputStream input = new FileInputStream(f);
				Document read = saxReader.read(input);
				Element rootElement = read.getRootElement();
				Element sqlElement = rootElement.element("sql");
				String mid = sqlElement.attribute("ID").getValue().trim();
				List<Element> list_sql = sqlElement.elements();
				for (Element sql : list_sql) {
                   String subid=sql.attribute("ID").getValue().trim();
                   String sqltext=sql.getStringValue().trim();
                   SqlHelper.addSqlToCache(mid+"."+subid, sqltext);
                   logger.info("加载sql："+mid+"."+subid+":{}",sqltext);
				}
			}
		}
		
		logger.info("*****************sql语句加载结束*****************");
	}
}
