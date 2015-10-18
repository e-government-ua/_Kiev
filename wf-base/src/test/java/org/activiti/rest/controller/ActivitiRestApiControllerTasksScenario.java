package org.activiti.rest.controller;

import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Assert;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.util.luna.CRCInvalidException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
public class ActivitiRestApiControllerTasksScenario {

    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Before
    public void initTaskServiceMock() {
        TaskQuery taskQueue = Mockito.mock(TaskQuery.class);
        List<Task> tasks = new ArrayList<>();
        tasks.add(IntegrationTestsFixtures.getTask());
        Mockito.when(taskQueue.list()).thenReturn(tasks);
        Mockito.when(taskQueue.taskAssignee(Mockito.eq("kermit"))).thenReturn(taskQueue);
        Mockito.when(taskService.createTaskQuery()).thenReturn(taskQueue);
    }

    @Ignore
    @Test
    public void shouldSuccessfullyReturnTasksJsonResponse() throws Exception {
        mockMvc.perform(get("/rest/tasks/kermit").
                accept(MediaType.APPLICATION_JSON).
                header("Authorization", "Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=")).
                andExpect(status().isOk()).
                andExpect(content().contentType("application/json;charset=UTF-8")).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].*", hasSize(18))).
                andExpect(jsonPath("$[0].id", is("38"))).
                andExpect(jsonPath("$[0].name", is("Первый процесс пользователя kermit"))).
                andExpect(jsonPath("$[0].description", is("Описание процесса"))).
                andExpect(jsonPath("$[0].priority", is(51))).
                andExpect(jsonPath("$[0].owner", is("kermit-owner"))).
                andExpect(jsonPath("$[0].assignee", is("kermit-assignee"))).
                andExpect(jsonPath("$[0].processInstanceId", is("12"))).
                andExpect(jsonPath("$[0].executionId", is("1"))).
                andExpect(jsonPath("$[0].createTime", is("2015-04-13 00:51:34.527"))).
                andExpect(jsonPath("$[0].taskDefinitionKey", is("task-definition"))).
                andExpect(jsonPath("$[0].delegationState", is("RESOLVED"))).
                andExpect(jsonPath("$[0].dueDate", is("2015-04-13 00:51:36.527"))).
                andExpect(jsonPath("$[0].category", is("my-category"))).
                andExpect(jsonPath("$[0].parentTaskId", is("2"))).
                andExpect(jsonPath("$[0].tenantId", is("diver"))).
                andExpect(jsonPath("$[0].formKey", is("form-key-12"))).
                andExpect(jsonPath("$[0].processDefinitionId", is("21"))).
                andExpect(jsonPath("$[0].suspended", is(true)));
    }

    @Test
    public void shouldReturnJsonErrorMessageOnAnyRuntimeException() throws Exception {
        Mockito.when(taskService.createTaskQuery()).
                thenThrow(new NullPointerException("Parameter not specified"));
        mockMvc.perform(get("/rest/tasks/kermit").
                accept(MediaType.APPLICATION_JSON).
                header("Authorization", "Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=")).
                andExpect(status().isInternalServerError()).
                andExpect(content().contentType("application/json;charset=UTF-8")).
                andExpect(jsonPath("$.*", hasSize(2))).
                andExpect(jsonPath("$.code", is("SYSTEM_ERR"))).
                andExpect(jsonPath("$.message", is("Parameter not specified")));
        Mockito.reset(taskService);
    }

    @Test
    public void testGetTaskByOrder() throws Exception {

        final String getTasksByOrderUrl = "/rest/tasks/getTasksByOrder";

        String jsonData = mockMvc.perform(get(getTasksByOrderUrl).
                param("nID_Protected", "123123")).
                andExpect(status().isForbidden()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        Map<String, String> res = JsonRestUtils.readObject(jsonData, Map.class);
        Assert.assertEquals(new CRCInvalidException().getMessage(), res.get("message"));

        TaskQuery taskQuery = Mockito.mock(TaskQuery.class);
        Mockito.when(taskService.createTaskQuery()).thenReturn(taskQuery);
        Mockito.when(taskQuery.processInstanceId("12345")).thenReturn(taskQuery);
        Mockito.when(taskQuery.singleResult()).thenReturn(null);

        jsonData = mockMvc.perform(get(getTasksByOrderUrl).
                param("nID_Protected", "123451")).
                andExpect(status().isForbidden()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        res = JsonRestUtils.readObject(jsonData, Map.class);
        Assert.assertEquals(new RecordNotFoundException().getMessage(), res.get("message"));

        HistoricTaskInstance historicTaskInstance = Mockito.mock(HistoricTaskInstance.class);
        String processTaskId = "777";
        Mockito.when(historicTaskInstance.getProcessInstanceId()).thenReturn(processTaskId);

        List<String> taskIds = Arrays.asList("1", "2");
        Mockito.when(taskService.createTaskQuery()).thenReturn(taskQuery);
        Mockito.when(taskQuery.processInstanceId(processTaskId)).thenReturn(taskQuery);
        List<Task> tasks = new ArrayList<>();
        for (String taskId : taskIds) {
            tasks.add(new TaskEntity(taskId));
        }
        Mockito.when(taskQuery.list()).thenReturn(tasks);

        jsonData = mockMvc.perform(get(getTasksByOrderUrl).
                param("nID_Protected", "123451")).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        String[] taskIdsResult = JsonRestUtils.readObject(jsonData, String[].class);
        Assert.assertEquals(Arrays.asList(taskIdsResult), taskIds);
    }

}
