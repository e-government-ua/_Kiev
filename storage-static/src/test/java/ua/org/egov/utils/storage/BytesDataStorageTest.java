package ua.org.egov.utils.storage;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ua.org.egov.utils.storage.durable.impl.GridFSFileStorage;
import ua.org.egov.utils.storage.exceptions.RecordNotFoundException;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"/META-INF/spring/ua-org-egov-utils-storage-context.xml"})
public class BytesDataStorageTest {

	@Autowired
	private BytesDataStorage storage;

	@Test(expected=RecordNotFoundException.class)
	public void openStreamShouldThrowOnInexistentKey() throws RecordNotFoundException {
		storage.openDataStream("inexistent_key");
	}
        
        @Test
	public void getDataShouldReturnNullOnInexistentKey()  {
		Assert.assertNull(storage.getData("inexistent_key"));
	}

	@Test
	public void shouldSaveReadDeleteRecord() {
		byte[] data1 = new byte[] {1, 2, 3};
		byte[] data2 = new byte[] {4, 5, 6, 7};
		
		String id = storage.saveData(data1);
		Assert.assertTrue(storage.keyExists(id));
		byte[] readData1 = storage.getData(id);
		Assert.assertArrayEquals(data1, readData1);
		Assert.assertTrue(storage.setData(id, data2));
		byte[] readData2 = storage.getData(id);
		Assert.assertArrayEquals(data2, readData2);
		Assert.assertTrue(storage.remove(id));
		Assert.assertFalse(storage.keyExists(id));
	}
	
}