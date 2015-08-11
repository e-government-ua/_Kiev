package org.wf.dp.dniprorada.base.dao;

import org.wf.dp.dniprorada.base.model.EscalationRuleFunction;

public class EscalationRuleFunctionDaoImpl extends AbstractEntityDao<EscalationRuleFunction>
        implements EscalationRuleFunctionDao
{
    protected EscalationRuleFunctionDaoImpl() {
        super(EscalationRuleFunction.class);
    }

    @Override
    public EscalationRuleFunction saveOrUpdate(Long nID, String sName, String sBeanHandler) {
        EscalationRuleFunction ruleFunction = nID != null ? getById(nID) : new EscalationRuleFunction();
        if (nID != null && ruleFunction == null){//??
            ruleFunction = new EscalationRuleFunction();
            ruleFunction.setId(nID);
        }
        ruleFunction.setName(sName);
        if (sBeanHandler != null){
            ruleFunction.setsBeanHandler(sBeanHandler);
        }
        saveOrUpdate(ruleFunction);
        return ruleFunction;
    }
}
