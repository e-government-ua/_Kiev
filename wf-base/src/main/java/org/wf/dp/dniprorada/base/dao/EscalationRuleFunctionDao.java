package org.wf.dp.dniprorada.base.dao;

import org.wf.dp.dniprorada.base.model.EscalationRuleFunction;

public interface EscalationRuleFunctionDao extends EntityDao<EscalationRuleFunction> {
    EscalationRuleFunction saveOrUpdate(Long nID, String sName, String sBeanHandler);
}
