package org.activiti.rest.controller.adapter;

import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.activiti.rest.controller.entity.TaskAssignee;
import org.activiti.rest.controller.entity.TaskAssigneeI;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by diver on 4/8/15.
 */
public final class TaskAssigneeAdapter implements AdapterI<Task, TaskAssigneeI> {

    private static final String TEMPLATE = "yyyy-MM-dd HH:mm:ss.SSS";

    @Override
    public TaskAssigneeI apply(Task task) {
        TaskAssignee taskAssignee = new TaskAssignee();
        taskAssignee.setAssignee(task.getAssignee());
        taskAssignee.setCategory(task.getCategory());
        taskAssignee.setCreateTime(dateToString(task.getCreateTime()));
        taskAssignee.setDelegationState(enumToString(task.getDelegationState()));
        taskAssignee.setDescription(task.getDescription());
        taskAssignee.setDueDate(dateToString(task.getDueDate()));
        taskAssignee.setExecutionId(task.getExecutionId());
        taskAssignee.setFormKey(task.getFormKey());
        taskAssignee.setId(task.getId());
        taskAssignee.setName(task.getName());
        taskAssignee.setOwner(task.getOwner());
        taskAssignee.setParentTaskId(task.getParentTaskId());
        taskAssignee.setPriority(task.getPriority());
        taskAssignee.setProcessDifinitionId(task.getProcessDefinitionId());
        taskAssignee.setProcessInstanceId(task.getProcessInstanceId());
        taskAssignee.setSuspended(task.isSuspended());
        taskAssignee.setTaskDefinitionKey(task.getTaskDefinitionKey());
        taskAssignee.setTenantId(task.getTenantId());
        return taskAssignee;
    }

    private String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(TEMPLATE);
        return date == null ? null : format.format(date);
    }

    private String enumToString(DelegationState delegationState) {
        return delegationState == null ? null : delegationState.name();
    }
}
