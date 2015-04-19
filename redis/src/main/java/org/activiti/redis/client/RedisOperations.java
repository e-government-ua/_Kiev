package org.activiti.redis.client;

import java.io.IOException;

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
	
	String putAttachments(byte[] file) throws IOException,Exception;
	
	byte[] getAttachments(String key) throws IOException;
}
