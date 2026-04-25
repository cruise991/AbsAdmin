package com.abs.system.filter;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class AbsApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
	
	
	private Logger logger=LoggerFactory.getLogger(AbsApplicationListener.class);

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub
		Map<String, Object> map = event.getApplicationContext().getBeansWithAnnotation(NoNeedLogin.class);
		if (map != null) {
			for (String key : map.keySet()) {
				
			}
		}
	}

}
