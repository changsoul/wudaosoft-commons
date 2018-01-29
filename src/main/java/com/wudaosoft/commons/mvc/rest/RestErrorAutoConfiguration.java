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

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author changsoul.wu
 *
 */
@Configuration
public class RestErrorAutoConfiguration {

	@Bean
	public BasicErrorController basicErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
		return new RestErrorController(errorAttributes, serverProperties.getError());
	}
    
    @Bean
    @ConditionalOnMissingBean(RestExceptionControllerAdvice.class)
    public RestExceptionControllerAdvice restExceptionControllerAdvice() {
    	return new RestExceptionControllerAdvice();
    }
}
