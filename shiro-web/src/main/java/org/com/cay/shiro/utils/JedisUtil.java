package org.com.cay.shiro.utils;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Created by Cay on 2018/6/8.
 */
@Component
public class JedisUtil {

	@Resource
	private JedisPool jedisPool;

	private Jedis getJedis() {
		return jedisPool.getResource();
	}

	public void set(byte[] key, byte[] value) {
		Jedis jedis = getJedis();
		try {
			jedis.set(key, value);
		} finally {
			jedis.close();
		}
	}

	public void expire(byte[] key, int seconds) {
		Jedis jedis = getJedis();

		try {
			jedis.expire(key, seconds);
		} finally {
			jedis.close();
		}

	}

	public byte[] get(byte[] key) {
		Jedis jedis = getJedis();
		try {
			return jedis.get(key);
		} finally {
			jedis.close();
		}
	}

	public void del(byte[] key) {
		Jedis jedis = getJedis();

		try {
			jedis.del(key);
		} finally {
			jedis.close();
		}
	}

	public Set<byte[]> keys(String prefix) {
		Jedis jedis = getJedis();

		try {
			return jedis.keys((prefix + "*").getBytes());
		} finally {
			jedis.close();
		}
	}
}
