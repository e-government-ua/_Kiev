package org.wf.dp.dniprorada.engine.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.activation.DataHandler;

import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.task.Attachment;
import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @author BW
 * 
 */
@Component("MailTaskWithAttachments")
public class MailTaskWithAttachments extends Abstract_MailTaskCustom {

    private final static Logger log = LoggerFactory.getLogger(MailTaskWithAttachments.class);
    
    private Expression saAttachmentsForSend;

    @Override
    public void execute(DelegateExecution oExecution) throws Exception {
        System.setProperty("mail.mime.address.strict", "false");

        MultiPartEmail oMultiPartEmail = MultiPartEmail_BaseFromTask(oExecution);
        
        String sAttachmentsForSend = getStringFromFieldExpression(this.saAttachmentsForSend, oExecution);
        
        log.info("sAttachmentsForSend=" + sAttachmentsForSend);
        List<Attachment> aAttachment = new ArrayList<>();
        String[] saID_Attachment = sAttachmentsForSend.split(",");
        for (String sID_Attachment : saID_Attachment) {
            log.info("sID_Attachment=" + sID_Attachment);
            String sID_AttachmentTrimmed = sID_Attachment.replaceAll("^\"|\"$", "");
            log.info("sID_AttachmentTrimmed= " + sID_AttachmentTrimmed);
            Attachment oAttachment = taskService.getAttachment(sID_AttachmentTrimmed);
            if (oAttachment != null) {
                aAttachment.add(oAttachment);
            }
        }

        if (aAttachment != null && !aAttachment.isEmpty()) {
            InputStream oInputStream_Attachment = null;
            String sFileName = "document";
            String sFileExt = "txt";
            String sDescription = "";
            for (Attachment oAttachment : aAttachment) {
                sFileName = oAttachment.getName();
                sFileExt = oAttachment.getType().split(";")[0];
                sDescription = oAttachment.getDescription();
                if(sDescription==null||"".equals(sDescription.trim())){
                    sDescription = "(no description)";
                }
                log.info("oAttachment.getId()="+oAttachment.getId()+", sFileName=" + sFileName + ", sFileExt=" + sFileExt + ", sDescription=" + sDescription);
                oInputStream_Attachment = oExecution.getEngineServices().getTaskService().getAttachmentContent(oAttachment.getId());
                if (oInputStream_Attachment == null) {
                    log.error("Attachment with id '" + oAttachment.getId() + "' doesn't have content associated with it.");
                    throw new ActivitiObjectNotFoundException(
                            "Attachment with id '" + oAttachment.getId() + "' doesn't have content associated with it.",
                            Attachment.class);
                }
                DataSource oDataSource = new ByteArrayDataSource(oInputStream_Attachment, sFileExt);
                if (oDataSource == null) {
                    log.error("Attachment: oDataSource == null");
                }
                
                log.info("1)oMultiPartEmail.isBoolHasAttachments()="+oMultiPartEmail.isBoolHasAttachments());
                // add the attachment
                oMultiPartEmail.attach(oDataSource, sFileName, sDescription);
                log.info("2)oMultiPartEmail.isBoolHasAttachments()="+oMultiPartEmail.isBoolHasAttachments());
                oMultiPartEmail.setBoolHasAttachments(true);
                log.info("3)oMultiPartEmail.isBoolHasAttachments()="+oMultiPartEmail.isBoolHasAttachments());
                
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
                
                log.info("oMultiPartEmail.attach: Ok!");
            }
        } else {
            log.error("aAttachment has nothing!");
            throw new ActivitiObjectNotFoundException("add the file to send");
        }

        // send the email
        oMultiPartEmail.send();
    }

}
