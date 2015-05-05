package org.wf.dp.dniprorada.model;

/**
 * User: goodg_000
 * Date: 04.05.2015
 * Time: 23:52
 */
public class ServiceData extends Entity {

   private Service service;
   private City city;
   private Region region;
   private ServiceType serviceType;
   private String data;
   private String url;

   public Service getService() {
      return service;
   }
   public void setService(Service service) {
      this.service = service;
   }

   public City getCity() {
      return city;
   }
   public void setCity(City city) {
      this.city = city;
   }

   public Region getRegion() {
      return region;
   }
   public void setRegion(Region region) {
      this.region = region;
   }

   public ServiceType getServiceType() {
      return serviceType;
   }
   public void setServiceType(ServiceType serviceType) {
      this.serviceType = serviceType;
   }

   public String getData() {
      return data;
   }
   public void setData(String data) {
      this.data = data;
   }

   public String getUrl() {
      return url;
   }
   public void setUrl(String url) {
      this.url = url;
   }
}
