package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.model.SubjectOrgan;
import org.wf.dp.dniprorada.model.SubjectOrganJoin;

import java.util.List;

public interface SubjectOrganDao extends EntityDao<SubjectOrgan> {

    public SubjectOrgan getSubjectOrgan(String sOKPO);

    public SubjectOrgan setSubjectOrgan(String sOKPO);

    public SubjectOrgan saveOrUpdateSubjectOrgan(SubjectOrgan subjectOrgan);

    public SubjectOrgan getSubjectOrgan(Long nID);

    List<SubjectOrganJoin> findSubjectOrganJoinsBy(Long organID, Long regionID, Long cityID, String uaID);

    void add(SubjectOrganJoin subjectOrganJoin);

    void removeSubjectOrganJoin(Long organID, String[] publicIDs);
}
