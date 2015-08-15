package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.base.model.NamedEntity;

import javax.persistence.Column;

/**
 * @author dgroup
 * @since  19.07.2015
 */
@javax.persistence.Entity
public class Place extends NamedEntity {

    @Column(name ="nID_PlaceType")
    @JsonProperty("nID_PlaceType")
    private Long placeTypeId;

    @Column(name ="sID_UA")
    @JsonProperty("sID_UA")
    private String uaId;

    @Column(name ="sNameOriginal")
    @JsonProperty("sNameOriginal")
    private String originalName;


    public Long getPlaceTypeId() {
        return placeTypeId;
    }
    public void setPlaceTypeId(Long placeTypeId) {
        this.placeTypeId = placeTypeId;
    }

    public String getUaId() {
        return uaId;
    }
    public void setUaId(String uaId) {
        this.uaId = uaId;
    }

    public String getOriginalName() {
        return originalName;
    }
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @Override
    public String toString() {
        return "Place{" +
                ", id=" + getId() +
                ", name=" + getName() +
                ", placeTypeId=" + placeTypeId +
                ", uaId='" + uaId + '\'' +
                ", originalName='" + originalName + '\'' +
                '}';
    }
}