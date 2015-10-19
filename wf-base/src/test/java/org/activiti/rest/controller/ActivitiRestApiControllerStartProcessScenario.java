package org.activiti.rest.controller;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
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
public class ActivitiRestApiControllerStartProcessScenario {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private RuntimeService runtimeService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Before
    public void initTaskServiceMock() {
        ProcessInstance processInstance = Mockito.mock(ProcessInstance.class);
        Mockito.when(processInstance.getProcessInstanceId()).thenReturn("38");
        Mockito.when(processInstance.getId()).thenReturn("12");
        Mockito.when(runtimeService.startProcessInstanceByKey(Mockito.eq("kermit"))).
                thenReturn(processInstance);
    }

    @Test
    public void shouldSuccessfullyReturnStartProcessJsonResponse() throws Exception {
        mockMvc.perform(get("/rest/start-process/kermit").
                accept(MediaType.APPLICATION_JSON).
                header("Authorization", "Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=")).
                andExpect(status().isOk()).
                andExpect(content().contentType("application/json;charset=UTF-8")).
                andExpect(jsonPath("$.*", hasSize(1))).
                andExpect(jsonPath("$.id", is("38")));
    }

    @Test
    public void shouldReturnJsonErrorMessageOnAnyRuntimeException() throws Exception {
        Mockito.when(runtimeService.startProcessInstanceByKey(Mockito.anyString())).
                thenThrow(new NullPointerException("Parameter not specified"));
        mockMvc.perform(get("/rest/start-process/kermit").
                accept(MediaType.APPLICATION_JSON).
                header("Authorization", "Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=")).
                andExpect(status().isInternalServerError()).
                andExpect(content().contentType("application/json;charset=UTF-8")).
                andExpect(jsonPath("$.*", hasSize(2))).
                andExpect(jsonPath("$.code", is("SYSTEM_ERR"))).
                andExpect(jsonPath("$.message", is("Parameter not specified")));
        Mockito.reset(runtimeService);
    }
}
