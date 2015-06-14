package org.wf.dp.dniprorada.base.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.sf.brunneng.jom.annotations.Identifier;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract entity.
 *
 * User: goodg_000
 * Date: 04.05.2015
 * Time: 21:51
 */
@MappedSuperclass
public abstract class Entity {

   @JsonProperty(value="nID")
   @Id
   @Column(name="nID")
   private Long id;

   @Identifier
   public Long getId() {
      return id;
   }
   public void setId(Long id) {
      this.id = id;
   }
}
