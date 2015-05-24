package org.wf.dp.dniprorada.ui.form;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalSplitPanel;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;
import org.wf.dp.dniprorada.form.MarkersFormType;

/**
 * Created by user on 5/24/2015.
 */
public class MarkersFormPropertyRenderer extends AbstractFormPropertyRenderer {

    private static final long serialVersionUID = 1L;

    public MarkersFormPropertyRenderer() {
        super(MarkersFormType.class);
    }

    @Override
    public Field getPropertyField(FormProperty formProperty) {
        String formValue = formProperty.getValue();
        StringBuilder resultString = new StringBuilder("<b>Markers: </b>");
        resultString.append(formValue);
        Label label = new Label(resultString.toString(), Label.CONTENT_XHTML);
        Form form = new Form();
        form.getLayout().addComponent(label);
        return form;
    }
}
