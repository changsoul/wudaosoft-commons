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
