package org.activiti.rest.controller;

import junit.framework.Assert;
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
import org.wf.dp.dniprorada.model.SubjectMessageType;

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

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void firstShouldSuccessfullySetAndGetMassage() throws Exception {
       String messageBody = "XXX";
       String jsonAfterSave = mockMvc.perform(post("/messages/setMessage").
                contentType(MediaType.APPLICATION_JSON).
                param("sHead", "expect").
                param("sBody", messageBody).
                param("sContacts", "093").
                param("sData", "some data").
                param("sMail", "ukr.net")).
                andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
       SubjectMessage savedMessage = JsonRestUtils.readObject(jsonAfterSave, SubjectMessage.class);
       assertNotNull(savedMessage.getId());
       assertNotNull(savedMessage.getSubjectMessageType());
       assertEquals(SubjectMessageType.DEFAULT.getId(), savedMessage.getSubjectMessageType().getId());
       assertEquals(messageBody, savedMessage.getBody());
       assertEquals(0L, savedMessage.getId_subject().longValue());

       String jsonAfterGet = mockMvc.perform(get("/messages/getMessage").param("nID", "" + savedMessage.getId())).
               andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
       assertEquals(jsonAfterSave, jsonAfterGet);
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

}
