/* 
 * Copyright(c)2010-2015 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */

package com.wudaosoft.commons.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

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
		sb.append(
				(request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort());
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
		sb.append(
				(request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort());
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
		sb.append(
				(request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort());

		return sb.toString();
	}

	public static String getIpAddr(HttpServletRequest request) {

		String ip = request.getHeader("X-Forwarded-For");

		if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
			int index = ip.indexOf(',');
			if (index > 0) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}

		ip = request.getHeader("x-forwarded-for");

		if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
			int index = ip.indexOf(',');
			if (index > 0) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}

		ip = request.getHeader("Proxy-Client-IP");

		if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}

		ip = request.getHeader("WL-Proxy-Client-IP");

		if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}

		ip = request.getHeader("X-Real-IP");

		if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}

		return request.getRemoteAddr();
	}
}
