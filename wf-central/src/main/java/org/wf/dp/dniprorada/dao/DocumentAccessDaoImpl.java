package org.wf.dp.dniprorada.dao;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;



import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.model.DocumentAccess;

@Repository
public class DocumentAccessDaoImpl implements DocumentAccessDao {
	private final String sURL = "https://igov.org.ua/index#";	
	private SessionFactory sessionFactory;
	private final String urlConn = "https://sms-inner.siteheart.com/api/otp_create_api.cgi";
	
	@Autowired
	public DocumentAccessDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public String setDocumentLink(Long nID_Document, String sFIO,
			String sTarget, String sTelephone, Long nMS, String sMail) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DocumentAccess oDocumentAccess = new DocumentAccess();
		oDocumentAccess.setID_Document(nID_Document);
		oDocumentAccess.setDateCreate(sdf.format(new Date()));
		oDocumentAccess.setMS(nMS);
		oDocumentAccess.setFIO(sFIO);
		oDocumentAccess.setMail(sMail);
		oDocumentAccess.setTarget(sTarget);
		oDocumentAccess.setTelephone(sTelephone);
		oDocumentAccess.setSecret(generateSecret());
		writeRow(oDocumentAccess);
		StringBuilder osURL = new StringBuilder(sURL);
		osURL.append("nID_Access=");
		osURL.append(getIdAccess()+"&");
		osURL.append("sSecret=");
		osURL.append(oDocumentAccess.getSecret());
		return osURL.toString();
	}

	private String generateSecret() {
		// 97-122 small character
		// 65-90 big character
		// 48-57 number
		StringBuilder os = new StringBuilder();
		Random ran = new Random();
		for (int i = 1; i <= 20; i++) {
			int a = ran.nextInt(3) + 1;
			switch (a) {
			case 1:
				int num = ran.nextInt((57 - 48) + 1) + 48;
				os.append((char) num);
				break;
			case 2:
				int small = ran.nextInt((122 - 97) + 1) + 97;
				os.append((char) small);
				break;
			case 3:
				int big = ran.nextInt((90 - 65) + 1) + 65;
				os.append((char) big);
				break;
			}
		}
		return os.toString();
	}
        
	private String generateAnswer() {
		// 48-57 number
		StringBuilder os = new StringBuilder();
		Random ran = new Random();
		for (int i = 1; i <= 4; i++) {
                    int big = ran.nextInt((57 - 48) + 1) + 48;
                    os.append((char) big);
                    break;
		}
		return os.toString();
	}        

	private void writeRow(DocumentAccess o) throws Exception{
		//Transaction t = null;
		Session s = getSession();
		try{
			/*t = s.beginTransaction();
			s.createQuery("INSERT INTO DocumentAccess (nID_Document, sDateCreate, nMS, sFIO, sTarget, sTelephone, sMail, sSecret) VALUES ("
			+ o.getID_Document()+","+ o.getDateCreate()+","+ o.getMS()+","+o.getFIO()+","+o.getTarget()+","+o.getTelephone()+","+"email"+","+o.getSecret()+")").executeUpdate();
			t.commit();*/
			/*Query query = s.createQuery("INSERT INTO DocumentAccess (nID_Document, sDateCreate, nMS, sFIO, sTarget, sTelephone, sMail, sSecret) VALUES (1,2014-06-03,222,LEO,secret,097,mail,qwe)");
			query.executeUpdate();*/
			Query query = s.createQuery("INSERT INTO DocumentAccess (nID_Document, sDateCreate, nMS, sFIO, sTarget, sTelephone, sMail, sSecret) VALUES ("+o.getID_Document()+","+o.getDateCreate()+
					","+o.getMS()+","+o.getFIO()+","+o.getTarget()+","+o.getTelephone()+","+o.getMail()+","+o.getSecret()+")");
			query.executeUpdate();
			//s.save(o);
			
		} catch(Exception e){
			//t.rollback();
			throw e;
		} finally {
			s.close();
		}
	}

	private Long getIdAccess() throws Exception{
		Session oSession = getSession();
		List <DocumentAccess> list = null;
		try{
			list = oSession.createCriteria(DocumentAccess.class).list();
		} catch(Exception e){
			throw e;
		} finally{
			oSession.close();
		}
		return list.get(0).getID();
	}

	@Override
	public DocumentAccess getDocumentLink(Long nID_Access, String sSecret) {
		Session oSession = getSession();
		List <DocumentAccess> list = null;
		DocumentAccess docAcc = null;
		try{
                    list = (List <DocumentAccess>)oSession.createCriteria(DocumentAccess.class).list();
                    for(DocumentAccess da : list){
                    	if(da.getID() == nID_Access && da.getSecret().equals(sSecret)){
                    		docAcc = da;
                    		break;
                    	}
                    }
		} catch(Exception e){
			throw e;
		} finally{
			oSession.close();
		}
		return docAcc;
	}

        
	@Override
	public String getDocumentAccess(Long nID_Access, String sSecret) throws Exception {
		Session oSession = getSession();
		List <DocumentAccess> list = null;
		String sTelephone = "";
		String sAnswer = "";
		DocumentAccess docAcc = new DocumentAccess();
		try{
                    //TODO убедиться что все проверяется по этим WHERE
                    list = (List <DocumentAccess>)oSession.createCriteria(DocumentAccess.class).list();
                    /*for(DocumentAccess da : list){
                    	System.out.println(da.toString());
                    }*/
                    if(list == null || list.isEmpty()){
                        throw new Exception("Access not accepted!");
                    } else {
                    	 for(DocumentAccess da : list){
                         	if(da.getID() == nID_Access && da.getSecret().equals(sSecret)){
                         		docAcc = da;
                         		break;
                         	}
                         }
                    }
                    if(docAcc.getTelephone() != null){
                     sTelephone = docAcc.getTelephone();
                    }
                    //TODO Generate random 4xDigits answercode
                    sAnswer = generateAnswer();
                    //TODO SEND SMS with this code
                    //
                    //o.setDateAnswerExpire(null);
                    docAcc.setAnswer(sAnswer);
                   // writeRow(docAcc);
		} catch(Exception e) {
			throw e;
		}finally{
			oSession.close();
		}
		String otpPassword = "-";
		try{
			otpPassword += getOtpPassword(docAcc);
		} catch(Exception e){
			otpPassword = "OTP is NULL";
		}
		return  "/"+ otpPassword;
	}

        
	@Override
	public String setDocumentAccess(Long nID_Access, String sSecret, String sAnswer) throws Exception {
		Session oSession = getSession();
		List <DocumentAccess> list = null;
		DocumentAccess docAcc = null;
		try{
                    //TODO убедиться что все проверяется по этим WHERE
                    list = (List <DocumentAccess>)oSession.createCriteria(DocumentAccess.class).list();
                    if(list == null || list.isEmpty()){
                        throw new Exception("Access not accepted!");
                    }         
                    else {
                   	 for(DocumentAccess da : list){
                        	if(da.getID() == nID_Access && da.getSecret().equals(sSecret)
                                        && ( da.getAnswer().equals(sAnswer) || "1234".equals(sAnswer) ) //TODO убрать бэкдур, после окончательной отладки, в т.ч. фронта
                                        
                                        ){
                        		docAcc = da;
                        		break;
                        	}
                        }
                   }
		} catch(Exception e){
			throw e;
		} finally{
			oSession.close();
		}
		return "/";
	}
        
	//public String setDocumentAccess(Integer nID_Access, String sSecret, String sAnswer) throws Exception;
	//public String getDocumentAccess(Integer nID_Access, String sSecret) throws Exception;
        
        
	private Session getSession(){
		return sessionFactory.openSession();
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	private String getOtpPassword(DocumentAccess docAcc) throws Exception{
		URL url = new URL(urlConn);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("content-type", "application/json;charset=UTF-8");
		con.setDoOutput(true);
		Properties prop = new Properties();
		prop.load(getClass().getClassLoader().getResourceAsStream("merch.properties"));
		String merchant_id = prop.getProperty("merchant_id");
		String merchant_password = prop.getProperty("merchant_password");
		String jsonObj = "{\"merchant_id\":\""+merchant_id+"\", \"merchant_password\":\""+merchant_password+"\", \"otp_create\":[" +
				"{\"from\":\"10060\", \"phone\":\"+380962731045\", \"category\":\"qwerty\", \"sms_template\":[" +
				"{\"text\":\"Parol: \"}, {\"password\":\"2\"}, {\"text\":\"-\"}, {\"password\":\"2\"}, {\"text\":\"-\"}, {\"password\":\"2\"}, {\"text\":\"-\"}, {\"password\":\"2\"}]}]}";
		DataOutputStream dos = new DataOutputStream(con.getOutputStream());
		dos.writeBytes(jsonObj);
		dos.flush();
		dos.close();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String inputLine;
		while((inputLine = br.readLine()) != null){
			sb.append(inputLine);
		}
		br.close();
		return sb.toString();
	}
}