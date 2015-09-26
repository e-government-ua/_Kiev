package org.activiti.rest.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.base.dao.BaseEntityDao;
import org.wf.dp.dniprorada.base.model.Entity;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.base.util.SerializableResponseEntity;
import org.wf.dp.dniprorada.base.util.caching.CachedInvocationBean;
import org.wf.dp.dniprorada.base.util.caching.MethodCacheInterceptor;
import org.wf.dp.dniprorada.base.viewobject.ResultMessage;
import org.wf.dp.dniprorada.constant.KOATUU;
import org.wf.dp.dniprorada.model.*;
import org.wf.dp.dniprorada.service.EntityService;
import org.wf.dp.dniprorada.service.TableDataService;
import org.wf.dp.dniprorada.util.GeneralConfig;
import org.wf.dp.dniprorada.viewobject.TableData;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping(value = "/services")
public class ActivitiRestServicesController {

   private static final String GET_SERVICES_TREE = "getServicesTree";


   @Autowired
   private BaseEntityDao baseEntityDao;

   @Autowired
   private EntityService entityService;

   @Autowired
   private TableDataService tableDataService;

   @Autowired
   private CachedInvocationBean cachedInvocationBean;

   @Autowired(required = false)
   private MethodCacheInterceptor methodCacheInterceptor;

   @Autowired
   GeneralConfig generalConfig;

   @RequestMapping(value = "/getService", method = RequestMethod.GET)
   public
   @ResponseBody
   ResponseEntity getService(@RequestParam(value = "nID") Long nID) {
      Service service = baseEntityDao.findById(Service.class, nID);
      return regionsToJsonResponse(service);
   }

   @RequestMapping(value = "/setService", method = RequestMethod.POST)
   public
   @ResponseBody
   ResponseEntity setService(@RequestBody String jsonData) throws IOException {

      Service service = JsonRestUtils.readObject(jsonData, Service.class);

      Service updatedService = entityService.update(service);
      clearGetServicesCache();

      return regionsToJsonResponse(updatedService);
   }

   private void clearGetServicesCache() {
      if (methodCacheInterceptor != null) {
         methodCacheInterceptor.clearCacheForMethod(CachedInvocationBean.class, "invokeUsingCache", GET_SERVICES_TREE);
      }
   }

   @RequestMapping(value = "/removeService", method = RequestMethod.DELETE)
   public
   @ResponseBody
   ResponseEntity removeService(@RequestParam(value = "nID") Long nID,
                                @RequestParam(value = "bRecursive", required = false) Boolean bRecursive) {
      bRecursive = (bRecursive == null) ? false : bRecursive;
      ResponseEntity response;
      if (bRecursive) {
         response = recursiveForceServiceDelete(Service.class, nID);
      } else
         response = deleteEmptyContentEntity(Service.class, nID);
      return response;
   }

   @RequestMapping(value = "/removeServiceData", method = RequestMethod.DELETE)
   public
   @ResponseBody
   ResponseEntity removeServiceData(@RequestParam(value = "nID") Long nID,
                                    @RequestParam(value = "bRecursive", required = false) Boolean bRecursive) {
      bRecursive = (bRecursive == null) ? false : bRecursive;
      ResponseEntity response;
      if (bRecursive) {
         response = recursiveForceServiceDelete(ServiceData.class, nID);
      } else
         response = deleteEmptyContentEntity(ServiceData.class, nID);
      return response;
   }

   @RequestMapping(value = "/removeSubcategory", method = RequestMethod.DELETE)
   public
   @ResponseBody
   ResponseEntity removeSubcategory(@RequestParam(value = "nID") Long nID,
                                    @RequestParam(value = "bRecursive", required = false) Boolean bRecursive) {
      bRecursive = (bRecursive == null) ? false : bRecursive;
      ResponseEntity response;
      if (bRecursive) {
         response = recursiveForceServiceDelete(Subcategory.class, nID);
      } else
         response = deleteEmptyContentEntity(Subcategory.class, nID);
      return response;
   }

   @RequestMapping(value = "/removeCategory", method = RequestMethod.DELETE)
   public
   @ResponseBody
   ResponseEntity removeCategory(@RequestParam(value = "nID") Long nID,
                                 @RequestParam(value = "bRecursive", required = false) Boolean bRecursive) {
      bRecursive = (bRecursive == null) ? false : bRecursive;
      ResponseEntity response;
      if (bRecursive) {
         response = recursiveForceServiceDelete(Category.class, nID);
      } else
         response = deleteEmptyContentEntity(Category.class, nID);
      return response;
   }

   private <T extends Entity> ResponseEntity deleteEmptyContentEntity(Class<T> entityClass, Long nID) {
      T entity = baseEntityDao.findById(entityClass, nID);
      if (entity.getClass() == Service.class) {
         if (((Service) entity).getServiceDataList().isEmpty()) {
            return deleteApropriateEntity(entity);
         }
      } else if (entity.getClass() == Subcategory.class) {
         if (((Subcategory) entity).getServices().isEmpty()) {
            return deleteApropriateEntity(entity);
         }
      } else if (entity.getClass() == Category.class) {
         if (((Category) entity).getSubcategories().isEmpty()) {
            return deleteApropriateEntity(entity);
         }
      } else if (entity.getClass() == ServiceData.class) {
         return deleteApropriateEntity(entity);
      }
      return JsonRestUtils.toJsonResponse(HttpStatus.NOT_MODIFIED,
              new ResultMessage("error", "Entity isn't empty"));
   }

   @RequestMapping(value = "/removeServicesTree", method = RequestMethod.DELETE)
   public
   @ResponseBody
   ResponseEntity removeServicesTree() {
      List<Category> categories = new ArrayList<>(baseEntityDao.findAll(Category.class));
      for (int i = 0; i < categories.size(); i++) {
         Category category = categories.get(i);
         baseEntityDao.delete(category);
      }
      return JsonRestUtils.toJsonResponse(HttpStatus.OK,
              new ResultMessage("success", "ServicesTree removed"));
   }

   private <T extends Entity> ResponseEntity deleteApropriateEntity(T entity) {
      baseEntityDao.delete(entity);
      return JsonRestUtils.toJsonResponse(HttpStatus.OK,
              new ResultMessage("success", entity.getClass() + " id: " + entity.getId() + " removed"));
   }

   private <T extends Entity> ResponseEntity recursiveForceServiceDelete(Class<T> entityClass, Long nID) {
      T entity = baseEntityDao.findById(entityClass, nID);
      // hibernate will handle recursive deletion of all child entities
      // because of annotation: @OneToMany(mappedBy = "category",cascade = CascadeType.ALL, orphanRemoval = true)
      baseEntityDao.delete(entity);
      return JsonRestUtils.toJsonResponse(HttpStatus.OK,
              new ResultMessage("success", entityClass + " id: " + nID + " removed"));
   }

   private ResponseEntity regionsToJsonResponse(Service oService) {
      oService.setSubcategory(null);
      //for (ServiceData oServiceData : oService.getServiceDataList()) {
      List<ServiceData> aServiceData = oService.getServiceDataFiltered(generalConfig.bTest());
      for (ServiceData oServiceData : aServiceData) {
         oServiceData.setService(null);
         if (oServiceData.getCity() != null) {
            //oServiceData.setRegion(oServiceData.getCity().getRegion());
            //oServiceData.getCity().setRegion(null);
            //oServiceData.getRegion().setCities(null);
            oServiceData.getCity().getRegion().setCities(null);
         }else if (oServiceData.getRegion() != null) {
            oServiceData.getRegion().setCities(null);
         }
      }

    oService.setServiceDataList(aServiceData);
      return JsonRestUtils.toJsonResponse(oService);
   }

   @RequestMapping(value = "/getPlaces", method = RequestMethod.GET)
   public
   @ResponseBody
   ResponseEntity getPlaces() {
      List<Region> regions = baseEntityDao.findAll(Region.class);
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
   
    public static final String SERVICE_NAME_TEST_PREFIX = "_";

    public static final List<String> SUPPORTED_PLACE_IDS = new ArrayList<>();
    static {
        SUPPORTED_PLACE_IDS.add(String.valueOf(KOATUU.KYIVSKA_OBLAST.getId()));
        SUPPORTED_PLACE_IDS.add(String.valueOf(KOATUU.KYIV.getId()));
    }

   @RequestMapping(value = "/getServicesTree", method = RequestMethod.GET)
   public
   @ResponseBody
   ResponseEntity<String> getServicesTree(
           @RequestParam(value = "sFind", required = false) final String partOfName,
           @RequestParam(value = "asID_Place_UA", required = false) final List<String> placeUaIds,
           @RequestParam(value = "bShowEmptyFolders", required = false, defaultValue = "false") final boolean bShowEmptyFolders) {
      final boolean bTest = generalConfig.bTest();
      SerializableResponseEntity<String> entity = cachedInvocationBean.invokeUsingCache(new CachedInvocationBean.Callback<SerializableResponseEntity<String>>(
              GET_SERVICES_TREE, partOfName, placeUaIds, bTest) {
         @Override
         public SerializableResponseEntity<String> execute() {
            List<Category> categories = new ArrayList<>(baseEntityDao.findAll(Category.class));

            if (!bTest) {
               filterOutServicesByServiceNamePrefix(categories, SERVICE_NAME_TEST_PREFIX);
            }

            if (partOfName != null) {
               filterServicesByServiceName(categories, partOfName);
            }

            if (placeUaIds != null) {
//TODO: Зачем это было добавлено?                    placeUaIds.retainAll(SUPPORTED_PLACE_IDS);
               if (!placeUaIds.isEmpty()) {
                  filterServicesByPlaceIds(categories, placeUaIds);
               }
            }

            if (!bShowEmptyFolders) {
               hideEmptyFolders(categories);
            }

            return categoriesToJsonResponse(categories);
         }
      });

      return entity.toResponseEntity();
   }

    private void filterOutServicesByServiceNamePrefix(List<Category> categories, String prefix) {
        for (Category category : categories) {
            for (Subcategory subcategory : category.getSubcategories()) {
                for (Iterator<Service> serviceIterator = subcategory.getServices().iterator(); serviceIterator.hasNext();) {
                    Service service = serviceIterator.next();
                    if (service.getName().startsWith(prefix)) {
                        serviceIterator.remove();
                    }
                }
            }
        }
    }

    private void filterServicesByServiceName(List<Category> categories, String sFind) {
        for (Category category : categories) {
            for (Subcategory subcategory : category.getSubcategories()) {
                for (Iterator<Service> serviceIterator = subcategory.getServices().iterator(); serviceIterator.hasNext();) {
                    Service service = serviceIterator.next();
                    if (!isTextMatched(service.getName(), sFind)) {
                        serviceIterator.remove();
                    }
                }
            }
        }
    }

    private void filterServicesByPlaceIds(List<Category> categories, List<String> placeIds) {
        for (Category category : categories) {
            for (Subcategory subcategory : category.getSubcategories()) {
                for (Iterator<Service> serviceIterator = subcategory.getServices().iterator(); serviceIterator
                        .hasNext();) {
                    Service service = serviceIterator.next();
                    boolean isPlaceMatched = false;
                    //List<ServiceData> serviceDatas = service.getServiceDataFiltered(generalConfig.bTest());
                    List<ServiceData> serviceDatas = service.getServiceDataFiltered(true);
                    if (serviceDatas != null) {
                        for (ServiceData serviceData : serviceDatas) {
                            City city = serviceData.getCity();
                            if (city != null && placeIds.contains(city.getsID_UA())) {
                                isPlaceMatched = true;
                                break;
                            }
                            Region region = serviceData.getRegion();
                            if (region != null && placeIds.contains(region.getsID_UA())) {
                                isPlaceMatched = true;
                                break;
                            }
                        }
                    }
                    if (!isPlaceMatched) {
                        serviceIterator.remove();
                    }
                }
            }
        }
    }

   /**
    * Filter out empty categories and subcategories
    *
    * @param categories
    */
   private void hideEmptyFolders(List<Category> categories) {
       for (Iterator<Category> categoryIterator = categories.iterator(); categoryIterator.hasNext();) {
           Category category = categoryIterator.next();

           for (Iterator<Subcategory> subcategoryIterator = category.getSubcategories().iterator(); subcategoryIterator
                   .hasNext();) {
               Subcategory subcategory = subcategoryIterator.next();
               if (subcategory.getServices().isEmpty()) {
                   subcategoryIterator.remove();
               }
           }

           if (category.getSubcategories().isEmpty()) {
               categoryIterator.remove();
           }
       }
   }
   
   @RequestMapping(value = "/setServicesTree", method = RequestMethod.POST)
   public
   @ResponseBody
   ResponseEntity setServicesTree(@RequestBody String jsonData) {

      List<Category> categories = Arrays.asList(JsonRestUtils.readObject(jsonData, Category[].class));
      List<Category> updatedCategories = entityService.update(categories);
      clearGetServicesCache();

      return categoriesToJsonResponse(updatedCategories).toResponseEntity();
   }

   @RequestMapping(value = "/getServicesAndPlacesTables", method = RequestMethod.GET)
   public @ResponseBody ResponseEntity getServicesAndPlacesTables() {
      List<TableData> tableDataList = tableDataService.exportData(TableDataService.TablesSet.ServicesAndPlaces);
      return JsonRestUtils.toJsonResponse(tableDataList);
   }

   @RequestMapping(value = "/setServicesAndPlacesTables", method = RequestMethod.POST)
   public @ResponseBody ResponseEntity setServicesAndPlacesTables(@RequestBody String jsonData) {
      List<TableData> tableDataList = Arrays.asList(JsonRestUtils.readObject(jsonData, TableData[].class));

      tableDataService.importData(TableDataService.TablesSet.ServicesAndPlaces, tableDataList);
      return JsonRestUtils.toJsonResponse(HttpStatus.OK,
              new ResultMessage("success", "Data successfully imported."));
   }

   @RequestMapping(value = "/downloadServicesAndPlacesTables", method = RequestMethod.GET)
   public @ResponseBody void downloadServicesAndPlacesTables(HttpServletResponse response) throws IOException {
      List<TableData> tableDataList = tableDataService.exportData(TableDataService.TablesSet.ServicesAndPlaces);

      String dateTimeString = DateTimeFormat.forPattern("yyyy-MM-dd_HH-mm-ss").print(new DateTime());

      String fileName = "igov.ua.catalog_" + dateTimeString + ".json";
      response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
      JsonRestUtils.writeJsonToOutputStream(tableDataList, response.getOutputStream());
   }

   @RequestMapping(value = "/uploadServicesAndPlacesTables", method = RequestMethod.POST)
   public @ResponseBody ResponseEntity uploadServicesAndPlacesTables(@RequestParam("file") MultipartFile file)
           throws IOException {
      List<TableData> tableDataList = Arrays.asList(JsonRestUtils.readObject(file.getInputStream(), TableData[].class));

      tableDataService.importData(TableDataService.TablesSet.ServicesAndPlaces, tableDataList);
      return JsonRestUtils.toJsonResponse(HttpStatus.OK,
              new ResultMessage("success", "Data successfully imported."));
   }

   private boolean isTextMatched(String sWhere, String sFind) {
      return sWhere.toLowerCase().contains(sFind.toLowerCase());
   }

   private SerializableResponseEntity<String> categoriesToJsonResponse(List<Category> categories) {
      for (Category c : categories) {
         for (Subcategory sc : c.getSubcategories()) {
            sc.setCategory(null);

            for (Service service : sc.getServices()) {
               service.setFaq(null);
               service.setInfo(null);
               service.setLaw(null);
               //service.setSub(service.getServiceDataList().size());

               List<ServiceData> serviceDataFiltered = service.getServiceDataFiltered(generalConfig.bTest());
               service.setSub(serviceDataFiltered != null ? serviceDataFiltered.size() : 0);
               //service.setTests(service.getTestsCount());
               //service.setStatus(service.getTests(); service.getTestsCount());
               service.setStatus(service.getStatusID());
               service.setServiceDataList(null);
               service.setSubcategory(null);
            }
         }
      }

      return new SerializableResponseEntity<>(JsonRestUtils.toJsonResponse(categories));
   }

}
