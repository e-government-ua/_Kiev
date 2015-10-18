package org.wf.dp.dniprorada.engine.task;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.Attachment;
import org.activiti.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.dao.FlowSlotDao;
import org.wf.dp.dniprorada.base.dao.FlowSlotTicketDao;
import org.wf.dp.dniprorada.base.model.AbstractModelTask;

import java.util.LinkedList;
import java.util.List;

/**
 * @author askosyr
 */
@Component("fileTaskInheritance")
public class FileTaskInheritance extends AbstractModelTask implements TaskListener {

    private static final long serialVersionUID = 1L;

    private static final transient Logger LOG = LoggerFactory.getLogger(FileTaskInheritance.class);

    @Autowired
    RedisService redisService;

    @Autowired
    TaskService taskService;

    @Autowired
    private FlowSlotDao flowSlotDao;

    @Autowired
    private FlowSlotTicketDao oFlowSlotTicketDao;

    private Expression aFieldInheritedAttachmentID;
    //private Expression osBody;

    @Override
    public void notify(DelegateTask task) {

        DelegateExecution execution = task.getExecution();
        //task.getId()
        //                Util.replacePatterns(execution, task, LOG);
        LOG.info("[notify]Util.replacePatterns:Ok(skiped)");

        try {

            String sInheritedAttachmentsIds = getStringFromFieldExpression(this.aFieldInheritedAttachmentID, execution);
            LOG.info("sInheritedAttachmentsIds(0)=" + sInheritedAttachmentsIds);

            if (sInheritedAttachmentsIds == null || "".equals(sInheritedAttachmentsIds.trim())) {
                LOG.error("aFieldInheritedAttachmentID field is not specified");
                return;
            }

            LOG.info("sInheritedAttachmentsIds(1)=" + sInheritedAttachmentsIds);
            LOG.info("task.getId()" + task.getId());

            List<Attachment> attachments = getAttachmentsFromParentTasks(execution);

            List<Attachment> attachmentsToAdd = getInheritedAttachmentIdsFromTask(attachments,
                    sInheritedAttachmentsIds);

            addAttachmentsToCurrentTask(attachmentsToAdd, task);
        } catch (Exception oException) {
            LOG.error("[notify]", oException);
        }

        //runtimeService.setVariable(snID_Process, "sID_Payment", sID_Payment);
        //String sBody=(String)execution.getVariable("sBody");
        //Util.replacePatterns(execution, this.osBody, LOG);

    }

    private void addAttachmentsToCurrentTask(List<Attachment> attachmentsToAdd,
            DelegateTask task) {
        final String METHOD_NAME = "addAttachmentsToCurrentTask(List<Attachment> attachmentsToAdd, DelegateExecution execution)";
        LOG.trace("Entering method " + METHOD_NAME);

        TaskService taskService = task.getExecution().getEngineServices()
                .getTaskService();
        int n = 0;
        for (Attachment attachment : attachmentsToAdd) {
            n++;
            LOG.info("[addAttachmentsToCurrentTask](n=" + n + "):task.getId()" + task.getId());
            LOG.info("[addAttachmentsToCurrentTask](n=" + n + "):task.getExecution().getProcessInstanceId()" + task
                    .getExecution().getProcessInstanceId());
            LOG.info("[addAttachmentsToCurrentTask](n=" + n + "):attachment.getName()" + attachment.getName());
            LOG.info("[addAttachmentsToCurrentTask](n=" + n + "):attachment.getDescription()" + attachment
                    .getDescription());
            Attachment newAttachment = taskService.createAttachment(
                    attachment.getType(), task.getId(),
                    task.getExecution().getProcessInstanceId(), attachment.getName(),
                    attachment.getDescription(),
                    taskService.getAttachmentContent(attachment.getId()));
            LOG.info(String
                    .format("Created new attachment for the task {0} with ID {1} from the attachment with ID {2}",
                            task.getId(), newAttachment.getId(),
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
            LOG.info("[getInheritedAttachmentIdsFromTask]:attachId" + attachId);
            int n = 0;
            for (Attachment attachment : attachments) {
                n++;
                LOG.info("[getInheritedAttachmentIdsFromTask](n=" + n + "):attachment.getId()" + attachment.getId());

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
        res = execution.getEngineServices().getTaskService()
                .getProcessInstanceAttachments(execution.getProcessInstanceId());
        LOG.info("res:" + res);

        LOG.trace("Exiting method " + METHOD_NAME);
        return res;
    }

}