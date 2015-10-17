package org.wf.dp.dniprorada.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.dao.GenericEntityDao;
import org.wf.dp.dniprorada.model.Subject;

@Repository
public class SubjectDaoImpl extends GenericEntityDao<Subject> implements SubjectDao {

    private static final Logger LOG = LoggerFactory.getLogger(SubjectDaoImpl.class);

    protected SubjectDaoImpl() {
        super(Subject.class);
    }

    @Override
    public Subject getSubject(Long nID) {
        return findById(nID).orNull();
    }

    @Override
    public Subject getSubject(String sID) {
        return findBy("sID", sID).orNull();
    }

    @Override
    public Subject saveOrUpdateSubject(Subject subject) {
        return saveOrUpdate(subject);
    }

}