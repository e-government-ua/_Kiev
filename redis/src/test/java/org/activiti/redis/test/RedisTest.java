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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/spring/org-activiti-redis-context.xml","/META-INF/spring/redis-test.xml"})
public class RedisTest {

	static final transient Logger LOG = LoggerFactory
			.getLogger(RedisTest.class);
	
	@Autowired
	RedisService redisService;
	
	@Value("#{testProps['loadFile']}")
	private String loadFile;
	
	@Value("#{testProps['pathToFile']}")
	private String pathToFile;
	
	@Test
    public void testRedis() throws IOException {
		byte[] data = loadfile(loadFile);
		String key = redisService.putAttachments(data);
		System.out.println(key);
		byte[] dataFile  = redisService.getAttachments(key);
		System.out.println(dataFile);
		if(dataFile!=null){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(pathToFile);
			fos.write(dataFile);
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
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