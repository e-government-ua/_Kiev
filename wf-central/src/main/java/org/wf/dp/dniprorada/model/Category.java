package org.wf.dp.dniprorada.model;

/**
 * User: goodg_000
 * Date: 04.05.2015
 * Time: 22:10
 */
public class Category extends Entity {

   private String code;
   private String name;
   private Integer order;

   public String getCode() {
      return code;
   }
   public void setCode(String code) {
      this.code = code;
   }

   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }

   public Integer getOrder() {
      return order;
   }
   public void setOrder(Integer order) {
      this.order = order;
   }
}
