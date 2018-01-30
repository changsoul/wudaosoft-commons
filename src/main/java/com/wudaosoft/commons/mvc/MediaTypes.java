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

/**
 * 带UTF-8 charset 定义的MediaType.
 * 
 * Jax-RS和Spring的MediaType没有UTF-8的版本，
 * Google的MediaType必须再调用toString()函数而不是常量，不能用于Restful方法的annotation。
 * 
 * @author Changsoul Wu
 */
public class MediaTypes {

	public static final String APPLICATION_XML = "application/xml";
	public static final String APPLICATION_XML_UTF_8 = "application/xml; charset=UTF-8";

	public static final String JSON = "application/json";
	public static final String JSON_UTF_8 = "application/json; charset=UTF-8";

	public static final String JAVASCRIPT = "application/javascript";
	public static final String JAVASCRIPT_UTF_8 = "application/javascript; charset=UTF-8";

	public static final String APPLICATION_XHTML_XML = "application/xhtml+xml";
	public static final String APPLICATION_XHTML_XML_UTF_8 = "application/xhtml+xml; charset=UTF-8";

	public static final String TEXT_PLAIN = "text/plain";
	public static final String TEXT_PLAIN_UTF_8 = "text/plain; charset=UTF-8";

	public static final String TEXT_XML = "text/xml";
	public static final String TEXT_XML_UTF_8 = "text/xml; charset=UTF-8";

	public static final String TEXT_HTML = "text/html";
	public static final String TEXT_HTML_UTF_8 = "text/html; charset=UTF-8";
}
