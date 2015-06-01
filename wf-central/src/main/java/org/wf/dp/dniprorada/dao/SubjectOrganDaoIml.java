package org.wf.dp.dniprorada.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.model.Subject;
import org.wf.dp.dniprorada.model.SubjectOrgan;

public class SubjectOrganDaoIml implements SubjectOrganDao{
	
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
	public SubjectOrgan getSubjectOrgan(String sOKPO) {
		SubjectOrgan subjectOrgan = null;
		List<SubjectOrgan> subjects = (List<SubjectOrgan>)getSession().createCriteria(SubjectOrgan.class).add(Restrictions.eq("sOKPO", sOKPO)).list();
		if(subjects != null && !subjects.isEmpty()){
			subjectOrgan = subjects.get(0);
		}
		return subjectOrgan;
	}

	@Override
	public SubjectOrgan setSubjectOrgan(String sOKPO) {
		SubjectOrgan oSubjectOrgan = new SubjectOrgan();
		oSubjectOrgan.setsOKPO(sOKPO);
		Subject oSubject = new Subject();
		oSubjectOrgan.setoSubject(oSubject);
		return saveOrUpdateSubjectOrgan(oSubjectOrgan);
	}

	@Override
	public SubjectOrgan saveOrUpdateSubjectOrgan(SubjectOrgan oSubjectOrgan) {
		oSubjectOrgan.getoSubject().setsID(oSubjectOrgan.getsOKPO());
		getSession().saveOrUpdate(oSubjectOrgan);
		return oSubjectOrgan;
	}

}
