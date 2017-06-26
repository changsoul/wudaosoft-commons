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
public class ReturnData implements Serializable {

	public static final ReturnData SUCCESS = new ReturnData();
	
	private static final long serialVersionUID = -6468714371712330704L;

	private int ret = 0;

	private String retDes = "ok";
	
	private Object data;
	
	public ReturnData() {
	}

	public ReturnData(Object data) {
		this.data = data;
	}
	
	public ReturnData(int ret, String retDes) {
		this.ret = ret;
		this.retDes = retDes;
	}

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public String getRetDes() {
		return retDes;
	}

	public void setRetDes(String retDes) {
		this.retDes = retDes;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
