package org.wf.dp.dniprorada.engine.task;

import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.Receiver;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author inna
 */
public class FileUploadReceiver implements Receiver, FinishedListener {

    private static final long serialVersionUID = 1L;

    // Upload directory.
    private static final String UPLOAD_DIR = "/";
    private static Logger log = LoggerFactory.getLogger(FileUploadReceiver.class);
    // Filename of the upload.
    protected String fileName;
    // Form field.
    protected SelectFileField field;

    public FileUploadReceiver(SelectFileField field) {
        this.field = field;
    }

    public OutputStream receiveUpload(String filename, String mimeType) {
        fileName = filename;

        File file = null;
        FileOutputStream fos = null;
        try {
            // Open the file for writing.
            file = new File(UPLOAD_DIR + File.separator + fileName);
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new ActivitiIllegalArgumentException("Could not write to file " + UPLOAD_DIR
                    + File.separator + fileName);
        }

        return fos;
    }

    public void uploadFinished(FinishedEvent event) {
        log.info("Upload of {}", UPLOAD_DIR + File.separator + fileName);
        //    System.out.println("Upload of " + UPLOAD_DIR + File.separator + fileName);
        field.setValue(fileName);
    }

}