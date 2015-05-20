package org.wf.dp.dniprorada.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.wf.dp.dniprorada.model.Subject;

public class SubjectRowMapper implements RowMapper<Subject>{

	@Override
	public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new SubjectExtractor().extractData(rs);
	}
}