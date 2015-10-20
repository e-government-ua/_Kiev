package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.base.model.NamedEntity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * User: goodg_000 Date: 04.05.2015 Time: 21:50
 */
@javax.persistence.Entity
public class City extends NamedEntity {

    @JsonProperty(value = "nID_Region")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nID_Region", insertable = false, updatable = false, nullable = false)
    private Region region;

    // ИД-строка кода классификатора КОАТУУ
    @Column
    private String sID_UA;

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getsID_UA() {
        return sID_UA;
    }

    public void setsID_UA(String sID_UA) {
        this.sID_UA = sID_UA;
    }
}
