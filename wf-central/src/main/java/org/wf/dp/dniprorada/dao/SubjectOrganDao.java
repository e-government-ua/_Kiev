package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.model.SubjectOrgan;

public interface SubjectOrganDao {
	
	public SubjectOrgan getSubjectOrgan(String sOKPO); 

	public SubjectOrgan setSubjectOrgan(String sOKPO);

	public SubjectOrgan saveOrUpdateSubjectOrgan(SubjectOrgan subjectOrgan);

    public SubjectOrgan getSubjectOrgan(Long nID);

}
