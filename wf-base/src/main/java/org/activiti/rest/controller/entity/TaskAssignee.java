package org.activiti.rest.controller.entity;

/**
 * Created by diver on 4/8/15.
 */
public class TaskAssignee implements TaskAssigneeI {

    private String delegationState;
    private boolean isSuspended;
    private String id;
    private String name;
    private String description;
    private int priority;
    private String owner;
    private String assignee;
    private String processInstanceId;
    private String executionId;
    private String createTime;
    private String taskDefinitionKey;
    private String dueDate;
    private String category;
    private String parentTaskId;
    private String tenantId;
    private String formKey;
    private String processDifinitionId;

    @Override
    public String getDelegationState() {
        return delegationState;
    }

    public void setDelegationState(String delegationState) {
        this.delegationState = delegationState;
    }

    @Override
    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean isSuspended) {
        this.isSuspended = isSuspended;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @Override
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    @Override
    public String getProcessDefinitionId() {
        return processDifinitionId;
    }

    @Override
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    @Override
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public void setProcessDifinitionId(String processDifinitionId) {
        this.processDifinitionId = processDifinitionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TaskAssignee that = (TaskAssignee) o;

        if (isSuspended != that.isSuspended)
            return false;
        if (priority != that.priority)
            return false;
        if (assignee != null ? !assignee.equals(that.assignee) : that.assignee != null)
            return false;
        if (category != null ? !category.equals(that.category) : that.category != null)
            return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null)
            return false;
        if (delegationState != null ? !delegationState.equals(that.delegationState) : that.delegationState != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (dueDate != null ? !dueDate.equals(that.dueDate) : that.dueDate != null)
            return false;
        if (executionId != null ? !executionId.equals(that.executionId) : that.executionId != null)
            return false;
        if (formKey != null ? !formKey.equals(that.formKey) : that.formKey != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null)
            return false;
        if (parentTaskId != null ? !parentTaskId.equals(that.parentTaskId) : that.parentTaskId != null)
            return false;
        if (processDifinitionId != null ?
                !processDifinitionId.equals(that.processDifinitionId) :
                that.processDifinitionId != null)
            return false;
        if (processInstanceId != null ?
                !processInstanceId.equals(that.processInstanceId) :
                that.processInstanceId != null)
            return false;
        if (taskDefinitionKey != null ?
                !taskDefinitionKey.equals(that.taskDefinitionKey) :
                that.taskDefinitionKey != null)
            return false;
        if (tenantId != null ? !tenantId.equals(that.tenantId) : that.tenantId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = delegationState != null ? delegationState.hashCode() : 0;
        result = 31 * result + (isSuspended ? 1 : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + priority;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (assignee != null ? assignee.hashCode() : 0);
        result = 31 * result + (processInstanceId != null ? processInstanceId.hashCode() : 0);
        result = 31 * result + (executionId != null ? executionId.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (taskDefinitionKey != null ? taskDefinitionKey.hashCode() : 0);
        result = 31 * result + (dueDate != null ? dueDate.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (parentTaskId != null ? parentTaskId.hashCode() : 0);
        result = 31 * result + (tenantId != null ? tenantId.hashCode() : 0);
        result = 31 * result + (formKey != null ? formKey.hashCode() : 0);
        result = 31 * result + (processDifinitionId != null ? processDifinitionId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TaskAssignee{" +
                "delegationState='" + delegationState + '\'' +
                ", isSuspended=" + isSuspended +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", owner='" + owner + '\'' +
                ", assignee='" + assignee + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", executionId='" + executionId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", taskDefinitionKey='" + taskDefinitionKey + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", category='" + category + '\'' +
                ", parentTaskId='" + parentTaskId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", formKey='" + formKey + '\'' +
                ", processDifinitionId='" + processDifinitionId + '\'' +
                '}';
    }
}
