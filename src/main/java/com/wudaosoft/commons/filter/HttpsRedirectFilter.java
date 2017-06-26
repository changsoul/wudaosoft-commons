/* 
 * Copyright(c)2010-2016 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */

package com.wudaosoft.commons.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.wudaosoft.commons.utils.WebUtils;

/**

 * @author Changsoul Wu
 * 
 */
public class HttpsRedirectFilter implements Filter {
	
	private boolean useHttps = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (!(request instanceof HttpServletRequest)) {
			chain.doFilter(request, response);
			return;
		}
		
		if(!useHttps) {
			chain.doFilter(request, response);
			return;
		}

		final HttpServletRequest req = (HttpServletRequest) request;
		
		HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper((HttpServletResponse) response) {
			
			@Override
			public void sendRedirect(String location) throws IOException {
				
				if(!location.startsWith("https://")) {
					
					if(location.startsWith("http://")) {
						
						location = "https://" + location.substring(7, location.length());
					} else {
						
						location = WebUtils.getServerUrl(req, true) + location;
					}
				}
				
				super.sendRedirect(location);
			}
		};

		chain.doFilter(request, wrappedResponse);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
		try {
			useHttps = Boolean.valueOf(arg0.getInitParameter("useHttps").trim().toLowerCase()).booleanValue();
		}catch(Exception e) {}
	}

}
