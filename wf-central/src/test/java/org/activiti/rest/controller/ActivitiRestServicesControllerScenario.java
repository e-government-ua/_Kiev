package org.activiti.rest.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wf.dp.dniprorada.model.Category;
import org.wf.dp.dniprorada.model.Region;
import org.wf.dp.dniprorada.model.Service;
import org.wf.dp.dniprorada.util.JsonRestUtils;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
public class ActivitiRestServicesControllerScenario {
   public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
   @Autowired
   private WebApplicationContext webApplicationContext;

   private MockMvc mockMvc;

   @Before
   public void setUp() {
      mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
   }

   @Test
   public void shouldSuccessfullyGetAndSetServicesTree() throws Exception {
      String jsonData = mockMvc.perform(get("/services/getServicesTree")).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andExpect(jsonPath("$", not(empty()))).
              andReturn().getResponse().getContentAsString();
      Category[] categoriesBeforeChange = JsonRestUtils.readObject(jsonData, Category[].class);

      String testName = "CategoryName438";
      Assert.assertNotEquals(categoriesBeforeChange[0].getName(), testName);
      categoriesBeforeChange[0].setName(testName);

      mockMvc.perform(post("/services/setServicesTree").content(JsonRestUtils.toJson(categoriesBeforeChange)).
              accept(MediaType.APPLICATION_JSON)).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andExpect(jsonPath("$[0].sName", is(testName)));

      jsonData = mockMvc.perform(get("/services/getServicesTree")).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andReturn().getResponse().getContentAsString();
      Category[] categoriesAfterChange = JsonRestUtils.readObject(jsonData, Category[].class);
      Assert.assertEquals(categoriesAfterChange[0].getName(), testName);
   }


   @Test
   public void shouldSuccessfullyGetAndSetService() throws Exception {
      String jsonData = mockMvc.perform(get("/services/getService").
              param("nID", "1")).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andExpect(jsonPath("$.nID", is(1))).
              andExpect(jsonPath("$.sName", not(empty()))).
              andReturn().getResponse().getContentAsString();
      Service serviceBeforeChange = JsonRestUtils.readObject(jsonData, Service.class);

      String testName = "ServiceName123";
      Assert.assertNotEquals(serviceBeforeChange.getName(), testName);
      serviceBeforeChange.setName(testName);

      mockMvc.perform(post("/services/setService").content(JsonRestUtils.toJson(serviceBeforeChange)).
              accept(MediaType.APPLICATION_JSON)).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andExpect(jsonPath("$.nID", is(1))).
              andExpect(jsonPath("$.sName", is(testName)));

      jsonData = mockMvc.perform(get("/services/getService").
              param("nID", "1")).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andReturn().getResponse().getContentAsString();
      Service serviceAfterChange = JsonRestUtils.readObject(jsonData, Service.class);
      Assert.assertEquals(serviceAfterChange.getName(), testName);
   }

   @Test
   public void shouldSuccessfullyGetAndSetPlaces() throws Exception {
      String jsonData = mockMvc.perform(get("/services/getPlaces")).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andExpect(jsonPath("$", not(empty()))).
              andReturn().getResponse().getContentAsString();
      Region[] regionsBeforeChange = JsonRestUtils.readObject(jsonData, Region[].class);

      String testName = "Place4378";
      Assert.assertNotEquals(regionsBeforeChange[0], testName);
      regionsBeforeChange[0].setName(testName);

      mockMvc.perform(post("/services/setPlaces").content(JsonRestUtils.toJson(regionsBeforeChange)).
              accept(MediaType.APPLICATION_JSON)).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andExpect(jsonPath("$[0].sName", is(testName)));

      jsonData = mockMvc.perform(get("/services/getPlaces")).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andReturn().getResponse().getContentAsString();
      Region[] placesAfterChange = JsonRestUtils.readObject(jsonData, Region[].class);
      Assert.assertEquals(placesAfterChange[0].getName(), testName);
   }


}