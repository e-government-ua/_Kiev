package org.activiti.redis.service;

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
	 */
void write(String key, byte[]value);
	
	byte[] read(String valueByKey);

}