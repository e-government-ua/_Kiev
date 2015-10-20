package org.activiti.rest.controller;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.junit.Before;
import org.junit.Ignore;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ASkosyr on 6/28/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
public class ActivitiRestApiControllerDownloadTimingScenario {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private HistoryService historyService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Before
    public void initHistoryServiceMock() {
        HistoricTaskInstance historicTask = Mockito.mock(HistoricTaskInstance.class);
        Mockito.when(historicTask.getAssignee()).thenReturn("Business user 1");
        Mockito.when(historicTask.getStartTime()).thenReturn(Calendar.getInstance().getTime());
        Mockito.when(historicTask.getDurationInMillis()).thenReturn(86400l);
        Mockito.when(historicTask.getName()).thenReturn("Name of task");
        List<HistoricTaskInstance> historicTasks = new ArrayList<HistoricTaskInstance>();

        historicTasks.add(historicTask);

        HistoricTaskInstanceQuery historicTaskInstance = Mockito.mock(HistoricTaskInstanceQuery.class);
        Mockito.when(historicTaskInstance.taskCompletedAfter((Date) Mockito.anyObject()))
                .thenReturn(historicTaskInstance);
        Mockito.when(historicTaskInstance.taskCompletedBefore((Date) Mockito.anyObject()))
                .thenReturn(historicTaskInstance);
        Mockito.when(historicTaskInstance.processDefinitionKey(Mockito.anyString())).thenReturn(historicTaskInstance);
        Mockito.when(historicTaskInstance.listPage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(historicTasks);

        Mockito.when(historyService.createHistoricTaskInstanceQuery()).thenReturn(historicTaskInstance);
    }

    @Ignore
    @Test
    public void shouldSuccessfullyReturnFileResponse() throws Exception {
        MvcResult result = mockMvc.perform(get("/rest/file/download_bp_timing").
                accept(MediaType.APPLICATION_JSON).
                param("sID_BP_Name", "test").
                param("sDateAt", "2015-06-01").
                param("sDateTo", "2015-06-30").
                header("Authorization", "Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=")).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("text/csv;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void shouldReturnError() throws Exception {
        mockMvc.perform(get("/rest/file/download_bp_timing").
                accept(MediaType.APPLICATION_JSON).
                param("sID_BP_Name", "").
                param("sDateAt", "2015-06-01").
                param("sDateTo", "2015-06-30").
                header("Authorization", "Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=")).
                andExpect(status().isInternalServerError());

    }

}