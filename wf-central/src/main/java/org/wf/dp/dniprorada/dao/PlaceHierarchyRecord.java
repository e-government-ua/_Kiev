package org.wf.dp.dniprorada.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author dgroup
 * @since  12.08.2015
 */
public class PlaceHierarchyRecord {

    @JsonProperty
    private long placeId;

    @JsonProperty
    private long typeId;

    @JsonProperty
    private String uaID;

    @JsonProperty
    private String name;

    @JsonProperty
    private String originalName;

    @JsonProperty
    private long rootId;

    @JsonProperty
    private long parentId;

    @JsonProperty
    private long deep;

    @JsonProperty
    private long areaId;

    public long getPlaceId() {
        return placeId;
    }
    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public long getTypeId() {
        return typeId;
    }
    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getUaID() {
        return uaID;
    }
    public void setUaID(String uaID) {
        this.uaID = uaID;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public long getRootId() {
        return rootId;
    }
    public void setRootId(long rootId) {
        this.rootId = rootId;
    }

    public long getParentId() {
        return parentId;
    }
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getDeep() {
        return deep;
    }
    public void setDeep(long deep) {
        this.deep = deep;
    }

    public long getAreaId() {
        return areaId;
    }
    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }
}