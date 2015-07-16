package ua.org.egov.utils.storage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import ua.org.egov.utils.storage.exceptions.RecordNotFoundException;
import ua.org.egov.utils.storage.model.UploadedFile;
 
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"/META-INF/spring/ua-org-egov-utils-storage-context.xml"})
public class FileStorageTest {

	@Autowired
	private FileStorage storage;

	
	@Test(expected=RecordNotFoundException.class)
	public void openStreamShouldThrowOnInexistentKey() throws RecordNotFoundException {
		storage.openFileStream("inexistent_key");
	}
	
	@Test
	public void getFileShouldReturnNullOnInexistentKey()  {
		Assert.assertNull(storage.getFile("inexistent_key"));
	}
	
	@Test
	public void getMetadataShouldReturnNullOnInexistentKey()  {
		Assert.assertNull(storage.getFileMetadata("inexistent_key"));
	}
	
	@Test
	public void shouldSaveReadDeleteFile() throws IOException {
		MultipartFile mf1 = new MockMultipartFile("test", "testOriginalFilename1", "text/plain", "test content first file".getBytes());
		MultipartFile mf2 = new MockMultipartFile("test", "testOriginalFilename1", "text/plain", "test,content,second,file".getBytes());
		
		String id = storage.createFile(mf1);
		UploadedFile readFile1 = storage.getFile(id);
		Assert.assertNotNull(readFile1);
		Assert.assertArrayEquals(mf1.getBytes(), readFile1.getContent());
		Assert.assertEquals(mf1.getContentType(), readFile1.getMetadata().getContentType());
		Assert.assertEquals(mf1.getOriginalFilename(), readFile1.getMetadata().getOriginalFilename());
                
		Assert.assertTrue(storage.saveFile(id, mf2));
		UploadedFile readFile2 = storage.getFile(id);
		Assert.assertArrayEquals(mf2.getBytes(), readFile2.getContent());
		Assert.assertEquals(mf2.getContentType(), readFile2.getMetadata().getContentType());
		Assert.assertEquals(mf2.getOriginalFilename(), readFile2.getMetadata().getOriginalFilename());
                
		
		Assert.assertTrue(storage.remove(id));
		Assert.assertFalse(storage.keyExists(id));
	}
	
	public static byte[] loadfile(String pathDirFile) {
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
