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
