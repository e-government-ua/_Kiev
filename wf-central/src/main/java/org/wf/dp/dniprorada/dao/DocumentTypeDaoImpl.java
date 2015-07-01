/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wf.dp.dniprorada.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.model.DocumentType;

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
}