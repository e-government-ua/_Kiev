package org.wf.dp.dniprorada.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.wf.dp.dniprorada.base.dao.BaseEntityDao;
import org.wf.dp.dniprorada.model.Subject;
import org.wf.dp.dniprorada.model.SubjectOrgan;
import org.wf.dp.dniprorada.model.SubjectOrganJoin;

import java.util.List;

public class SubjectOrganDaoIml implements SubjectOrganDao {

	private SessionFactory sessionFactory;

	@Autowired
	private BaseEntityDao baseEntityDao;

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

    @Override
    public SubjectOrgan getSubjectOrgan(Long nID) {
        SubjectOrgan subjectOrgan = null;
        List<SubjectOrgan> subjects = (List<SubjectOrgan>)getSession().createCriteria(SubjectOrgan.class).add(Restrictions.eq("nID", nID)).list();
        if(subjects != null && !subjects.isEmpty()){
            subjectOrgan = subjects.get(0);
        }
        return subjectOrgan;
    }

	@SuppressWarnings("unchecked" /* православно тут все... */)
	public List<SubjectOrganJoin> findSubjectOrganJoinsBy(Long organID, Long regionID, Long cityID) {
		Criteria crt = getSession()
			.createCriteria(SubjectOrganJoin.class)
			.add(Restrictions.eq("subjectOrganId", organID));

		if (regionID != null && regionID > 0)
			crt.add(Restrictions.eq("regionId", regionID));

		if (cityID != null && cityID > 0)
			crt.add(Restrictions.eq("cityId", cityID));

		return crt.list();
	}

	@Override
	public void add(SubjectOrganJoin subjectOrganJoin) {
		baseEntityDao.saveOrUpdate(subjectOrganJoin);
	}
}