package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.base.model.NamedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author dgroup
 * @since  20.07.2015
 */
@Entity
public class PlaceType extends NamedEntity {

    @Column(name ="nOrder")
    @JsonProperty("nOrder")
    private Long order;

    @Column(name ="bArea")
    @JsonProperty("bArea")
    private boolean area; // "Площадь" (true = область/район/регион и т.д.)

    @Column(name ="bRoot")
    @JsonProperty("bRoot")
    private boolean root; // "Корень" (true = административная еденица страны)


    public Long getOrder() {
        return order;
    }
    public void setOrder(Long order) {
        this.order = order;
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
}