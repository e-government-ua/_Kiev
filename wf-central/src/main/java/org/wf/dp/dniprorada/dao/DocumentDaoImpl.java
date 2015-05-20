package org.wf.dp.dniprorada.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.DocumentContentType;
import org.wf.dp.dniprorada.model.DocumentType;

import ua.org.egov.utils.storage.durable.impl.GridFSBytesDataStorage;

public class DocumentDaoImpl implements DocumentDao {

	private SessionFactory sessionFactory;
	
	//@Autowired
	private GridFSBytesDataStorage gridFSBytesDataStorage;
	

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
		return gridFSBytesDataStorage.getData(document.getСontentKey());
	}

	@Override
	public byte[] getDocumentContent(String contentKey) {
		return gridFSBytesDataStorage.getData(contentKey);
	}

	@Override
	public Long setDocument(String subject_Upload, String subjectName_Upload,
			String name, String file, Integer documentTypeId,
			Integer documentContentTypeId, byte[] content) {
		Document document = new Document();
		document.setSubject_Upload(subject_Upload);
		document.setSubjectName_Upload(subjectName_Upload);
		document.setName(name);
		DocumentType documentType = new DocumentType();
		documentType.setId(documentTypeId);
		document.setDocumentType(documentType);
		DocumentContentType documentContentType = new DocumentContentType();
		documentContentType.setId(documentContentTypeId);
		document.setDocumentContentType(documentContentType);
		//document.setСontentKey(gridFSBytesDataStorage.saveData(content));
		document.setСontentKey("ff");
		document.setFile(file);
		document.setDate_Upload(new Date());
		getSession().saveOrUpdate(document);
		return document.getId();

	}
}
