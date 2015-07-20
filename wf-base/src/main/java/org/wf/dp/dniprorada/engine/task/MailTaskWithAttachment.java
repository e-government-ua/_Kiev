package org.wf.dp.dniprorada.engine.task;

import java.io.InputStream;
import java.util.List;

import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.task.Attachment;
import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * 
 * @author BW
 * 
 */
@Component("mailTaskWithAttachment")
public class MailTaskWithAttachment extends Abstract_MailTaskCustom {

    private final static Logger log = LoggerFactory.getLogger(MailTaskWithAttachment.class);


    
    private Expression docName;

    @Override
    public void execute(DelegateExecution oExecution) throws Exception {
        
        MultiPartEmail oMultiPartEmail = MultiPartEmail_BaseFromTask(oExecution);
        
        List<Attachment> aAttachment = oExecution.getEngineServices().getTaskService().getProcessInstanceAttachments(oExecution.getProcessInstanceId());
        InputStream oInputStream_Attachment = null;
        String sFileName = "document";
        String sFileExt = "txt";
        for (Attachment oAttachment : aAttachment) {
            sFileName = oAttachment.getName();
            sFileExt = oAttachment.getType();
            oInputStream_Attachment = oExecution.getEngineServices().getTaskService().getAttachmentContent(oAttachment.getId());
            if (oInputStream_Attachment == null) {
                throw new ActivitiObjectNotFoundException("Attachment with id '" + oAttachment.getId() + "' doesn't have content associated with it.", Attachment.class);
            }
        }
        String sAttachName = getStringFromFieldExpression(this.docName, oExecution);
        log.info("sAttachName= " + sAttachName);
        if (aAttachment != null && !aAttachment.isEmpty()) {
            String sFileExtNew = sFileExt.startsWith("imag") ? sFileExt.substring(11) : sFileExt.substring(25);
            DataSource oDataSource = new ByteArrayDataSource(oInputStream_Attachment, "application/" + sFileExtNew);
            // add the attachment
            log.info("sFileName= " + sFileName);
            log.info("sFileExt= " + sFileExt);
            log.info("sFileExtNew= " + sFileExtNew);
            oMultiPartEmail.attach(oDataSource, sFileName + "." + sFileExtNew, sAttachName);
        } else {
            throw new ActivitiObjectNotFoundException("add the file to send");
        }

        // send the email
        oMultiPartEmail.send();
    }

}
