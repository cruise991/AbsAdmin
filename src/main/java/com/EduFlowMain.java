package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "com.abs.system", "com.abs.article", "com.abs.openapi" })
@ServletComponentScan
@SpringBootApplication
public class EduFlowMain extends SpringBootServletInitializer {
	// 继承SpringBootServletInitializer可以使用外部tomcat

	public static void main(String[] args) {
		SpringApplication.run(EduFlowMain.class, args);
	}

	// 实现configure可以打为war包启动（需要有tomcat）
	/*
	 * @Override protected SpringApplicationBuilder
	 * configure(SpringApplicationBuilder application) { return
	 * application.sources(AbsPlusMain.class); }
	 */

}
