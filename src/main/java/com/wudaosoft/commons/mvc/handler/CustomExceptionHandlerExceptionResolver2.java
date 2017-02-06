/* 
 * Copyright(c)2010-2014 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */

package com.wudaosoft.commons.mvc.handler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.wudaosoft.commons.mvc.ReturnData;
import com.wudaosoft.commons.mvc.exception.ParameterException;
import com.wudaosoft.commons.mvc.exception.ServiceException;
import com.wudaosoft.commons.mvc.i18n.I18NInterface;

/**
 * @author Changsoul Wu
 * 
 */
public class CustomExceptionHandlerExceptionResolver2 extends
		ExceptionHandlerExceptionResolver {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandlerExceptionResolver2.class);
	
	private static final String DEFAULT_EXCEPTION_ATTRIBUTE = "exception";
	
	private static final ReturnData DEFAULT_ERROR_STRING = new ReturnData(-1, "System error");
	private static final ReturnData NOT_FOUND_STRING = new ReturnData(404, "Not found");
	
	private final static String APPLICATION_JSON_VALUE = "application/json; charset=utf-8";
	private final static MediaType APPLICATION_JSON_UTF8 = MediaType.valueOf(APPLICATION_JSON_VALUE);

	private String defaultErrorView;
	
	private String i18nKey;
	
	private I18NInterface i18nBean;

	public String getDefaultErrorView() {
		return defaultErrorView;
	}

	public void setDefaultErrorView(String defaultErrorView) {
		this.defaultErrorView = defaultErrorView;
	}

	public String getI18nKey() {
		return i18nKey;
	}

	public void setI18nKey(String i18nKey) {
		this.i18nKey = i18nKey;
	}

	public I18NInterface getI18nBean() {
		return i18nBean;
	}

	public void setI18nBean(I18NInterface i18nBean) {
		this.i18nBean = i18nBean;
	}

	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response,
			HandlerMethod handlerMethod, Exception exception) {
		
		if (exception instanceof HttpRequestMethodNotSupportedException) {

			try {
				response.setStatus(404);

				return handleResponseBody(request, response, exception);
			} catch (Exception e) {
				return null;
			}
		}

		if (handlerMethod == null) {
			return null;
		}

		Method method = handlerMethod.getMethod();

		if (method == null) {
			return null;
		}

		ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
		
		if (responseBodyAnn != null) {
			try {
				response.setStatus(200);

				return handleResponseBody(request, response, exception);
			} catch (Exception e) {
				return null;
			}
		} else {
			
			return getModelAndView(defaultErrorView, exception);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ModelAndView handleResponseBody(HttpServletRequest request, HttpServletResponse response,
			Exception exception) throws ServletException, IOException {

		ReturnData value = DEFAULT_ERROR_STRING;
		
		if (exception instanceof ServiceException) {
			ServiceException ex = ((ServiceException) exception);
			
			if(checkI18n()) {
				String lang = request.getParameter(i18nKey);
				
				if(lang != null && lang.length() > 0) {
					
					value = getI18nValue(lang, ex);
				} else {
					value = ex.getReturnData();
				}
			} else {
				value = ex.getReturnData();
			}
		} else if (exception instanceof HttpRequestMethodNotSupportedException) {

			value = NOT_FOUND_STRING;

			logger.error(exception.getMessage());
		} else if (exception instanceof PropertyAccessException
				|| exception instanceof ServletRequestBindingException
				|| exception instanceof BindException) {

			value = new ParameterException().getReturnData();

			logger.error(exception.getMessage());
		} else {
			logger.error(exception.getMessage(), exception);
		}

		HttpOutputMessage outputMessage = createHttpOutputMessage(response);
		outputMessage.getHeaders().setContentType(APPLICATION_JSON_UTF8);
		
		Class<?> returnValueType = value.getClass();
		
		List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
		
		MediaType acceptedMediaType = APPLICATION_JSON_UTF8;
		
		if (messageConverters != null) {
			for (HttpMessageConverter messageConverter : messageConverters) {
				if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
					messageConverter.write(value, acceptedMediaType, outputMessage);
					
					return new ModelAndView();
				}
			}
		}

		if (logger.isWarnEnabled()) {
			logger.warn("Could not find HttpMessageConverter that supports return type ["
					+ returnValueType + "] and " + acceptedMediaType);
		}
		
		return null;
	}
	
	protected HttpOutputMessage createHttpOutputMessage(HttpServletResponse servletResponse) {
		return new ServletServerHttpResponse(servletResponse);
	}

	protected ModelAndView getModelAndView(String viewName, Exception ex) {
		ModelAndView mv = new ModelAndView(viewName);
		mv.addObject(DEFAULT_EXCEPTION_ATTRIBUTE, ex);
		return mv;
	}
	
	private ReturnData getI18nValue(String lang, ServiceException ex) {
		String value = i18nBean.get(lang, ex.getErrCode());
		
		if(value != null) {
			ex.setErrMsg(value);
		}
		
		return ex.getReturnData();
	}

	private boolean checkI18n() {
		return (i18nKey != null && i18nBean != null);
	}
}
