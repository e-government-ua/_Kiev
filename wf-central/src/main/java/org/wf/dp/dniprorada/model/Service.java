package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * User: goodg_000
 * Date: 04.05.2015
 * Time: 23:10
 */
public class Service extends Entity {

	@JsonProperty(value="sName")
   private String name;
	@JsonProperty(value="nOrder")
   private Integer order;
	@JsonProperty(value="nID_Subcategory")
   private Subcategory subcategory;
	@JsonProperty(value="aServiceData")
   private List<ServiceData> serviceDataList;   
	@JsonProperty(value="sInfo")
   private String info;
    @JsonProperty("sFAQ")
   private String faq;
    @JsonProperty("sLaw")
   private String law;

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

   public Subcategory getSubcategory() {
      return subcategory;
   }
   public void setSubcategory(Subcategory subcategory) {
      this.subcategory = subcategory;
   }

   public List<ServiceData> getServiceDataList() {
      return serviceDataList;
   }
   public void setServiceDataList(List<ServiceData> serviceDataList) {
      this.serviceDataList = serviceDataList;
   }

   public String getInfo() {
      return info;
   }
   public void setInfo(String info) {
      this.info = info;
   }

   public String getFaq() {
      return faq;
   }
   public void setFaq(String faq) {
      this.faq = faq;
   }

   public String getLaw() {
      return law;
   }
   public void setLaw(String law) {
      this.law = law;
   }
}
