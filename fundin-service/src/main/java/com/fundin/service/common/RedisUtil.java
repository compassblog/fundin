package com.fundin.service.common;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.fundin.dao.redis.RedisDao;

@Component("redisUtil")
public class RedisUtil {

	@Resource
	private RedisDao redisDao;
	
//	private static final String NOT_EXIST = "nil";
	
	public <T> T getByCache(String redisKey, Class<T> clazz, DbQueryExecutor<T> executor) {
		return getByCache(redisKey, clazz, executor, null);
	}
	
	public <T> T getByCache(String redisKey, Class<T> clazz, DbQueryExecutor<T> executor, Integer expiry) {
//		String redisVal = redisDao.get(redisKey);
//		if (StringUtils.isNotBlank(redisVal) && ! NOT_EXIST.equalsIgnoreCase(redisVal)) {
//			return JSON.parseObject(redisVal, clazz);
//		}
		
		T t = executor.getFromDb();
//		if (null != t) {
//			if (null != expiry) {
//				redisDao.setex(redisKey, JSON.toJSONString(t), expiry);
//			} else {
//				redisDao.set(redisKey, JSON.toJSONString(t));
//			}
//		}
		return t;
	}
	
	public <T> List<T> getListByCache(String redisKey, Class<T> clazz, 
			DbQuery4ListExecutor<T> executor) {
		return getListByCache(redisKey, clazz, executor, null);
	}
	
	public <T> List<T> getListByCache(String redisKey, Class<T> clazz, 
			DbQuery4ListExecutor<T> executor, Integer expiry) {
//		String redisVal = redisDao.get(redisKey);
//		if (StringUtils.isNotBlank(redisVal) && ! NOT_EXIST.equalsIgnoreCase(redisVal)) {
//			return JSON.parseArray(redisVal, clazz);
//		}
		
		List<T> list = executor.getListFromDb();
//		if (CollectionUtils.isNotEmpty(list)) {
//			if (null != expiry) {
//				redisDao.setex(redisKey, JSON.toJSONString(list), expiry);
//			} else {
//				redisDao.set(redisKey, JSON.toJSONString(list));
//			}
//		}
		return list;
	}
	
	public interface DbQueryExecutor<T> {
		public T getFromDb();
	}
	
	public interface DbQuery4ListExecutor<T> {
		public List<T> getListFromDb();
	}
	
	public Long del(String key) {
//		return redisDao.del(key);
		return (long)1;
	}
}
