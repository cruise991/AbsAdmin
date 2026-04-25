package com.abs.system.filter;

import java.io.PrintWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.abs.system.api.IAbsLoginInfoService;
import com.abs.system.util.AbsSessionHelper;
import com.abs.system.util.BuildJsonOfObject;
import com.abs.system.util.MSG;
import com.abs.system.util.StrUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 拦截器
 * 
 * @author wdg
 *
 */

public class AbsHandlerInterceptor implements HandlerInterceptor {

	private Logger logger = LoggerFactory.getLogger(AbsHandlerInterceptor.class);

	long start = System.currentTimeMillis();

	@Autowired
	private IAbsLoginInfoService loginService;

	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object handler) throws Exception {
		
		
		
		if (handler instanceof HandlerMethod) {
			HandlerMethod h = (HandlerMethod) handler;
			String mname=h.getMethod().getName();
			if (h.hasMethodAnnotation(NoNeedLogin.class)) {
				return true;
			}else if(mname.contains("JSP")) {
				logger.info("jsp页面访问");
				return true;
			}else {
				String token = httpServletRequest.getHeader("Authorization");
				System.out.println("用户传入token"+token);
				if (StrUtil.isNotBlank(token)) {
					if (StrUtil.isNotBlank(AbsSessionHelper.getCurrentUserGuid(token))) {
						return true;
					} else {
						Map<String, Object> map = loginService.queryUserGuid(token);
						if (map != null) {
							String userinfo = map.get("userinfo").toString();
							AbsSessionHelper.setCurrentUserInfo(token, userinfo);
							return true;
						} else {
							httpServletResponse.setContentType("application/json");
							PrintWriter writer = httpServletResponse.getWriter();
							writer.write(BuildJsonOfObject.getJsonString("登录失效，请重新登录", MSG.NOLOGIN).toJSONString());
							return false;
						}
					}
				}else {
					httpServletResponse.setContentType("application/json");
					PrintWriter writer = httpServletResponse.getWriter();
					writer.write(BuildJsonOfObject.getJsonString(MSG.fail, MSG.NOLOGIN).toJSONString());
					return false;
				}
			}
		} else if (handler instanceof ResourceHttpRequestHandler) {
			return true;
		} else {
			return true;
		}

	}

	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object handler, ModelAndView modelAndView) throws Exception {
		System.out.println(start);

	}

	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {

	}
}
