package org.activiti.redis.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.activiti.redis.model.ByteArrayMultipartFile;
import org.activiti.redis.service.RedisService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"/META-INF/spring/org-activiti-redis-context.xml",
		"/META-INF/spring/redis-test.xml" })
public class RedisTest {

	static final transient Logger LOG = LoggerFactory
			.getLogger(RedisTest.class);

	@Autowired
	RedisService redisService;

	@Value("#{testProps['loadFile']}")
	private String loadFile;

	@Value("#{testProps['pathToFile']}")
	private String pathToFile;

	@Ignore
	@Test
	public void testRedis() throws IOException {
		byte[] data = loadfile(loadFile);
		File newFile = new File(loadFile);
		MultipartFile byteArrayMultipartFile = new ByteArrayMultipartFile(
				data, newFile.getName(), newFile.getName(), "text/plain");
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
		oos.writeObject(byteArrayMultipartFile);
		oos.flush();
		oos.close();
		String key = redisService.putAttachments(byteArrayOutputStream
				.toByteArray());
		System.out.println(key);
		byte[] byteFile = redisService.getAttachments(key);
		ByteArrayMultipartFile contentMultipartFile = null;
		try {
			contentMultipartFile = getByteArrayMultipartFileFromRedis(byteFile);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(contentMultipartFile);
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
	
	public static ByteArrayMultipartFile getByteArrayMultipartFileFromRedis(
			byte[] byteFile) throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteFile);
		ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);
		  ByteArrayMultipartFile contentMultipartFile = (ByteArrayMultipartFile) ois.readObject();
		ois.close();
		return contentMultipartFile;
	}

}