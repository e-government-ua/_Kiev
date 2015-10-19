package org.wf.dp.dniprorada.form;

import org.activiti.engine.form.AbstractFormType;

/**
 * @author inna
 */
public class FormFileType extends AbstractFormType {

    public static final String TYPE_NAME = "file";
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String getName() {
        return TYPE_NAME;
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