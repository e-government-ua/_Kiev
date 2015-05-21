package org.wf.dp.dniprorada.dao;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.wf.dp.dniprorada.model.DocumentAccess;


public class DocumentAccessDaoImpl implements DocumentAccessDao {
	private final String url = "https://igov.org.ua/index#";
	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public String setDocumentLink(Integer nID_Document, String sFIO,
			String sTarget, String sTelephone, Long nDays, String sMail) {
		DocumentAccess da = new DocumentAccess();
		da.setnID_Document(nID_Document);
		da.setsDateCreate(new Date());
		da.setsDays(nDays);
		da.setsFIO(sFIO);
		da.setsMail(sMail);
		da.setsTarget(sTarget);
		da.setsTelephone(sTelephone);
		da.setsSecret(generateSecret());
		try {
			createRecord(da);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		StringBuilder sURL = new StringBuilder(url);
		sURL.append("nID_Document=" + nID_Document + "&");
		sURL.append("nID_Access=" + getIdAccess() + "&");
		sURL.append("sSecret=" + da.getsSecret());
		return sURL.toString();
	}

	private String generateSecret() {
		// 97-122 small
		// 65-90 big
		// 48-57 number
		StringBuilder sb = new StringBuilder();
		Random ran = new Random();
		for (int i = 1; i <= 20; i++) {
			int a = ran.nextInt(3) + 1;
			switch (a) {
			case 1:
				int num = ran.nextInt((57 - 48) + 1) + 48;
				sb.append((char) num);
				break;
			case 2:
				int small = ran.nextInt((122 - 97) + 1) + 97;
				sb.append((char) small);
				break;
			case 3:
				int big = ran.nextInt((90 - 65) + 1) + 65;
				sb.append((char) big);
				break;
			}
		}
		return sb.toString();
	}

	private void createRecord(DocumentAccess da) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate
				.update("INSERT INTO DocumentAccess"
						+ " (nID_Document, sDateCreate, nMS, sFIO, sTarget, sTelephone, sMail, "
						+ "sSecret) VALUES (?,?,?,?,?,?,?,?)",
						da.getnID_Document(), da.getsDateCreate(),
						da.getsDays(), da.getsFIO(), da.getsTarget(),
						da.getsTelephone(), da.getsMail(), da.getsSecret());
	}

	private Integer getIdAccess() {
		jdbcTemplate = new JdbcTemplate(dataSource);
		DocumentAccess da = null;
		try{
		da = jdbcTemplate
				.query("nID, nID_Document,"
						+ " sDateCreate, nMS, sFIO, sTarget, sTelephone, sMail, sSecret"
						+ " FROM DocumentAccess order by nID desc limit 1",
						new DocumentAccessRowMapper()).get(0);
		} catch(Exception e){
			da.setnID(0);
		}
		return da.getnID();
	}

	@Override
	public DocumentAccess getDocumentLink(String nID_Access, String sSecret) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List <DocumentAccess> listDa = jdbcTemplate
				.query("SELECT nID, nID_Document, "
						+ "sDateCreate, nMS, sFIO, sTarget, sTelephone, sMail, sSecret "
						+ "FROM DocumentAccess "
						+ "WHERE nID=? AND sSecret=?",
						new DocumentAccessRowMapper(), nID_Access, sSecret);
		DocumentAccess da = listDa.get(0);
		return da;
	}
}