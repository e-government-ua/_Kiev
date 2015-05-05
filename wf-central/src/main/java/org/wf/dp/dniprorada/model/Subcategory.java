package org.wf.dp.dniprorada.model;

/**
 * User: goodg_000
 * Date: 04.05.2015
 * Time: 22:24
 */
public class Subcategory extends Entity {

   private Integer id;
   private String name;
   private String code;
   private Category category;
   private Integer order;

   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }

   public String getCode() {
      return code;
   }
   public void setCode(String code) {
      this.code = code;
   }

   public Category getCategory() {
      return category;
   }
   public void setCategory(Category category) {
      this.category = category;
   }

   public Integer getOrder() {
      return order;
   }
   public void setOrder(Integer order) {
      this.order = order;
   }
}
