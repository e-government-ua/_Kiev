package org.activiti.rest.controller.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
* Created by diver on 4/8/15.
*/
//TODO diver: Create JavaDoc
@JsonSerialize(as = TaskAssignee.class)
@JsonDeserialize(as = TaskAssignee.class)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public interface TaskAssigneeI {

    String getDelegationState();

    boolean isSuspended();

    String getId();

    String getName();

    String getDescription();

    int getPriority();

    String getOwner();

    String getAssignee();

    String getProcessInstanceId();

    String getExecutionId();

    String getProcessDefinitionId();

    String getCreateTime();

    String getTaskDefinitionKey();

    String getDueDate();

    String getCategory();

    String getParentTaskId();

    String getTenantId();

    String getFormKey();
}
