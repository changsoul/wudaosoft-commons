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
package com.wudaosoft.commons.mvc.exception;

import com.wudaosoft.commons.mvc.ResultData;
import com.wudaosoft.commons.mvc.ReturnData;

/** 
 * @author Changsoul Wu
 * 
 */
public class ServiceException extends RuntimeException {
	
	public static final ServiceException PARAMETER_EXCEPTION = new ServiceException(100, "Invalid parameter.");

	private static final long serialVersionUID = 3529318243869480912L;

	private int errCode = -1;
	
	private String errMsg = "System error.";

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public ServiceException() {
		super();
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		this.errMsg = message;
	}

	public ServiceException(String message) {
		super(message);
		this.errMsg = message;
	}

	public ServiceException(Throwable cause) {
		super(cause);
		this.errMsg = cause.getMessage();
	}
	
	public ServiceException(int errCode, String errMsg) {
		super(errMsg);
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	/**
	 * 
	 */
	public String toJsonString() {
		return "{\"errCode\":"+errCode+",\"errMsg\":\""+errMsg+"\"}";
	}
	
	public ResultData getResultData() {
		return new ResultData(errCode, errMsg);
	}
	
	public ReturnData getReturnData() {
		return new ReturnData(errCode, errMsg);
	}
	
	public static void throwParameterException() {
		throw PARAMETER_EXCEPTION;
	}
	
}
