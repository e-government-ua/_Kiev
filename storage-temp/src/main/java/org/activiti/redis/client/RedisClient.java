package org.activiti.redis.client;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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
    private RedisTemplate<String, byte[]> template;
    
    @Value("${redis.storageTimeMinutes}")
	private String storageTimeKey;

    
    @Override
    public String putAttachments(byte[] file) throws Exception {
        String key = UUID.randomUUID().toString();
        while (template.hasKey(key)) {
            key = UUID.randomUUID().toString();
        }
    	template.boundValueOps(key).set(file);
    	template.expire(key, Long.valueOf(storageTimeKey), TimeUnit.MINUTES);
        return key;
    }

	@Override
	public byte[] getAttachments(String key) throws Exception {
		return template.boundValueOps(key).get();
	}
	
	
}
