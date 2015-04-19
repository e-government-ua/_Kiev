package org.activiti.redis.service.impl;

import java.io.IOException;

import org.activiti.redis.client.RedisClient;
import org.activiti.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Запись/вычитка redis.
 * 
 * @author inna
 * 
 */
@Service("redisService")
public class RedisServiceImpl implements RedisService {

	private static final transient Logger LOG = LoggerFactory
			.getLogger(RedisServiceImpl.class);

	@Autowired
	private RedisClient jedisClient;

	@Override
	public String putAttachments(byte[]file) throws IOException {
		String key = null;
		try {
			key = getJedisClient().putAttachments(file);

		} catch (Exception e) {
			LOG.error("Error Log: " + e.getMessage());
		}
		return key;
	}

	@Override
	public byte[] getAttachments(String key) throws IOException {
		byte[] byteFile= null;
		try {
			byteFile = getJedisClient().getAttachments(key);

		} catch (Exception e) {
			
			LOG.error("Error Log: " + e.getMessage());
		}
		return byteFile;
	}

	
	public RedisClient getJedisClient() {
		return jedisClient;
	}
}
