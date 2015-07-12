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
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.viewobject.MerchantVO;
import org.wf.dp.dniprorada.viewobject.TableData;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Created by yevhenii 4/27/15
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
public class ActivitiRestMerchantControllerScenario {

   public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";

   @Autowired
   private WebApplicationContext webApplicationContext;

   private MockMvc mockMvc;

   @Before
   public void setUp() {
      mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
   }

   @Test
   public void shouldSuccessfullySaveUpdateGetAndRemove() throws Exception {
      String sID = "AAA";

      String jsonData = mockMvc.perform(post("/merchant/setMerchant").
              param("sID", sID).
              param("sName", sID).
              param("sURL_CallbackStatusNew", "FFF").
              param("sURL_CallbackPaySuccess", "WWW").
              param("nID_SubjectOrgan", "1")).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andReturn().getResponse().getContentAsString();
      MerchantVO savedMerchantVO = JsonRestUtils.readObject(jsonData, MerchantVO.class);

      Assert.assertNotNull(savedMerchantVO);
      Assert.assertEquals(savedMerchantVO.getsURL_CallbackStatusNew(), "FFF");
      Assert.assertNotNull(savedMerchantVO.getnID());

      jsonData = mockMvc.perform(post("/merchant/setMerchant").
              param("nID", "" + savedMerchantVO.getnID()).
              param("sURL_CallbackStatusNew", "FFF2")).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andReturn().getResponse().getContentAsString();
      savedMerchantVO = JsonRestUtils.readObject(jsonData, MerchantVO.class);
      Assert.assertEquals(savedMerchantVO.getsURL_CallbackStatusNew(), "FFF2");

      jsonData = mockMvc.perform(get("/merchant/getMerchants")).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andReturn().getResponse().getContentAsString();
      MerchantVO[] merchants = JsonRestUtils.readObject(jsonData, MerchantVO[].class);
      Assert.assertTrue(merchants.length > 0);
      Assert.assertEquals(savedMerchantVO, merchants[merchants.length - 1]);

      String wrongsID = "WRONGID";
      mockMvc.perform(get("/merchant/getMerchant").param("sID", wrongsID)).andExpect(status().isNotFound());

      jsonData = mockMvc.perform(get("/merchant/getMerchant").
              param("sID", sID)).
              andExpect(status().isOk()).
              andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
              andReturn().getResponse().getContentAsString();

      MerchantVO foundMerchant = JsonRestUtils.readObject(jsonData, MerchantVO.class);
      Assert.assertEquals(savedMerchantVO, foundMerchant);

      mockMvc.perform(delete("/merchant/removeMerchant").param("sID", wrongsID)).andExpect(status().isNotFound());
      mockMvc.perform(delete("/merchant/removeMerchant").param("sID", sID)).andExpect(status().isOk());

      mockMvc.perform(get("/merchant/getMerchant").param("sID", sID)).andExpect(status().isNotFound());

   }

}