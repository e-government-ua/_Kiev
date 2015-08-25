package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.model.SubjectMessage;

import java.util.List;

public interface SubjectMessagesDao extends EntityDao<SubjectMessage> {

    void setMessage(SubjectMessage message);

    List<SubjectMessage> getMessages();

    SubjectMessage getMessage(Long nID);
}
