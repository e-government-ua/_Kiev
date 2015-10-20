package org.wf.dp.dniprorada.model.document;

import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.DocumentAccess;

/**
 * @author dgroup
 * @since 28.06.15
 */
@Component
public interface DocumentAccessHandler {

    DocumentAccessHandler setDocumentType(Long docTypeID);

    DocumentAccessHandler setAccessCode(String sCode_DocumentAccess);

    DocumentAccessHandler setPassword(String sPass);

    DocumentAccessHandler setWithContent(Boolean bWithContent);

    DocumentAccessHandler setIdSubject(Long nID_Subject);

    DocumentAccess getAccess();

    Document getDocument();
}