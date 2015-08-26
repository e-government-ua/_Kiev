package org.wf.dp.dniprorada.dao;

import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.dao.GenericEntityDao;
import org.wf.dp.dniprorada.model.SubjectMessage;

import java.util.List;

@Repository
public class SubjectMessagesDaoImpl extends GenericEntityDao<SubjectMessage> implements SubjectMessagesDao {

    protected SubjectMessagesDaoImpl() {
        super(SubjectMessage.class);
    }

    @Override
    public void setMessage(SubjectMessage message) {
        saveOrUpdate(message);
    }

    @Override
    public List<SubjectMessage> getMessages() {
        return findAll();
    }

    @Override
    public SubjectMessage getMessage(Long nID) {
        return findById(nID).orNull();
    }
}
