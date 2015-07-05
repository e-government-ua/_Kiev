package org.wf.dp.dniprorada.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.wf.dp.dniprorada.model.DocumentAccess;

public class DocumentAccessLinkExtractor implements ResultSetExtractor<DocumentAccess>{

	@Override
	public DocumentAccess extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		DocumentAccess da = new DocumentAccess();
		da.setId(rs.getLong("nID"));
		da.setID_Document(rs.getLong("nID_Document"));
		da.setDateCreate(rs.getString("sDateCreate"));
		da.setMS(rs.getLong("nMS"));
		da.setFIO(rs.getString("sFIO"));
		da.setMail(rs.getString("sMail"));
		da.setSecret(rs.getString("sSecret"));
		da.setTarget(rs.getString("sTarget"));
		da.setTelephone(rs.getString("sTelephone"));
		return da;
	}
		
}
