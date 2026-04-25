package com.abs.system.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class AbsApplicationClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    private static Logger logger = LoggerFactory.getLogger(AbsApplicationClosedEventListener.class);

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
       
    	
    	logger.info("关闭之前执行");
    	
    	
    	
    	
    }
}
