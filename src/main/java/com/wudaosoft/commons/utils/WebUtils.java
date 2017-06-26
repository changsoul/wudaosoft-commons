/* 
 * Copyright(c)2010-2015 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */ 
 
package com.wudaosoft.commons.utils;

import javax.servlet.http.HttpServletRequest;

/** 
 * @author Changsoul Wu
 * 
 */
public class WebUtils {
	
	public static String getFullRequestUrl(HttpServletRequest request) {
		return getFullRequestUrl(request, false);
	}
	
	public static String getFullRequestUrl(HttpServletRequest request, boolean forceHttps) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(forceHttps ? "https" : request.getScheme());
		sb.append("://");
		sb.append(request.getServerName());
		sb.append((request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort());
		sb.append(request.getContextPath());
		sb.append(request.getServletPath());
		sb.append(request.getQueryString() == null ? "" : "?" + request.getQueryString());
		
		return sb.toString();
	}
	
	public static String getWebApprUrl(HttpServletRequest request) {
		return getWebApprUrl(request, false);
	}
	
	public static String getWebApprUrl(HttpServletRequest request, boolean forceHttps) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(forceHttps ? "https" : request.getScheme());
		sb.append("://");
		sb.append(request.getServerName());
		sb.append((request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort());
		sb.append(request.getContextPath());
		
		return sb.toString();
	}
	
	public static String getServerUrl(HttpServletRequest request) {
		return getServerUrl(request, false);
	}
	
	public static String getServerUrl(HttpServletRequest request, boolean forceHttps) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(forceHttps ? "https" : request.getScheme());
		sb.append("://");
		sb.append(request.getServerName());
		sb.append((request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort());
		
		return sb.toString();
	}

}
