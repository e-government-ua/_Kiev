package org.wf.dp.dniprorada.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wf.dp.dniprorada.dao.DocumentDao;
import org.wf.dp.dniprorada.model.document.DocumentAccessHandler;
import org.wf.dp.dniprorada.model.document.DocumentAccessHandler_IGov;
import org.wf.dp.dniprorada.model.document.HandlerFactory;
import org.wf.dp.dniprorada.model.document.HandlerNotFoundException;

import static org.junit.Assert.*;

/**
 * @author dgroup
 * @since 28.06.15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("default")
@ContextConfiguration(locations = { "classpath:context.xml" })
public class DocumentOperatorTest {
    public static final Long DUMMY_OPERATOR_ID = 2L;

    @Autowired
    private DocumentDao documentDao;

    @Autowired
    private HandlerFactory handlerFactory;

    @Test
    public void notNull() {
        assertNotNull(documentDao); // just test that Spring DI is working :)
    }

    @Test
    public void buildHandlerForDummyOperator() {
        DocumentOperator_SubjectOrgan operator =
                documentDao.getOperator(DUMMY_OPERATOR_ID);

        assertNotNull("Operator not found", operator);

        DocumentAccessHandler handler = handlerFactory.buildHandlerFor(operator);
        assertNotNull("Unable to build handler", handler);
        assertTrue("Incorrect handler type", handler instanceof DocumentAccessHandler_IGov);

        DocumentAccess access = handler.setAccessCode("1").getAccess();
        assertNotNull("DocumentAccess not found", access);

        Document doc = documentDao.getDocument(access.getId());
        assertNotNull("Document not found", doc);
        assertNotNull("Document name is empty", doc.getName());
    }

    @Test(expected = HandlerNotFoundException.class)
    public void tryToBuildNonExistentHandler() {
        DocumentOperator_SubjectOrgan operator = documentDao.getOperator(DUMMY_OPERATOR_ID);
        operator.setsHandlerClass("non existent class name");
        handlerFactory.buildHandlerFor(operator);
        fail("Expected exception was missing");
    }

    @Test
    public void oneOperatorShouldBePresent() {
        assertTrue(1 <= documentDao.getAllOperators().size());
    }
}