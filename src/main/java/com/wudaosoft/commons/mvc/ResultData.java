/* 
 * Copyright(c)2010-2014 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */

package com.wudaosoft.commons.mvc;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author Changsoul Wu
 * 
 */
@XmlRootElement(name="xml")
@JacksonXmlRootElement(localName="xml")
public class ResultData implements Serializable {

	private static final long serialVersionUID = -7373727940885790938L;

	private int errCode = 0;

	private String errMsg = "ok";
	
	private Object data;
	
	public ResultData() {
	}

	public ResultData(Object data) {
		this.data = data;
	}

	public ResultData(int errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
