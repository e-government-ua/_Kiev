package org.activiti.rest.controller;

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
    public void getAccessByHandlersWithoutPassword() throws Exception {
        String jsonData = mockMvc
            .perform(get("/services/getDocumentAccessByHandler")
                    .param("sCode_DocumentAccess", "1")
                    .param("nID_DocumentOperator_SubjectOrgan", "2")
                    .param("nID_DocumentType", "0"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertNotNull(jsonData);

        Document doc = JsonRestUtils.readObject(jsonData, Document.class);
        assertNotNull("Document not found", doc);
        assertEquals("ID aren't match", 1L, doc.getId().longValue());
        assertNotNull("Document name is empty", doc.getName());
        assertEquals("Document types aren't match", 0, doc.getDocumentType().getId().intValue());
        assertEquals("Content keys aren't match", "1", doc.getContentKey());
    }


    @Test
    public void getAccessByHandlersWithPassword() throws Exception {
        String jsonData = mockMvc
            .perform(get("/services/getDocumentAccessByHandler")
                    .param("sCode_DocumentAccess", "1")
                    .param("nID_DocumentOperator_SubjectOrgan", "2")
                    .param("nID_DocumentType", "0")
                    .param("sPass", "123"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertNotNull(jsonData);
        assertNotNull("Document not found", JsonRestUtils.readObject(jsonData, Document.class));
    }
}
