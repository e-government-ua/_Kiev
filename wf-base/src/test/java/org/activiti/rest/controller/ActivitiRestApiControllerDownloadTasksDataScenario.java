package org.activiti.rest.controller;

import org.activiti.engine.FormService;
import org.activiti.engine.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

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
    private TaskService taskService;
    @Autowired
    private FormService formService;

    //    @Before
    //    public void setUp() {
    //        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    //    }
    //
    //    @Before
    //    public void initTaskServiceMock() {
    //        Task mockedTask = mock(Task.class);
    //        when(mockedTask.getId()).thenReturn("42");
    //        when(mockedTask.getCreateTime()).thenReturn(DateTime.now().withYear(2015).withMonthOfYear(1).withDayOfMonth(21)
    //                .withHourOfDay(9).withMinuteOfHour(45).withSecondOfMinute(33).withMillisOfSecond(0).toDate());
    //
    //        TaskQuery taskQuery = Mockito.mock(TaskQuery.class);
    //    	when(taskQuery.taskCreatedAfter((Date) Mockito.anyObject())).thenReturn(taskQuery);
    //    	when(taskQuery.taskCreatedBefore((Date) Mockito.anyObject())).thenReturn(taskQuery);
    //        when(taskQuery.processDefinitionKey(Mockito.anyString())).thenReturn(taskQuery);
    //        when(taskQuery.taskDelegationState(DelegationState.PENDING)).thenReturn(taskQuery);
    //    	when(taskQuery.listPage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Arrays.asList(mockedTask));
    //
    //    	when(taskService.createTaskQuery()).thenReturn(taskQuery);
    //
    //        StringFormType stringFormType = mock(StringFormType.class);
    //        when(stringFormType.getName()).thenReturn("string");
    //
    //        FormProperty formProperty = mock(FormProperty.class);
    //        when(formProperty.getId()).thenReturn("bankIdlastName");
    //        when(formProperty.getType()).thenReturn(stringFormType);
    //        when(formProperty.getValue()).thenReturn("LastName");
    //
    //        TaskFormData data = mock(TaskFormData.class);
    //        when(data.getFormProperties()).thenReturn(Arrays.asList(formProperty));
    //
    //        when(formService.getTaskFormData("42")).thenReturn(data);
    //    }
    //
    @Test
    public void shouldSuccessfullyReturnFileResponse() throws Exception {
        //    	MvcResult result = mockMvc.perform(get("/rest/file/downloadTasksData").
        //                accept(MediaType.APPLICATION_JSON).
        //                param("sID_BP", "test").
        //                param("sID_State_BP", "PENDING").
        //                param("saFields", "nID_Task;bankIdlastName").
        //                param("nASCI_Spliter", "18").
        //                param("sDateAt", "2015-06-01").
        //                header("Authorization", "Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=")).andReturn();
        //
        //    	assertEquals("expected no error", 200, result.getResponse().getStatus());
        //    	assertEquals("expected cp1251 encoding", "text/csv;charset=windows-1251", result.getResponse().getContentType());
        //        assertEquals("expected correct content", "\"nID_Task\"\u0012\"sDateCreate\"\u0012\"bankIdlastName\"\n" +
        //                "\"42\"\u0012\"2015-01-21 09:45:33\"\u0012\"LastName\"\n", result.getResponse().getContentAsString());
    }
    //
    //    @Test
    //    public void shouldReturnError() throws Exception {
    //    	mockMvc.perform(get("/rest/file/downloadTasksData").
    //                accept(MediaType.APPLICATION_JSON).
    //                param("sID_BP", "").
    //                param("saFields", "nID_Task;sDateCreate;1").
    //                param("nASCI_Spliter", "18").
    //                param("sID_Codepage", "win1251").
    //                header("Authorization", "Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=")).
    //                andExpect(status().isInternalServerError());
    //
    //    }
    //
    //    @Test
    //    public void testParseEnum(){
    //        assertEquals("expect", ActivitiRestApiController.parseEnumValue("expect"));
    //        assertEquals("expect", ActivitiRestApiController.parseEnumValue("test;expect"));
    //        assertEquals("expect", ActivitiRestApiController.parseEnumValue("expect;"));
    //        assertEquals("expect", ActivitiRestApiController.parseEnumValue("another;expect;"));
    //        assertEquals("", ActivitiRestApiController.parseEnumValue(""));
    //        assertEquals("", ActivitiRestApiController.parseEnumValue(null));
    //    }

}