package org.activiti.redis.client;

/**
 * @author inna
 *
 */
public interface RedisOperations extends Operations {
	/**
	 * запись redis.
	 * 
	 * @param key
	 * @param message
	 */
	void write(String key, byte[]value);
	
	byte[] read(String valueByKey);
}
