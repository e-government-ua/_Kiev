package org.wf.dp.dniprorada.form;

import org.activiti.engine.form.AbstractFormType;

public class TextAreaFormType extends AbstractFormType {

    public static final String FORM_NAME = "textArea";
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String getName() {
        return FORM_NAME;
    }

    @Override
    public Object convertFormValueToModelValue(String propertyValue) {
        return propertyValue;
    }

    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        return (String) modelValue;

    }

}
