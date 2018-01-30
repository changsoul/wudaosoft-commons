/**
 *    Copyright 2009-2018 Wudao Software Studio(wudaosoft.com)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
