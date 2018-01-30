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
package com.wudaosoft.commons.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Changsoul.Wu
 * @date 2013-3-11 下午3:25:24
 */
public class LRUMap<K, V> extends LinkedHashMap<K, V> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3677821801367145194L;
	private final ReentrantLock lock = new ReentrantLock();
	private final int maxSize;

	public LRUMap(int initCapacity, int maxSize) {
		super(initCapacity, 0.75F, true);
		this.maxSize = maxSize;
	}

	public LRUMap(int maxSize) {
		super(maxSize >> 2, 0.75F, true);
		this.maxSize = maxSize;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return (size() >= this.maxSize);
	}

	@Override
	public V get(Object key) {
		try {
			this.lock.lock();
			return super.get(key);
		} finally {
			this.lock.unlock();
		}
	}

	@SuppressWarnings("unused")
	@Override
	public V put(K key, V val) {
		try {
			Object localObject2;
			this.lock.lock();
			return super.put(key, val);
		} finally {
			this.lock.unlock();
		}
	}
}