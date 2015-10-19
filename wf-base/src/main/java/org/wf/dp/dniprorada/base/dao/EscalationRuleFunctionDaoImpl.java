package org.wf.dp.dniprorada.base.dao;

import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.model.EscalationRuleFunction;

@Repository
public class EscalationRuleFunctionDaoImpl extends GenericEntityDao<EscalationRuleFunction>
        implements EscalationRuleFunctionDao {

    protected EscalationRuleFunctionDaoImpl() {
        super(EscalationRuleFunction.class);
    }

    @Override
    public EscalationRuleFunction saveOrUpdate(Long nID, String sName, String sBeanHandler) {
        EscalationRuleFunction ruleFunction = nID != null ? findByIdExpected(nID) : new EscalationRuleFunction();
        if (nID != null && ruleFunction == null) {//??
            ruleFunction = new EscalationRuleFunction();
            ruleFunction.setId(nID);
        }
        ruleFunction.setName(sName);
        if (sBeanHandler != null) {
            ruleFunction.setsBeanHandler(sBeanHandler);
        }
        saveOrUpdate(ruleFunction);
        return ruleFunction;
    }
}
