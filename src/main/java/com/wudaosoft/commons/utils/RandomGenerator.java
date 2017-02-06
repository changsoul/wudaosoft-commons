/* 
 * Copyright(c)2010-2014 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */ 

package com.wudaosoft.commons.utils;

import java.util.Random;

/**
 * 返回随机数,范围.比如6 返回0-5
 * 
 * @author Changsoul Wu 
 */
public class RandomGenerator {
	static Random ran = new Random();

	public static int ramdomUnm(int range) {
		int num = -1;
		num = Math.abs(ran.nextInt()) % range;
		return num;
	}
}
