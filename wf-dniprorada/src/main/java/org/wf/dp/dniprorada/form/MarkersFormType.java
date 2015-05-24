package org.wf.dp.dniprorada.form;

import org.activiti.engine.form.AbstractFormType;

/**
 * Created by Dmytro Tsapko on 5/24/2015.
 */
public class MarkersFormType extends AbstractFormType {

    public static final String TYPE_NAME = "markers";

    @Override
    public Object convertFormValueToModelValue(String propertyValue) {
        return propertyValue;
    }

    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        return modelValue.toString();
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }
}
