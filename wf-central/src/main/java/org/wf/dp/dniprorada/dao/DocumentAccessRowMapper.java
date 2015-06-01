package org.wf.dp.dniprorada.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.wf.dp.dniprorada.model.DocumentAccess;


public class DocumentAccessRowMapper implements RowMapper<DocumentAccess> {

	@Override
	public DocumentAccess mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new DocumentAccessLinkExtractor().extractData(rs);
	}
}