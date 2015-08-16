package org.wf.dp.dniprorada.base.dao;

import org.wf.dp.dniprorada.base.model.EscalationRule;
import org.wf.dp.dniprorada.base.model.EscalationRuleFunction;

import javax.persistence.EntityNotFoundException;

public class EscalationRuleDaoImpl extends AbstractEntityDao<EscalationRule>
        implements EscalationRuleDao
{
    protected EscalationRuleDaoImpl() {
        super(EscalationRule.class);
    }

    @Override
    public EscalationRule saveOrUpdate(Long nID, String sID_BP, String sID_userTask,
                                       String sCondition, String soData,
                                       String sPatternFile, Long nID_escalationRuleFunction) {

        EscalationRule rule = nID != null ? getById(nID) : new EscalationRule();
        if (nID != null && rule == null) {//??
            rule = new EscalationRule();
            rule.setId(nID);
        }
        rule.setsID_BP(sID_BP);
        rule.setsID_UserTask(sID_userTask);
        if (sCondition != null) {
            rule.setsCondition(sCondition);
        }
        if (soData != null) {
            rule.setSoData(soData);
        }
        if (sPatternFile != null) {
            rule.setsPatternFile(sPatternFile);
        }
        if (nID_escalationRuleFunction != null) {
            EscalationRuleFunction ruleFunction = new EscalationRuleFunction();
            ruleFunction.setId(nID);
            rule.setoEscalationRuleFunction(ruleFunction);
        }
        saveOrUpdate(rule);
        return rule;
    }

    @Override
    public void delete(Long nID) {
        EscalationRule rule = getById(nID);
        if (rule == null) {
            throw new EntityNotFoundException("Record not found. No such rule with nID=" + nID);
        }
        delete(rule);
    }
}
