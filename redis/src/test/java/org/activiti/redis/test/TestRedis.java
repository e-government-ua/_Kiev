package org.activiti.redis.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.activiti.redis.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/spring/org-activiti-redis-context.xml"})
public class TestRedis {

	static final transient Logger LOG = LoggerFactory
			.getLogger(TestRedis.class);
	
	@Autowired
	RedisService redisService;
	
	@Test
    public void testRedis() throws IOException {
		byte[] data = loadfile("D:/db.properties");
		redisService.write("File1", data);
		LOG.info("redisService read "+redisService.read("File1"));
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("D:/em1.properties");
			fos.write(redisService.read("File1"));
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	
	
	public byte[] loadfile(String pathDirFile) {
		Path path = Paths.get(pathDirFile);
		byte[] data = null;
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	

}