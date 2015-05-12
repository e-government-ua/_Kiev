package ua.org.egov.utils.storage.durable.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;

import ua.org.egov.utils.storage.FileStorage;
import ua.org.egov.utils.storage.exceptions.RecordNotFoundException;
import ua.org.egov.utils.storage.model.UploadedFile;
import ua.org.egov.utils.storage.model.UploadedFileMetadata;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

public class GridFSFileStorage implements FileStorage {

	@Autowired
	private GridFsTemplate gfsTemplate;

	private static String getExtension(MultipartFile file) {
		return FilenameUtils.getExtension(file.getOriginalFilename());
	}
	
	@Override
	public String createFile(MultipartFile file) {
		String key = UUID.randomUUID().toString() + getExtension(file);
		if (saveFile(key, file)) {
			return key;
		}
		return null;
	}

	@Override
	public boolean saveFile(String key, MultipartFile file) {
		GridFSFile targetFile;
		try {
			targetFile = gfsTemplate.store(file.getInputStream(), key);
			targetFile.put("contentType", file.getContentType());
			targetFile.put("originalName", file.getOriginalFilename());
			targetFile.save();
			return true;
		} catch (IOException e) {
			return false;
		}
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
	public boolean keyExists(String key) {
		return findLatestEdition(key) != null;
	}

	private UploadedFileMetadata getFileMetadataInternal(GridFSFile gfsFile) {
		if (gfsFile == null) {
			return null;
		}
		
		Object origName = gfsFile.get("originalName");
		return new UploadedFileMetadata(gfsFile.getContentType(),
				origName == null ? null : origName.toString(),
				gfsFile.getUploadDate());
	}
	
	@Override
	public UploadedFile getFile(String key) {
		GridFSDBFile gfsFile = findLatestEdition(key);
		
		try (InputStream is = gfsFile.getInputStream()) {
			UploadedFileMetadata metadata = getFileMetadataInternal(gfsFile);
			byte[] data = IOUtils.toByteArray(is);
			return new UploadedFile(data, metadata);
		} catch (NullPointerException | IOException e) {
			return null;
		}
	}

	@Override
	public UploadedFileMetadata getFileMetadata(String key) {
		return getFileMetadataInternal(findLatestEdition(key));
	}

	@Override
	public InputStream openFileStream(String key)
			throws RecordNotFoundException {
		GridFSDBFile gfsFile = findLatestEdition(key);
		if (gfsFile == null) {
			throw new RecordNotFoundException(String.format("Value for key '%s' not found", key));
		}
		return gfsFile.getInputStream();
	}
	
}
