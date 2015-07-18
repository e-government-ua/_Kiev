package org.activiti.rest.controller;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Taky on 7/14/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
public class ActivitiRestApiControllerDownloadTasksDataScenario {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Before
    public void initTaskServiceMock() {
        Task mockedTask = mock(Task.class);
        //stub data

    	List<Task> tasks = new ArrayList<>();
    	
    	tasks.add(mockedTask);

        TaskQuery taskQuery = Mockito.mock(TaskQuery.class);
    	when(taskQuery.taskCreatedAfter((Date) Mockito.anyObject())).thenReturn(taskQuery);
    	when(taskQuery.taskCreatedBefore((Date) Mockito.anyObject())).thenReturn(taskQuery);
        when(taskQuery.processDefinitionKey(Mockito.anyString())).thenReturn(taskQuery);
    	when(taskQuery.listPage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(tasks);
    	
    	when(taskService.createTaskQuery()).thenReturn(taskQuery);
    }

    @Test
    public void shouldSuccessfullyReturnFileResponse() throws Exception {
    	MvcResult result = mockMvc.perform(get("/rest/file/downloadTasksData").
                accept(MediaType.APPLICATION_JSON).
                param("sID_BP", "test").
                param("sID_State_BP", "PENDING").
                param("saFields", "nID_Task;sDateCreate;1").
                param("nASCI_Spliter", "18").
                param("sID_Codepage", "win1251").
                param("sDateAt", "2015-06-01").
                param("sDateTo", "2015-06-30").
                header("Authorization", "Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=")).andReturn();
    	
    	assertEquals(200, result.getResponse().getStatus());
    	assertEquals("text/csv;charset=windows-1251", result.getResponse().getContentType());
    }
    
    @Test
    public void shouldReturnError() throws Exception {
    	mockMvc.perform(get("/rest/file/downloadTasksData").
                accept(MediaType.APPLICATION_JSON).
                param("sID_BP", "").
                param("sID_State_BP", "PENDING").
                param("saFields", "nID_Task;sDateCreate;1").
                param("nASCI_Spliter", "18").
                param("sID_Codepage", "win1251").
                param("sDateAt", "2015-06-01").
                param("sDateTo", "2015-06-30").
                header("Authorization", "Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=")).
                andExpect(status().isInternalServerError());
    	
    }

    @Test
    public void testParseEnum(){
        assertEquals("expect", ActivitiRestApiController.parseEnumValue("expect"));
        assertEquals("expect", ActivitiRestApiController.parseEnumValue("test;expect"));
        assertEquals("expect", ActivitiRestApiController.parseEnumValue("expect;"));
        assertEquals("expect", ActivitiRestApiController.parseEnumValue("another;expect;"));
        assertEquals("", ActivitiRestApiController.parseEnumValue(""));
        assertEquals("", ActivitiRestApiController.parseEnumValue(null));
    }

}