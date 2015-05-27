package org.wf.dp.dniprorada.dao;


import java.util.Date;
import java.util.List;
import java.util.Random;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.model.DocumentAccess;

@Repository
public class DocumentAccessDaoImpl implements DocumentAccessDao {
	private final String url = "https://igov.org.ua/index#";	
	private SessionFactory sessionFactory;
	
	@Autowired
	public DocumentAccessDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public String setDocumentLink(Integer nID_Document, String sFIO,
			String sTarget, String sTelephone, Long nDays, String sMail) throws Exception{
		DocumentAccess da = new DocumentAccess();
		da.setnID_Document(nID_Document);
		da.setsDateCreate(new Date());
		da.setsDays(nDays);
		da.setsFIO(sFIO);
		da.setsMail(sMail);
		da.setsTarget(sTarget);
		da.setsTelephone(sTelephone);
		da.setsSecret(generateSecret());
		createRecord(da);
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
		Transaction t = null;
		Session s = getSession();
		try{
			t = s.beginTransaction();
			getSession().saveOrUpdate(da);
			t.commit();
		} catch(Exception e){
			t.rollback();
			throw e;
		} finally {
			s.close();
		}
	}

	private Integer getIdAccess() throws Exception{
		Session s = getSession();
		List <DocumentAccess> list = null;
		try{
			list = getSession().createCriteria(DocumentAccess.class).list();
		} catch(Exception e){
			throw e;
		} finally{
			s.close();
		}
		return list.get(0).getnID();
	}

	@Override
	public DocumentAccess getDocumentLink(String nID_Access, String sSecret) {
		Session s = getSession();
		List <DocumentAccess> list = null;
		try{
		list = s.createSQLQuery("FROM DocumentAccess WHERE nID=? AND sSecret=?").list();
		} catch(Exception e){
			throw e;
		} finally{
			s.close();
		}
		return list.get(0);
	}
		
	private Session getSession(){
		return sessionFactory.openSession();
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}