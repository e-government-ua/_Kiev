package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

/**
 * User: goodg_000
 * Date: 04.05.2015
 * Time: 23:10
 */
@javax.persistence.Entity
public class Service extends Entity {

	@JsonProperty(value="sName")
   @Column(name = "sName", nullable = false)
   private String name;

	@JsonProperty(value="nOrder")
   @Column(name = "nOrder", nullable = false)
   private Integer order;

	@JsonProperty(value="nID_Subcategory")
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "nID_Subcategory", nullable = false)
   private Subcategory subcategory;

	@JsonProperty(value="aServiceData")
   @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
   @LazyCollection(LazyCollectionOption.FALSE)
   private List<ServiceData> serviceDataList;

	@JsonProperty(value="sInfo")
   @Column(name = "sInfo", nullable = false)
   private String info;

   @JsonProperty("sFAQ")
   @Column(name = "sFAQ", nullable = false)
   private String faq;

   @JsonProperty("sLaw")
   @Column(name = "sLaw", nullable = false)
   private String law;

   transient public boolean bContain = false;
   
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }

   
    /*public boolean isServiceDataExist() {
        return serviceDataList.size() > 0;
    }*/
    public boolean bContain() {
        return bContain;
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
