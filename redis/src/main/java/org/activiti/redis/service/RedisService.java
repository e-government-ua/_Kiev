package org.activiti.redis.service;

import java.io.IOException;

/**
 * Сервис логирования данных.
 * @author inna
 *
 */
public interface RedisService {

	/**
	 * запись сообщения в лог.
	 * 
	 * @param key
	 * @param message
	 * @return 
	 */
	String putAttachments(byte[]file) throws IOException;
	
byte[] getAttachments(String key) throws IOException;

}