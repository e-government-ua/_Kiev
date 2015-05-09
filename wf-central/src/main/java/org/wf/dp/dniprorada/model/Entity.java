package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.sf.brunneng.jom.annotations.Identifier;

/**
 * User: goodg_000
 * Date: 04.05.2015
 * Time: 21:51
 */
public abstract class Entity {

   @JsonProperty(value="nID")
   private Integer id;

   @Identifier
   public Integer getId() {
      return id;
   }
   public void setId(Integer id) {
      this.id = id;
   }
}
