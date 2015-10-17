package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.model.Subject;

public interface SubjectDao extends EntityDao<Subject> {

    public Subject getSubject(Long nID);

    public Subject getSubject(String sID);

    public Subject saveOrUpdateSubject(Subject subject);

}
