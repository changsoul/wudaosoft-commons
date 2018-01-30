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

import javax.servlet.ServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.util.HtmlUtils;

/** 
 * @author Changsoul Wu
 * 
 */
public class EscapeHtmlServletModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor{

	public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
	
	/**
	 * @param annotationNotRequired
	 */
	public EscapeHtmlServletModelAttributeMethodProcessor(boolean annotationNotRequired) {
		super(annotationNotRequired);
	}
	
	@Override
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
		
		EscapeHtmlServletRequestDataBinder bd = new EscapeHtmlServletRequestDataBinder(binder.getTarget(), binder.getObjectName());
		ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
		bd.bind(servletRequest);
	}
	
	@Override
	protected Object createAttributeFromRequestValue(String sourceValue, String attributeName,
			MethodParameter methodParam, WebDataBinderFactory binderFactory, NativeWebRequest request)
			throws Exception {

		DataBinder binder = binderFactory.createBinder(request, null, attributeName);
		ConversionService conversionService = binder.getConversionService();
		if (conversionService != null) {
			TypeDescriptor source = TypeDescriptor.valueOf(String.class);
			TypeDescriptor target = new TypeDescriptor(methodParam);
			if (conversionService.canConvert(source, target)) {
				
				if(String.class == methodParam.getParameterType() && !methodParam.hasParameterAnnotation(IgnoreEscapeHtml.class)) {
					sourceValue = HtmlUtils.htmlEscape(sourceValue, DEFAULT_CHARACTER_ENCODING);
				}
				
				return binder.convertIfNecessary(sourceValue, methodParam.getParameterType(), methodParam);
			}
		}
		
		return null;
	}
}
