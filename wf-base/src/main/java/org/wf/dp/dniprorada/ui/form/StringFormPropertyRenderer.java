package org.wf.dp.dniprorada.ui.form;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;
import org.wf.dp.dniprorada.constant.Form;

import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

public class StringFormPropertyRenderer extends AbstractFormPropertyRenderer
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public StringFormPropertyRenderer()
  {
    super(StringFormType.class);
  }

  public Field getPropertyField(FormProperty formProperty)
  {
    TextField textField = new TextField(getPropertyLabel(formProperty));
    textField.setRequired(formProperty.isRequired());
    textField.setEnabled(formProperty.isWritable());
    textField.setRequiredError(getMessage("form.field.required", new Object[] { getPropertyLabel(formProperty) }));
    textField.setImmediate(true);
    textField.setWidth(Form.STRING_W.getDimension().getWidth());
    
    if (formProperty.getValue() != null) {
      textField.setValue(formProperty.getValue());
    }

    return textField;
  }
}