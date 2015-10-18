package org.activiti.rest.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * User: goodg_000
 * Date: 06.10.2015
 * Time: 23:08
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
public class ActivitiRestAccessControllerScenario {
    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetAccessServiceLoginRight() throws Exception {

        List<String> services = getAccessServiceLoginRight("TestLogin");
        Assert.assertTrue(services.size() == 1);
        Assert.assertEquals("TestService", services.get(0));

        services = getAccessServiceLoginRight("NonExistedLogin");
        Assert.assertTrue(services.isEmpty());
    }

    @Test
    public void testSetAccessServiceLoginRight() throws Exception {

        String newLogin = "NewLogin1";
        String newService = "NewService1";
        String nonExistedBean = "NonExistedBean";

        mockMvc.perform(post("/access/setAccessServiceLoginRight").
                param("sLogin", newLogin).param("sService", newService).param("sHandlerBean", nonExistedBean)).
                andExpect(status().is5xxServerError());

        setAccessServiceLoginRight(newLogin, newService);

        List<String> services = getAccessServiceLoginRight(newLogin);
        Assert.assertTrue(services.size() == 1);
        Assert.assertEquals(newService, services.get(0));
    }

    @Test
    public void testRemoveAccessServiceLoginRight() throws Exception {

        String newLogin = "NewLogin2";
        String newService = "NewService2";

        mockMvc.perform(delete("/access/removeAccessServiceLoginRight").
                param("sLogin", newLogin).param("sService", newService)).
                andExpect(status().isNotModified());

        setAccessServiceLoginRight(newLogin, newService);

        mockMvc.perform(delete("/access/removeAccessServiceLoginRight").
                param("sLogin", newLogin).param("sService", newService)).
                andExpect(status().isOk());

        List<String> services = getAccessServiceLoginRight(newLogin);
        Assert.assertTrue(services.isEmpty());
    }

    @Test
    public void testHasAccessServiceLoginRight() throws Exception {

        String newLogin = "NewLogin3";
        String newService = "NewService3";

        Assert.assertFalse(hasAccessServiceLoginRight(newLogin, newService));

        setAccessServiceLoginRight(newLogin, newService);

        Assert.assertTrue(hasAccessServiceLoginRight(newLogin, newService));
    }

    private void setAccessServiceLoginRight(String sLogin, String sService) throws Exception {
        mockMvc.perform(post("/access/setAccessServiceLoginRight").
                param("sLogin", sLogin).param("sService", sService)).
                andExpect(status().isOk());
    }

    private List<String> getAccessServiceLoginRight(String sLogin) throws Exception {
        String getJsonData = mockMvc.perform(get("/access/getAccessServiceLoginRight").
                param("sLogin", sLogin)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        return JsonRestUtils.readObject(getJsonData, List.class);
    }

    private boolean hasAccessServiceLoginRight(String sLogin, String sService) throws Exception {
        String getJsonData = mockMvc.perform(get("/access/hasAccessServiceLoginRight").
                param("sLogin", sLogin).param("sService", sService)).
                andExpect(status().isOk()).
                andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8)).
                andReturn().getResponse().getContentAsString();
        return JsonRestUtils.readObject(getJsonData, Boolean.class);
    }

}
