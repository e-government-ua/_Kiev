package org.activiti.rest.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.model.Country;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
@ActiveProfiles("default")
public class ActivitiRestCountryControllerTest {

    public static final String GET_COUNTRIES = "/services/getCountries";
    public static final int HTTP_OK = 200;
    public static final int HTTP_FORBIDDEN = 403;
    public static final String DASH = "-";
    public static final String RECORD_NOT_FOUND = "Record not found!";
    public static final String REQUIRED_PARAMETERS = "required at least one of parameters (nID, nID_UA, sID_Two, sID_Three)!";
    private static final String SET_COUNTRY = "/services/setCountry";
    private static final String REMOVE_COUNTRY = "/services/removeCountry";
    private static final String GET_COUNTRY = "/services/getCountry";
    private static final String DOUBLE_DASH = "--";
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private Country expCountry;
    private Map<String, String> expCountryParams;
    private Integer httpStatus = HTTP_OK;
    private Country expCountry2;
    private Map<String, String> expCountryParams2;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        expCountry = new Country();
        expCountry.setnID_UA(0L);
        expCountry.setsID_Two(DASH);
        expCountry.setsID_Three(DASH);
        expCountry.setsNameShort_UA(DASH);
        expCountry.setsNameShort_EN(DASH);
        expCountry.setsReference_LocalISO(DASH);

        expCountryParams = new HashMap();
        expCountryParams.put("nID_UA", "0");
        expCountryParams.put("sID_Two", DASH);
        expCountryParams.put("sID_Three", DASH);
        expCountryParams.put("sNameShort_UA", DASH);
        expCountryParams.put("sNameShort_EN", DASH);
        expCountryParams.put("sReference_LocalISO", DASH);

        expCountry2 = new Country();
        expCountry2.setnID_UA(1L);
        expCountry2.setsID_Two(DOUBLE_DASH);
        expCountry2.setsID_Three(DOUBLE_DASH);
        expCountry2.setsNameShort_UA(DOUBLE_DASH);
        expCountry2.setsNameShort_EN(DOUBLE_DASH);
        expCountry2.setsReference_LocalISO(DOUBLE_DASH);

        expCountryParams2 = new HashMap();
        expCountryParams2.put("nID_UA", "1");
        expCountryParams2.put("sID_Two", DOUBLE_DASH);
        expCountryParams2.put("sID_Three", DOUBLE_DASH);
        expCountryParams2.put("sNameShort_UA", DOUBLE_DASH);
        expCountryParams2.put("sNameShort_EN", DOUBLE_DASH);
        expCountryParams2.put("sReference_LocalISO", DOUBLE_DASH);
    }

    private Country getCountryFromResponse(MockHttpServletResponse response) throws UnsupportedEncodingException {
        String jsonData = response.getContentAsString();
        return JsonRestUtils.readObject(jsonData, Country.class);
    }

    private MockHttpServletResponse getResponse(String url, Map<String, String> params) throws Exception {
        MockHttpServletRequestBuilder request = get(url);
        if (params.size() > 0) {
            for (String key : params.keySet()) {
                request.param(key, params.get(key));
            }
        }
        return mockMvc.perform(request)
                .andExpect(status().is(httpStatus))
                .andReturn()
                .getResponse();
    }

    @Test
    public void getCountriesNotNull() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get(GET_COUNTRIES))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertNotNull("response can`t be null", response);
    }

    @Test
    public void setCountry() throws Exception {

        MockHttpServletResponse response;
        //set
        response = getResponse(SET_COUNTRY, expCountryParams);
        Country actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        expCountry.setId(actCountry.getId());
        assertEquals("objects aren't match", expCountry, actCountry);

        //remove
        getResponse(REMOVE_COUNTRY, expCountryParams);

    }

    @Test
    public void getCountry() throws Exception {
        MockHttpServletResponse response;
        Map<String, String> params = new HashMap<>();
        //set
        response = getResponse(SET_COUNTRY, expCountryParams);
        Country actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        Long nID = actCountry.getId();
        expCountry.setId(nID);
        assertEquals("objects aren't match", expCountry, actCountry);

        //get by nID
        params.put("nID", "" + nID);
        response = getResponse(GET_COUNTRY, params);
        actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        assertEquals("objects aren't match", expCountry, actCountry);

        //get by nID_UA
        params.clear();
        params.put("nID_UA", "0");
        response = getResponse(GET_COUNTRY, params);
        actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        assertEquals("objects aren't match", expCountry, actCountry);

        //get by sID_Two
        params.clear();
        params.put("sID_Two", DASH);
        response = getResponse(GET_COUNTRY, params);
        actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        assertEquals("objects aren't match", expCountry, actCountry);

        //get by sID_Three
        params.clear();
        params.put("sID_Three", DASH);
        response = getResponse(GET_COUNTRY, params);
        actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        assertEquals("objects aren't match", expCountry, actCountry);

        //remove
        getResponse(REMOVE_COUNTRY, expCountryParams);
    }

    @Test
    public void getCountry_NotFound() throws Exception {
        MockHttpServletResponse response;
        Map<String, String> params = new HashMap<>();
        String reason;

        //get by nID -- must be null
        params.put("nID", "1");
        //httpStatus = HTTP_FORBIDDEN;
        response = getResponse(GET_COUNTRY, params);
        Country country = getCountryFromResponse(response);
        //        reason = response.getHeader("Reason");
        assertNull(country);
        //        assertEquals(RECORD_NOT_FOUND, reason);

        //remove by nID
        params.put("nID", "1");
        httpStatus = HTTP_FORBIDDEN;
        response = getResponse(REMOVE_COUNTRY, params);
        reason = response.getHeader("Reason");
        assertNotNull(reason);
        assertEquals(RECORD_NOT_FOUND, reason);

    }

    @Test
    public void getCountry_NullParams() throws Exception {
        MockHttpServletResponse response;
        Map<String, String> params = new HashMap<>();
        String reason;
        //get by nID
        params.put("nID", "");
        httpStatus = HTTP_FORBIDDEN;
        response = getResponse(GET_COUNTRY, params);
        reason = response.getHeader("Reason");
        assertNotNull(reason);
        assertEquals(REQUIRED_PARAMETERS, reason);

        //get by nID_UA
        params.put("nID_UA", "");
        httpStatus = HTTP_FORBIDDEN;
        response = getResponse(GET_COUNTRY, params);
        reason = response.getHeader("Reason");
        assertNotNull(reason);
        assertEquals(REQUIRED_PARAMETERS, reason);

        //get by sID_Two
        params.put("sID_Two", null);
        httpStatus = HTTP_FORBIDDEN;
        response = getResponse(GET_COUNTRY, params);
        reason = response.getHeader("Reason");
        assertNotNull(reason);
        assertEquals(REQUIRED_PARAMETERS, reason);

        //get by sID_Three
        params.put("sID_Three", null);
        httpStatus = HTTP_FORBIDDEN;
        response = getResponse(GET_COUNTRY, params);
        reason = response.getHeader("Reason");
        assertNotNull(reason);
        assertEquals(REQUIRED_PARAMETERS, reason);

    }

    @Test
    public void getCountryByMultiplyKeys() throws Exception {
        MockHttpServletResponse response;
        Map<String, String> params = new HashMap<>();

        //set country 1
        response = getResponse(SET_COUNTRY, expCountryParams);
        Country actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        Long nID_1 = actCountry.getId();
        expCountry.setId(nID_1);
        assertEquals("objects aren't match", expCountry, actCountry);

        //set country 2
        response = getResponse(SET_COUNTRY, expCountryParams2);
        actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        Long nID_2 = actCountry.getId();
        expCountry2.setId(nID_2);
        assertEquals("objects aren't match", expCountry2, actCountry);

        //get by nID_1
        params.put("nID", "" + nID_1);
        response = getResponse(GET_COUNTRY, params);
        actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        assertEquals("objects aren't match", expCountry, actCountry);

        //get by nID_1 and nID_UA_1 -- must be 1
        params.clear();
        params.put("nID", "" + nID_1);
        params.put("nID_UA", "0");
        response = getResponse(GET_COUNTRY, params);
        actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        assertEquals("objects aren't match", expCountry, actCountry);

        //get by nID_1 and nID_UA_2 -- must be 1
        params.clear();
        params.put("nID", "" + nID_1);
        params.put("nID_UA", "1");
        response = getResponse(GET_COUNTRY, params);
        actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        assertEquals("objects aren't match", expCountry, actCountry);

        //get by nID_UA_1 and sID_Two_2 -- must be 1
        params.clear();
        params.put("sID_Two", DOUBLE_DASH);
        params.put("nID_UA", "0");
        response = getResponse(GET_COUNTRY, params);
        actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        assertEquals("objects aren't match", expCountry, actCountry);

        //get by nID_UA_2 and sID_Two_1 -- must be 2
        params.clear();
        params.put("sID_Two", DASH);
        params.put("nID_UA", "1");
        response = getResponse(GET_COUNTRY, params);
        actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        assertEquals("objects aren't match", expCountry2, actCountry);

        //remove country 1 and 2
        getResponse(REMOVE_COUNTRY, expCountryParams);
        getResponse(REMOVE_COUNTRY, expCountryParams2);
    }

    @Test
    public void removeCountry() {
        //algorithm the same as get-method + test in get method
    }

    //exceptions on unique
    @Test
    public void setDuplicateCountry() throws Exception {
        Map<String, String> params = new HashMap<>();
        String reason;
        MockHttpServletResponse response;
        //set
        response = getResponse(SET_COUNTRY, expCountryParams);
        Country actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        expCountry.setId(actCountry.getId());
        assertEquals("objects aren't match", expCountry, actCountry);

        //set the same sID_Two -- must be error
        expCountryParams2.put("nID_UA", "1");
        expCountryParams2.put("sID_Two", DASH);
        httpStatus = HTTP_FORBIDDEN;
        response = getResponse(SET_COUNTRY, expCountryParams2);
        reason = response.getHeader("Reason");
        assertNotNull(reason);

        //set the same sID_Three -- must be error
        expCountryParams2.put("sID_Three", DASH);
        expCountryParams2.put("sID_Two", DOUBLE_DASH);
        httpStatus = HTTP_FORBIDDEN;
        response = getResponse(SET_COUNTRY, expCountryParams2);
        reason = response.getHeader("Reason");
        assertNotNull(reason);

        //set the same sNameShort_UA -- must be error
        expCountryParams2.put("sID_Three", DOUBLE_DASH);
        expCountryParams2.put("sNameShort_UA", DASH);
        httpStatus = HTTP_FORBIDDEN;
        response = getResponse(SET_COUNTRY, expCountryParams2);
        reason = response.getHeader("Reason");
        assertNotNull(reason);

        //set the same sNameShort_EN -- must be error
        expCountryParams2.put("sNameShort_EN", DASH);
        expCountryParams2.put("sNameShort_UA", DOUBLE_DASH);
        httpStatus = HTTP_FORBIDDEN;
        response = getResponse(SET_COUNTRY, expCountryParams2);
        reason = response.getHeader("Reason");
        assertNotNull(reason);

        //set the same nID_UA -- must be NOT error -- due TZ must be update!
        /* ТЗ: setCountry - апдейтит элемент(если задан один из уникальных-ключей)
        * или вставляет (если не задан nID), и отдает экземпляр нового объекта */
        expCountryParams2.put("nID_UA", "0");
        httpStatus = HTTP_OK;
        response = getResponse(SET_COUNTRY, expCountryParams2);
        actCountry = getCountryFromResponse(response);

        assertNotNull("response country can't be a null", actCountry);
        //expCountry.setId(actCountry.getId());
        assertEquals("nID_UA aren't match", expCountryParams2.get("nID_UA"), "" + actCountry.getnID_UA());
        assertEquals("sID_Two aren't match", expCountryParams2.get("sID_Two"), actCountry.getsID_Two());

    }
}