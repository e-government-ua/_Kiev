package org.wf.dp.dniprorada.base.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The property of flow. Stored in regional server.
 *
 * User: goodg_000
 * Date: 14.06.2015
 * Time: 15:18
 */
@javax.persistence.Entity
public class FlowProperty extends Entity {

   @JsonProperty(value = "nID_Flow_ServiceData")
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nID_Flow_ServiceData")
   private Flow_ServiceData oFlow_ServiceData;

   @JsonProperty(value = "nID_FlowPropertyClass")
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nID_FlowPropertyClass")
   private FlowPropertyClass oFlowPropertyClass;

   @Column
   private String sData;

   public Flow_ServiceData getoFlow_ServiceData() {
      return oFlow_ServiceData;
   }
   public void setoFlow_ServiceData(Flow_ServiceData oFlow_ServiceData) {
      this.oFlow_ServiceData = oFlow_ServiceData;
   }

   public FlowPropertyClass getoFlowPropertyClass() {
      return oFlowPropertyClass;
   }
   public void setoFlowPropertyClass(FlowPropertyClass oFlowPropertyClass) {
      this.oFlowPropertyClass = oFlowPropertyClass;
   }

   public String getsData() {
      return sData;
   }
   public void setsData(String sData) {
      this.sData = sData;
   }
}
