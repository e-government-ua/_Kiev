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

    @Column
    @JsonProperty
    private String sID_UA;

    @Column(name ="sNameOriginal")
    @JsonProperty("sNameOriginal")
    private String originalName;


    public Place(){
        // no actions required
    }

    public Place(Long placeId, String name, Long typeId, String uaId, String originalName){
        setId(placeId);
        setName(name);
        setPlaceTypeId(typeId);
        setsID_UA(uaId);
        setOriginalName(originalName);
    }


    public Long getPlaceTypeId() {
        return placeTypeId;
    }
    public void setPlaceTypeId(Long placeTypeId) {
        this.placeTypeId = placeTypeId;
    }

    public String getsID_UA() {
        return sID_UA;
    }
    public void setsID_UA(String sID_UA) {
        this.sID_UA = sID_UA;
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
                ", uaId='" + sID_UA + '\'' +
                ", originalName='" + originalName + '\'' +
                '}';
    }
}