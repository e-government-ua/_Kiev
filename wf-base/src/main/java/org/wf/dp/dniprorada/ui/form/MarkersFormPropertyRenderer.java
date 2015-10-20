package org.wf.dp.dniprorada.ui.form;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.activiti.engine.form.FormProperty;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wf.dp.dniprorada.form.MarkersFormType;
import org.wf.dp.dniprorada.util.Util;

/**
 * Created by user on 5/24/2015.
 */
public class MarkersFormPropertyRenderer extends AbstractFormPropertyRenderer {
    
    private final static Logger log = LoggerFactory.getLogger(MarkersFormPropertyRenderer.class);

    private static final long serialVersionUID = 1L;
    
    private static final Pattern PROPERTY_sVALUE_COMPILED = Pattern.compile("\\[/bpmn/markers/motion/(.*?)\\]");

    public MarkersFormPropertyRenderer() {
        super(MarkersFormType.class);
    }

    @Override
    public Field getPropertyField(FormProperty formProperty) {
        String formValue = formProperty.getValue();
        Matcher matcher = PROPERTY_sVALUE_COMPILED.matcher(formValue);
        if (matcher.find()) {
            String filename = matcher.group(1);
            log.info("File {} will be loaded", filename);
            try {
                byte[] bytes = Util.getMarkersMotionJson(filename);
                formValue = Util.sData(bytes);
            }
            catch (IOException ex) {
                log.error("File {} can't be loaded", filename);
                throw new IllegalStateException("File can't be loaded: " + ex.getMessage());
            }
        }
        StringBuilder resultString = new StringBuilder("<b>Markers: </b>");
        resultString.append(formValue);
        Label label = new Label(resultString.toString(), Label.CONTENT_XHTML);
        Form form = new Form();
        form.getLayout().addComponent(label);
        return form;
    }
}
