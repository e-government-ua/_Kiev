package org.activiti.rest.controller;

import org.activiti.rest.controller.util.ErrMessage;
import org.apache.commons.collections.Predicate;
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
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.DocumentOperator_SubjectOrgan;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.find;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class ActivitiRestDocumentControllerTest {

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
            .perform    (get("/services/getDocumentOperators"))
            .andExpect  (status().isOk())
            .andExpect  (content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

        List<DocumentOperator_SubjectOrgan> operators = Arrays.asList(
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
                    .param("nID_DocumentOperator_SubjectOrgan", "2"))
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
        assertEquals("Subjects aren't match, ", 1, doc.getSubject().getnID().longValue());
    }

    @Test
    public void getDocumentByCodeAndWrongOrgan() throws Exception {
        String organID = "100500";
        String jsonData = mockMvc
            .perform(get("/services/getDocumentAccessByHandler")
                    .param("sCode_DocumentAccess", "1")
                    .param("nID_DocumentOperator_SubjectOrgan", organID))
            .andExpect(status().is5xxServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrMessage msg = JsonRestUtils.readObject(jsonData, ErrMessage.class);
        assertNotNull("Expected error message not found", msg);
        assertEquals("Organ with ID:"+organID+" not found", msg.getMessage());
    }

    @Test
    public void getDocumentByWrongCode() throws Exception {
        String jsonData = mockMvc
            .perform(get("/services/getDocumentAccessByHandler")
                    .param("sCode_DocumentAccess", "100500")
                    .param("nID_DocumentOperator_SubjectOrgan", "2"))
            .andExpect(status().is5xxServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrMessage msg = JsonRestUtils.readObject(jsonData, ErrMessage.class);
        assertNotNull("Expected error message not found", msg);
        assertEquals("Document Access not found", msg.getMessage());
    }

    @Test
    public void getDocumentByCodeAndOrganAndPassword() throws Exception{
        String jsonData = mockMvc
            .perform(get("/services/getDocumentAccessByHandler")
                    .param("sCode_DocumentAccess", "2")
                    .param("nID_DocumentOperator_SubjectOrgan", "2")
                    .param("sPass", "123"))
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
        assertEquals("Subjects aren't match, ", 2, doc.getSubject().getnID().longValue());
    }



    @Test
    public void getDocumentByCodeAndOrganAndWrongPassword() throws Exception{
        String jsonData = mockMvc
            .perform(get("/services/getDocumentAccessByHandler")
                    .param("sCode_DocumentAccess", "2")
                    .param("nID_DocumentOperator_SubjectOrgan", "2")
                    .param("sPass", "100500"))
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
                    .param("sPass", "123"))
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
        assertEquals("Subjects aren't match, ", 2, doc.getSubject().getnID().longValue());
    }

    @Test
    public void getDocumentByCodeAndWrongDocumentTypeAndOrganAndPassword() throws Exception {
        String jsonData = mockMvc
            .perform(get("/services/getDocumentAccessByHandler")
                    .param("sCode_DocumentAccess", "2")
                    .param("nID_DocumentOperator_SubjectOrgan", "2")
                    .param("nID_DocumentType", "2")
                    .param("sPass", "123"))
            .andExpect(status().is5xxServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrMessage msg = JsonRestUtils.readObject(jsonData, ErrMessage.class);
        assertNotNull("Expected error message not found", msg);
        assertEquals("Document Access not found", msg.getMessage());
    }
}