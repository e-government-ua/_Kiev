package org.wf.dp.dniprorada.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

/**
 * User: goodg_000 Date: 03.05.2015 Time: 15:34
 */
@javax.persistence.Entity
public class Region extends Entity {

   @JsonProperty(value = "sName")
   @Column(name = "sName", nullable = false)
   private String name;

   @JsonProperty(value = "aCity")
   @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
   @LazyCollection(LazyCollectionOption.FALSE)
   @OrderBy("name asc")
   private List<City> cities;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List<City> getCities() {
      return cities;
   }

   public void setCities(List<City> cities) {
      this.cities = cities;
   }
}
