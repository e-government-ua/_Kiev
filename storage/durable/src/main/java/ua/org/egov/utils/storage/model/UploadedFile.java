package ua.org.egov.utils.storage.model;


/**
 * 
 * @author vbolshutkin
 *
 * This class resembles Spring's MultipartFile, however 
 * is not logically limited by the usecase of temporary 
 * usage. 
 * 
 * This class represents a stored uploaded file. It contains 
 * some fields of MultipartFile necessary for successful
 * downloading
 */
public class UploadedFile {

	private byte[] content;
    private UploadedFileMetadata metadata;
    
	public UploadedFile(byte[] content, UploadedFileMetadata metadata) {
		super();
		this.content = content;
		this.metadata = metadata;
	}

	public byte[] getContent() {
		return content;
	}

	public UploadedFileMetadata getMetadata() {
		return metadata;
	}
    
}

