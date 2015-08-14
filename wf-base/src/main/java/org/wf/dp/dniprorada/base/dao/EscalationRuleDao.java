package org.wf.dp.dniprorada.base.dao;

import org.wf.dp.dniprorada.base.model.EscalationRule;

public interface EscalationRuleDao extends EntityDao<EscalationRule> {
    EscalationRule saveOrUpdate(Long nID, String sID_BP, String sID_userTask,
                                String sCondition, String soData, String sPatternFile,
                                Long nID_escalationRuleFunction);

    void delete(Long nID);
}
