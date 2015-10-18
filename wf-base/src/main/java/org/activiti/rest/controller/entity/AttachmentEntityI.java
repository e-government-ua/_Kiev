package org.activiti.rest.controller.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * Created by Dmytro Tsapko on 6/5/2015.
 */

@JsonSerialize(as = AttachmentEntity.class)
@JsonDeserialize(as = AttachmentEntity.class)
public interface AttachmentEntityI {

    /**
     * returns attachment ID
     */
    String getId();

    /**
     * returns attachment name
     */
    String getName();

    /**
     * returns attachment description
     */
    String getDescription();

    /**
     * returns MIME type of content that this attachment refers to
     */
    String getType();

    /**
     * returns task ID which this attachment is associated
     */
    String getTaskId();

    /**
     * returns returns process instance ID which this attachment is associated
     */
    String getProcessInstanceId();

    /**
     * returns returns URI where attachement content is located, if contaent uploaded with input stream returns null
     */
    String getUrl();

    /**
     * reference to the user who created this attachment.
     */
    String getUserId();

    /**
     * timestamp when this attachment was created
     */
    Date getTime();

}
