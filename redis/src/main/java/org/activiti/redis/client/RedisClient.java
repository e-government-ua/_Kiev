package org.activiti.redis.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * @author inna
 * 
 */
@Component
public class RedisClient implements RedisOperations {
	static final transient Logger LOG = LoggerFactory
			.getLogger(RedisClient.class);


	@Autowired
	private JedisPool jedisPool;

	@Override
	public void write(final String key, final byte[] message) {
		Jedis jedis = null;
		try {
			jedis = getJedisPool().getResource();
			if (!jedis.isConnected()) {
				jedis.connect();
			}
			jedis.set(key.getBytes(), message);
		} catch (Exception e) {
			LOG.error("Write to jedis e.class:{}, e.getMessage: {}",
					e.getCause(), e.getMessage());
			getJedisPool().returnBrokenResource(jedis);
		} finally {
			getJedisPool().returnResource(jedis);
		}
	}


	@Override
	public byte[] read(String valueByKey) {
		Jedis jedis = null;
		byte[] data = null;
		try {
			jedis = getJedisPool().getResource();
			if (!jedis.isConnected()) {
				jedis.connect();
			}
			data = jedis.get(valueByKey.getBytes());
		} catch (Exception e) {
			LOG.error("Write to jedis e.class:{}, e.getMessage: {}",
					e.getCause(), e.getMessage());
			getJedisPool().returnBrokenResource(jedis);
		} finally {
			getJedisPool().returnResource(jedis);
		}
		return data;
		
	}
	
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	
}
