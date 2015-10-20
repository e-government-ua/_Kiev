package org.wf.dp.dniprorada.engine.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.EnumFormType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.model.AbstractModelTask;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Сервис шаг который проставляет значения поля типа енам
 *
 * @author inna
 */
@Component("enamTask")
public class EnamSetTask extends AbstractModelTask implements JavaDelegate {
    static final transient Logger LOG = LoggerFactory
            .getLogger(EnamSetTask.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        FormData oFormData = execution.getEngineServices()
                .getFormService()
                .getStartFormData(execution.getProcessDefinitionId());

        List<FormProperty> aFormProperty = oFormData.getFormProperties();
        if (!aFormProperty.isEmpty()) {
            for (FormProperty prop : aFormProperty) {
                if (prop.getType() instanceof EnumFormType) {
                    Map<String, String> values = (Map) prop.getType().getInformation("values");
                    if (values != null) {
                        for (Entry<String, String> enumEntry : values.entrySet()) {
                            execution.setVariable(enumEntry.getKey(), enumEntry.getValue());
                        }
                    }
                }
            }
        }
    }
}