package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;

/**
 * User: goodg_000 Date: 04.05.2015 Time: 22:10
 */
@javax.persistence.Entity
public class Category extends org.wf.dp.dniprorada.base.model.NamedEntity {

    @JsonProperty(value = "sID")
    @Column(name = "sID", nullable = false)
    private String code;

    @JsonProperty(value = "nOrder")
    @Column(name = "nOrder", nullable = false)
    private Integer order;

    @JsonProperty(value = "aSubcategory")
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy("order asc")
    private List<Subcategory> subcategories;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }
}
