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

    @SuppressWarnings("unchecked")
    @Override
    public List<DocumentType> getDocumentTypes() {
        Criteria cr =  getSession().createCriteria(DocumentType.class);
        cr.add(Restrictions.eq("bHidden", false));
        return (List<DocumentType>)cr.list();
    }

    @Override
    public DocumentType setDocumentType(Long nID, String sName, Boolean bHidden) {
        DocumentType documentType = getDocumentType(nID);
        if (documentType == null){
            documentType = new DocumentType();
        }
        documentType.setName(sName);
        documentType.setbHidden(bHidden == null ? false : bHidden);
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