/* 
 * Copyright(c)2010-2014 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */ 
 
package com.wudaosoft.commons.mvc.exception;

/** 
 * @author Changsoul Wu
 * 
 */
public class ParameterException extends ServiceException {

	private static final long serialVersionUID = 618552387915600076L;

	public ParameterException() {
		super(100, "Invalid parameter.");
	}
	
	public static void throwThis() {
		throw new ParameterException();
	}

}
