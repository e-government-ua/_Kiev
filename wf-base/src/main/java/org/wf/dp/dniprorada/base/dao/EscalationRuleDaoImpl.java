package org.wf.dp.dniprorada.base.dao;

import org.wf.dp.dniprorada.base.model.EscalationRule;

public class EscalationRuleDaoImpl extends AbstractEntityDao<EscalationRule>
        implements EscalationRuleDao
{
    protected EscalationRuleDaoImpl() {
        super(EscalationRule.class);
    }
}
