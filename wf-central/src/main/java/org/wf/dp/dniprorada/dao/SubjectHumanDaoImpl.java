package org.wf.dp.dniprorada.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.model.Subject;
import org.wf.dp.dniprorada.model.SubjectHuman;

public class SubjectHumanDaoImpl implements SubjectHumanDao{
	
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
	public SubjectHuman getSubjectHuman(String sINN) {
		SubjectHuman subjectHuman = null;
		List<SubjectHuman> subjects = (List<SubjectHuman>)getSession().createCriteria(SubjectHuman.class)
				.add(Restrictions.eq("sINN", sINN)).list();
		if(subjects != null && !subjects.isEmpty()){
			subjectHuman = subjects.get(0);
		}
		return subjectHuman;
	}

	@Override
	public SubjectHuman setSubjectHuman(String sINN) {
		SubjectHuman oSubjectHuman = new SubjectHuman();
		oSubjectHuman.setsINN(sINN);
		Subject oSubject = new Subject();
		oSubjectHuman.setoSubject(oSubject);
		return saveOrUpdateHuman(oSubjectHuman);
	}

	@Override
	public SubjectHuman saveOrUpdateHuman(SubjectHuman oSubjectHuman) {
		oSubjectHuman.getoSubject().setsID(oSubjectHuman.getsINN());
		getSession().saveOrUpdate(oSubjectHuman);
		return oSubjectHuman;
	}

}
