package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.DocumentOperator_SubjectOrgan;

import java.io.IOException;
import java.util.List;

public interface DocumentDao extends EntityDao<Document> {

    public Document getDocument(Long id);

    public byte[] getDocumentContent(Long id);

    public byte[] getDocumentContent(String contentKey);

    public List<Document> getDocuments(Long nID_Subject);

    //public Long setDocument(String subject_Upload, String subjectName_Upload,
    //		String name, String file, Integer documentTypeId, Integer contentTypeId, byte[] content);
    //public Long setDocument(String sID_Subject_Upload, String sSubjectName_Upload,
    //		String sName, Integer nID_DocumentType,
    //		Integer nID_DocumentContentType, MultipartFile oFile) throws IOException;
    public Long setDocument(Long nID_Subject, Long nID_Subject_Upload, String sID_Subject_Upload,
            String sSubjectName_Upload,
            String sName, Long nID_DocumentType,
            Long nID_DocumentContentType, String sFileName, String sFileContentType, byte[] aoContent, String oSignData)
            throws IOException;

    DocumentOperator_SubjectOrgan getOperator(Long organID);

    List<DocumentOperator_SubjectOrgan> getAllOperators();
}
