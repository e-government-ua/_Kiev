package org.wf.dp.dniprorada.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.model.DocumentContentType;


public class DocumentContentTypeDaoImpl implements DocumentContentTypeDao {

	private SessionFactory sessionFactory;

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
	public DocumentContentType getDocumentContentType(String name) {
		DocumentContentType documentContentType = null;
		List<DocumentContentType> documentsContentType = (List<DocumentContentType>) getSession()
				.createCriteria(DocumentContentType.class)
				.add(Restrictions.eq("name", name)).list();
		if (documentsContentType != null && !documentsContentType.isEmpty()) {
			documentContentType = documentsContentType.get(0);
		}
		return documentContentType;
	}

	@Override
	public Long setDocumentContent(DocumentContentType documentContentType) {
		getSession().save(documentContentType);
		return documentContentType.getId();
	}

}