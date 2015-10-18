package org.wf.dp.dniprorada.ui.form;

import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import org.activiti.engine.form.FormProperty;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.constant.Form;
import org.wf.dp.dniprorada.form.TextAreaFormType;

@Component
public class TextAreaFormPropertyRenderer extends AbstractFormPropertyRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public TextAreaFormPropertyRenderer() {
        super(TextAreaFormType.class);
    }

    @Override
    public Field getPropertyField(FormProperty formProperty) {
        TextField textField = new TextField(getPropertyLabel(formProperty));
        textField.setRequired(formProperty.isRequired());
        textField.setEnabled(formProperty.isWritable());
        textField.setRequiredError(getMessage(Messages.FORM_FIELD_REQUIRED, getPropertyLabel(formProperty)));

        textField.setHeight(Form.TEXT_AREA.getDimension().getHeight());
        textField.setWidth(Form.TEXT_AREA.getDimension().getWidth());

        if (formProperty.getValue() != null) {
            textField.setValue(formProperty.getValue());
        }

        textField.setValidationVisible(true);
        textField.setImmediate(true);
        return textField;
    }

}
