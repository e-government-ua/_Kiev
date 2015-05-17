package org.wf.dp.dniprorada.dao;

import java.util.List;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.wf.dp.dniprorada.model.Subject;

public class SubjectDaoImpl implements SubjectDao {

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Subject getSubject(String inn) {
		/*return jdbcTemplate
				.query("SELECT nID, sID, sINN, sPassport, sSB, sOKPO, sName FROM subjects WHERE sID = ?",
						new SubjectRowMapper(), inn).get(0);*/
		List<Subject> a = jdbcTemplate
				.query("SELECT nID, sID, sINN, sPassport, sSB, sOKPO, sName FROM subjects WHERE sID = ?",
						new SubjectRowMapper(), inn);
                if(a.size()>0){
                    return a.get(0);
                }else{
                    return null;
                }
	}

	@Override
	public Subject insertSubject(String inn) {
		//jdbcTemplate.update("INSERT INTO subjects (sINN) VALUES (?)", inn);
		jdbcTemplate.update("INSERT INTO subjects (sID, sINN) VALUES (?, ?)", inn, inn);
		return getSubject(inn);
	}

	@Override
	public Subject updateSubject(Subject subject) {
		try{
		jdbcTemplate
				.update("UPDATE subjects SET sID=?, sINN=?, sPassport=?, sSB=?, sOKPO=?, sName=? WHERE nID = ?",
						subject.getnID(), subject.getsID(), subject.getsINN(),
						subject.getsPassport(), subject.getsSB(),
						subject.getsOKPO(), subject.getsName());
		return subject;
		} catch(Exception ex){
			return null;
		}
	}

}