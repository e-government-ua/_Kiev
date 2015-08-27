package org.wf.dp.dniprorada.dao.place;

import org.wf.dp.dniprorada.model.Place;

/**
 * @author dgroup
 * @since  12.08.2015
 */
public class PlaceHierarchyRecord  {

    private Long placeId;
    private Long typeId;
    private String uaID;
    private String name;
    private String originalName;
    private Long parentId;
    private Boolean area;
    private Boolean root;
    private Long deep;
    private Boolean alreadyIncluded = false;

    public Long getPlaceId() {
        return placeId;
    }
    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public Long getTypeId() {
        return typeId;
    }
    public void setTypeId(Long typeId) {
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

    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getDeep() {
        return deep;
    }
    public void setDeep(Long deep) {
        this.deep = deep;
    }

    public Boolean isAlreadyIncluded() {
        return alreadyIncluded;
    }
    public void setAlreadyIncluded(Boolean alreadyIncluded) {
        this.alreadyIncluded = alreadyIncluded;
    }

    public Boolean isArea() {
        return area;
    }
    public void setArea(Boolean area) {
        this.area = area;
    }

    public Boolean isRoot() {
        return root;
    }
    public void setRoot(Boolean root) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceHierarchyRecord)) return false;

        PlaceHierarchyRecord that = (PlaceHierarchyRecord) o;

        if (!alreadyIncluded.equals(that.alreadyIncluded)) return false;
        if (area != null ? !area.equals(that.area) : that.area != null) return false;
        if (!deep.equals(that.deep)) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (originalName != null ? !originalName.equals(that.originalName) : that.originalName != null) return false;
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null) return false;
        if (placeId != null ? !placeId.equals(that.placeId) : that.placeId != null) return false;
        if (root != null ? !root.equals(that.root) : that.root != null) return false;
        if (typeId != null ? !typeId.equals(that.typeId) : that.typeId != null) return false;
        if (uaID != null ? !uaID.equals(that.uaID) : that.uaID != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = placeId != null ? placeId.hashCode() : 0;
        result = 31 * result + (typeId != null ? typeId.hashCode() : 0);
        result = 31 * result + (uaID != null ? uaID.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (originalName != null ? originalName.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (area != null ? area.hashCode() : 0);
        result = 31 * result + (root != null ? root.hashCode() : 0);
        result = 31 * result + deep.hashCode();
        result = 31 * result + alreadyIncluded.hashCode();
        return result;
    }
}