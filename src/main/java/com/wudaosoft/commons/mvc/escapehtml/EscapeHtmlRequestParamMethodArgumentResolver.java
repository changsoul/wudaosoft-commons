/* 
 * Copyright(c)2010-2016 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */ 
 
package com.wudaosoft.commons.mvc.escapehtml;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.util.HtmlUtils;

/** 
 * @author Changsoul Wu
 * 
 */
public class EscapeHtmlRequestParamMethodArgumentResolver extends RequestParamMethodArgumentResolver {
	
	public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
	
	/**
	 * @param useDefaultResolution
	 */
	public EscapeHtmlRequestParamMethodArgumentResolver(boolean useDefaultResolution) {
		super(useDefaultResolution);
	}
	
	public EscapeHtmlRequestParamMethodArgumentResolver(ConfigurableBeanFactory beanFactory, boolean useDefaultResolution) {
		super(beanFactory, useDefaultResolution);
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {

		if (parameter.hasParameterAnnotation(IgnoreEscapeHtml.class) || parameter.getMethodAnnotation(IgnoreEscapeHtml.class) != null) {
			return super.resolveName(name, parameter, webRequest);
		}
		
		Object arg = super.resolveName(name, parameter, webRequest);
		
		if (String.class == parameter.getParameterType() || String[].class == parameter.getParameterType()) {
			String[] paramValues = webRequest.getParameterValues(name);
			if (paramValues != null) {
				
				for(int i = 0; i < paramValues.length; i++) {
					paramValues[i] = HtmlUtils.htmlEscape(paramValues[i], DEFAULT_CHARACTER_ENCODING);
				}
				
				arg = (paramValues.length == 1 ? paramValues[0] : paramValues);
			}
		}
		return arg;
	}

}
