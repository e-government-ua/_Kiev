package org.wf.dp.dniprorada.model;

/**
 * User: goodg_000
 * Date: 04.05.2015
 * Time: 21:50
 */
public class City extends Entity {
   private String name;
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
