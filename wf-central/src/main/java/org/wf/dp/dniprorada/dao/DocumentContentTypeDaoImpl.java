package org.wf.dp.dniprorada.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.DocumentContentType;

import ua.org.egov.utils.storage.durable.impl.GridFSBytesDataStorage;

public class DocumentContentTypeDaoImpl implements DocumentContentTypeDao {

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
	//@Transactional(readOnly=false) 
	public DocumentContentType getDocumentContentType(String name) {
		DocumentContentType documentContentType;
		List<DocumentContentType> documentsContentType = (List<DocumentContentType>) getSession()
				.createCriteria(DocumentContentType.class)
				.add(Restrictions.eq("name", name)).list();
		if (documentsContentType == null || documentsContentType.isEmpty()) {
			documentContentType = new DocumentContentType();
			documentContentType.setName(name);
			documentContentType.setId(setDocumentContent(documentContentType));
		} else{
			documentContentType = documentsContentType.get(0);
		}
		return documentContentType;
	}

	@Override
	//@Transactional(readOnly=false) 
	public Integer setDocumentContent(DocumentContentType documentContentType) {
		getSession().save(documentContentType);
		return documentContentType.getId();
	}

}
