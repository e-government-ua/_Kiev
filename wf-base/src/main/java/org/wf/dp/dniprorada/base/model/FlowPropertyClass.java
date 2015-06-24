package org.wf.dp.dniprorada.base.model;

import javax.persistence.Column;

/**
 * Handler Class for FlowProperty
 *
 * User: goodg_000
 * Date: 14.06.2015
 * Time: 15:11
 */
@javax.persistence.Entity
public class FlowPropertyClass extends NamedEntity {

   /**
    * Fully qualified class name. I.e. package.className
    */
   @Column
   private String sPath;

   public String getsPath() {
      return sPath;
   }

   public void setsPath(String sPath) {
      this.sPath = sPath;
   }
}
