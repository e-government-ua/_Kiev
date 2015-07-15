package org.wf.dp.dniprorada.engine.task;

import java.util.LinkedList;
import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Attachment;
import org.activiti.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.dao.BaseEntityDao;
import org.wf.dp.dniprorada.base.dao.FlowSlotDao;
import org.wf.dp.dniprorada.base.dao.FlowSlotTicketDao;
import org.wf.dp.dniprorada.base.model.AbstractModelTask;

/**
 * @author askosyr
 */
@Component("fileTaskInheritance")
public class FileTaskInheritance extends AbstractModelTask implements
		JavaDelegate {

	private static final transient Logger LOG = LoggerFactory.getLogger(FileTaskInheritance.class);

	@Autowired
	RedisService redisService;

	@Autowired
	TaskService taskService;

	@Autowired
	private BaseEntityDao baseEntityDao;

	@Autowired
	private FlowSlotDao flowSlotDao;

	@Autowired
	private FlowSlotTicketDao oFlowSlotTicketDao;

	private Expression aFieldInheritedAttachmentID;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String sInheritedAttachmentsIds = getStringFromFieldExpression(this.aFieldInheritedAttachmentID, execution);

		if (sInheritedAttachmentsIds == null) {
			LOG.error("aFieldInheritedAttachmentID field is not specified");
			return;
		}

		LOG.info("execution.getParentId():" + execution.getParentId());
		LOG.info("execution.getId()" + execution.getId());
		
		List<Attachment> attachments = getAttachmentsFromParentTasks(execution);

		List<Attachment> attachmentsToAdd = getInheritedAttachmentIdsFromTask(attachments, sInheritedAttachmentsIds);

		addAttachmentsToCurrentTask(attachmentsToAdd, execution);
	}

	private void addAttachmentsToCurrentTask(List<Attachment> attachmentsToAdd,
			DelegateExecution execution) {
		final String METHOD_NAME = "addAttachmentsToCurrentTask(List<Attachment> attachmentsToAdd, DelegateExecution execution)";
		LOG.trace("Entering method " + METHOD_NAME);

		TaskService taskService = execution.getEngineServices()
				.getTaskService();
		for (Attachment attachment : attachmentsToAdd) {
			Attachment newAttachment = taskService.createAttachment(
					attachment.getType(), execution.getId(),
					execution.getProcessInstanceId(), attachment.getName(),
					attachment.getDescription(),
					taskService.getAttachmentContent(attachment.getId()));
			LOG.info(String
					.format("Created new attachment for the task {0} with ID {1} from the attachment with ID {2}",
							execution.getId(), newAttachment.getId(),
							attachment.getId()));
		}
		LOG.trace("Exiting method " + METHOD_NAME);
	}

	protected List<Attachment> getInheritedAttachmentIdsFromTask(
			List<Attachment> attachments, String sInheritedAttachmentsIds) {
		final String METHOD_NAME = "getInheritedAttachmentIdsFromTask(List<Attachment> attachments, String sInheritedAttachmentsIds)";
		LOG.trace("Entering method " + METHOD_NAME);
		LOG.info("sInheritedAttachmentsIds:" + sInheritedAttachmentsIds);
		List<Attachment> res = new LinkedList<Attachment>();

		String[] attachIds = sInheritedAttachmentsIds.split(",");
		for (String attachId : attachIds) {
			for (Attachment attachment : attachments) {
				if (attachment.getId().equals(attachId)) {
					res.add(attachment);
					LOG.info(String
							.format("Found attachment with ID {0}. Adding to the current task",
									attachId));
					break;
				}
			}
		}
		LOG.trace("Exiting method " + METHOD_NAME);
		return res;
	}

	protected List<Attachment> getAttachmentsFromParentTasks(DelegateExecution execution) {
		final String METHOD_NAME = "getAttachmentsFromParentTasks(DelegateExecution execution)";
		LOG.trace("Entering method " + METHOD_NAME);
		
		List<Attachment> res = new LinkedList<Attachment>();

		LOG.info("execution.getProcessInstanceId():" + execution.getProcessInstanceId());
		res = execution.getEngineServices().getTaskService().getProcessInstanceAttachments(execution.getProcessInstanceId());
		LOG.info("res:" + res);
		
		LOG.trace("Exiting method " + METHOD_NAME);
		return res;
	}

}