/* 
 * Copyright(c)2010-2015 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */

package com.wudaosoft.commons.mvc.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wudaosoft.commons.mvc.exception.ServiceException;
import com.wudaosoft.commons.utils.SignatureUtil;

/**
 * 
 * 验证签名拦截器
 * 
 * @author Changsoul Wu
 * 
 */
public class SignHandlerInterceptor extends HandlerInterceptorAdapter {
	private static final String SIGN_KEY = "sign";
	private static final ServiceException INVALID_SIGN_EXCEPTION = new ServiceException(1000, "Access Denied");

	private String signKey;
	private String secretKey = "sign";
	private List<String> ignoreKey;

	public SignHandlerInterceptor() {
		super();
		ignoreKey = new ArrayList<String>();
		ignoreKey.add(SIGN_KEY);
		ignoreKey.add("format");
		ignoreKey.add("callback");
		signKey = SIGN_KEY;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
		
		ignoreKey.remove(SIGN_KEY);
		ignoreKey.add(signKey);
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!(handler instanceof HandlerMethod))
			return true;

		HandlerMethod handlerMethod = (HandlerMethod) handler;

		if (AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), RestController.class) != null
				|| AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ResponseBody.class) != null) {

			String sign = request.getParameter(signKey);

			List<String> params = new ArrayList<String>(30);

			for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {

				if (!ignoreKey.contains(entry.getKey()) && entry.getValue() != null && entry.getValue().length > 0)
					params.add(entry.getValue()[0]);
			}

			if (!SignatureUtil.validateSign(sign, secretKey, params))
				throw INVALID_SIGN_EXCEPTION;
		}

		return true;
	}

}
