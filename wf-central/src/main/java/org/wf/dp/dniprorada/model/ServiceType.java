package org.wf.dp.dniprorada.model;

/**
 * User: goodg_000
 * Date: 04.05.2015
 * Time: 22:58
 */
public class ServiceType extends Entity {

   private String name;
   private String note;

   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }

   public String getNote() {
      return note;
   }
   public void setNote(String note) {
      this.note = note;
   }
}
