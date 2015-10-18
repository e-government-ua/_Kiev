package org.wf.dp.dniprorada.base.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Abstract Entity with name.
 * <p/>
 * User: goodg_000
 * Date: 14.06.2015
 * Time: 15:07
 */
@MappedSuperclass
public abstract class NamedEntity extends Entity {

    @JsonProperty(value = "sName")
    @Column(name = "sName", nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
