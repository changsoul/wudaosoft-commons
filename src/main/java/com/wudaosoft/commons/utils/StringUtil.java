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
 * @author Changsoul Wu
 *
 */
public class StringUtil {
	public static String arrayToString(Object ... arr){
		if(arr.length == 0)
			return "";
		return arrayToString(",", arr);
	}
	
	public static String arrayToString(String ... arr){
		if(arr.length == 0)
			return "";
		return arrayToString(",", arr);
	}
	
	public static String arrayToString(String sp,Object ...arr){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]).append(sp);
		}
		return sb.delete(sb.length()-1, sb.length()).toString();
	}
	public static String arrayToString(String sp,String ...arr){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]).append(sp);
		}
		return sb.delete(sb.length()-1, sb.length()).toString();
	}
	
	public static String arrayToStringNoSplit(Object ...arr){
		if(arr.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
		}
		return sb.toString();
	}
}
