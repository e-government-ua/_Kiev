package org.wf.dp.dniprorada.ui.form;

import org.activiti.engine.form.FormProperty;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;
import org.wf.dp.dniprorada.engine.task.SelectFileField;
import org.wf.dp.dniprorada.form.FormFileType;
 
import com.vaadin.ui.Field;

public class FileFormPropertyRenderer extends AbstractFormPropertyRenderer
{
 
  private static final long serialVersionUID = 1L;
 

  public FileFormPropertyRenderer()
  {
    super(FormFileType.class);
  }
 
  @Override
  public Field getPropertyField(FormProperty formProperty)
  {
    SelectFileField selectFileField = new SelectFileField(getPropertyLabel(formProperty));
    selectFileField.setRequired(formProperty.isRequired());
    selectFileField.setRequiredError(getMessage(Messages.FORM_FIELD_REQUIRED,
        getPropertyLabel(formProperty)));
    selectFileField.setEnabled(formProperty.isWritable());
 
    if (formProperty.getValue() != null)
    {
      selectFileField.setValue(formProperty.getValue());
    }
 
    return selectFileField;
  }
 
}