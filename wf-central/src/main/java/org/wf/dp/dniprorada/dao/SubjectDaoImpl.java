package org.wf.dp.dniprorada.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.wf.dp.dniprorada.model.Subject;

public class SubjectDaoImpl implements SubjectDao{
	
private JdbcTemplate jdbcTemplate;
    
    public void setDataSource(DataSource dataSource){
    	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public Subject getSubject(String inn) {
		return jdbcTemplate.query("SELECT nID, sID, sINN, sPassport, sSB FROM subjects WHERE sINN=?", new SubjectRowMapper(), inn).get(0);
	}

	@Override
	public Subject insertSubject(String inn) {
		jdbcTemplate.update("INSERT INTO subjects (sINN) VALUES (?)", inn);
		return getSubject(inn);
	}

}