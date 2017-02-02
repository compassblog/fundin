package com.fundin.dao.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.fundin.dao.redis.JedisManager.RedisExecutor;

@Component("redisDao")
public class RedisDaoImpl implements RedisDao {

	private static final Logger LOG = LoggerFactory.getLogger(
			RedisDaoImpl.class);
	
	public void set(final String key, final String val) {
		JedisManager.execute(new RedisExecutor() {
			@Override
			public Object execute(Jedis jedis) {
				jedis.set(key, val);
				LOG.debug("redisDao set key: {} & val: {}", key, val);
				return null;
			}
		});
	}
	
	@Override
	public void setex(final String key, final String val, final int expiry) {
		JedisManager.execute(new RedisExecutor() {
			@Override
			public Object execute(Jedis jedis) {
				jedis.setex(key, expiry, val);
				LOG.debug("redisDao set key: {} & val: {} & expiry :{}", key, val, expiry);
				return null;
			}
		});
		
	}
	
	public String get(final String key) {
		return (String) JedisManager.execute(new RedisExecutor() {
			@Override
			public Object execute(Jedis jedis) {
				String val = jedis.get(key);
				LOG.debug("redisDao get key: {} ---> val: {}", key, val);
				return val;
			}
		});
	}

	@Override
	public Long del(final String key) {
		return (Long) JedisManager.execute(new RedisExecutor() {
			@Override
			public Object execute(Jedis jedis) {
				Long val = jedis.del(key);
				LOG.debug("redisDao del key: {}", key);
				return val;
			}
		});
	}

}
