/* 
 * Copyright(c)2010-2014 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
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
