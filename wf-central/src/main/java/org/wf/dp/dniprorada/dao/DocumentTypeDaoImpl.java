/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wf.dp.dniprorada.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.model.DocumentType;
import org.wf.dp.dniprorada.model.EntityNotFoundException;

import java.util.List;

/**
 *
 * @author olya
 */
public class DocumentTypeDaoImpl implements DocumentTypeDao {

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

    public List<DocumentType> getDocumentTypes() {
        return (List<DocumentType>) getSession()
                .createCriteria(DocumentType.class).list();
    }

    @Override
    public DocumentType setDocumentType(Long nID, String sName) {
        DocumentType documentType = getDocumentType(nID);
        if (documentType == null){
            documentType = new DocumentType();
        }
        documentType.setName(sName);
        getSession().saveOrUpdate(documentType);
        return documentType;
    }

    @Override
    public void removeDocumentType(Long nID) {
        DocumentType documentType = getDocumentType(nID);
        if (documentType == null)
            throw new EntityNotFoundException("Record not found");
        getSession().delete(documentType);
    }

    @Override
    public DocumentType getDocumentType(Long nID) {
        Criteria criteria = getSession().createCriteria(DocumentType.class);
        criteria.add(Restrictions.eq("id", nID));
        return (DocumentType) criteria.uniqueResult();
    }
}