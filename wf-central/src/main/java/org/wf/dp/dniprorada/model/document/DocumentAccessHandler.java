package org.wf.dp.dniprorada.model.document;


import org.wf.dp.dniprorada.model.DocumentAccess;

/**
 * @author dgroup
 * @since 28.06.15
 */
@org.springframework.stereotype.Service
public interface DocumentAccessHandler {

    DocumentAccessHandler setAccessCode  (String sCode_DocumentAccess);
    DocumentAccessHandler setPassword    (String sPass);

    DocumentAccess getAccess();
}