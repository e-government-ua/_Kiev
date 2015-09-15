package org.wf.dp.dniprorada.form;

import org.activiti.engine.form.AbstractFormType;

/**
 * Created by Dmytro Tsapko on 5/17/2015.
 */
public class HTMLFormType extends AbstractFormType{

    public static final String TYPE_NAME = "HTML";

    @Override
    public String convertFormValueToModelValue(String propertyValue) {
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
