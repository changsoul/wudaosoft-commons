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
