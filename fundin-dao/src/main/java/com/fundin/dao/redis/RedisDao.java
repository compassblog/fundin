package com.fundin.dao.redis;

public interface RedisDao {

	public void set(String key, String val);
	
	public void setex(String key, String val, int expiry);
	
	public String get(String key);
	
	public Long del(String key);
	
}
