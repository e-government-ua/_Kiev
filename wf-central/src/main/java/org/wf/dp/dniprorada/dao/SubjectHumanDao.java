package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.model.SubjectHuman;

public interface SubjectHumanDao {

	public SubjectHuman getSubjectHuman(String sINN);

	public SubjectHuman setSubjectHuman(String sINN);

	public SubjectHuman saveOrUpdateHuman(SubjectHuman subject);

}
