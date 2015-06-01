package org.activiti.redis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Настройки Редис.
 * @author inna
 *
 */
@Configuration
public class RedisConfiguration {
	static final transient Logger LOG = LoggerFactory
			.getLogger(RedisConfiguration.class);

	/*@Bean
	public redis.clients.jedis.JedisPoolConfig jedisConfigKibana(
			final @Value("#{redisProps['redis.maxActive']}") Integer maxActive,
			final @Value("#{redisProps['redis.maxIdle']}") Integer maxIdle,
			final @Value("#{redisProps['redis.minIdle']}") Integer minIdle,
			final @Value("#{redisProps['redis.testOnBorrow']}") Boolean testOnBorrow,
			final @Value("#{redisProps['redis.testOnReturn']}") Boolean testOnReturn,
			final @Value("#{redisProps['redis.testWhileIdle']}") Boolean testWhileIdle,
			final @Value("#{redisProps['redis.numTestsPerEvictionRun']}") Integer numTestsPerEvictionRun,
			final @Value("#{redisProps['redis.timeBetweenEvictionRunsMillis']}") Integer timeBetweenEvictionRunsMillis,
			final @Value("#{redisProps['redis.maxWait']}") Integer maxWaitMillis) {
		redis.clients.jedis.JedisPoolConfig jpc = new JedisPoolConfig();
		jpc.setMaxTotal(maxActive);
		jpc.setMaxIdle(maxIdle);
		jpc.setMinIdle(minIdle);
		jpc.setTestOnBorrow(testOnBorrow);
		jpc.setTestOnReturn(testOnReturn);
		jpc.setTestWhileIdle(testWhileIdle);
		jpc.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
		jpc.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		jpc.setMaxWaitMillis(maxWaitMillis);
		return jpc;
	}

	@Bean
	public redis.clients.jedis.JedisPool jedisPoolKibana(
			final redis.clients.jedis.JedisPoolConfig poolConfig,
			final @Value("#{redisProps['redis.host']}") String host,
			final @Value("#{redisProps['redis.port']}") Integer port) {
		LOG.info("jedisPoolKibana init. host:{}, port:{}",host, port);
		if(null == host){
			throw new IllegalArgumentException("host can not be null");
		}
		return new JedisPool(poolConfig, host, port);
	}*/
	

}
