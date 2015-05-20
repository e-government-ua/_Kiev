package org.wf.dp.dniprorada.ui.form;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import org.activiti.engine.form.FormProperty;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.form.LabelFormType;

/**
 * Created by Dmytro Tsapko on 5/17/2015.
 */

@Component
public class LabelFormPropertyRenderer extends AbstractFormPropertyRenderer {

    private static final long serialVersionUID = 1L;

    public LabelFormPropertyRenderer() {
        super(LabelFormType.class);
    }

    @Override
    public Field getPropertyField(FormProperty formProperty) {
        Label label = new Label(formProperty.getValue(), Label.CONTENT_XHTML);
        Form form = new Form();
        form.getLayout().addComponent(label);
        return form;
    }
}
