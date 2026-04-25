package com.abs.system.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateConverter {
	
	
    public static final ZoneId CHINA_ZONE = ZoneId.of("Asia/Shanghai");

	/**
     * 将Date转换为LocalDateTime（使用系统默认时区）
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant()
                .atZone(CHINA_ZONE)
                .toLocalDateTime();
    }
    
    /**
     * 将Date转换为LocalDateTime（指定时区）
     */
    public static LocalDateTime toLocalDateTime(Date date, ZoneId zoneId) {
        if (date == null) {
            return null;
        }
        return date.toInstant()
                .atZone(CHINA_ZONE)
                .toLocalDateTime();
    }
    
    /**
     * 将LocalDateTime转换回Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime
                .atZone(CHINA_ZONE)
                .toInstant());
    }
    
    
    public static long getZoneMillisTime(LocalDateTime localDateTime) {
          return localDateTime.atZone(CHINA_ZONE)
                  .toInstant()
                  .toEpochMilli();
    }
    
    
    
    
    public static void main(String[] args) {
        Date date = new Date();
        
        // 转换为LocalDateTime
        LocalDateTime localDateTime = toLocalDateTime(date);
        System.out.println("Date: " + date);
        System.out.println("LocalDateTime: " + localDateTime);
        
        // 转换回Date
        Date convertedBack = toDate(localDateTime);
        System.out.println("Converted back to Date: " + convertedBack);
    }
}



