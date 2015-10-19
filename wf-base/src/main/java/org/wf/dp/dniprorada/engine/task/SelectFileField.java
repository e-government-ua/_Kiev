package org.wf.dp.dniprorada.engine.task;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.custom.UploadPopupWindow;
import org.activiti.explorer.ui.mainlayout.ExplorerLayout;

import java.util.Collection;

public class SelectFileField extends HorizontalLayout implements Field {

    private static final long serialVersionUID = 1L;

    // Text of the label before upload.
    private static final String NO_FILE_SELECTED = "вкладень немає";

    // Button text.
    private static final String FORM_FILE_SELECT = "Виберіть файл";

    protected I18nManager i18nManager;

    // Receiver to process upload data.
    protected FileUploadReceiver receiver;

    // Invisible textfield.
    protected TextField wrappedField;

    // Label to present the filename.
    protected Label selectedFileLabel;

    // Button to open up the dialog.
    protected Button selectFileButton;

    public SelectFileField(String caption) {
        i18nManager = ExplorerApp.get().getI18nManager();
        receiver = new FileUploadReceiver(this);

        setSpacing(true);
        setCaption(caption);

        selectedFileLabel = new Label();
        selectedFileLabel.setValue(NO_FILE_SELECTED);
        selectedFileLabel.addStyleName(ExplorerLayout.STYLE_FORM_NO_USER_SELECTED);
        addComponent(selectedFileLabel);

        selectFileButton = new Button();
        selectFileButton.addStyleName(Reindeer.BUTTON_SMALL);
        selectFileButton.setCaption(FORM_FILE_SELECT);
        addComponent(selectFileButton);

        selectFileButton.addListener(new ClickListener() {
            private static final long serialVersionUID = 1L;

            public void buttonClick(ClickEvent event) {
                final UploadPopupWindow window = new UploadPopupWindow(i18nManager
                        .getMessage(Messages.UPLOAD_SELECT), "вкладення", receiver);
                window.addFinishedListener(receiver);
                ExplorerApp.get().getViewManager().showPopupWindow(window);
            }
        });

        wrappedField = new TextField();
        wrappedField.setVisible(false);
        addComponent(wrappedField);
    }

    public boolean isInvalidCommitted() {
        return wrappedField.isInvalidCommitted();
    }

    public void setInvalidCommitted(boolean isCommitted) {
        wrappedField.setInvalidCommitted(isCommitted);
    }

    public void commit() throws SourceException, InvalidValueException {
        wrappedField.commit();
    }

    public void discard() throws SourceException {
        wrappedField.discard();
    }

    public boolean isWriteThrough() {
        return wrappedField.isWriteThrough();
    }

    public void setWriteThrough(boolean writeThrough) throws SourceException, InvalidValueException {
        wrappedField.setWriteThrough(true);
    }

    public boolean isReadThrough() {
        return wrappedField.isReadThrough();
    }

    public void setReadThrough(boolean readThrough) throws SourceException {
        wrappedField.setReadThrough(readThrough);
    }

    public boolean isModified() {
        return wrappedField.isModified();
    }

    public void addValidator(Validator validator) {
        wrappedField.addValidator(validator);
    }

    public void removeValidator(Validator validator) {
        wrappedField.removeValidator(validator);
    }

    public Collection<Validator> getValidators() {
        return wrappedField.getValidators();
    }

    public boolean isValid() {
        return wrappedField.isValid();
    }

    public void validate() throws InvalidValueException {
        wrappedField.validate();
    }

    public boolean isInvalidAllowed() {
        return wrappedField.isInvalidAllowed();
    }

    public void setInvalidAllowed(boolean invalidValueAllowed) throws UnsupportedOperationException {
        wrappedField.setInvalidAllowed(invalidValueAllowed);
    }

    public Object getValue() {
        return wrappedField.getValue();
    }

    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        wrappedField.setValue(newValue);

        // Update label
        if (newValue != null) {
            selectedFileLabel.setValue(newValue);
        } else {
            selectedFileLabel.setValue(NO_FILE_SELECTED);
        }
    }

    public Class<?> getType() {
        return wrappedField.getType();
    }

    public void addListener(ValueChangeListener listener) {
        wrappedField.addListener(listener);
    }

    public void removeListener(ValueChangeListener listener) {
        wrappedField.removeListener(listener);
    }

    public void valueChange(Property.ValueChangeEvent event) {
        wrappedField.valueChange(event);
    }

    public Property getPropertyDataSource() {
        return wrappedField.getPropertyDataSource();
    }

    public void setPropertyDataSource(Property newDataSource) {
        wrappedField.setPropertyDataSource(newDataSource);
    }

    public int getTabIndex() {
        return wrappedField.getTabIndex();
    }

    public void setTabIndex(int tabIndex) {
        wrappedField.setTabIndex(tabIndex);
    }

    public boolean isRequired() {
        return wrappedField.isRequired();
    }

    public void setRequired(boolean required) {
        wrappedField.setRequired(required);
    }

    public String getRequiredError() {
        return wrappedField.getRequiredError();
    }

    public void setRequiredError(String requiredMessage) {
        wrappedField.setRequiredError(requiredMessage);
    }

    @Override
    public void focus() {
        wrappedField.focus();
    }
}