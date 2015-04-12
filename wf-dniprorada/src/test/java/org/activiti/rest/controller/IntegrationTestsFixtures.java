package org.activiti.rest.controller;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;

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
}