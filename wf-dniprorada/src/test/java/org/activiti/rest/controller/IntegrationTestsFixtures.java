package org.activiti.rest.controller;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by diver on 4/12/15.
 */
public final class IntegrationTestsFixtures {

    public static ProcessDefinition getProcessDefinitions() {
        ProcessDefinitionEntity entity = new ProcessDefinitionEntity();
        entity.setCategory("http://www.activiti.org/test");
        entity.setId("CivilCardAccountlRequest:1:9");
        entity.setName("Видача картки обліку об’єкта торговельного призначення");
        entity.setKey("CivilCardAccountlRequest");
        entity.setDescription("Описание процесса");
        entity.setVersion(1);
        entity.setResourceName("dnepr-2.bpmn");
        entity.setDeploymentId("1");
        entity.setDiagramResourceName("dnepr-2.CivilCardAccountlRequest.png");
        entity.setTenantId("diver");
        entity.setSuspensionState(2);
        return entity;
    }

    public static Task getTask() {
        TaskEntity entity = new TaskEntity();
        entity.setDelegationState(DelegationState.RESOLVED);
        entity.setId("38");
        entity.setName("Первый процесс пользователя kermit");
        entity.setDescription("Описание процесса");
        entity.setPriority(51);
        entity.setOwner("kermit-owner");
        entity.setAssignee("kermit-assignee");
        entity.setProcessInstanceId("12");
        entity.setExecutionId("1");
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(1428875494527L);
        entity.setCreateTime(calendar.getTime());
        entity.setTaskDefinitionKey("task-definition");
        calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(1428875496527L);
        entity.setDueDate(calendar.getTime());
        entity.setCategory("my-category");
        entity.setParentTaskId("2");
        entity.setTenantId("diver");
        entity.setFormKey("form-key-12");
        entity.setSuspensionState(2);
        entity.setProcessDefinitionId("21");
        return entity;
    }
}