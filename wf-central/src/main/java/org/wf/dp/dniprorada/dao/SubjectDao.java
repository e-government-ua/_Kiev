package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.model.Subject;

public interface SubjectDao {
	public Subject getSubject(String inn);
	public Subject insertSubject(String inn);
	public Subject updateSubject(Subject subject);
}
