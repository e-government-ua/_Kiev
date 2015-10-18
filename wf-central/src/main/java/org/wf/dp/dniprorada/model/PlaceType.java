package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.base.model.NamedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author dgroup
 * @since 20.07.2015
 */
@Entity
public class PlaceType extends NamedEntity {

    @Column(name = "nOrder")
    @JsonProperty("nOrder")
    private Long order;

    @Column
    @JsonProperty
    private boolean bArea; // "Площадь" (true = область/район/регион и т.д.)

    @Column
    @JsonProperty
    private boolean bRoot; // "Корень" (true = административная еденица страны)

    public PlaceType() {
        // no action required
    }

    public PlaceType(Long placeTypeId, String name, Long order, Boolean area, Boolean root) {
        setId(placeTypeId);
        setName(name);
        setOrder(order);
        setbArea(area);
        setbRoot(root);
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public boolean isbArea() {
        return bArea;
    }

    public void setbArea(boolean bArea) {
        this.bArea = bArea;
    }

    public boolean isbRoot() {
        return bRoot;
    }

    public void setbRoot(boolean bRoot) {
        this.bRoot = bRoot;
    }
}