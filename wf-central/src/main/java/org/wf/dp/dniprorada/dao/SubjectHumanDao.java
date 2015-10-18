package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.model.SubjectHuman;

public interface SubjectHumanDao extends EntityDao<SubjectHuman> {

    SubjectHuman getSubjectHuman(String sINN);

    SubjectHuman setSubjectHuman(String sINN);

    SubjectHuman saveOrUpdateHuman(SubjectHuman subject);

}
