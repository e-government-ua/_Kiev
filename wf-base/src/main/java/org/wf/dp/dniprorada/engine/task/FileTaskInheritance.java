package org.wf.dp.dniprorada.engine.task;

import java.util.LinkedList;
import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
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

		List<Attachment> attachments = getAttachmentsFromParentTasks(execution.getParentId());

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
		if (res.isEmpty() && !attachments.isEmpty()){
			LOG.info("!!! Debug case. Adding first attachment from the list and print its properties");
			res.add(attachments.get(0));
			LOG.info("!!! DEBUG case. Adding attachment: " + attachments.get(0).getId() + "|" + attachments.get(0).getName() + "|" +
					attachments.get(0).getDescription() + "|" + attachments.get(0).getTaskId());
		}
		LOG.trace("Exiting method " + METHOD_NAME);
		return res;
	}

	protected List<Attachment> getAttachmentsFromParentTasks(String taskId) {
		final String METHOD_NAME = "getAttachmentsFromParentTasks(String taskId)";
		LOG.trace("Entering method " + METHOD_NAME);
		
		List<Attachment> res = new LinkedList<Attachment>();

		List<Task> tasks = taskService.createTaskQuery().taskId(taskId).list();
		if (tasks != null && !tasks.isEmpty()) {
			Task task = tasks.iterator().next();
			res.addAll(taskService.getTaskAttachments(taskId));

			LOG.info(String.format("Took all the attachments from task with ID {0} ", taskId));
			if (task.getParentTaskId() != null) {
				LOG.info(String.format("Will take attachments from parent task now with ID {0} ", task.getParentTaskId()));
				res.addAll(getAttachmentsFromParentTasks(task.getParentTaskId()));
			}
		}

		LOG.trace("Exiting method " + METHOD_NAME);
		return res;
	}

}