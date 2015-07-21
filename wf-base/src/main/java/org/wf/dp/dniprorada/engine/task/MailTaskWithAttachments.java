package org.wf.dp.dniprorada.engine.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

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

                // Create the attachment
                /*EmailAttachment oEmailAttachment = new EmailAttachment();
                oEmailAttachment.setPath("mypictures/john.jpg");
                oEmailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
                oEmailAttachment.setDescription("Picture of John");
                oEmailAttachment.setName("John");*/
                
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
