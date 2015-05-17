package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.sf.brunneng.jom.annotations.Identifier;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * User: goodg_000
 * Date: 04.05.2015
 * Time: 21:51
 */
@MappedSuperclass
public abstract class Entity {

   @JsonProperty(value="nID")
   @Id
   @Column(name="nID")
   private Integer id;

   @Identifier
   public Integer getId() {
      return id;
   }
   public void setId(Integer id) {
      this.id = id;
   }
}
