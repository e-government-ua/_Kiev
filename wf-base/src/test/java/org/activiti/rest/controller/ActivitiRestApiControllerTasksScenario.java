package org.activiti.rest.controller;

import org.activiti.engine.*;
import org.activiti.engine.task.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.web.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.*;
import org.springframework.web.context.*;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by diver on 4/12/15.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
public class ActivitiRestApiControllerTasksScenario {

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
}
