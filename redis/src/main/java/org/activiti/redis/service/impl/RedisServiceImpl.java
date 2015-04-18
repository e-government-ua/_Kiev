package org.activiti.redis.service.impl;

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
	public void write(String key, byte[]value) {
		try {
			LOG.debug("Log message:{} ", value);
			getJedisClient().write(key, value);

		} catch (Exception e) {
			LOG.error("Error Log: " + e.getMessage());
		}

	}

	@Override
	public byte[] read(String valueByKey) {
		byte[] mess = null;
		try {
			LOG.debug("Log message:{} ", valueByKey);
			mess = getJedisClient().read(valueByKey);

		} catch (Exception e) {
			LOG.error("Error Log: " + e.getMessage());
		}
		return mess;
	}

	public RedisClient getJedisClient() {
		return jedisClient;
	}
}
