package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.model.Subject;

public interface SubjectDao {
	
	public Subject getSubject(Long nID);
	
	public Subject saveOrUpdateSubject(Subject subject);
	
}
