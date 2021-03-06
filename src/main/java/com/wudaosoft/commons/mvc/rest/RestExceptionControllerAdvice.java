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
package com.wudaosoft.commons.mvc.rest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.wudaosoft.commons.mvc.ResultData;
import com.wudaosoft.commons.mvc.exception.ServiceException;
import com.wudaosoft.commons.mvc.i18n.Lang;
import com.wudaosoft.commons.mvc.i18n.LangType;
import com.wudaosoft.commons.mvc.i18n.RestErrorI18N;

/**
 * @author changsoul.wu
 *
 */
@ControllerAdvice
public class RestExceptionControllerAdvice extends ResponseEntityExceptionHandler {

	private Class<?> resultClazz;

	private Method codeMethod;

	private Method messageMethod;

	private RestErrorI18N i18nBean;

	private Lang lang = new Lang();

	@Autowired(required = false)
	public void setI18nBean(RestErrorI18N i18nBean) {
		this.i18nBean = i18nBean;
	}

	public void setLang(Lang lang) {
		this.lang = lang;
	}

	/**
	 * 
	 */
	public RestExceptionControllerAdvice() {
		super();
	}

	/**
	 * @param resultClazz
	 * @param codeName
	 * @param messageName
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public RestExceptionControllerAdvice(Class<?> resultClazz, String codeName, String messageName)
			throws SecurityException, NoSuchMethodException {
		super();
		Assert.notNull(resultClazz, "resultClazz must not be null");
		Assert.hasText(codeName, "codeName must not be empty");
		Assert.hasText(messageName, "messageName must not be empty");
		this.resultClazz = resultClazz;
		String codeNameSetterMethodName = "set" + com.wudaosoft.commons.utils.StringUtils.capitalize(codeName);
		String messageNameSetterMethodName = "set" + com.wudaosoft.commons.utils.StringUtils.capitalize(messageName);
		this.codeMethod = resultClazz.getMethod(codeNameSetterMethodName, int.class);
		this.messageMethod = resultClazz.getMethod(messageNameSetterMethodName, String.class);
		makeAccessible(this.codeMethod);
		makeAccessible(this.messageMethod);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		HttpServletRequest req = null;

		if (request instanceof NativeWebRequest) {
			req = ((NativeWebRequest) request).getNativeRequest(HttpServletRequest.class);
		}

		if (HttpStatus.NOT_FOUND.equals(status) || HttpStatus.METHOD_NOT_ALLOWED.equals(status)
				|| HttpStatus.UNSUPPORTED_MEDIA_TYPE.equals(status) || HttpStatus.NOT_ACCEPTABLE.equals(status)) {

			return new ResponseEntity<Object>(genResultData(status.value(), status.getReasonPhrase(), req), status);
		} else if (HttpStatus.BAD_REQUEST.equals(status) || ex instanceof MissingPathVariableException) {

			ServiceException sexc = ServiceException.PARAMETER_EXCEPTION;

			logger.warn("[" + req.getMethod() + "] - " + req.getRequestURL() + ". " + ex.getMessage());

			return new ResponseEntity<Object>(genResultData(sexc.getErrCode(), sexc.getErrMsg(), req), HttpStatus.OK);
		} else if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {

			logger.error(ex.getMessage(), ex);

			return new ResponseEntity<Object>(genResultData(status.value(), status.getReasonPhrase(), req),
					HttpStatus.OK);
		} else {

			logger.error(ex.getMessage(), ex);

			return new ResponseEntity<Object>(body, headers, status);
		}
	}

	@Override
	protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		logger.error(ex.getMessage(), ex);

		return new ResponseEntity<Object>(null, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		logger.error(ex.getMessage(), ex);

		return new ResponseEntity<Object>(null, headers, status);
	}

	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	ResponseEntity<?> handleServiceException(HttpServletRequest request, Throwable ex) {

		logger.debug(ex.getMessage() + " -- at " + ex.getStackTrace()[0]);

		ServiceException exs = (ServiceException) ex;
		return new ResponseEntity<Object>(genResultData(exs.getErrCode(), exs.getErrMsg(), request), HttpStatus.OK);
	}

	@ExceptionHandler
	@ResponseBody
	ResponseEntity<?> handleControllerException(HttpServletRequest request, Throwable ex) {
		HttpStatus status = getStatus(request);

		logger.error(ex.getMessage(), ex);

		return new ResponseEntity<Object>(genResultData(status.value(), status.getReasonPhrase(), request),
				HttpStatus.OK);
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.valueOf(statusCode);
	}

	private Object genResultData(int code, String msg, HttpServletRequest request) {

		String i18nMsg = null;

		if (i18nBean != null) {
			String langStr = null;

			if (lang.getLangType().equals(LangType.Param)) {
				langStr = request.getParameter(lang.getLangKey());

			} else if (lang.getLangType().equals(LangType.Header)) {
				langStr = request.getHeader(lang.getLangKey());
			}

			if (StringUtils.hasText(langStr)) {
				i18nMsg = i18nBean.get(langStr, code);
			}
		}

		if (i18nMsg == null) {
			i18nMsg = msg;
		}

		if (resultClazz == null)
			return new ResultData(code, i18nMsg);

		try {

			Object resultData = resultClazz.newInstance();
			codeMethod.invoke(resultData, code);
			messageMethod.invoke(resultData, i18nMsg);

			return resultData;

		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage(), e);
		}

		return new ResultData(code, i18nMsg);
	}

	private void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
				&& !method.isAccessible()) {
			method.setAccessible(true);
		}
	}
}
