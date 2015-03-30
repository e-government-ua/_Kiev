package org.wf.dp.dniprorada.engine.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Шаг для отправки уведомления с прикрепленным документом
 * @author inna
 *
 */
@Component("mailTaskWithAttachment")
public class MailTaskWithAttachment implements JavaDelegate {

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
	private Expression path;
	private Expression docName;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String fromStr = getStringFromFieldExpression(this.from, execution);
		String toStr = getStringFromFieldExpression(this.to, execution);
		String subjectStr = getStringFromFieldExpression(this.subject,
				execution);
		String textStr = getStringFromFieldExpression(this.text, execution);
		String pathStr = getStringFromFieldExpression(this.path, execution);
		String docNameStr = getStringFromFieldExpression(this.docName, execution);

		EmailAttachment emailAttachment = new EmailAttachment();
		emailAttachment.setPath(pathStr);
		emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
		emailAttachment.setDescription(docNameStr==null?"":docNameStr);
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
		email.attach(emailAttachment);
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
