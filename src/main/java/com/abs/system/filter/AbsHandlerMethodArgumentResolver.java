package com.abs.system.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.abs.system.util.Params;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

public class AbsHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
	
	
	
	private Logger logger =LoggerFactory.getLogger(AbsHandlerMethodArgumentResolver.class);

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(ToToken.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		logger.info("当前正在执行的是方法解析参数");
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String token = request.getHeader("Authorization");
		ServletContext en = request.getServletContext();
		Params params = new Params();
		params.put("token", token);		
		return params;
	}

}
