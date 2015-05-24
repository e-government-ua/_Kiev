package org.activiti.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.wf.dp.dniprorada.dao.BaseEntityDao;
import org.wf.dp.dniprorada.model.*;
import org.wf.dp.dniprorada.service.EntityService;
import org.wf.dp.dniprorada.util.JsonRestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping(value = "/services")
public class ActivitiRestServicesController {

   @Autowired
   private BaseEntityDao baseEntityDao;

   @Autowired
   private EntityService entityService;

   @RequestMapping(value = "/getService", method = RequestMethod.GET)
   public
   @ResponseBody
   ResponseEntity getService(@RequestParam(value = "nID") Integer nID) {
      Service service = baseEntityDao.getById(Service.class, nID);
      return regionsToJsonResponse(service);
   }

   @RequestMapping(value = "/setService", method = RequestMethod.POST)
   public
   @ResponseBody
   ResponseEntity setService(@RequestBody String jsonData) throws IOException {

      Service service = JsonRestUtils.readObject(jsonData, Service.class);

      Service updatedService = entityService.update(service);
      return regionsToJsonResponse(updatedService);
   }

   private ResponseEntity regionsToJsonResponse(Service oService) {
      oService.setSubcategory(null);
      for (ServiceData oServiceData : oService.getServiceDataList()) {
         oServiceData.setService(null);
         if (oServiceData.getCity() != null) {
            oServiceData.getCity().setRegion(null);
         }
         if (oServiceData.getRegion() != null) {
            oServiceData.getRegion().setCities(null);
         }
      }
      return JsonRestUtils.toJsonResponse(oService);
   }

   @RequestMapping(value = "/getPlaces", method = RequestMethod.GET)
   public
   @ResponseBody
   ResponseEntity getPlaces() {
      List<Region> regions = baseEntityDao.getAll(Region.class);
      return regionsToJsonResponse(regions);
   }

   @RequestMapping(value = "/setPlaces", method = RequestMethod.POST)
   public
   @ResponseBody
   ResponseEntity setPlaces(@RequestBody String jsonData) {

      List<Region> regions = Arrays.asList(JsonRestUtils.readObject(jsonData, Region[].class));
      List<Region> updatedRegions = entityService.update(regions);
      return regionsToJsonResponse(updatedRegions);
   }

   private ResponseEntity regionsToJsonResponse(List<Region> regions) {
      for (Region r : regions) {
         for (City c : r.getCities()) {
            c.setRegion(null);
         }
      }

      return JsonRestUtils.toJsonResponse(regions);
   }

   @RequestMapping(value = "/getServicesTree", method = RequestMethod.GET)
   public
   @ResponseBody
   ResponseEntity getServicesTree(@RequestParam(value = "sFind", required = false) String partOfName) {
      List<Category> categories = new ArrayList<>(baseEntityDao.getAll(Category.class));

      if (partOfName != null) {
         filterCategories(categories, partOfName);
      }

      return categoriesToJsonResponse(categories);
   }

   private void filterCategories(List<Category> categories, @RequestParam(value = "sFind", required = false) String sFind) {
      for (Iterator<Category> aCategory = categories.iterator(); aCategory.hasNext(); ) {
         Category oCategory = aCategory.next();

         for (Iterator<Subcategory> aSubcategory = oCategory.getSubcategories().iterator(); aSubcategory.hasNext(); ) {
            Subcategory oSubcategory = aSubcategory.next();

            for (Iterator<Service> aService = oSubcategory.getServices().iterator(); aService.hasNext(); ) {
               Service oService = aService.next();
               if (!isTextMatched(oService.getName(), sFind)) {
                  aService.remove();
               }
            }

            if (oSubcategory.getServices().isEmpty()) {
               aSubcategory.remove();
            }
         }

         if (oCategory.getSubcategories().isEmpty()) {
            aCategory.remove();
         }
      }
   }

   @RequestMapping(value = "/setServicesTree", method = RequestMethod.POST)
   public
   @ResponseBody
   ResponseEntity setServicesTree(@RequestBody String jsonData) {

      List<Category> categories = Arrays.asList(JsonRestUtils.readObject(jsonData, Category[].class));
      List<Category> updatedCategories = entityService.update(categories);

      return categoriesToJsonResponse(updatedCategories);
   }

   private boolean isTextMatched(String sWhere, String sFind) {
      return sWhere.toLowerCase().contains(sFind.toLowerCase());
   }

   private ResponseEntity categoriesToJsonResponse(List<Category> categories) {
      for (Category c : categories) {
         for (Subcategory sc : c.getSubcategories()) {
            sc.setCategory(null);

            for (Service service : sc.getServices()) {
               service.setFaq(null);
               service.setInfo(null);
               service.setLaw(null);
               service.setSub(service.getServiceDataList().size());
               service.setSub(service.getServiceDataFiltered().size());
               service.setServiceDataList(null);
               service.setSubcategory(null);
            }
         }
      }

      return JsonRestUtils.toJsonResponse(categories);
   }

}