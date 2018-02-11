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
package com.wudaosoft.commons.mvc.rest;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author changsoul.wu
 *
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class RestErrorController extends BasicErrorController {

	/**
	 * @param errorAttributes
	 * @param errorProperties
	 */
	public RestErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
		super(errorAttributes, errorProperties);
	}

	/**
	 * @param errorAttributes
	 * @param errorProperties
	 * @param errorViewResolvers
	 */
	public RestErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties,
			List<ErrorViewResolver> errorViewResolvers) {
		super(errorAttributes, errorProperties, errorViewResolvers);
	}

	@Override
	@RequestMapping(produces = "text/html")
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status = getStatus(request);
		response.setStatus(status.value());
		response.setContentType(MediaType.TEXT_HTML_VALUE);
		response.setCharacterEncoding("utf-8");

		String result = null;

		if (HttpStatus.NOT_FOUND.equals(status)) {
			// response.setContentType(MediaType.TEXT_HTML_VALUE);

			Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(request, false));
			result = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n<html><head>\n<title>404 Not Found</title>\n</head><body>\n<h1>Not Found</h1>\n" 
					+ "<p>The requested URL " + model.get("path") + " was not found on this server.</p>\n</body></html>";

//			result = "<html><body><h1>Not Found Page</h1>" + "<p>This application has no explicit mapping for "
//			+ model.get("path") + ", so you are seeing this as a fallback.</p>" + "<div id='created'>"
//			+ model.get("timestamp") + "</div>" + "<div>There was an unexpected error (type="
//			+ model.get("error") + ", status=" + model.get("status") + ").</div>" + "<div>"
//			+ model.get("message") + "</div></body></html>";
		} else {
//			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//			result = "{\"errCode\": " + model.get("status") + ", \"errMsg\": \"" + model.get("error") + "\"}";
//	
//			result = "<html><body><h1>Information Page</h1>" + "<p>There are something bad to happen. For more information please set the \"Accept\" with \"application/json;charset=UTF-8\" or \"application/xml;charset=UTF-8\" for HTTP request header.</p></body></html>";
			result = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n<html><head>\n<title>Notice</title>\n</head><body>\n<h1>Notice</h1>\n"
					+ "<p>There are something bad to happen. </p>\n</body></html>";
		}

		try {
			response.getWriter().append(result);
		} catch (IOException e) {
		}

		return null;
	}

}
