package org.activiti.rest.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.dao.place.PlaceHierarchyTree;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author dgroup
 * @since 09.08.2015
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
@ActiveProfiles("default")
public class PlaceControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @Ignore(value = "Should be run only on test evn, but 'Test' profile is working on local env.")
    public void getPlacesTreeById() {
        try {

            String jsonData = mockMvc
                    .perform(get("/getPlacesTree").param("nID", "459"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            PlaceHierarchyTree tree = JsonRestUtils.readObject(jsonData, PlaceHierarchyTree.class);
            assertNotNull(tree);

        } catch (Exception e) {
            fail(ExceptionUtils.getStackTrace(e));
        }
    }

    @Test
    @Ignore(value = "Should be run only on test evn, but 'Test' profile is working on local env.")
    public void getPlace() {
        try {

            String jsonData = mockMvc
                    .perform(get("/getPlace").param("nID", "459"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            PlaceHierarchyTree tree = JsonRestUtils.readObject(jsonData, PlaceHierarchyTree.class);
            assertNotNull(tree);
            assertEquals("IDs aren't match", 459L, tree.getPlace().getId().longValue());

        } catch (Exception e) {
            fail(ExceptionUtils.getStackTrace(e));
        }
    }
}