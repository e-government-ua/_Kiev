package org.wf.dp.dniprorada.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.DocumentContentType;
import org.wf.dp.dniprorada.model.DocumentType;
import org.wf.dp.dniprorada.util.Util;

import ua.org.egov.utils.storage.durable.impl.GridFSBytesDataStorage;

public class DocumentDaoImpl implements DocumentDao {

	private SessionFactory sessionFactory;
	
	private String mokeContentDocument = "123456789";
	
	@Autowired
	private GridFSBytesDataStorage durableBytesDataStorage;
	

	@Required
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<Document> getDocuments(String subject_Upload) {
		return (List<Document>)getSession().createCriteria(Document.class).add(Restrictions.eq("subject_Upload", subject_Upload)).list();
	}

	@Override
	public Document getDocument(Long id) {
		return (Document) getSession().get(Document.class, id);
	}

	@Override
	public byte[] getDocumentContent(Long id) {
		Document document = (Document) getSession().get(Document.class, id);
		return durableBytesDataStorage.getData(document.getСontentKey());
		//return Util.contentStringToByte(mokeContentDocument);
	}

	@Override
	public byte[] getDocumentContent(String contentKey) {
		return durableBytesDataStorage.getData(contentKey);
		//return Util.contentStringToByte(mokeContentDocument);
	}

	@Override
	//public Long setDocument(String subject_Upload, String subjectName_Upload,
	//		String name, String file, Integer documentTypeId,
	//		Integer documentContentTypeId, byte[] content) {
	//public Long setDocument(String sID_Subject_Upload, String sSubjectName_Upload,
	//		String sName, Integer nID_DocumentType,
	//		Integer nID_DocumentContentType, MultipartFile oFile) throws IOException {
	public Long setDocument(String sID_Subject_Upload, String sSubjectName_Upload,
			String sName, Integer nID_DocumentType,
			Integer nID_DocumentContentType, String sFileName, String sFileContentType, byte[] aoContent) throws IOException {
            
		Document document = new Document();
		document.setSubject_Upload(sID_Subject_Upload);
		document.setSubjectName_Upload(sSubjectName_Upload);
		document.setName(sName);
		DocumentType oDocumentType = new DocumentType();
		oDocumentType.setId(nID_DocumentType);
		document.setDocumentType(oDocumentType);
		DocumentContentType documentContentType = new DocumentContentType();
		documentContentType.setId(nID_DocumentContentType==null?2:nID_DocumentContentType);//TODO определять/генерить реальный ИД, по Контенттайп с oFile
		document.setDocumentContentType(documentContentType);
		document.setСontentKey(durableBytesDataStorage.saveData(aoContent));
		//document.setСontentKey(durableBytesDataStorage.saveData(content));
		//document.setСontentKey(new GridFSBytesDataStorage().saveData(content));
		//document.setСontentKey("ff");
		document.setContentType(sFileContentType);
		document.setFile(sFileName);
		//document.setFile(file);
		document.setDate_Upload(new Date());
		getSession().saveOrUpdate(document);
		return document.getId();

	}
}
