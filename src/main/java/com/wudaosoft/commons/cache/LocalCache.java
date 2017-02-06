/* Copyright(c)2010-2013 WUDAOSOFT.COM
 * 
 * Email:changsoul.wu@gmail.com
 * 
 * QQ:275100589
 */

package com.wudaosoft.commons.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * <p> </p>
 * @author Changsoul.Wu
 * @date 2013-3-11 下午3:25:53
 * @param <K>
 * @param <V>
 */
public class LocalCache<K, V> {
	private final LRUMap<K, V> CACHE_MAP;
	private final LRUMap<K, Long> TIMEOUT_MAP;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public LocalCache(int maxSize) {
		this.CACHE_MAP = new LRUMap(maxSize);
		this.TIMEOUT_MAP = new LRUMap(maxSize);
	}

	public V get(K key) {
		Long timeout = this.TIMEOUT_MAP.get(key);
		
		if (timeout != null) {
			if (System.currentTimeMillis() < timeout.longValue())
				return this.CACHE_MAP.get(key);

			this.TIMEOUT_MAP.remove(key);
			this.CACHE_MAP.remove(key);
		}
		
		return null;
	}

	public boolean put(K key, V value, int TTL) {
		this.CACHE_MAP.put(key, value);
		this.TIMEOUT_MAP.put(key, Long.valueOf(System.currentTimeMillis() + TTL * 1000));

		return true;
	}
	
	public boolean refresh(K key, int TTL) {
		this.TIMEOUT_MAP.put(key, Long.valueOf(System.currentTimeMillis() + TTL * 1000));
		
		return true;
	}

	public boolean remove(K key) {
		this.CACHE_MAP.remove(key);
		this.TIMEOUT_MAP.remove(key);
		return true;
	}
	
	public void clearExpiredCache() {
		Set<K> keys = this.CACHE_MAP.keySet();
		
		if(keys == null)
			return;
		
		List<K> newKeys = new ArrayList<K>(keys);
		
		Iterator<K> iter = newKeys.iterator();
		
		while (iter.hasNext()) {
			
			get(iter.next());
		}
	}
}