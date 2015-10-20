package org.wf.dp.dniprorada.dao;

import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.dao.GenericEntityDao;
import org.wf.dp.dniprorada.model.*;
import org.wf.dp.dniprorada.util.GeneralConfig;
import org.wf.dp.dniprorada.util.Mail;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

@Repository
public class DocumentAccessDaoImpl extends GenericEntityDao<DocumentAccess> implements DocumentAccessDao {
    final static Logger log = Logger.getLogger(DocumentAccessDaoImpl.class);
    private final String sURL = "https://igov.org.ua/index#";
    private final String urlConn = "https://sms-inner.siteheart.com/api/otp_create_api.cgi";
    @Autowired
    GeneralConfig generalConfig;
    @Autowired
    Mail oMail;
    private SessionFactory sessionFactory;

    public DocumentAccessDaoImpl() {
        super(DocumentAccess.class);
    }

    @Override
    public String setDocumentLink(Long nID_Document, String sFIO,
            String sTarget, String sTelephone, Long nMS, String sMail) throws Exception {
        DocumentAccess oDocumentAccess = new DocumentAccess();
        oDocumentAccess.setID_Document(nID_Document);
        oDocumentAccess.setDateCreate(new DateTime());
        oDocumentAccess.setMS(nMS);
        oDocumentAccess.setFIO(sFIO);
        oDocumentAccess.setMail(sMail);
        oDocumentAccess.setTarget(sTarget);
        oDocumentAccess.setTelephone(sTelephone);
        oDocumentAccess.setSecret(generateSecret());

        //		String id = writeRow(oDocumentAccess).toString();

        if (oDocumentAccess.getsCode() == null)
            oDocumentAccess.setsCode("null");
        if (oDocumentAccess.getsCodeType() == null)
            oDocumentAccess.setsCodeType("null");

        saveOrUpdate(oDocumentAccess);

        String id = oDocumentAccess.getId().toString();
        log.info("id=" + id);

        //sCode;sCodeType
        oDocumentAccess.setsCode(id);
        oDocumentAccess.setsCodeType((sTelephone != null && sTelephone.length() > 6) ? "sms" : "");
        //		writeRow(oDocumentAccess);
        saveOrUpdate(oDocumentAccess);
        log.info("id=" + id + ":Ok!");
                
		/*StringBuilder osURL = new StringBuilder(sURL);
        osURL.append("nID_Access=");
		osURL.append(getIdAccess()+"&");
		osURL.append("sSecret=");
		osURL.append(oDocumentAccess.getSecret());*/
        //return osURL.toString();

        if (sMail != null && !"".equals(sMail.trim())) {
            String saToMail = sMail;
            String sHead = "Доступ до документу";
            String sBody = "Вам надано доступ до документу на Порталі державних послуг iGov.org.ua.<br>" +
                    "<br>" +
                    "<b>Код документу:</b> %" + id + "%<br>" +
                    "<br>" +
                    "Щоб переглянути цей документ, зайдіть на <a href=\"" + generalConfig.sHostCentral()
                    + "\">iGov.org.ua</a>, пункт меню <b>Документи</b>, вкладка <b>Пошук документу за кодом</b>. Там оберіть тип документу, того, хто його надає та введіть код.<br>"
                    +
                    "<br>" +
                    "З повагою,<br>" +
                    "команда порталу державних послу iGov";
            oMail.reset();
            oMail._To(saToMail)._Head(sHead)._Body(sBody);
            oMail.send();
        }

        return id;

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
            //int big = ran.nextInt((57 - 48) + 1) + 48;
            //os.append((char) big);
            int n = ran.nextInt(9999);
            os.append(n);
            break;
        }
        String s = os.toString();
        if (s.length() > 4) {
            s = s.substring(0, 5);
        } else if (s.length() == 3) {
            s = "0" + s;
        } else if (s.length() == 2) {
            s = "00" + s;
        } else if (s.length() == 1) {
            s = "000" + s;
        }
        return s;
    }

	/*private String writeRow(DocumentAccess o) throws Exception{
		Session s = getSession();

		if(o.getsCode() == null) o.setsCode("null");
		if(o.getsCodeType() == null) o.setsCodeType("null");
		s.saveOrUpdate(o);
		s.flush();
		return o.getId().toString();

	}*/

    @Deprecated
    public Long getIdAccess() throws Exception {
        //не оптимально
        return Iterables.getLast(findAll()).getId();
    }

    @Override
    public DocumentAccess getDocumentLink(Long nID_Access, String sSecret) {
        Session oSession = getSession();
        List<DocumentAccess> list = null;
        DocumentAccess docAcc = null;
        try {
            list = (List<DocumentAccess>) oSession.createCriteria(DocumentAccess.class).list();
            for (DocumentAccess da : list) {
                if (da.getId() == nID_Access && da.getSecret().equals(sSecret)) {
                    docAcc = da;
                    break;
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return docAcc;
    }

    @Override
    public String sSentDocumentAccessOTP_Phone(String sCode) throws Exception {
        String sPhoneSent = null;
        //Session oSession = getSession();
        boolean bSent = false;
        DocumentAccess oDocumentAccess = findBy("sCode", sCode).orNull();
        if (oDocumentAccess.getTelephone() != null && oDocumentAccess.getTelephone().trim().length() > 6) {
            String sPhone = "";
            String sAnswer = "";
            sPhone = oDocumentAccess.getTelephone();
            sPhoneSent = sPhone;
            log.info("[bSentDocumentAccessOTP]sPhone=" + sPhone);

            //Generate random 4xDigits answercode
            sAnswer = generateAnswer();
            log.info("[bSentDocumentAccessOTP]sAnswer=" + sAnswer);

            //o.setDateAnswerExpire(null);
            //SEND SMS with this code
            String sReturn;
            if (generalConfig.bTest()) {
                sAnswer = "4444";
            }
            oDocumentAccess.setAnswer(sAnswer);
            //                        writeRow(oDocumentAccess);
            saveOrUpdate(oDocumentAccess);
            log.info("oDocumentAccess.getId()=" + oDocumentAccess.getId() + ":Ok!");

            if (generalConfig.bTest()) {
                sReturn = "test";
            } else {
                sReturn = sendPasswordOTP(sPhone, sAnswer);
            }

            log.info("[bSentDocumentAccessOTP]sReturn=" + sReturn);

            bSent = true;
        } else {
            //TODO loging warn
        }
        //return  bSent;
        return sPhoneSent;
    }

    @Override
    public String getDocumentAccess(Long nID_Access, String sSecret) throws Exception {
        Session oSession = getSession();
        List<DocumentAccess> list = null;
        String sTelephone = "";
        String sAnswer = "";
        String otpPassword = "";
        DocumentAccess docAcc = new DocumentAccess();
        list = findAll();
        if (list == null || list.isEmpty()) {
            throw new Exception("Access not accepted!");
        } else {
            for (DocumentAccess da : list) {
                if (da.getId() == nID_Access && da.getSecret().equals(sSecret)) {
                    docAcc = da;
                    break;
                }
            }
        }
        if (docAcc.getTelephone() != null) {
            sTelephone = docAcc.getTelephone();
        }
        sAnswer = generateAnswer();
        docAcc.setAnswer(sAnswer);
        otpPassword = getOtpPassword(docAcc);
        return otpPassword;
    }

    @Override
    public DocumentAccess getDocumentAccess(String accessCode) {
        return (DocumentAccess) getSession()
                .createCriteria(DocumentAccess.class)
                .add(Restrictions.eq("sCode", accessCode))
                .uniqueResult();
    }

    @Override
    public String setDocumentAccess(Long nID_Access, String sSecret, String sAnswer) throws Exception {
        Session oSession = getSession();
        DocumentAccess docAcc = (DocumentAccess) createCriteria()
                .add(Restrictions.eq("nID", nID_Access))
                .add(Restrictions.eq("sSecret", sSecret))
                .add(Restrictions.eq("sAnswer", sAnswer))
                .uniqueResult();
        if (docAcc == null) {
            throw new Exception("Access not accepted!");
        } else {
            oSession.saveOrUpdate(docAcc);
        }
        return docAcc.toString();
    }

    //public String setDocumentAccess(Integer nID_Access, String sSecret, String sAnswer) throws Exception;
    //public String getDocumentAccess(Integer nID_Access, String sSecret) throws Exception;

    private <T> String sendPasswordOTP(String sPhone, String sPassword) throws Exception {
        Properties oProperties = new Properties();
        File oFile = new File(System.getProperty("catalina.base") + "/conf/merch.properties");
        FileInputStream oFileInputStream = new FileInputStream(oFile);
        oProperties.load(oFileInputStream);
        oFileInputStream.close();

        OtpPassword oOtpPassword = new OtpPassword();
        oOtpPassword.setMerchant_id(oProperties.getProperty("merchant_id"));
        oOtpPassword.setMerchant_password(oProperties.getProperty("merchant_password"));

        OtpCreate oOtpCreate = new OtpCreate();
        oOtpCreate.setCategory("qwerty");
        oOtpCreate.setFrom("10060");
        oOtpCreate.setPhone(sPhone);
		/*SmsTemplate oSmsTemplate = new SmsTemplate();
		oSmsTemplate.setText("text:"+"Parol: ");
		oSmsTemplate.setPassword("password:"+"2");
		SmsTemplate oSmsTemplate2 = new SmsTemplate();
		oSmsTemplate2.setText("text:"+"-");
		oSmsTemplate2.setPassword("password:"+"2");
		SmsTemplate oSmsTemplate3 = new SmsTemplate();
		oSmsTemplate3.setText("text:"+"-");
		oSmsTemplate3.setPassword("password:"+"2");
		SmsTemplate oSmsTemplate4 = new SmsTemplate();
		oSmsTemplate4.setText("text:"+"-");
		oSmsTemplate4.setPassword("password:"+"2");*/
        List<T> a = new ArrayList<T>();
        a.add((T) new OtpText("Parol:"));
        a.add((T) new OtpPass(sPassword));
		/*a.add((T)new OtpPass("2"));
		a.add((T)new OtpText("-"));
		a.add((T)new OtpPass("2"));
		a.add((T)new OtpText("-"));
		a.add((T)new OtpPass("2"));
		a.add((T)new OtpText("-"));
		a.add((T)new OtpPass("2"));*/
        oOtpCreate.setSms_template(a);
        List<OtpCreate> aOtpCreate = new ArrayList<>();
        aOtpCreate.add(oOtpCreate);
        oOtpPassword.setOtp_create(aOtpCreate);
        Gson oGson = new Gson();
        String jsonObj = oGson.toJson(oOtpPassword);
        URL oURL = new URL(urlConn);
        HttpURLConnection oHttpURLConnection = (HttpURLConnection) oURL.openConnection();
        oHttpURLConnection.setRequestMethod("POST");
        oHttpURLConnection.setRequestProperty("content-type", "application/json;charset=UTF-8");
        oHttpURLConnection.setDoOutput(true);
        DataOutputStream oDataOutputStream = new DataOutputStream(oHttpURLConnection.getOutputStream());
        oDataOutputStream.writeBytes(jsonObj);
        oDataOutputStream.flush();
        oDataOutputStream.close();

        BufferedReader oBufferedReader = new BufferedReader(new InputStreamReader(oHttpURLConnection.getInputStream()));
        StringBuilder os = new StringBuilder();
        String s;
        while ((s = oBufferedReader.readLine()) != null) {
            os.append(s);
        }
        oBufferedReader.close();
        return os.toString();
    }

    private <T> String getOtpPassword(DocumentAccess docAcc) throws Exception {
        Properties prop = new Properties();
        File file = new File(System.getProperty("catalina.base") + "/conf/merch.properties");
        FileInputStream fis = new FileInputStream(file);
        prop.load(fis);
        OtpPassword otp = new OtpPassword();
        otp.setMerchant_id(prop.getProperty("merchant_id"));
        otp.setMerchant_password(prop.getProperty("merchant_password"));
        fis.close();
        OtpCreate otpCreate = new OtpCreate();
        otpCreate.setCategory("qwerty");
        otpCreate.setFrom("10060");
        if (!docAcc.getTelephone().isEmpty() || docAcc.getTelephone() != null) {
            otpCreate.setPhone(docAcc.getTelephone());
        } else {
            otpCreate.setPhone("null");
        }
        SmsTemplate smsTemplate1 = new SmsTemplate();
        smsTemplate1.setText("text:" + "Parol: ");
        smsTemplate1.setPassword("password:" + "2");
        SmsTemplate smsTemplate2 = new SmsTemplate();
        smsTemplate2.setText("text:" + "-");
        smsTemplate2.setPassword("password:" + "2");
        SmsTemplate smsTemplate3 = new SmsTemplate();
        smsTemplate3.setText("text:" + "-");
        smsTemplate3.setPassword("password:" + "2");
        SmsTemplate smsTemplate4 = new SmsTemplate();
        smsTemplate4.setText("text:" + "-");
        smsTemplate4.setPassword("password:" + "2");
        List<T> list = new ArrayList<T>();
        list.add((T) new OtpText("Parol:"));
        list.add((T) new OtpPass("2"));
        list.add((T) new OtpText("-"));
        list.add((T) new OtpPass("2"));
        list.add((T) new OtpText("-"));
        list.add((T) new OtpPass("2"));
        list.add((T) new OtpText("-"));
        list.add((T) new OtpPass("2"));
        otpCreate.setSms_template(list);
        List<OtpCreate> listOtpCreate = new ArrayList<>();
        listOtpCreate.add(otpCreate);
        otp.setOtp_create(listOtpCreate);
        Gson g = new Gson();
        String jsonObj = g.toJson(otp);
        URL url = new URL(urlConn);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("content-type", "application/json;charset=UTF-8");
        con.setDoOutput(true);
        DataOutputStream dos = new DataOutputStream(con.getOutputStream());
        dos.writeBytes(jsonObj);
        dos.flush();
        dos.close();
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }
        br.close();
        return sb.toString();
    }
}
