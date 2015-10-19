package org.wf.dp.dniprorada.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Belyavtsev Vladimir Vladimirovich (BW)
 */
@Component("mailOld")
public class MailOld extends Abstract_Mail {

    private final static Logger log = LoggerFactory.getLogger(Mail.class);
    //private MultiPartEmail oMultiPartEmail = new MultiPartEmail();
    private MultiPartEmail oMultiPartEmail = null;

    public MailOld() {
    }

    //public Mail() throws EmailException{
    public void init() throws EmailException {
        //if(oMultiPartEmail!=null){
        //    return;
        //}
        log.info("init");
        oMultiPartEmail = new MultiPartEmail();
        oMultiPartEmail.setHostName(getHost());
        log.info("getHost()=" + getHost());
        oMultiPartEmail.addTo(getTo(), "receiver");
        log.info("getTo()=" + getTo());
        oMultiPartEmail.setFrom(getFrom(), getFrom());//"iGov"
        log.info("getFrom()=" + getFrom());
        oMultiPartEmail.setSubject(getHead());
        log.info("getHead()=" + getHead());
    }

    public MailOld _BodyAsText() throws EmailException {
        //        init();
        log.info("_BodyAsText");
        oMultiPartEmail.setMsg(getBody());
        //oMultiPartEmail.setContent(sBody, "text/html; charset=\"utf-8\"");
        log.info("getBody()=" + getBody());
        return this;
    }

    public MailOld _BodyAsHTML() throws EmailException {
        //        init();
        log.info("_BodyAsHTML");
        //oMultiPartEmail.setMsg(sBody);
        oMultiPartEmail.setContent(getBody(), "text/html");
        oMultiPartEmail.setCharset("UTF-8");
        log.info("getBody()=" + getBody());
        return this;
    }

    public MailOld _PartHTML() throws MessagingException, EmailException {
        //        init();
        log.info("_PartHTML");
        //oMultiPartEmail.setMsg("0");
        MimeMultipart oMimeMultipart = new MimeMultipart("related");
        BodyPart oBodyPart = new MimeBodyPart();
        oBodyPart.setContent(getBody(), "text/html; charset=\"utf-8\"");
        oMimeMultipart.addBodyPart(oBodyPart);
        oMultiPartEmail.setContent(oMimeMultipart);
        log.info("getBody()=" + getBody());
        return this;
    }

    public MailOld _Part(DataSource oDataSource) throws MessagingException, EmailException {
        //        init();
        log.info("_Part");
        MimeMultipart oMimeMultipart = new MimeMultipart("related");
        BodyPart oBodyPart = new MimeBodyPart();
        oBodyPart.setContent(oDataSource, "application/zip");
        oMimeMultipart.addBodyPart(oBodyPart);
        return this;
    }

    public MailOld _Attach(DataSource oDataSource, String sName, String sNote)
            throws MessagingException, EmailException {
        //        init();
        log.info("1)oMultiPartEmail.isBoolHasAttachments()=" + oMultiPartEmail.isBoolHasAttachments());
        // add the attachment
        oMultiPartEmail.attach(oDataSource, sName, sNote);
        log.info("2)oMultiPartEmail.isBoolHasAttachments()=" + oMultiPartEmail.isBoolHasAttachments());
        oMultiPartEmail.setBoolHasAttachments(true);
        log.info("3)oMultiPartEmail.isBoolHasAttachments()=" + oMultiPartEmail.isBoolHasAttachments());
        return this;
    }

    private MailOld _Parts(List<DataSource> aDataSource) throws MessagingException {
                /*Multipart oMultipart = new Multipart(oDataSource) {

                    @Override
                    public void writeTo(OutputStream out) throws IOException, MessagingException {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
                MultipartDataSource oMultipartDataSource=new();*/
                
                /*
                MimeMultipart oMimeMultipart = new MimeMultipart("related");
                BodyPart oBodyPart = new MimeBodyPart();
                oBodyPart.setContent(oDataSource, "application/zip");
                oMimeMultipart.addBodyPart(oBodyPart);
                //oMultiPartEmail.setContent(oMimeMultipart);
                oMultiPartEmail.addPart(oMimeMultipart);
                */

                /*
                // Create the attachment
                EmailAttachment oEmailAttachment = new EmailAttachment();
                oEmailAttachment.setPath("mypictures/john.jpg");
                oEmailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
                oEmailAttachment.setDescription("Picture of John");
                oEmailAttachment.setName("John");
                */
                
/*
                //File oFile;
                Multipart aPart = new MimeMultipart();
                MimeBodyPart oPart = new MimeBodyPart();
                oPart.setHeader("Content-Type","multipart/mixed");
//                DataSource source = new FileDataSource(oFile);
                //oPart.setDataHandler(new DataHandler(source));
                oPart.setDataHandler(new DataHandler(oDataSource));
                //if(sLog!=null||sDebug!=null)System.out.println("_"+sLog+"|"+sDebug+"_"+"[Mail.bSend] (oFile.getName()="+oFile.getName()+") ");
                //oPart.setFileName(MimeUtility.encodeText(oFile.getName()));
                //oPart.setFileName(MimeUtility.encodeText(oFile.getName()));
                oPart.setFileName(MimeUtility.encodeText(sFileName));
                aPart.addBodyPart(oPart);                

                oMultiPartEmail.setContent(aPart);
*/
        //oMultiPartEmail.addPart(oPart);
/*
                
                try{if(oSession==null)oSession="".equals(sContext)?Session.getInstance(oProps
                        ,new Authenticator(){
                             @Override
                             public PasswordAuthentication getPasswordAuthentication(){
                                    return new PasswordAuthentication(sLogin,sPassword);//"dn310780bvv"//"9111391113"
                             }
                        }
                ):(Session)(new InitialContext()).lookup(sContext);//session=getMailSession();
                MimeMessage oMsg = new MimeMessage(oSession);
                String[] as=sAt.split(">")[0].trim().split("<");String sAtName="";
                if(as.length>1){sAt=as[1].trim();if(!"".equals(sAt)){sAtName=as[0].trim();}}
                if(sLog!=null||sDebug!=null)System.out.println("_"+sLog+"|"+sDebug+"_"+"[Mail.bSend] (sAt="+sAt+",sAtName="+sAtName+") ");
                oMsg.setFrom(new InternetAddress(sAt,sAtName,DEFAULT_ENCODING));
                for(int n=0;n<asTo.length;n++){
                    as=asTo[n].split(">")[0].trim().split("<");String sTo=as[0].trim(),sToName="";
                    if(as.length>1){sTo=as[1].trim();if(!"".equals(sTo)){sToName=as[0].trim();}}
                    if(sLog!=null||sDebug!=null)System.out.println("_"+sLog+"|"+sDebug+"_"+"[Mail.bSend] (sTo="+sTo+",sToName="+sToName+") ");
                    oMsg.addRecipient(Message.RecipientType.CC,new InternetAddress(sTo,sToName,DEFAULT_ENCODING));
                   if(sDebug!=null)System.out.println("_"+sDebug+"_"+sMsg+"_ADDED!");
                }oMsg.setSubject(sHead,DEFAULT_ENCODING);
                oMsg.setContent(oParts);
                oMsg.getRecipients(Message.RecipientType.CC);
                Transport.send(oMsg);                

                        */

        //oMultiPartEmail.addPart(saToMail, sBody);
        return this;
    }

    
    
    
    
    
    
        /*MultiPartEmail oMultiPartEmail = new MultiPartEmail();
        //oMultiPartEmail.setHostName(mailServerHost);
        oMultiPartEmail.addTo(saToMail, "receiver");
        oMultiPartEmail.setFrom(sFromMail, mailAddressNoreplay);
        oMultiPartEmail.setSubject(sHead);
    */
        
        /*
        oMultiPartEmail.setMsg(sBody);
        //oMultiPartEmail.setContent(sBody, "text/html; charset=\"utf-8\"");
        */
        
        /*
        oMultiPartEmail.setContent(sBody, "text/html");
        oMultiPartEmail.setCharset("UTF-8");
        */

    /*
        oMultiPartEmail.setMsg("0");
        MimeMultipart oMimeMultipart = new MimeMultipart("related");
        BodyPart oBodyPart = new MimeBodyPart();
        oBodyPart.setContent(sBody, "text/html; charset=\"utf-8\"");
        oMimeMultipart.addBodyPart(oBodyPart);
        oMultiPartEmail.setContent(oMimeMultipart);
        */
    
        /*
        oMimeMultipart = new MimeMultipart("related");
        oBodyPart = new MimeBodyPart();
        oBodyPart.setContent(sBody, "application/zip");
        oMimeMultipart.addBodyPart(oBodyPart);
        */
    
    /*
        oMultiPartEmail.setAuthentication(mailServerUsername, mailServerPassword);
        oMultiPartEmail.setSmtpPort(Integer.valueOf(mailServerPort));
        oMultiPartEmail.setSSL(true);
        oMultiPartEmail.setTLS(true);        
      */  
        
        
        
        
        
        
        
        /*
        
        // load your HTML email template
        String htmlEmailTemplate = ".... <img src=\"http://www.apache.org/images/feather.gif\"> ....";

        // define you base URL to resolve relative resource locations
        URL url = new URL("http://www.apache.org");

        // create the email message
        ImageHtmlEmail email = new ImageHtmlEmail();
        email.setDataSourceResolver(new DataSourceUrlResolver(url));
        email.setHostName("mail.myserver.com");
        email.addTo("jdoe@somewhere.org", "John Doe");
        email.setFrom("me@apache.org", "Me");
        email.setSubject("Test email with inline image");

        // set the html message
        email.setHtmlMsg(htmlEmailTemplate);

        // set the alternative message
        email.setTextMsg("Your email client does not support HTML messages");        
        
        */
        
        
        
        /*MimeMultipart oMimeMultipart = new MimeMultipart("related");
        BodyPart oBodyPart = new MimeBodyPart();
        oBodyPart.setContent(sBody, "text/html; charset=\"utf-8\"");
        oMimeMultipart.addBodyPart(oBodyPart);*/
    //oMimeMultipart.

    //oMimeMultipart.addBodyPart(oBodyPart);
        
        /*email.addTo(toStr, "receiver");
        email.setFrom(fromStr, mailAddressNoreplay);
        email.setSubject(subjectStr);
        email.setMsg(replaceTags(textStr, execution));
        email.setAuthentication(mailServerUsername, mailServerPassword);
        email.setSmtpPort(Integer.valueOf(mailServerPort));
        email.setSSL(true);
        email.setTLS(true);*/
        
        /*String fromStr = getStringFromFieldExpression(this.from, execution);
        String toStr = getStringFromFieldExpression(this.to, execution);
        String subjectStr = getStringFromFieldExpression(this.subject, execution);
        String textStr = getStringFromFieldExpression(this.text, execution);

        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(mailServerHost);
        email.addTo(toStr, "receiver");
        email.setFrom(fromStr, mailAddressNoreplay);
        email.setSubject(subjectStr);
        email.setMsg(replaceTags(textStr, execution));
        email.setAuthentication(mailServerUsername, mailServerPassword);
        email.setSmtpPort(Integer.valueOf(mailServerPort));
        email.setSSL(true);
        email.setTLS(true);*/        
        
        /*
        MimeMessage oMimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper oMMH = new MimeMessageHelper(oMimeMessage, true);
        oMMH.setTo(saToMail);
        oMMH.setFrom(sFromMail);
        oMMH.setSubject(sHead);
        
        MimeMultipart oMimeMultipart = new MimeMultipart("related");
        BodyPart oBodyPart = new MimeBodyPart();
        oBodyPart.setContent(sBody, "text/html; charset=\"utf-8\"");
        oMimeMultipart.addBodyPart(oBodyPart);
        
        oMimeMessage.setContent(oMimeMultipart);
        */

    @Override
    public void send() throws EmailException {
        //init();
        oMultiPartEmail.setAuthentication(getAuthUser(), getAuthPassword());
        log.info("getAuthUser()=" + getAuthUser());
        log.info("getAuthPassword()=" + getAuthPassword());
        oMultiPartEmail.setSmtpPort(getPort());
        log.info("getPort()=" + getPort());
        oMultiPartEmail.setSSL(isSSL());
        log.info("isSSL()=" + isSSL());
        oMultiPartEmail.setTLS(isTLS());
        log.info("isTLS()=" + isTLS());

        oMultiPartEmail.sendMimeMessage();
        log.info("sendMimeMessage!");
    }
    
    /*public Mail oThis() throws EmailException, MessagingException{
        init();
        _PartHTML();

        return this;
    }*/
}
