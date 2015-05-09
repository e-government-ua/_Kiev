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
	
	String putAttachments(byte[] file) throws Exception;
	
	byte[] getAttachments(String key) throws Exception;
}
