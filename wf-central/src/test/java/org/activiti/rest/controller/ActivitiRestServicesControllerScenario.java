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
import org.wf.dp.dniprorada.service.TableDataService;
import org.wf.dp.dniprorada.util.JsonRestUtils;
import org.wf.dp.dniprorada.viewobject.TableData;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
   public void shouldSuccessfullyGetAndSetServicesAndPlacesTables() throws Exception {
      String jsonData = mockMvc.perform(get("/services/getServicesAndPlacesTables")).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andExpect(jsonPath("$", not(empty()))).
              andReturn().getResponse().getContentAsString();
      TableData[] tableDataList = JsonRestUtils.readObject(jsonData, TableData[].class);
      Assert.assertEquals(TableDataService.TablesSet.ServicesAndPlaces.getEntityClasses().length, tableDataList.length);

      mockMvc.perform(post("/services/setServicesAndPlacesTables").content(jsonData).
              contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8));
   }

    @Test
    public void shouldSuccessfullyGetAndSetServicesTree() throws Exception {
        String jsonData = mockMvc.perform(get("/services/getServicesTree")).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andExpect(jsonPath("$", not(empty()))).
                andReturn().getResponse().getContentAsString();
        Category[] categoriesBeforeChange = JsonRestUtils.readObject(jsonData, Category[].class);

        String categoryName = "CategoryName438";
        String subcategoryName = "SubcategoryName9873";
        categoriesBeforeChange[0].setName(categoryName);
        categoriesBeforeChange[0].getSubcategories().get(0).setName(subcategoryName);
        String serviceName = categoriesBeforeChange[0].getSubcategories().get(0).getServices().get(0).getName();

        mockMvc.perform(post("/services/setServicesTree").content(JsonRestUtils.toJson(categoriesBeforeChange)).
                contentType(APPLICATION_JSON_CHARSET_UTF_8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andExpect(jsonPath("$[0].sName", is(categoryName)));

        jsonData = mockMvc.perform(get("/services/getServicesTree").
                param("sFind", serviceName).
                contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        Category[] categoriesAfterChange = JsonRestUtils.readObject(jsonData, Category[].class);
        Assert.assertEquals(categoryName, categoriesAfterChange[0].getName());
        Assert.assertEquals(subcategoryName, categoriesAfterChange[0].getSubcategories().get(0).getName());
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

        String serviceName = "ServiceName123";
        String serviceUrl = "ServiceDataUrl7483";
        serviceBeforeChange.setName(serviceName);
        serviceBeforeChange.getServiceDataList().get(0).setUrl(serviceUrl);

        mockMvc.perform(post("/services/setService").content(JsonRestUtils.toJson(serviceBeforeChange)).
                contentType(APPLICATION_JSON_CHARSET_UTF_8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andExpect(jsonPath("$.nID", is(1))).
                andExpect(jsonPath("$.sName", is(serviceName)));

        jsonData = mockMvc.perform(get("/services/getService").
                param("nID", "1").
                contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        Service serviceAfterChange = JsonRestUtils.readObject(jsonData, Service.class);
        Assert.assertEquals(serviceName, serviceAfterChange.getName());
        Assert.assertEquals(serviceUrl, serviceAfterChange.getServiceDataList().get(0).getUrl());
    }

    @Test
    public void shouldSuccessfullyGetAndSetPlaces() throws Exception {
        String jsonData = mockMvc.perform(get("/services/getPlaces").
                contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andExpect(jsonPath("$", not(empty()))).
                andReturn().getResponse().getContentAsString();
        Region[] regionsBeforeChange = JsonRestUtils.readObject(jsonData, Region[].class);

        String testName = "Place4378";
        String cityName = "City438";
        regionsBeforeChange[0].setName(testName);
        regionsBeforeChange[0].getCities().get(0).setName(cityName);

        mockMvc.perform(post("/services/setPlaces").content(JsonRestUtils.toJson(regionsBeforeChange)).
                contentType(APPLICATION_JSON_CHARSET_UTF_8).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andExpect(jsonPath("$[0].sName", is(testName)));

        jsonData = mockMvc.perform(get("/services/getPlaces")).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        Region[] placesAfterChange = JsonRestUtils.readObject(jsonData, Region[].class);
        Assert.assertEquals(testName, placesAfterChange[0].getName());
        Assert.assertEquals(cityName, placesAfterChange[0].getCities().get(0).getName());
    }

    @Test
    public void recursiveCompletelyDeletedService() throws Exception {

        int serviceId = 4;
        String jsonData = mockMvc.perform(get("/services/getService").
                param("nID", ""+serviceId)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andExpect(jsonPath("$.nID", is(serviceId))).
                andExpect(jsonPath("$.aServiceData", not(empty()))).
                andExpect(jsonPath("$.sName", not(empty()))).
                andReturn().getResponse().getContentAsString();
        Service actualService = JsonRestUtils.readObject(jsonData, Service.class);

        jsonData = mockMvc.perform(delete("/services/removeService").
                param("nID", String.valueOf(actualService.getId()))).
                andExpect(status().isNotModified()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        Assert.assertTrue(jsonData.contains("error"));

        jsonData = mockMvc.perform(delete("/services/removeService").
                param("nID", String.valueOf(actualService.getId())).
                param("bRecursive", "true")).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        Assert.assertTrue(jsonData.contains("success"));
    }

    @Test
    public void deletedServiceById() throws Exception {
        String jsonData = mockMvc.perform(get("/services/getService").
                param("nID", "215")).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andExpect(jsonPath("$.nID", is(215))).
                andExpect(jsonPath("$.aServiceData", is(empty()))).
                andExpect(jsonPath("$.sName", not(empty()))).
                andReturn().getResponse().getContentAsString();
        Service actualService = JsonRestUtils.readObject(jsonData, Service.class);

        jsonData = mockMvc.perform(delete("/services/removeService").
                param("nID", String.valueOf(actualService.getId()))).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        Assert.assertTrue(jsonData.contains("success"));
    }

    @Test
    public void recursiveCompletelyDeletedSubcategory() throws Exception {
        String jsonData = mockMvc.perform(delete("/services/removeSubcategory").
                param("nID", "6")).
                andExpect(status().isNotModified()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        Assert.assertTrue(jsonData.contains("error"));

        jsonData = mockMvc.perform(delete("/services/removeSubcategory").
                param("nID", "6").
                param("bRecursive", "true")).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        Assert.assertTrue(jsonData.contains("success"));
    }

    @Test
    public void deletedSubcategoryById() throws Exception {
       String jsonData = mockMvc.perform(delete("/services/removeSubcategory").
                param("nID", "6")).
                andExpect(status().isNotModified()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
       Assert.assertTrue(jsonData.contains("error"));

       // currently no subcategory without services

//       jsonData = mockMvc.perform(delete("/services/removeSubcategory").
//               param("nID", "6").param("bRecursive", "true")).
//               andExpect(status().isOk()).
//               andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
//               andReturn().getResponse().getContentAsString();
//       Assert.assertTrue(jsonData.contains("success"));
    }

    @Test
    public void recursiveRemoveCategory() throws Exception {
        String jsonData = mockMvc.perform(delete("/services/removeCategory").
                param("nID", "2").
                param("bRecursive", "true")).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        Assert.assertTrue(jsonData.contains("success"));
    }

    @Test
    public void removeCategoryById() throws Exception {
        String jsonData = mockMvc.perform(delete("/services/removeCategory").
                param("nID", "1")).
                andExpect(status().isNotModified()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        Assert.assertTrue(jsonData.contains("error"));
    }

    @Test
    public void removeServiceData() throws Exception {
        String jsonData = mockMvc.perform(delete("/services/removeServiceData").
                param("nID", "1").
                param("bRecursive", "true")).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        Assert.assertTrue(jsonData.contains("success"));
    }
}