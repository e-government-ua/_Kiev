package org.wf.dp.dniprorada.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.wf.dp.dniprorada.model.Subject;

public class SubjectExtractor implements ResultSetExtractor<Subject>{

	@Override
	public Subject extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		Subject subject = new Subject();
		subject.setnID(rs.getInt("nID"));
		subject.setsID(rs.getString("sID"));
		subject.setsINN(rs.getString("sINN"));
		subject.setsPassport(rs.getString("sPassport"));
		subject.setsSB(rs.getString("sSB"));
		subject.setsOKPO(rs.getString("sOKPO"));
		subject.setsName(rs.getString("sName"));
		return null;
	}

}
