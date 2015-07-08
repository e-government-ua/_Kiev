package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.model.SubjectOrgan;
import org.wf.dp.dniprorada.model.SubjectOrganJoin;

import java.util.List;

public interface SubjectOrganDao {
	
	public SubjectOrgan getSubjectOrgan(String sOKPO); 

	public SubjectOrgan setSubjectOrgan(String sOKPO);

	public SubjectOrgan saveOrUpdateSubjectOrgan(SubjectOrgan subjectOrgan);

    public SubjectOrgan getSubjectOrgan(Long nID);

	List<SubjectOrganJoin> findSubjectOrganJoinsBy(Long organID, Long regionID, Long cityID);
	void add(SubjectOrganJoin subjectOrganJoin);
}
