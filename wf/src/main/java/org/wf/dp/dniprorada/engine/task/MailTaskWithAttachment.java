package org.wf.dp.dniprorada.engine.task;

import java.io.InputStream;
import java.util.List;

import javax.activation.DataSource;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Attachment;
import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Шаг для отправки уведомления с прикрепленным документом
 * 
 * @author inna
 * 
 */
@Component("mailTaskWithAttachment")
public class MailTaskWithAttachment implements JavaDelegate {

	private final static Logger log = LoggerFactory
			.getLogger(MailTaskWithAttachment.class);

	@Autowired
	TaskService taskService;

	@Value("${mailServerHost}")
	private String mailServerHost;

	@Value("${mailServerPort}")
	private String mailServerPort;

	@Value("${mailServerDefaultFrom}")
	private String mailServerDefaultFrom;

	@Value("${mailServerUsername}")
	private String mailServerUsername;

	@Value("${mailServerPassword}")
	private String mailServerPassword;

	private Expression from;
	private Expression to;
	private Expression subject;
	private Expression text;
	private Expression docName;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<Attachment> attachmentList = execution
				.getEngineServices()
				.getTaskService()
				.getProcessInstanceAttachments(execution.getProcessInstanceId());
		InputStream attachmentStream = null;
		String nameFile = "document";
		String typeFile = "txt";
		for (Attachment attachment : attachmentList) {
			nameFile = attachment.getName();
			typeFile = attachment.getType();
			attachmentStream = execution.getEngineServices().getTaskService()
					.getAttachmentContent(attachment.getId());
			if (attachmentStream == null) {
				throw new ActivitiObjectNotFoundException(
						"Attachment with id '" + attachment.getId()
								+ "' doesn't have content associated with it.",
						Attachment.class);
			}

		}

		String fromStr = getStringFromFieldExpression(this.from, execution);
		String toStr = getStringFromFieldExpression(this.to, execution);
		String subjectStr = getStringFromFieldExpression(this.subject,
				execution);
		String textStr = getStringFromFieldExpression(this.text, execution);
		String docNameStr = getStringFromFieldExpression(this.docName,
				execution);
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName(mailServerHost);
		email.addTo(toStr, "dniprorada");
		email.setFrom(fromStr, "dniprorada@egov.ua");
		email.setSubject(subjectStr);
		email.setMsg(textStr);
		email.setAuthentication(mailServerUsername, mailServerPassword);
		email.setSmtpPort(Integer.valueOf(mailServerPort));
		email.setSSL(true);
		email.setTLS(true);
		if(attachmentList!=null && !attachmentList.isEmpty()){
		String exp = "";
		if (typeFile.startsWith("imag")) {
			exp = typeFile.substring(11);
		} else {
			exp = typeFile.substring(25);
		}
		DataSource source = new ByteArrayDataSource(attachmentStream,
				"application/" + exp);

		// add the attachment
		email.attach(source, nameFile + "." + exp, docNameStr);
		}else{
			throw new ActivitiObjectNotFoundException(
					"add the file to send");
		}
		// send the email
		email.send();
	}

	protected String getStringFromFieldExpression(Expression expression,
			DelegateExecution execution) {
		if (expression != null) {
			Object value = expression.getValue(execution);
			if (value != null) {
				return value.toString();
			}
		}
		return null;
	}

}
