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

/**
 * <p> </p>
 * @author Changsoul.Wu
 * @date 2014-3-29 上午2:43:31
 */
public class StringUtils {
	
	/**
	 * @param t
	 * @param split
	 * @return
	 */
	public static <T> String arrToString(T[] t, String split){
		if(t == null){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (T t2 : t) {
			sb.append(t2).append(split);
		}
		String str = sb.toString();
		
		if(!"".equals(split))
			str = str.substring(0,str.length()-1);
		
		return str;
	}
	
	/**
	 * @param t
	 * @return
	 */
	public static <T> String arrToString(T[] t){
		return arrToString(t, "");
	}
	
	public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen)
            .append(Character.toTitleCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
}
