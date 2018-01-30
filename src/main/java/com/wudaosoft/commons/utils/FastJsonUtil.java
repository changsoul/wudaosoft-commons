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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author Changsoul Wu
 * 
 */
public class FastJsonUtil {

	public static final SerializerFeature[] EASY_UI_FEATURES = {
			SerializerFeature.DisableCircularReferenceDetect,
			SerializerFeature.WriteNonStringKeyAsString,
			SerializerFeature.WriteNullStringAsEmpty,
			SerializerFeature.WriteNullNumberAsZero,
			SerializerFeature.WriteNullBooleanAsFalse,
			//SerializerFeature.WriteNullListAsEmpty,
			SerializerFeature.WriteDateUseDateFormat };

	public static String toJSONString(Object obj) {
		if (obj == null)
			return "{}";

		return JSON.toJSONString(obj, EASY_UI_FEATURES);
	}
}
