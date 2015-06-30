package org.activiti.rest.controller;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wf.dp.dniprorada.model.SubjectMessage;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ActivitiRestSubjectMessageControllerScenario {

    public static final String APPLICATION_JSON_VALUE = "application/json";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void firstShouldSuccessfullySetMassage() throws Exception {
        mockMvc.perform(post("/messages/setMessage").
                contentType(MediaType.APPLICATION_JSON).
                param("sHead", "expect").
                param("sBody", "XXX").
                param("sContacts", "093").
                param("sData", "some data").
                param("nID_Subject", "34").
                param("sMail", "ukr.net")).
                andExpect(status().isOk());
    }

    @Test
    public void nextShouldSuccessfullySetMassageWithDefaultSubjectID() throws Exception {
        mockMvc.perform(post("/messages/setMessage").
                contentType(MediaType.APPLICATION_JSON).
                param("sHead", "expect").
                param("sBody", "XXX").
                param("sMail", "ukr.net")).
                andExpect(status().isOk());
    }

    @Test
    public void shouldFailedNoObligatedParam() throws Exception {
        mockMvc.perform(post("/messages/setMessage").
                contentType(MediaType.APPLICATION_JSON).
                param("sBody", "XXXXXxxx").
                param("sMail", "ukr.ed")).
                andExpect(status().isBadRequest());
    }

    @Test
    @Ignore
    public void shouldSuccessfullyGetMessageById() throws Exception {
        String jsonData = mockMvc.perform(get("/messages/getMessage").
                param("nID", "1")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.sHead", is("expect"))).
                andExpect(jsonPath("$.sBody", is("XXX"))).
                andExpect(jsonPath("$.sDate", not(empty()))).
                andExpect(content().contentType(APPLICATION_JSON_VALUE)).
                andReturn().getResponse().getContentAsString();
        SubjectMessage actualSubjectMessage = JsonRestUtils.readObject(jsonData, SubjectMessage.class);
        assertNotNull(actualSubjectMessage);
    }

    @Test
    @Ignore
    public void shouldSuccessfullyGetMessages() throws Exception {
        String jsonData = mockMvc.perform(get("/messages/getMessages").
                param("nID", "null")).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_VALUE)).
                andExpect(jsonPath("$.sHead", not(empty()))).
                andExpect(jsonPath("$.sBody", not(empty()))).
                andExpect(jsonPath("$[0].nID_Subject", is(34))).
                andExpect(jsonPath("$[1].nID_Subject", is(0))).
                andReturn().getResponse().getContentAsString();
        List<SubjectMessage> actualSubjectMessage = JsonRestUtils.readObject(jsonData, List.class);
        assertEquals(2, actualSubjectMessage.size());
    }

}
