/* 
 * Copyright(c)2010-2016 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */ 
 
package com.wudaosoft.commons.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

/** 
 * @author Changsoul Wu
 * 
 */
public class SignatureUtil {

	private static final String SPLIT = "";

	/**
	 * 验证签名
	 * @param sign
	 * @param paramsList
	 * @return
	 */
	public static boolean validateSign(String sign, String accessKey, List<String> paramsList) {
    	
    	if(sign == null || paramsList == null || paramsList.size() == 0)
			return false;
    	
    	paramsList.add(accessKey);
		
		String[] params = new String[paramsList.size()];
		
		paramsList.toArray(params);
		
		Arrays.sort(params);
		
		String s = StringUtils.arrToString(params, SPLIT);
		
		//System.out.println("paramsStr:"+s);
		
		String md5Str = DigestUtils.sha1(s);
		
		//System.out.println("md5Str:"+md5Str);
		
		return sign.equals(md5Str);
	}
	
	/**
	 * 签名
	 * @param accessKey
	 * @param params
	 * @return
	 */
	public static String signature(String accessKey, Map<String, String> params) {
		
		List<String> psList = Lists.newArrayList();
		
		psList.add(accessKey);
		
		for (Map.Entry<String, String> entry : params.entrySet()) {
			
			if(entry.getValue() != null && entry.getValue().length() > 0)
				psList.add(entry.getValue());
		}
		
		String[] paramArry = new String[psList.size()];
		
		psList.toArray(paramArry);
		
		Arrays.sort(paramArry);
		
		String s = StringUtils.arrToString(paramArry, SPLIT);
		
		return DigestUtils.sha1(s);
	}
}
