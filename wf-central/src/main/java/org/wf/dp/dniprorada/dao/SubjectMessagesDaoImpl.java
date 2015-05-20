package org.wf.dp.dniprorada.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.model.SubjectMessage;

import java.util.List;

public class SubjectMessagesDaoImpl implements SubjectMessagesDao {

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
    public void setMessage(SubjectMessage message) {
        getSession().saveOrUpdate(message);
    }

    @Override
    public List<SubjectMessage> getMessages() {
        return getSession().createCriteria(SubjectMessage.class).list();
    }

    @Override
    public SubjectMessage getMessage(Long nID) {
        SubjectMessage message = (SubjectMessage) getSession().get(SubjectMessage.class, nID);
        return message;
    }
}
