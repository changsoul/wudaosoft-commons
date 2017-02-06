/* Copyright(c)2010-2012 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */

package com.wudaosoft.commons.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p> </p>
 * @author Changsoul.Wu
 * @date 2013-3-11 下午3:25:24
 * @param <K>
 * @param <V>
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