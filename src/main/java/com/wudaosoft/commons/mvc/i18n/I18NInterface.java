/* 
 * Copyright(c)2010-2015 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */ 
 
package com.wudaosoft.commons.mvc.i18n;

/** 
 * @author Changsoul Wu
 * 
 */
public interface I18NInterface {
	
	/**
	 * 多语言
	 * @param lang 语言
	 * @param code 代码
	 * @return
	 */
	String get(String lang, Object code);
}
