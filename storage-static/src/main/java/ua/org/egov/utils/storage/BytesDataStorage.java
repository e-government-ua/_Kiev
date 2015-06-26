package ua.org.egov.utils.storage;

import java.io.InputStream;

import ua.org.egov.utils.storage.exceptions.RecordNotFoundException;

/**
 * @author vbolshutkin & BW
 */
public interface BytesDataStorage {
	
	/**
	 * Saves data to the storage. Generates a random key.
	 * 
	 * @param data as byte array 
	 * @return key as String. <b>null</b> if saving failed. 
	 */
	public String saveData(byte[] data);
	
	/**
	 * Saves data to the storage with the predefined key. 
	 * Overwrites previously saved data with the same key.
	 * 
	 * @param key String
	 * @param data byte array 
	 * @return boolean, whether the data was successfully saved 
	 */
	public boolean setData(String key, byte[] data);
	
	/**
	 * Removes data from the storage by given key.
	 * 
	 * @param key String
	 * @return <b>true</b> if the data 
	 * 	was removed or not found, <b>false</b> otherwise . 
	 */
	public boolean remove(String key);
	
	
	/**
	 * Determines whether there exists data with the provided key.
	 * It is NOT GUARANTEED that consequent getData or openDataStream
	 *  will return the data, as the data might be removed from a different thread. 
	 * 
	 * @param key String
	 * @return boolean whether there exists data with the provided key. 
	 */
	public boolean keyExists(String key);
	
	/**
	 * Queries data byte array by the key. 
	 * 
	 * @param key String
	 * @return data as byte array. <b>null</b> if not found.
	 */
	public byte[] getData(String key);
	
	/**
	 * Queries data by the key and opens a stream. Ideal 
	 * for streaming large data. Recommended to use with
	 * try-with-resources syntax.
	 * 
	 * @param key String
	 * @return data as InputStream.
	 */
	public InputStream openDataStream(String key) throws RecordNotFoundException;
}
