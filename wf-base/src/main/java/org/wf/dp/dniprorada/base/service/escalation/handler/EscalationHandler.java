package org.wf.dp.dniprorada.base.service.escalation.handler;

import java.util.Map;

public interface EscalationHandler {
    public void execute(Map<String, Object> mParam, String[] asRecipientMail, String sPatternFile);
}
