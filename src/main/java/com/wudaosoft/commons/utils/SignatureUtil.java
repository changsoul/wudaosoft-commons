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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
		
		List<String> psList = new ArrayList<String>(30);
		
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
