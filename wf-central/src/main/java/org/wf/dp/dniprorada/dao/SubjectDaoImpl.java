package org.wf.dp.dniprorada.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.model.Subject;

public class SubjectDaoImpl implements SubjectDao {

    private final Logger log = LoggerFactory.getLogger(SubjectDaoImpl.class);
    
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
	public Subject getSubject(Long nID) {
		return (Subject) getSession().get(Subject.class, nID);
	}
	
	@Override
	public Subject getSubject(String sID) {
		Subject subject = null;
		List<Subject> subjects = (List<Subject>)getSession().createCriteria(Subject.class).add(Restrictions.eq("sID", sID)).list();
		if(subjects != null && !subjects.isEmpty()){
			subject = subjects.get(0);
		}
		return subject;
	}

	@Override
	public Subject saveOrUpdateSubject(Subject subject) {
		getSession().saveOrUpdate(subject);
		return subject;
	}

}