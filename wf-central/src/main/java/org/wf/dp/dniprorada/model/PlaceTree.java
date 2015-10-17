package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.base.model.Entity;

import javax.persistence.Column;

/**
 * @author dgroup
 * @since 20.07.2015
 */
@javax.persistence.Entity
public class PlaceTree extends Entity {

    @Column(name = "nID_Place")
    @JsonProperty("nID_Place")
    private Long placeId;   // ИД-номер места

    @Column(name = "nID_Place_Root")
    @JsonProperty("nID_Place_Root")
    private Long rootId;    // ИД-номер места-корня

    @Column(name = "nID_Place_Area")
    @JsonProperty("nID_Place_Area")
    private Long areaId;    // ИД-номер места-площади

    @Column(name = "nID_Place_Parent")
    @JsonProperty("nID_Place_Parent")
    private Long parentId;  // ИД-номер места-родителя

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}