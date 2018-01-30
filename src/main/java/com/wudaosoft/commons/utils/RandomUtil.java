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
import java.util.List;
import java.util.Random;

/**
 * <p>随机工具类 </p>
 * @author Changsoul.Wu
 * @date 2013-3-11 下午1:58:30
 */
public class RandomUtil {

	private static Random _random = new Random(System.currentTimeMillis());

	public static int getRandomByArea(int min, int max) {
		List<Integer> is = new ArrayList<Integer>();
		for (int i = min; i <= max; i++) {
			is.add(i);
		}
		return randNInM(is.toArray(new Integer[] {}))[0];
	}

	public static float randomFloat() {
		return _random.nextFloat();
	}

	public static int getRandom(int num) {
		return (int) (Math.random() * num);
	}

	public static long getRandom(long num) {
		return (long) (Math.random() * num);
	}

	public static Integer getRandomByKeySet(Integer... value) {
		if(value == null || value.length == 0){
			return null;
		}
		return value[getRandom(value.length)];
	}

	public static String getRandomByKeySet(String... value) {
		return value[getRandom(value.length)];
	}

	public static String getRandomByKeySet(String except, String... value) {
		String v = value[getRandom(value.length)];
		while (v.equals(except)) {
			v = value[getRandom(value.length)];
		}
		return v;
	}

	public static <T> T[] randNInM(T[] obj) {
		int length = obj.length;
		for (int i = 0; i < length; i++) {
			int p = getRandom(length);
			T tmp = obj[i];
			obj[i] = obj[p];
			obj[p] = tmp;
		}
		return obj;
	}

	public static String randOne(String[] str) {
		return randNInM(str)[0];
	}

	public static Integer randOne(Integer[] str) {
		return randNInM(str)[0];
	}

	public static <T> T randOne(T[] obj) {
		return randNInM(obj)[0];
	}

	public static String[] randNInM(int n, String[] str) {
		String s[] = randNInM(str);
		String tmp[] = new String[n];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = s[i];
		}
		s = null;
		return tmp;
	}

	public static Integer[] randNInM(int n, Integer[] str) {
		Integer s[] = randNInM(str);
		Integer tmp[] = new Integer[n];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = s[i];
		}
		s = null;
		return tmp;
	}

	public static Object[] randNInM(int n, Object[] str) {
		Object s[] = randNInM(str);
		Object tmp[] = new Object[n];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = s[i];
		}
		s = null;
		return tmp;
	}
	
	public static boolean getRandomBoolean() {
		int i =  (int) (Math.random() * 2);
		return i == 0 ? true:false;
	}

//	public static void main(String[] args) {
//
//		for (int i = 0; i < 10; i++) {
//			int t = getRandom(4);
//			Log.info(t);
//		}
//		Log.info(getRandomBoolean());
//	}
}
