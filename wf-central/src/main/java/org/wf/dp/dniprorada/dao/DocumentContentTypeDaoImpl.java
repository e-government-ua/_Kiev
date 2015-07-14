package org.wf.dp.dniprorada.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.model.DocumentContentType;
import org.wf.dp.dniprorada.model.EntityNotFoundException;

import java.util.List;


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

    @SuppressWarnings("unchecked")
    @Override
    public List<DocumentContentType> getDocumentContentTypes() {
        return (List<DocumentContentType>) getSession()
                .createCriteria(DocumentContentType.class).list();
    }

    @Override
    public DocumentContentType setDocumentContentType(Long nID, String sName) {
        DocumentContentType type = getDocumentContentType(nID);
        if (type == null){
            type = new DocumentContentType();
        }
        type.setName(sName);
        getSession().saveOrUpdate(type);
        return type;
    }

    @Override
    public DocumentContentType getDocumentContentType(Long nID) {
        Criteria criteria = getSession().createCriteria(DocumentContentType.class);
        criteria.add(Restrictions.eq("id", nID));
        return (DocumentContentType) criteria.uniqueResult();
    }

    @Override
    public void removeDocumentContentType(Long nID) {
        DocumentContentType type = getDocumentContentType(nID);
        if (type == null)
            throw new EntityNotFoundException("Record not found");
        getSession().delete(type);
    }

}