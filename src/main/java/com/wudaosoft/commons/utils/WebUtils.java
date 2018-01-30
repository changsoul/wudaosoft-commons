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
		sb.append(forceHttps ? "https" : getScheme(request));
		sb.append("://");
		sb.append(request.getServerName());
		sb.append((request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort());
		sb.append(request.getContextPath());
		sb.append(request.getServletPath());
		sb.append(request.getQueryString() == null ? "" : "?" + request.getQueryString());

		return sb.toString();
	}

	public static String getContextPathUrl(HttpServletRequest request) {
		return getContextPathUrl(request, false);
	}

	public static String getContextPathUrl(HttpServletRequest request, boolean forceHttps) {

		StringBuilder sb = new StringBuilder();
		sb.append(forceHttps ? "https" : getScheme(request));
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
		sb.append(forceHttps ? "https" : getScheme(request));
		sb.append("://");
		sb.append(request.getServerName());
		sb.append((request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : ":" + request.getServerPort());

		return sb.toString();
	}

	public static String getScheme(HttpServletRequest request) {

		String scheme = request.getHeader("X-Forwarded-Proto");

		if (StringUtils.hasText(scheme) && !"unknown".equalsIgnoreCase(scheme)) {
			return scheme;
		}

		scheme = request.getHeader("X-Forwarded-Scheme");

		if (StringUtils.hasText(scheme) && !"unknown".equalsIgnoreCase(scheme)) {
			return scheme;
		}

		return request.getScheme();
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
