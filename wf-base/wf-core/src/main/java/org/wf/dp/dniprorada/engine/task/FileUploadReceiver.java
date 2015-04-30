package org.wf.dp.dniprorada.engine.task;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
 
import org.activiti.engine.ActivitiIllegalArgumentException;
 
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.Receiver;
 

/**
 * 
 * @author inna
 *
 */
public class FileUploadReceiver implements Receiver, FinishedListener
{
 
  private static final long   serialVersionUID = 1L;
 
  // Upload directory.
  private static final String UPLOAD_DIR       = "/";
 
  // Filename of the upload.
  protected String            fileName;
 
  // Form field.
  protected SelectFileField   field;
 
  public FileUploadReceiver(SelectFileField field)
  {
    this.field = field;
  }
 
  public OutputStream receiveUpload(String filename, String mimeType)
  {
    fileName = filename;
 
    File file = null;
    FileOutputStream fos = null;
    try
    {
      // Open the file for writing.
      file = new File(UPLOAD_DIR + File.separator + fileName);
      fos = new FileOutputStream(file);
    }
    catch (FileNotFoundException e)
    {
      throw new ActivitiIllegalArgumentException("Could not write to file " + UPLOAD_DIR
          + File.separator + fileName);
    }
 
    return fos;
  }
 
  public void uploadFinished(FinishedEvent event)
  {
    System.out.println("Upload of " + UPLOAD_DIR + File.separator + fileName);
    field.setValue(fileName);
  }
 
}