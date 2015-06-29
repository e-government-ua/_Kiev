package org.wf.dp.dniprorada.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.model.*;
import ua.org.egov.utils.storage.durable.impl.GridFSBytesDataStorage;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class DocumentDaoImpl implements DocumentDao {

	private static final String contentMock = "No content!!!";

	private SessionFactory sessionFactory;

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
	public List<Document> getDocuments(Long nID_Subject) {
		return (List<Document>) getSession().createCriteria(Document.class)
				.add(Restrictions.eq("subject.nID", nID_Subject))
				.list();
	}

	@Override
	public Document getDocument(Long id) {
		return (Document) getSession().get(Document.class, id);
	}

	@Override
	public byte[] getDocumentContent(Long id) {
		Document document = (Document) getSession().get(Document.class, id);
		byte[] contentByte = durableBytesDataStorage.getData(document.getContentKey());
		return contentByte != null ? contentByte : contentMock.getBytes();
	}

	@Override
	public byte[] getDocumentContent(String contentKey) {
		byte[] contentByte = durableBytesDataStorage.getData(contentKey);
		return contentByte != null ? contentByte : contentMock.getBytes();
	}

	public Long setDocument(Long nID_Subject, Long nID_Subject_Upload, String sID_Subject_Upload,
			String sSubjectName_Upload, String sName, Integer nID_DocumentType,
			Integer nID_DocumentContentType, String sFileName,
			String sFileContentType, byte[] aoContent) throws IOException {

		Document document = new Document();
		document.setsID_subject_Upload(sID_Subject_Upload);
		document.setSubjectName_Upload(sSubjectName_Upload);
		document.setName(sName);
		
		Subject oSubject_Upload = new Subject();
		oSubject_Upload.setnID(nID_Subject_Upload);
		document.setSubject_Upload(oSubject_Upload);

		DocumentType oDocumentType = new DocumentType();
		oDocumentType.setId(nID_DocumentType);
		document.setDocumentType(oDocumentType);

		DocumentContentType documentContentType = new DocumentContentType();
		documentContentType.setId(nID_DocumentContentType == null ? 2
				: nID_DocumentContentType);// TODO определять/генерить реальный
											// ИД, по Контенттайп с oFile
		document.setDocumentContentType(documentContentType);

		if (nID_Subject != null) {
			Subject oSubject = new Subject();
			oSubject.setnID(nID_Subject);
			document.setSubject(oSubject);
		}

		document.setСontentKey(durableBytesDataStorage.saveData(aoContent));
		document.setContentType(sFileContentType);
		document.setFile(sFileName);
		document.setDate_Upload(new Date());
		getSession().saveOrUpdate(document);
		return document.getId();

	}


	@Override
	public DocumentOperator_SubjectOrgan getOperator(Long operatorId) {
		return (DocumentOperator_SubjectOrgan) getSession()
				.createCriteria(DocumentOperator_SubjectOrgan.class)
				.add(Restrictions.eq("nID_SubjectOrgan", operatorId))
				.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DocumentOperator_SubjectOrgan> getAllOperators() {
		return (List<DocumentOperator_SubjectOrgan>) getSession()
				.createCriteria(DocumentOperator_SubjectOrgan.class)
				.list();
	}
}
