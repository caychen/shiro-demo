package org.com.cay.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.com.cay.shiro.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Cay on 2018/6/8.
 */
@Component
public class RedisCache<K, V> implements Cache<K, V> {

	@Autowired
	private JedisUtil jedisUtil;

	private final String CACHE_PREFIX = "redis-cache:";

	private byte[] getKey(K k) {
		if (k instanceof String) {
			return (CACHE_PREFIX + k).getBytes();
		} else {
			return SerializationUtils.serialize(k);
		}
	}

	@Override
	public V get(K k) throws CacheException {
		System.out.println("从redis中获取数据...");
		byte[] key = getKey(k);
		byte[] value = jedisUtil.get(key);
		if (value != null) {
			return (V) SerializationUtils.deserialize(value);
		}
		return null;
	}

	@Override
	public V put(K k, V v) throws CacheException {
		byte[] key = getKey(k);
		byte[] value = SerializationUtils.serialize(v);
		jedisUtil.set(key, value);
		jedisUtil.expire(key, 600);
		return v;
	}

	@Override
	public V remove(K k) throws CacheException {
		byte[] key = getKey(k);
		byte[] value = jedisUtil.get(key);
		jedisUtil.del(key);

		if (value != null) {
			return (V) SerializationUtils.deserialize(value);
		}
		return null;
	}

	@Override
	public void clear() throws CacheException {

	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public Set<K> keys() {
		return null;
	}

	@Override
	public Collection<V> values() {
		return null;
	}
}
