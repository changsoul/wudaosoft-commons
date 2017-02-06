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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessException;
import org.springframework.core.MethodParameter;
import org.springframework.core.OrderComparator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.wudaosoft.commons.mvc.ResultData;
import com.wudaosoft.commons.mvc.exception.ParameterException;
import com.wudaosoft.commons.mvc.exception.ServiceException;
import com.wudaosoft.commons.mvc.i18n.I18NInterface;

/**
 * @author Changsoul Wu
 * 
 */
public class JsonXmlExceptionHandlerExceptionResolver extends
		ExceptionHandlerExceptionResolver {
	
	private static final Logger logger = LoggerFactory.getLogger(JsonXmlExceptionHandlerExceptionResolver.class);
	
	private static final String DEFAULT_EXCEPTION_ATTRIBUTE = "exception";
	private static final String XML_STR = ".xml";
	private static final String XML_STR1 = "xml";
	private static final String XML_FORMAT_STR = "format";
	private static final char SP_CHAR = '/';
	
	private static final ResultData DEFAULT_ERROR = new ResultData(-1, "unknown error");
	private static final ResultData NOT_FOUND_ERROR = new ResultData(404, "not found");
	private static final ResultData PARAMETER_ERROR = new ParameterException().getResultData();
	
	private final static String APPLICATION_JSON_VALUE = "application/json; charset=utf-8";
	private final static MediaType APPLICATION_JSON_UTF8 = MediaType.valueOf(APPLICATION_JSON_VALUE);
	
	private final static String APPLICATION_XML_VALUE = "application/xml; charset=utf-8";
	private final static MediaType APPLICATION_XML_UTF8 = MediaType.valueOf(APPLICATION_XML_VALUE);
	
	private final List<Object> responseBodyAdvice = new ArrayList<Object>();

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
	
	@Override
	public void afterPropertiesSet() {
		// Do this first, it may add ResponseBodyAdvice beans
		initExceptionHandlerAdviceCache();
	}

	@Override
	protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response,
			HandlerMethod handlerMethod, Exception exception) {
		
		if (exception instanceof HttpRequestMethodNotSupportedException) {

			try {
				response.setStatus(404);

				return handleResponseBody(handlerMethod, request, response, exception);
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
		RestController restControllerAnn = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), RestController.class);
		
		if (responseBodyAnn != null || restControllerAnn != null) {
			try {
				ResponseStatus responseStatusAnn = AnnotationUtils.findAnnotation(method, ResponseStatus.class);
                if (responseStatusAnn != null) {
                    HttpStatus responseStatus = responseStatusAnn.value();
                    String reason = responseStatusAnn.reason();
                    if (!StringUtils.hasText(reason)) {
                        response.setStatus(responseStatus.value());
                    } else {
                        try {
                            response.sendError(responseStatus.value(), reason);
                        } catch (IOException e) { }
                    }
                }else {
				
                	response.setStatus(200);
                }

				return handleResponseBody(handlerMethod, request, response, exception);
			} catch (Exception e) {
				logger.error(exception.getMessage(), e);
				return null;
			}
		} else {
			
			return getModelAndView(defaultErrorView, exception);
		}
	}

//	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected ModelAndView handleResponseBody(HandlerMethod handlerMethod, HttpServletRequest request, HttpServletResponse response,
			Exception exception) throws ServletException, IOException {

		ResultData value = DEFAULT_ERROR;
		
		if (exception instanceof ServiceException) {
			ServiceException ex = ((ServiceException) exception);
			
			if(checkI18n()) {
				String lang = request.getParameter(i18nKey);
				
				value = getI18nValue(lang, ex);
			} else {
				value = ex.getResultData();
			}
		} else if (exception instanceof HttpRequestMethodNotSupportedException) {

			value = NOT_FOUND_ERROR;

			logger.debug(exception.getMessage());
		} else if (exception instanceof PropertyAccessException
				|| exception instanceof ServletRequestBindingException
				|| exception instanceof BindException) {

			value = PARAMETER_ERROR;

			logger.debug(exception.getMessage());
		} else {
			logger.error(exception.getMessage(), exception);
		}

//		HttpOutputMessage outputMessage = createHttpOutputMessage(response);
//		outputMessage.getHeaders().setContentType(APPLICATION_JSON_UTF8);
//		
//		Class<?> returnValueType = value.getClass();
//		
//		List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
//		
//		MediaType acceptedMediaType = APPLICATION_JSON_UTF8;
//		
//		if (messageConverters != null) {
//			for (HttpMessageConverter messageConverter : messageConverters) {
//				if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
//					messageConverter.write(value, acceptedMediaType, outputMessage);
//					
//					return new ModelAndView();
//				}
//			}
//		}
//
//		if (logger.isWarnEnabled()) {
//			logger.warn("Could not find HttpMessageConverter that supports return type ["
//					+ returnValueType + "] and " + acceptedMediaType);
//		}
		
		return handleResponseError(handlerMethod, value, request, response);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked" })
	private ModelAndView handleResponseError(HandlerMethod handlerMethod, ResultData returnValue, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        MediaType acceptedMediaType = APPLICATION_JSON_UTF8;
        
        String url = request.getRequestURI();
        if(url.lastIndexOf(XML_STR) > url.lastIndexOf(SP_CHAR) || XML_STR1.equalsIgnoreCase(request.getParameter(XML_FORMAT_STR)))
        	acceptedMediaType = APPLICATION_XML_UTF8;
        
        ServletServerHttpResponse outputMessage = createHttpOutputMessage(response);
        Class<?> returnValueType = returnValue.getClass();
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        
        MethodParameter returnValueType1 = null;
        
        if(handlerMethod != null) {
        	returnValueType1 = handlerMethod.getReturnValueType(returnValue);
        }else {
        	returnValueType1 = getReturnValueType(returnValue);
        }
        
        if (messageConverters != null) {
            for (HttpMessageConverter messageConverter : messageConverters) {
                if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
                	Object returnValue1 = invokeAdviceChain(returnValue, returnValueType1, acceptedMediaType,
							(Class<HttpMessageConverter<?>>) messageConverter.getClass(), new ServletServerHttpRequest(request), outputMessage);
                	
					if (returnValue1 != null) {
                	
	                    messageConverter.write(returnValue1, acceptedMediaType, outputMessage);
	                    return new ModelAndView();
                    }
                }
            }
        }
        
        if (logger.isWarnEnabled()) {
            logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and " + acceptedMediaType);
        }
        
        return null;
    }
	
	protected ServletServerHttpResponse createHttpOutputMessage(HttpServletResponse servletResponse) {
		return new ServletServerHttpResponse(servletResponse);
	}

	protected ModelAndView getModelAndView(String viewName, Exception ex) {
		ModelAndView mv = new ModelAndView(viewName);
		mv.addObject(DEFAULT_EXCEPTION_ATTRIBUTE, ex);
		return mv;
	}
	
	private ResultData getI18nValue(String lang, ServiceException ex) {
		String value = i18nBean.get(lang, ex.getErrCode());
		
		if(value != null) {
			ex.setErrMsg(value);
		}
		
		return ex.getResultData();
	}

	private boolean checkI18n() {
		return (i18nKey != null && i18nBean != null);
	}
	
	private void initExceptionHandlerAdviceCache() {
		if (getApplicationContext() == null) {
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Looking for exception mappings: " + getApplicationContext());
		}

		List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext());
		Collections.sort(adviceBeans, new OrderComparator());

		for (ControllerAdviceBean adviceBean : adviceBeans) {
			if (ResponseBodyAdvice.class.isAssignableFrom(adviceBean.getBeanType())) {
				this.responseBodyAdvice.add(adviceBean);
				logger.info("Detected ResponseBodyAdvice implementation in " + adviceBean);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T invokeAdviceChain(T body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {

		if (this.responseBodyAdvice != null) {
			for (Object advice : this.responseBodyAdvice) {
				if (advice instanceof ControllerAdviceBean) {
					ControllerAdviceBean adviceBean = (ControllerAdviceBean) advice;
					if (!adviceBean.isApplicableToBeanType(returnType.getContainingClass())) {
						continue;
					}
					advice = adviceBean.resolveBean();
				}
				if (advice instanceof ResponseBodyAdvice) {
					ResponseBodyAdvice<T> typedAdvice = (ResponseBodyAdvice<T>) advice;
					if (typedAdvice.supports(returnType, selectedConverterType)) {
						body = typedAdvice.beforeBodyWrite(body, returnType,
								selectedContentType, selectedConverterType, request, response);
					}
				}
				else {
					throw new IllegalStateException("Expected ResponseBodyAdvice: " + advice);
				}
			}
		}
		return body;
	}
	
	/**
	 * Return the actual return value type.
	 */
	public MethodParameter getReturnValueType(Object returnValue) {
		return new ReturnValueMethodParameter(returnValue);
	}
	
	private class ReturnValueMethodParameter extends MethodParameter {

		private final Object returnValue;

		public ReturnValueMethodParameter(Object returnValue) {
			super(null);
			this.returnValue = returnValue;
		}

		@Override
		public Class<?> getParameterType() {
			return (this.returnValue != null ? this.returnValue.getClass() : super.getParameterType());
		}
	}
}
