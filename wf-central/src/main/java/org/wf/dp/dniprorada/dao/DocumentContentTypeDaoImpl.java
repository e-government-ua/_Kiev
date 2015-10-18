package org.wf.dp.dniprorada.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.dao.EntityNotFoundException;
import org.wf.dp.dniprorada.base.dao.GenericEntityDao;
import org.wf.dp.dniprorada.model.DocumentContentType;

import java.util.List;

@Repository
public class DocumentContentTypeDaoImpl extends GenericEntityDao<DocumentContentType>
        implements DocumentContentTypeDao {

    protected DocumentContentTypeDaoImpl() {
        super(DocumentContentType.class);
    }

    @Override
    public DocumentContentType getDocumentContentType(String name) {
        DocumentContentType documentContentType = null;
        List<DocumentContentType> documentsContentType = (List<DocumentContentType>) getSession()
                .createCriteria(DocumentContentType.class)
                .add(Restrictions.eq("name", name)).list();
        if (documentsContentType != null && !documentsContentType.isEmpty()) {
            documentContentType = documentsContentType.get(0);
        }
        return documentContentType;
    }

    @Override
    public Long setDocumentContent(DocumentContentType documentContentType) {
        getSession().save(documentContentType);
        return documentContentType.getId();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DocumentContentType> getDocumentContentTypes() {
        return (List<DocumentContentType>) getSession()
                .createCriteria(DocumentContentType.class).list();
    }

    @Override
    public DocumentContentType setDocumentContentType(Long nID, String sName) {
        DocumentContentType type = getDocumentContentType(nID);
        if (type == null) {
            type = new DocumentContentType();
        }
        type.setName(sName);
        getSession().saveOrUpdate(type);
        return type;
    }

    @Override
    public DocumentContentType getDocumentContentType(Long nID) {
        Criteria criteria = getSession().createCriteria(DocumentContentType.class);
        criteria.add(Restrictions.eq("id", nID));
        return (DocumentContentType) criteria.uniqueResult();
    }

    @Override
    public void removeDocumentContentType(Long nID) {
        DocumentContentType type = getDocumentContentType(nID);
        if (type == null)
            throw new EntityNotFoundException("Record not found");
        getSession().delete(type);
    }

}