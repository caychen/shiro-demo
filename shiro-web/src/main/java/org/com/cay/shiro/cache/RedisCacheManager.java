package org.com.cay.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Cay on 2018/6/8.
 */
public class RedisCacheManager implements CacheManager {


	@Autowired
	private RedisCache redisCache;

	@Override
	public <K, V> Cache<K, V> getCache(String s) throws CacheException {
		return redisCache;
	}
}
