package org.activiti.rest.controller;

import org.activiti.rest.controller.util.ErrMessage;
import org.apache.commons.collections.Predicate;
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
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.DocumentOperator_SubjectOrgan;
import org.wf.dp.dniprorada.model.SubjectOrganJoin;

import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.collections.CollectionUtils.find;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.wf.dp.dniprorada.model.DocumentOperatorTest.DUMMY_OPERATOR_ID;

/**
 * @author dgroup
 * @since 28.06.15
 */

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsApplicationConfiguration.class)
@ActiveProfiles("default")
public class ActivitiRestDocumentControllerTest {

    public static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";

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
    public void getAvailableOperators() throws Exception {
        String jsonData = mockMvc
                .perform(get("/services/getDocumentOperators"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<DocumentOperator_SubjectOrgan> operators = asList(
                JsonRestUtils.readObject(jsonData, DocumentOperator_SubjectOrgan[].class));

        assertFalse(operators.isEmpty());

        DocumentOperator_SubjectOrgan iGov = (DocumentOperator_SubjectOrgan)
                find(operators, new Predicate() {
                    public boolean evaluate(Object object) {
                        DocumentOperator_SubjectOrgan opr = (DocumentOperator_SubjectOrgan) object;
                        return DUMMY_OPERATOR_ID.equals(opr.getnID_SubjectOrgan());
                    }
                });

        assertEquals("ID aren't match", 1L, iGov.getId().longValue()); // Long vs Object = compiler error
    }

    @Test
    public void getDocumentByCodeAndOrgan() throws Exception {
        String jsonData = mockMvc
                .perform(get("/services/getDocumentAccessByHandler")
                        .param("sCode_DocumentAccess", "1")
                        .param("nID_DocumentOperator_SubjectOrgan", "2")
                        .param("nID_Subject", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Document doc = JsonRestUtils.readObject(jsonData, Document.class);

        assertNotNull("Document can't be a null", doc);
        assertEquals("IDs aren't match", 1, doc.getId().longValue());
        assertNotNull("Name can't be empty", doc.getName());
        assertEquals("Doc. types aren't match", 0, doc.getDocumentType().getId().longValue());
        assertEquals("Subjects aren't match, ", 1, doc.getSubject().getId().longValue());
    }

    @Test
    public void getDocumentByCodeAndWrongOrgan() throws Exception {
        String organID = "100500";
        String jsonData = mockMvc
                .perform(get("/services/getDocumentAccessByHandler")
                        .param("sCode_DocumentAccess", "1")
                        .param("nID_DocumentOperator_SubjectOrgan", organID)
                        .param("nID_Subject", "1"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrMessage msg = JsonRestUtils.readObject(jsonData, ErrMessage.class);
        assertNotNull("Expected error message not found", msg);
        assertEquals("Entity with nID_SubjectOrgan='" + organID + "' not found", msg.getMessage());
    }

    @Test
    public void getDocumentByWrongCode() throws Exception {
        String jsonData = mockMvc
                .perform(get("/services/getDocumentAccessByHandler")
                        .param("sCode_DocumentAccess", "100500")
                        .param("nID_DocumentOperator_SubjectOrgan", "2")
                        .param("nID_Subject", "1"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrMessage msg = JsonRestUtils.readObject(jsonData, ErrMessage.class);
        assertNotNull("Expected error message not found", msg);
        assertEquals("Document Access not found", msg.getMessage());
    }

    @Test
    public void getDocumentByCodeAndOrganAndPassword() throws Exception {
        String jsonData = mockMvc
                .perform(get("/services/getDocumentAccessByHandler")
                        .param("sCode_DocumentAccess", "2")
                        .param("nID_DocumentOperator_SubjectOrgan", "2")
                        .param("sPass", "123")
                        .param("nID_Subject", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Document doc = JsonRestUtils.readObject(jsonData, Document.class);

        assertNotNull("Document can't be a null", doc);
        assertEquals("IDs aren't match", 2, doc.getId().longValue());
        assertNotNull("Name can't be empty", doc.getName());
        assertEquals("Doc. types aren't match", 1, doc.getDocumentType().getId().longValue());
        assertEquals("Subjects aren't match, ", 2, doc.getSubject().getId().longValue());
    }

    @Test
    @Ignore
    public void getDocumentByCodeAndOrganAndWrongPassword() throws Exception {
        String jsonData = mockMvc
                .perform(get("/services/getDocumentAccessByHandler")
                        .param("sCode_DocumentAccess", "2")
                        .param("nID_DocumentOperator_SubjectOrgan", "2")
                        .param("sPass", "100500")
                        .param("nID_Subject", "1"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrMessage msg = JsonRestUtils.readObject(jsonData, ErrMessage.class);
        assertNotNull("Expected error message not found", msg);
        assertEquals("Document Access wrong password", msg.getMessage());
    }

    @Test
    public void getDocumentByCodeAndDocumentTypeAndOrganAndPassword() throws Exception {
        String jsonData = mockMvc
                .perform(get("/services/getDocumentAccessByHandler")
                        .param("sCode_DocumentAccess", "2")
                        .param("nID_DocumentOperator_SubjectOrgan", "2")
                        .param("nID_DocumentType", "1")
                        .param("sPass", "123")
                        .param("nID_Subject", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Document doc = JsonRestUtils.readObject(jsonData, Document.class);

        assertNotNull("Document can't be a null", doc);
        assertEquals("IDs aren't match", 2, doc.getId().longValue());
        assertNotNull("Name can't be empty", doc.getName());
        assertEquals("Doc. types aren't match", 1, doc.getDocumentType().getId().longValue());
        assertEquals("Subjects aren't match, ", 2, doc.getSubject().getId().longValue());
    }

    @Test
    public void getDocumentByCodeAndWrongDocumentTypeAndOrganAndPassword() throws Exception {
        String jsonData = mockMvc
                .perform(get("/services/getDocumentAccessByHandler")
                                .param("sCode_DocumentAccess", "2")
                                .param("nID_DocumentOperator_SubjectOrgan", "2")
                                .param("nID_DocumentType", "2")
                                .param("sPass", "123")
                                .param("nID_Subject", "1")
                )
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrMessage msg = JsonRestUtils.readObject(jsonData, ErrMessage.class);
        assertNotNull("Expected error message not found", msg);
        assertEquals("Document Access not found", msg.getMessage());
    }

    @Test
    @Ignore
    public void getSubjectOrganJoinByOrganID() throws Exception {
        final Long region = 11L;
        final Long city = 33L;

        String jsonData = mockMvc
                .perform(get("/services/getSubjectOrganJoins")
                        .param("nID_SubjectOrgan", "1")
                        .param("nID_Region", "11")
                        .param("nID_City", "33"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<SubjectOrganJoin> operators = asList(
                JsonRestUtils.readObject(jsonData, SubjectOrganJoin[].class));

        assertFalse("Two or three test operators must be here", operators.isEmpty());

        SubjectOrganJoin mvsGovUa = (SubjectOrganJoin)
                find(operators, new Predicate() {
                    public boolean evaluate(Object object) {
                        SubjectOrganJoin mvs = (SubjectOrganJoin) object;
                        return region.equals(mvs.getRegionId()) &&
                                city.equals(mvs.getCityId());
                    }
                });

        assertEquals("ID aren't match", 356L, mvsGovUa.getId().longValue()); // compile error: Long vs Object

        jsonData = mockMvc
                .perform(get("/services/getSubjectOrganJoins")
                        .param("nID_SubjectOrgan", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        operators = asList(JsonRestUtils.readObject(jsonData, SubjectOrganJoin[].class));
        assertFalse("Only one row expected, test data error", operators.size() > 1);

        mvsGovUa = operators.iterator().next();
        assertEquals("ID[2] aren't match", 358L, mvsGovUa.getId().longValue()); // compile error: Long vs Object
    }

    @Test
    @Ignore
    public void setSubjectOrganJoins() throws Exception {
        SubjectOrganJoin soj = new SubjectOrganJoin();
        soj.setId(400L);
        soj.setSubjectOrganId(5L);
        soj.setNameUa("Українська мова");
        soj.setNameRu("Русский язык");
        soj.setPrivatId("12345");
        soj.setPublicId("55555");
        soj.setGeoLongitude("15.232312");
        soj.setGeoLatitude("23.234231");

        String jsonSoj = JsonRestUtils.toJson(soj);

        mockMvc.perform(post("/services/setSubjectOrganJoins")
                .content(jsonSoj)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        jsonSoj = mockMvc.
                perform(get("/services/getSubjectOrganJoins")
                        .param("nID_SubjectOrgan", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<SubjectOrganJoin> sojs = asList(JsonRestUtils.readObject(jsonSoj, SubjectOrganJoin[].class));
        assertTrue("Only one element should be available", sojs.size() == 1);

        SubjectOrganJoin persistedSoj = sojs.iterator().next();

        assertEquals("ID's aren't match", soj.getId(), persistedSoj.getId());
        assertEquals("Public ID's aren't match", soj.getPublicId(), persistedSoj.getPublicId());
        assertEquals("Longitude aren't match", soj.getGeoLongitude(), persistedSoj.getGeoLongitude());
        assertEquals("Latitude aren't match", soj.getGeoLatitude(), persistedSoj.getGeoLatitude());
    }
}
