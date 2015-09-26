package org.wf.dp.dniprorada.dao.place;

import org.wf.dp.dniprorada.model.Place;

import static java.util.Objects.hash;
import static org.wf.dp.dniprorada.dao.place.PlaceDaoImpl.valid;

/**
 * @author dgroup
 * @since  12.08.2015
 */
public class PlaceHibernateHierarchyRecord {

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

    public PlaceHibernateHierarchyRecord(){
        // no actions required
    }

    public PlaceHibernateHierarchyRecord(Long placeId, Long typeId, String uaId,
                                         Boolean area, Boolean root, Long deep) {
        setPlaceId(placeId);
        setTypeId(typeId);
        setUaID(uaId);
        setArea(area);
        setRoot(root);
        setDeep(deep);
    }

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
        place.setsID_UA(getUaID());
        place.setPlaceTypeId(getTypeId());
        place.setOriginalName(getOriginalName());
        return place;
    }


    public PlaceHierarchyTree toTreeElement() {
        PlaceHierarchyTree node = new PlaceHierarchyTree();
        node.setPlace(toPlace());
        node.setLevel(getDeep());
        return node;
    }

    public Boolean isNotEmpty(){
        return  valid(getTypeId()) ||
                valid(isArea()) ||
                valid(isRoot()) ||
                valid(getDeep());
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PlaceHibernateHierarchyRecord))
            return false;

        PlaceHibernateHierarchyRecord that = (PlaceHibernateHierarchyRecord) obj;
        return placeId != null && that.placeId != null && placeId.equals(that.placeId);
    }

    @Override
    public int hashCode() {
        return hash(placeId, typeId, uaID, name, originalName, parentId, area, root, deep, alreadyIncluded);
    }
}