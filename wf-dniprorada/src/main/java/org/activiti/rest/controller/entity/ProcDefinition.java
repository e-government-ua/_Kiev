package org.activiti.rest.controller.entity;

/**
 * Created by diver on 4/12/15.
 */
public class ProcDefinition implements ProcDefinitionI {

    private String id;
    private String category;
    private String name;
    private String key;
    private String description;
    private int version;
    private String resourceName;
    private String deploymentId;
    private String diagramResourceName;
    private boolean hasStartFormKey;
    private boolean hasGraphicalNotation;
    private boolean isSuspended;
    private String tenantId;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public String getResourceName() {
        return resourceName;
    }

    @Override
    public String getDeploymentId() {
        return deploymentId;
    }

    @Override
    public String getDiagramResourceName() {
        return diagramResourceName;
    }

    @Override
    public boolean hasStartFormKey() {
        return hasStartFormKey;
    }

    @Override
    public boolean hasGraphicalNotation() {
        return hasGraphicalNotation;
    }

    @Override
    public boolean isSuspended() {
        return isSuspended;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }

    public void setHasStartFormKey(boolean hasStartFormKey) {
        this.hasStartFormKey = hasStartFormKey;
    }

    public void setHasGraphicalNotation(boolean hasGraphicalNotation) {
        this.hasGraphicalNotation = hasGraphicalNotation;
    }

    public void setSuspended(boolean isSuspended) {
        this.isSuspended = isSuspended;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcDefinition that = (ProcDefinition) o;

        if (hasGraphicalNotation != that.hasGraphicalNotation) return false;
        if (hasStartFormKey != that.hasStartFormKey) return false;
        if (isSuspended != that.isSuspended) return false;
        if (version != that.version) return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (deploymentId != null ? !deploymentId.equals(that.deploymentId) : that.deploymentId != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (diagramResourceName != null ? !diagramResourceName.equals(that.diagramResourceName) : that.diagramResourceName != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (resourceName != null ? !resourceName.equals(that.resourceName) : that.resourceName != null) return false;
        if (tenantId != null ? !tenantId.equals(that.tenantId) : that.tenantId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + version;
        result = 31 * result + (resourceName != null ? resourceName.hashCode() : 0);
        result = 31 * result + (deploymentId != null ? deploymentId.hashCode() : 0);
        result = 31 * result + (diagramResourceName != null ? diagramResourceName.hashCode() : 0);
        result = 31 * result + (hasStartFormKey ? 1 : 0);
        result = 31 * result + (hasGraphicalNotation ? 1 : 0);
        result = 31 * result + (isSuspended ? 1 : 0);
        result = 31 * result + (tenantId != null ? tenantId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProcessDefinition{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", description='" + description + '\'' +
                ", version=" + version +
                ", resourceName='" + resourceName + '\'' +
                ", deploymentId='" + deploymentId + '\'' +
                ", diagramResourceName='" + diagramResourceName + '\'' +
                ", hasStartFormKey=" + hasStartFormKey +
                ", hasGraphicalNotation=" + hasGraphicalNotation +
                ", isSuspended=" + isSuspended +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }
}
