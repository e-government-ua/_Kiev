/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.model.DocumentType;

import java.util.List;

/**
 * @author olya
 */
public interface DocumentTypeDao extends EntityDao<DocumentType> {

    public List<DocumentType> getDocumentTypes();

    public DocumentType setDocumentType(Long nID, String sName, Boolean bHidden);

    public void removeDocumentType(Long nID);

    public DocumentType getDocumentType(Long nID);
}