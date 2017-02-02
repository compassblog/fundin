package com.fundin.dao.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisManager {

	private static final Logger LOG = LoggerFactory.getLogger(
			JedisManager.class);
	
	private static String HOST = "127.0.0.1";
	private static int PORT = 6379;
	private static int TIMEOUT = 10000;
	private static String AUTH = "wz180295";
	
    //	可用连接实例的最大数目，默认值为8；如果赋值为-1，则表示不限制；
	//	如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)
	private static int MAX_ACTIVE = 100;
	//	控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8
	private static int MAX_IDLE = 20;
	//	等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时；
	//	如果超过等待时间，则直接抛出JedisConnectionException
	private static int MAX_WAIT = 10000;
	//	在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的
	private static boolean TEST_ON_BORROW = true;
	
	private static JedisPool jedisPool = null;
	/**
	 * 初始化Jedis连接池
	 */
	static {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(MAX_ACTIVE);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWaitMillis(MAX_WAIT);
			config.setTestOnBorrow(TEST_ON_BORROW);
			jedisPool = new JedisPool(config, HOST, PORT, TIMEOUT, AUTH);
		} catch (Exception ex) {
			LOG.error("init JedisPool error !!!", ex);
		}
	}
	
	/**
	 * 获取Jedis实例
	 * @return
	 */
	public synchronized static Jedis getJedis() {
		try {
			if (jedisPool != null) {
				return jedisPool.getResource();
			}
		} catch (Exception ex) {
			LOG.error("JedisManager getJedis error !!!", ex);
		}
		return null;
	}
	
	public static Object execute(RedisExecutor executor) {
		Jedis jedis = null;
		try {
            jedis = getJedis();
            if (jedis == null) {
            	return null;
            }
            return executor.execute(jedis);
        } catch (Exception ex) {
        	LOG.error("JedisManager execute error !!!", ex);
        	jedisPool.returnBrokenResource(jedis);
        } finally {
        	jedisPool.returnResource(jedis);
        }
		return null;
	}
	
	public interface RedisExecutor {
		public Object execute(Jedis jedis);
	}
	
}
