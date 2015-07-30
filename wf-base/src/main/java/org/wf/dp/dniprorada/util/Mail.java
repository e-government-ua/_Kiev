package org.wf.dp.dniprorada.util;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.naming.InitialContext;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Belyavtsev Vladimir Vladimirovich (BW)
 */
@Component("mail")
public class Mail extends Abstract_Mail{

    Properties oProps = new Properties();
    private Session oSession=null;
    String DEFAULT_ENCODING="UTF-8";
    
    
    private Multipart oMultiparts = new MimeMultipart();
    //private String sContext="";
    
    
    
    public Mail(){}
            
    private final static Logger log = LoggerFactory.getLogger(Mail.class);
    
    
    public void reset() throws EmailException{
        oMultiparts = new MimeMultipart();
    }
    
    @Override
    public void send() throws EmailException{
        /*oProps.clear();//"10.1.100.70"//"true"
        oProps.put("mail.smtp.port",getPort());// Mail server
        oProps.put("mail.smtp.host",getHost());// Mail server
        oProps.put("mail.smtp.auth","true");*/
        try {
            /*if (oSession == null) {
                oSession = "".equals(sContext) ? Session.getInstance(oProps, new Authenticator() {
                    @Override
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(getAuthUser(), getAuthPassword()); //"dn310780bvv"//"9111391113"
                    }
                }
                ) : (Session) (new InitialContext()).lookup(sContext);//session=getMailSession();
            }*/
            
            log.info("init");
            MultiPartEmail oMultiPartEmail = new MultiPartEmail();
            oMultiPartEmail.setHostName(getHost());
                log.info("getHost()="+getHost());
            oMultiPartEmail.addTo(getTo(), "receiver");
                log.info("getTo()="+getTo());
            oMultiPartEmail.setFrom(getFrom(), getFrom());//"iGov"
                log.info("getFrom()="+getFrom());
            oMultiPartEmail.setSubject(getHead());
                log.info("getHead()="+getHead());

            oMultiPartEmail.setAuthentication(getAuthUser(), getAuthPassword());
                log.info("getAuthUser()="+getAuthUser());
                log.info("getAuthPassword()="+getAuthPassword());
            oMultiPartEmail.setSmtpPort(getPort());
                log.info("getPort()="+getPort());
            oMultiPartEmail.setSSL(isSSL());
                log.info("isSSL()="+isSSL());
            oMultiPartEmail.setTLS(isTLS());
                log.info("isTLS()="+isTLS());

            oSession = oMultiPartEmail.getMailSession();
            MimeMessage oMimeMessage = new MimeMessage(oSession);

            /*
             String[] as = sAt.split(">")[0].trim().split("<");
             String sAtName = "";
             if (as.length > 1) {
                sAt = as[1].trim();
                if (!"".equals(sAt)) {
                    sAtName = as[0].trim();
                }
            }
            if (sLog != null || sDebug != null) {
                System.out.println("_" + sLog + "|" + sDebug + "_" + "[Mail.bSend] (sAt=" + sAt + ",sAtName=" + sAtName + ") ");
            }
            */
            
            //oMimeMessage.setFrom(new InternetAddress(getFrom(), "iGov", DEFAULT_ENCODING));
            oMimeMessage.setFrom(new InternetAddress(getFrom(),getFrom()));
            //oMimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(sTo, sToName, DEFAULT_ENCODING));
            oMimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(getTo(), "recipient", DEFAULT_ENCODING));
            /*for (int n = 0; n < asTo.length; n++) {
                as = asTo[n].split(">")[0].trim().split("<");
                String sTo = as[0].trim(), sToName = "";
                if (as.length > 1) {
                    sTo = as[1].trim();
                    if (!"".equals(sTo)) {
                        sToName = as[0].trim();
                    }
                }
                if (sLog != null || sDebug != null) {
                    System.out.println("_" + sLog + "|" + sDebug + "_" + "[Mail.bSend] (sTo=" + sTo + ",sToName=" + sToName + ") ");
                }
                oMimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(sTo, sToName, DEFAULT_ENCODING));
                if (sDebug != null) {
                    System.out.println("_" + sDebug + "_" + sMsg + "_ADDED!");
                }
            }*/
            oMimeMessage.setSubject(getHead(), DEFAULT_ENCODING);
            
            
            _Attach(getBody());
            oMimeMessage.setContent(oMultiparts);
            
//            oMimeMessage.getRecipients(Message.RecipientType.CC);
            Transport.send(oMimeMessage);
            log.info("[send]:Transport.send!");
        } catch (Exception oException) {
            log.error("[send]", oException);
        }
    }
    
    
    public Mail _Attach(String sBody) {
        try {
            MimeBodyPart oMimeBodyPart = new MimeBodyPart();
            //oMimeBodyPart.setText(sBody,DEFAULT_ENCODING,"Content-Type: text/html;");
            oMimeBodyPart.setText(sBody, DEFAULT_ENCODING);
            oMimeBodyPart.setHeader("Content-Type", "text/html");
            oMultiparts.addBodyPart(oMimeBodyPart);
            log.info("[_Attach:sBody]:sBody="+sBody);
        } catch (Exception oException) {
            log.error("[_Attach:sBody]", oException);
        }
        return this;
    }

        
    public Mail _Attach(File oFile) {
        _Attach(new FileDataSource(oFile), oFile.getName(), "");
        return this;
    }        
        
        
    public Mail _Attach(File[] aFile) {
        log.info("[_Attach:aoFile]:aFile.length="+aFile.length);
        for (File oFile : aFile) {
            _Attach(oFile);
        }
        return this;
    }

    public Mail _Attach(DataSource oDataSource, String sFileName, String sDescription) {
        try {
            MimeBodyPart oMimeBodyPart = new MimeBodyPart();
            oMimeBodyPart.setHeader("Content-Type", "multipart/mixed");
            oMimeBodyPart.setDataHandler(new DataHandler(oDataSource));
            oMimeBodyPart.setFileName(MimeUtility.encodeText(sFileName));
            oMultiparts.addBodyPart(oMimeBodyPart);
            log.info("[_Attach:oFile]:sFileName="+sFileName+",sDescription="+sDescription);
        } catch (Exception oException) {
            log.error("[_Attach:oFile]sFileName="+sFileName+",sDescription="+sDescription, oException);
        }
        return this;
    }

    public Mail _Attach(URL[] aoURL) {
        return _Attach(aoURL, null);
    }

    public Mail _Attach(URL[] aoURL, String[] asName) {
        log.info("[_Attach:aoURL]:asName="+asName);
        for (int n = 0; n < aoURL.length; n++) {
            try {
                if (asName == null) {
                    _Attach(aoURL[n], null);
                } else {
                    _Attach(aoURL[n], asName[n]);
                }
            } catch (Exception oException) {
                log.error("[_Attach:aoURL]", oException);
            }
        }
        return this;
    }

    public Mail _Attach(URL oURL, String sName) {
        try {
            MimeBodyPart oMimeBodyPart = new MimeBodyPart();//javax.activation
            oMimeBodyPart.setHeader("Content-Type", "multipart/mixed");
            DataSource oDataSource = new URLDataSource(oURL);
            oMimeBodyPart.setDataHandler(new DataHandler(oDataSource));
            //oPart.setFileName(MimeUtility.encodeText(source.getName()));
            oMimeBodyPart.setFileName(MimeUtility.encodeText(sName == null || "".equals(sName) ? oDataSource.getName() : sName));
            oMultiparts.addBodyPart(oMimeBodyPart);
            log.info("[_Attach:oURL]:sName="+sName);
        } catch (Exception oException) {
            log.error("[_Attach:oURL]:sName="+sName, oException);
        }
        return this;
    }
}
