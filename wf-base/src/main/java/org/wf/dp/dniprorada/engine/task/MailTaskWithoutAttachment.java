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
import org.wf.dp.dniprorada.util.Mail;

/**
 * 
 * @author BW
 * 
 */
@Component("MailTaskWithoutAttachment")
public class MailTaskWithoutAttachment extends Abstract_MailTaskCustom {

    private final static Logger log = LoggerFactory.getLogger(MailTaskWithoutAttachment.class);

    //private Expression saAttachmentsForSend;

    @Override
    public void execute(DelegateExecution oExecution) throws Exception {
        
        //MultiPartEmail oMultiPartEmail = MultiPartEmail_BaseFromTask(oExecution);
        Mail oMail = Mail_BaseFromTask(oExecution);

        // send the email
        //oMultiPartEmail.send();
        oMail.send();
    }

}
