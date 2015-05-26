package org.wf.dp.dniprorada.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.wf.dp.dniprorada.model.DocumentAccess;

public class DocumentAccessExtractor implements ResultSetExtractor<DocumentAccess>{

	@Override
	public DocumentAccess extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		DocumentAccess da = new DocumentAccess();
		da.setnID(rs.getInt("nID"));
		da.setnID_Document(rs.getInt("nID_Document"));
		da.setsDateCreate(rs.getDate("sDateCreate"));
		da.setsDays(rs.getLong("sMS"));
		da.setsFIO(rs.getString("sFIO"));
		da.setsMail(rs.getString("sMail"));
		da.setsSecret(rs.getString("sSecret"));
		da.setsTarget(rs.getString("sTarget"));
		da.setsTelephone(rs.getString("sTelephone"));
		return da;
	}
		
}
