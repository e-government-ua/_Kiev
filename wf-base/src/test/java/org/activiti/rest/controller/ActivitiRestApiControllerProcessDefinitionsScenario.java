package org.activiti.rest.controller;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by diver on 4/12/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
public class ActivitiRestApiControllerProcessDefinitionsScenario {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private RepositoryService repositoryService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Before
    public void initRepositoryServiceMock() {
        List<ProcessDefinition> processDefinitionQueryList = new ArrayList<>();
        processDefinitionQueryList.add(IntegrationTestsFixtures.getProcessDefinitions());
        ProcessDefinitionQuery processDefinitionQuery = Mockito.mock(ProcessDefinitionQuery.class);
        Mockito.when(processDefinitionQuery.list()).thenReturn(processDefinitionQueryList);
        Mockito.when(processDefinitionQuery.latestVersion()).thenReturn(processDefinitionQuery);
        Mockito.when(repositoryService.createProcessDefinitionQuery()).thenReturn(processDefinitionQuery);
    }

    @Test
    public void shouldSuccessfullyReturnProcessDefinitionsJsonResponse() throws Exception {
        if (1 == 1) {
            return;
        } //TODO uncomment
        mockMvc.perform(get("/rest/process-definitions").
                accept(MediaType.APPLICATION_JSON).
                header("Authorization", "Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=")).
                andExpect(status().isOk()).
                andExpect(content().contentType("application/json;charset=UTF-8")).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].*", hasSize(11))).
                andExpect(jsonPath("$[0].id", is("CivilCardAccountlRequest:1:9"))).
                andExpect(jsonPath("$[0].name", is("Видача картки обліку об’єкта торговельного призначення"))).
                andExpect(jsonPath("$[0].key", is("CivilCardAccountlRequest"))).
                andExpect(jsonPath("$[0].description", is("Описание процесса"))).
                andExpect(jsonPath("$[0].version", is(1))).
                andExpect(jsonPath("$[0].resourceName", is("dnepr-2.bpmn"))).
                andExpect(jsonPath("$[0].deploymentId", is("1"))).
                andExpect(jsonPath("$[0].diagramResourceName", is("dnepr-2.CivilCardAccountlRequest.png"))).
                andExpect(jsonPath("$[0].tenantId", is("diver"))).
                andExpect(jsonPath("$[0].id", is("CivilCardAccountlRequest:1:9"))).
                andExpect(jsonPath("$[0].suspended", is(true)));
    }

    @Test
    public void shouldReturnJsonErrorMessageOnAnyRuntimeException() throws Exception {
        if (1 == 1) {
            return;
        } //TODO uncomment
        Mockito.when(repositoryService.createProcessDefinitionQuery()).
                thenThrow(new NullPointerException("Parameter not specified"));
        mockMvc.perform(get("/rest/process-definitions").
                accept(MediaType.APPLICATION_JSON).
                header("Authorization", "Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=")).
                andExpect(status().isInternalServerError()).
                andExpect(content().contentType("application/json;charset=UTF-8")).
                andExpect(jsonPath("$.*", hasSize(2))).
                andExpect(jsonPath("$.code", is("SYSTEM_ERR"))).
                andExpect(jsonPath("$.message", is("Parameter not specified")));
        Mockito.reset(repositoryService);
    }
}
