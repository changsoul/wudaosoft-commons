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
package com.wudaosoft.commons.mvc.condition;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Conditional;

/** 
 * @author Changsoul Wu
 * 
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnMyBeanCondition.class)
public @interface ConditionalOnMissingMyBean {
	
	/**
	 * The class type of bean that should be checked. The condition matches when each
	 * class specified is missing in the {@link ApplicationContext}.
	 * @return the class types of beans to check
	 */
	Class<?>[] value() default {};

	/**
	 * The class type names of bean that should be checked. The condition matches when
	 * each class specified is missing in the {@link ApplicationContext}.
	 * @return the class type names of beans to check
	 */
	String[] type() default {};

	/**
	 * The class type of beans that should be ignored when identifying matching beans.
	 * @return the class types of beans to ignore
	 * @since 1.2.5
	 */
	Class<?>[] ignored() default {};

	/**
	 * The class type names of beans that should be ignored when identifying matching
	 * beans.
	 * @return the class type names of beans to ignore
	 * @since 1.2.5
	 */
	String[] ignoredType() default {};

	/**
	 * The annotation type decorating a bean that should be checked. The condition matches
	 * when each annotation specified is missing from all beans in the
	 * {@link ApplicationContext}.
	 * @return the class-level annotation types to check
	 */
	Class<? extends Annotation>[] annotation() default {};

	/**
	 * The names of beans to check. The condition matches when each bean name specified is
	 * missing in the {@link ApplicationContext}.
	 * @return the name of beans to check
	 */
	String[] name() default {};

	/**
	 * Strategy to decide if the application context hierarchy (parent contexts) should be
	 * considered.
	 * @return the search strategy
	 */
	SearchStrategy search() default SearchStrategy.ALL;

}
