/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wf.dp.dniprorada.dao;

import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.dao.GenericEntityDao;
import org.wf.dp.dniprorada.model.DocumentType;

import java.util.List;

/**
 * @author olya
 */
@Repository
public class DocumentTypeDaoImpl extends GenericEntityDao<DocumentType> implements DocumentTypeDao {

    protected DocumentTypeDaoImpl() {
        super(DocumentType.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DocumentType> getDocumentTypes() {
        return findAllBy("bHidden", false);
    }

    @Override
    public DocumentType setDocumentType(Long nID, String sName, Boolean bHidden) {
        DocumentType documentType = getDocumentType(nID);
        if (documentType == null) {
            documentType = new DocumentType();
        }
        documentType.setName(sName);
        documentType.setbHidden(bHidden == null ? false : bHidden);
        saveOrUpdate(documentType);
        return documentType;
    }

    @Override
    public void removeDocumentType(Long nID) {
        delete(nID);
    }

    @Override
    public DocumentType getDocumentType(Long nID) {
        return findById(nID).orNull();
    }
}