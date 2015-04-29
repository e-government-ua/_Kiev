package org.activiti.redis.service;

import org.activiti.redis.exception.RedisException;
import org.activiti.redis.model.ByteArrayMultipartFile;

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
	String putAttachments(byte[]file) throws RedisException;
	
byte[] getAttachments(String key) throws RedisException;

ByteArrayMultipartFile getAttachObjFromRedis(String key) throws RedisException;

}