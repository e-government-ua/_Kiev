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
    private long parentId;
    private boolean area;
    private boolean root;
    private long deep;
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

    public boolean isAlreadyIncluded() {
        return alreadyIncluded;
    }
    public void setAlreadyIncluded(boolean alreadyIncluded) {
        this.alreadyIncluded = alreadyIncluded;
    }

    public boolean isArea() {
        return area;
    }
    public void setArea(boolean area) {
        this.area = area;
    }

    public boolean isRoot() {
        return root;
    }
    public void setRoot(boolean root) {
        this.root = root;
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


    public PlaceHierarchyTree toTree() {
        PlaceHierarchyTree node = new PlaceHierarchyTree();
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
                ", parentId=" + parentId +
                ", area=" + area +
                ", root=" + root +
                ", deep=" + deep +
                ", alreadyIncluded=" + alreadyIncluded +
                '}';
    }
}