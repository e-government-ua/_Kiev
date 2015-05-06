package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User: goodg_000
 * Date: 04.05.2015
 * Time: 21:50
 */
public class City extends Entity {
	@JsonProperty(value="sName")
   private String name;
	@JsonProperty(value="nID_Region")
   private Region region;

   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }

   public Region getRegion() {
      return region;
   }
   public void setRegion(Region region) {
      this.region = region;
   }
}
