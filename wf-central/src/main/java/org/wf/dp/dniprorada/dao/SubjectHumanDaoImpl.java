package org.wf.dp.dniprorada.dao;

import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.dao.GenericEntityDao;
import org.wf.dp.dniprorada.model.Subject;
import org.wf.dp.dniprorada.model.SubjectHuman;

@Repository
public class SubjectHumanDaoImpl extends GenericEntityDao<SubjectHuman> implements SubjectHumanDao {

    protected SubjectHumanDaoImpl() {
        super(SubjectHuman.class);
    }

    @Override
    public SubjectHuman getSubjectHuman(String sINN) {
        return findBy("sINN", sINN).orNull();
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
        saveOrUpdate(oSubjectHuman);
        return oSubjectHuman;
    }

}
