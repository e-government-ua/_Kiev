package ua.org.egov.utils.storage.model;

import java.util.Date;



public class UploadedFileMetadata {

    private String contentType;
    private String originalFilename;
    private Date lastUpdated;
    
	public UploadedFileMetadata(String contentType, String originalFilename,
			Date lastUpdated) {
		super();
		this.contentType = contentType;
		this.originalFilename = originalFilename;
		this.lastUpdated = lastUpdated;
	}

	public String getContentType() {
		return contentType;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

}

