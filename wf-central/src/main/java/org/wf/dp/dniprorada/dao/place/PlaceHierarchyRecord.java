package org.wf.dp.dniprorada.dao.place;

import org.wf.dp.dniprorada.model.Place;

/**
 * @author dgroup
 * @since  12.08.2015
 */
public class PlaceHierarchyRecord {

    private long placeId;
    private long typeId;
    private String uaID;
    private String name;
    private String originalName;
    private long rootId;
    private long parentId;
    private long deep;
    private long areaId;
    private boolean alreadyIncluded;

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

    public boolean isAlreadyIncluded() {
        return alreadyIncluded;
    }
    public void setAlreadyIncluded(boolean alreadyIncluded) {
        this.alreadyIncluded = alreadyIncluded;
    }

    public Place toPlace() {
        Place place = new Place();
        place.setId(getPlaceId());
        place.setName(getName());
        place.setUaId(getUaID());
        place.setPlaceTypeId(getTypeId());
        place.setOriginalName(getOriginalName());
        return place;
    }


    public PlaceHierarchy toTree() {
        PlaceHierarchy node = new PlaceHierarchy();
        node.setPlace(toPlace());
        node.setLevel(getDeep());
        return node;
    }

    @Override
    public String toString() {
        return "PlaceHierarchyRecord{" +
                "placeId=" + placeId +
                ", typeId=" + typeId +
                ", uaID='" + uaID + '\'' +
                ", name='" + name + '\'' +
                ", originalName='" + originalName + '\'' +
                ", rootId=" + rootId +
                ", parentId=" + parentId +
                ", deep=" + deep +
                ", areaId=" + areaId +
                ", alreadyIncluded=" + alreadyIncluded +
                '}';
    }
}