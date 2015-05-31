package ua.org.egov.utils.storage.durable.impl;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import ua.org.egov.utils.storage.BytesDataStorage;
import ua.org.egov.utils.storage.exceptions.RecordNotFoundException;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
/*import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;*/

public class GridFSBytesDataStorage implements BytesDataStorage {

	@Autowired
	private GridFsTemplate gfsTemplate;
	
        /*private mongoTemplate
        
        String mongoUser="";
        String databaseName="";
        String mongoPass="";
        String mongoHost="";
        Integer mongoPort=0*/
        /**
        * DB connection Factory
        * 
        * @return a ready to use MongoDbFactory
        */
       /*@Bean
       public MongoDbFactory mongoDbFactory() throws Exception {

           // Set credentials      
           MongoCredential credential = MongoCredential.createCredential(mongoUser, databaseName, mongoPass.toCharArray());
           ServerAddress serverAddress = new ServerAddress(mongoHost, mongoPort);

           // Mongo Client
           MongoClient mongoClient = new MongoClient(serverAddress,Arrays.asList(credential)); 

           // Mongo DB Factory
           SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(
                   mongoClient, databaseName);

           return simpleMongoDbFactory;
       }*/

       /**
        * Template ready to use to operate on the database
        * 
        * @return Mongo Template ready to use
        */
       /*@Bean
       public MongoTemplate mongoTemplate() throws Exception {
           return new MongoTemplate(mongoDbFactory());
       }*/       
        
        
	@Override
	public String saveData(byte[] data) {
		
		String key = UUID.randomUUID().toString();
		if (setData(key, data)) {
			return key;
		}
		return null;
	}

	@Override
	public boolean setData(String key, byte[] data) {
		try (InputStream is = new ByteArrayInputStream(data)) {
			GridFSFile targetFile = gfsTemplate.store(is, key);
			targetFile.save();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	private static Query getKeyQuery(String key) {
		return new Query(GridFsCriteria.whereFilename().is(key));
	}
	
	private GridFSDBFile findLatestEdition(String key) {
		List<GridFSDBFile> fileList = gfsTemplate.find(
			getKeyQuery(key)
				.with(new Sort(Direction.DESC, "uploadDate"))
				.limit(1));
		if (fileList == null || fileList.isEmpty()) {
			return null;
		}
		return fileList.get(0);
	}

	@Override
	public boolean remove(String key) {
		gfsTemplate.delete(getKeyQuery(key));
		return true;
	}

	@Override
	public byte[] getData(String key) {
		
		GridFSDBFile foundFile = findLatestEdition(key);
		try (InputStream is = foundFile.getInputStream()) {
			return IOUtils.toByteArray(is);
		} catch (NullPointerException | IOException e) {
			return null;
		} 
	}

	@Override
	public InputStream openDataStream(String key) throws RecordNotFoundException {
		GridFSDBFile foundFile = findLatestEdition(key);
		if (foundFile == null) {
			throw new RecordNotFoundException(String.format("Value for key '%s' not found", key));
		}
		return foundFile.getInputStream();
	}

	@Override
	public boolean keyExists(String key) {
		GridFSDBFile foundFile = findLatestEdition(key);
		return foundFile != null;
	}
	
}
