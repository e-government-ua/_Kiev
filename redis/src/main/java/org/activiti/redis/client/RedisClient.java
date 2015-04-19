package org.activiti.redis.client;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    
    @Override
    public String putAttachments(byte[] file) throws IOException, Exception {
        String key = UUID.randomUUID().toString();
        while (template.hasKey(key)) {
            key = UUID.randomUUID().toString();
        }
    	template.boundValueOps(key).set(file);
        return key;
    }

	@Override
	public byte[] getAttachments(String key) throws IOException {
		byte[] boundValue = template.boundValueOps(key).get();
		return boundValue;
	}
	
	
}
