package com.sideprojects.cachesystems;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import com.sideprojects.cachesystems.util.CacheCleaner;
import com.sideprojects.cachesystems.util.ValueObj;

public class CacheSystem {
	private ConcurrentHashMap<String, ValueObj> cache = null;
	private TreeMap<Long, ArrayList<String>> expiryIndex = null;
	private CacheCleaner cacheCleaner = null;

	CacheSystem() {
		cache = new ConcurrentHashMap<String, ValueObj>();
		expiryIndex = new TreeMap<Long, ArrayList<String>>();
		cacheCleaner = new CacheCleaner(cache, expiryIndex, 1);
		cacheCleaner.start();
	}

	public void add(String key, Object object, long inputTTL) {
		long timeToLive = System.currentTimeMillis() + (inputTTL * 1000);
		ValueObj valueObj = new ValueObj(key, inputTTL);
		cache.put(key, valueObj);

		ArrayList<String> keys = expiryIndex.get(timeToLive);
		if (keys == null) {
			keys = new ArrayList<String>();
		}
		keys.add(key);
		expiryIndex.put(timeToLive, keys);
	}

	public Object get(String key) throws Exception {
		if (cache.isEmpty()) {
			throw new Exception("Data is not available.");
		}
		ValueObj value = cache.get(key);
		if (value != null && !value.isExpired()) {
			return value;
		}
		throw new Exception("Data is not available.");
	}

	public synchronized void delete(String key) throws Exception {
		if (cache.isEmpty()) {
			throw new Exception("Data is not available.");
		}
		cache.remove(key);
	}
}