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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import javax.servlet.ServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;
import org.springframework.web.util.HtmlUtils;

/** 
 * @author Changsoul Wu
 * 
 */
public class EscapeHtmlServletRequestDataBinder extends ExtendedServletRequestDataBinder {

	public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
	
	/**
	 * @param target
	 */
	public EscapeHtmlServletRequestDataBinder(Object target) {
		super(target);
	}

	public EscapeHtmlServletRequestDataBinder(Object target, String objectName) {
		super(target, objectName);
	}

	@Override
	protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
		super.addBindValues(mpvs, request);

		PropertyValue[] pvs = mpvs.getPropertyValues();
		
		if(pvs == null)
			return;
		
		Class<?> clazz = getTarget().getClass();
		
		if(AnnotationUtils.findAnnotation(clazz, IgnoreEscapeHtml.class) != null)
			return;
		
		for (PropertyValue pv : pvs) {
			
			PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, pv.getName());
			if(pd == null || String.class != pd.getPropertyType()) {
				continue;
			}
			
			Method readMethod = pd.getReadMethod();
			
			if(readMethod == null) {
				continue;
			}
			
			MethodParameter methodParameter = new MethodParameter(readMethod, 0);
			
			if(methodParameter.getMethodAnnotation(IgnoreEscapeHtml.class) != null) {
				continue;
			}
			
			PropertyValue mPv = new PropertyValue(pv.getName(), HtmlUtils.htmlEscape((String)pv.getValue(), DEFAULT_CHARACTER_ENCODING));
			mpvs.addPropertyValue(mPv);
		}
	}
	
}
