package org.activiti.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

/*
 * Created by yevhenii 4/27/15
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
public class ActivitiRestMerchControllerScenario {
	 @Autowired
	    private WebApplicationContext webApplicationContext;

	    private MockMvc mockMvc;

	    @Before
	    public void setUp() {
	        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	    }
	    
	    //@Test
	    public void shouldSuccessfullyUpdateMerchant() throws Exception {
	        mockMvc.perform(post("/merchant/addMerchant").
	                accept(MediaType.APPLICATION_JSON).
	                param("idOwner", "A").
	                param("ownerName", "XXX").
	                param("id", "1")).
	                andExpect(status().isOk()).
	                andExpect(content().contentType("application/json;charset=UTF-8"));
	    }
	    
	    @Test
	    public void shouldSuccessfullySelectMerchant() throws Exception {
	    	 mockMvc.perform(get("/merchant/getMerchants").
	                 accept(MediaType.APPLICATION_JSON));
	    }
	    
	    //@Test
	    public void shouldSuccessfullyDeleteMerchant() throws Exception {
	        mockMvc.perform(post("/merchant/removeMerchant").
	                accept(MediaType.APPLICATION_JSON).
	                param("idOwner", "A").
	                param("ownerName", "XXX").
	                param("id", "1")).
	                andExpect(status().isOk()).
	                andExpect(content().contentType("application/json;charset=UTF-8"));
	    }
	    
	    //@Test
	    public void shouldSuccessfullyInsertMerchant() throws Exception {
	        mockMvc.perform(post("/merchant/setMerchant").
	        		accept(MediaType.APPLICATION_JSON).
	                param("idOwner", "A").
	                param("ownerName", "XXX").
	                param("id", "1")).
	                andExpect(status().is2xxSuccessful());
	    }
}