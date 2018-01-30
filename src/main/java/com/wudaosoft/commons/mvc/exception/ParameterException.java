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
