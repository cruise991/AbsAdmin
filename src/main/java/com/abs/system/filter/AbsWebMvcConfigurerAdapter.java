package com.abs.system.filter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // 改为 WebMvcConfigurer

@Configuration
public class AbsWebMvcConfigurerAdapter implements WebMvcConfigurer { // 改为实现接口
    
    // 移除 getValidator() 方法，让 Spring 自动配置
    // @Override
    // public org.springframework.validation.Validator getValidator() {
    //     return null;
    // }
    
    @Bean
    public AbsHandlerInterceptor getAbsHandlerInterceptor() {
        return new AbsHandlerInterceptor();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) { // 改为 public
        // 移除流式对话页面的重定向，统一使用 EduFlow Platform
        // registry.addRedirectViewController("/", "canvas/index.html");
        // registry.addRedirectViewController("/index", "canvas/index.html");
        // registry.addRedirectViewController("/index.html", "canvas/index.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { // 改为 public
        registry.addResourceHandler("/canvas/**").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/www/**").addResourceLocations("classpath:/www/");
        registry.addResourceHandler("/db/**").addResourceLocations("classpath:/db/");
        registry.addResourceHandler("/font/**").addResourceLocations("classpath:/font/");
        // registry.addResourceHandler("/jsp/**").addResourceLocations("classpath:/jsp/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) { // 改为 public
        argumentResolvers.add(new AbsHandlerMethodArgumentResolver());
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 是否允许请求带有验证信息
        config.setAllowCredentials(true);
        // 允许访问的客户端域名
        List<String> allowedOriginPatterns = new ArrayList<>();
        allowedOriginPatterns.add("*");
        config.setAllowedOriginPatterns(allowedOriginPatterns);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptor) { // 改为 public
        interceptor.addInterceptor(getAbsHandlerInterceptor()).addPathPatterns("/**");
    }
}