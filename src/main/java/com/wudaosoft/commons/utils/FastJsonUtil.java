/* 
 * Copyright(c)2010-2014 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
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
