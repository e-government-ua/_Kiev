package org.activiti.rest.controller.entity;

import java.util.Date;

/**
 * Created by Tsapko Dmytro on 6/5/2015.
 */
public class AttachmentEntity implements AttachmentEntityI {

    String attachmentId;
    String attachmentName;
    String attachmentDescription;
    String contentType;
    String taskId;
    String processInstanceId;
    String contentUrl;
    String userId;
    Date createDate;

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public void setAttachmentDescription(String attachmentDescription) {
        this.attachmentDescription = attachmentDescription;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String getId() {
        return attachmentId;
    }

    @Override
    public String getName() {
        return attachmentName;
    }

    @Override
    public String getDescription() {
        return attachmentDescription;
    }

    @Override
    public String getType() {
        return contentType;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public String getUrl() {
        return contentUrl;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public Date getTime() {
        return createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        AttachmentEntity that = (AttachmentEntity) o;

        if (attachmentId != null ? !attachmentId.equals(that.attachmentId) : that.attachmentId != null)
            return false;
        if (attachmentName != null ? !attachmentName.equals(that.attachmentName) : that.attachmentName != null)
            return false;
        if (attachmentDescription != null ?
                !attachmentDescription.equals(that.attachmentDescription) :
                that.attachmentDescription != null)
            return false;
        if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null)
            return false;
        if (getTaskId() != null ? !getTaskId().equals(that.getTaskId()) : that.getTaskId() != null)
            return false;
        if (getProcessInstanceId() != null ?
                !getProcessInstanceId().equals(that.getProcessInstanceId()) :
                that.getProcessInstanceId() != null)
            return false;
        if (contentUrl != null ? !contentUrl.equals(that.contentUrl) : that.contentUrl != null)
            return false;
        if (getUserId() != null ? !getUserId().equals(that.getUserId()) : that.getUserId() != null)
            return false;
        return !(createDate != null ? !createDate.equals(that.createDate) : that.createDate != null);

    }

    @Override
    public int hashCode() {
        int result = attachmentId != null ? attachmentId.hashCode() : 0;
        result = 31 * result + (attachmentName != null ? attachmentName.hashCode() : 0);
        result = 31 * result + (attachmentDescription != null ? attachmentDescription.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (getTaskId() != null ? getTaskId().hashCode() : 0);
        result = 31 * result + (getProcessInstanceId() != null ? getProcessInstanceId().hashCode() : 0);
        result = 31 * result + (contentUrl != null ? contentUrl.hashCode() : 0);
        result = 31 * result + (getUserId() != null ? getUserId().hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AttachmentEntity{" +
                "attachmentId='" + attachmentId + '\'' +
                ", attachmentName='" + attachmentName + '\'' +
                ", attachmentDescription='" + attachmentDescription + '\'' +
                ", contentType='" + contentType + '\'' +
                ", taskId='" + taskId + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", userId='" + userId + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
