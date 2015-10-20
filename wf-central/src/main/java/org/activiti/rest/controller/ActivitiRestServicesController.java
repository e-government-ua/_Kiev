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
import org.wf.dp.dniprorada.dao.PlaceDao;
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
    public static final String SERVICE_NAME_TEST_PREFIX = "_";
    public static final List<String> SUPPORTED_PLACE_IDS = new ArrayList<>();
    private static final String GET_SERVICES_TREE = "getServicesTree";

    static {
        SUPPORTED_PLACE_IDS.add(String.valueOf(KOATUU.KYIVSKA_OBLAST.getId()));
        SUPPORTED_PLACE_IDS.add(String.valueOf(KOATUU.KYIV.getId()));
    }

    @Autowired
    GeneralConfig generalConfig;
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
    private PlaceDao placeDao;

    @RequestMapping(value = "/getService", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity getService(@RequestParam(value = "nID") Long nID) {
        Service oService = baseEntityDao.findById(Service.class, nID);
        return regionsToJsonResponse(oService);
    }

    @RequestMapping(value = "/setService", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity setService(@RequestBody String soData_JSON) throws IOException {

        Service oService = JsonRestUtils.readObject(soData_JSON, Service.class);

        Service oServiceUpdated = entityService.update(oService);

        return tryClearGetServicesCache(regionsToJsonResponse(oServiceUpdated));
    }

    private ResponseEntity tryClearGetServicesCache(ResponseEntity oResponseEntity) {
        if (methodCacheInterceptor != null && HttpStatus.OK.equals(oResponseEntity.getStatusCode())) {
            methodCacheInterceptor
                    .clearCacheForMethod(CachedInvocationBean.class, "invokeUsingCache", GET_SERVICES_TREE);
        }

        return oResponseEntity;
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
        return tryClearGetServicesCache(response);
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
        return tryClearGetServicesCache(response);
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
        return tryClearGetServicesCache(response);
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
        return tryClearGetServicesCache(response);
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
        for (Category category : categories) {
            baseEntityDao.delete(category);
        }
        return tryClearGetServicesCache(JsonRestUtils.toJsonResponse(HttpStatus.OK,
                new ResultMessage("success", "ServicesTree removed")));
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

        List<Region> aRegion = Arrays.asList(JsonRestUtils.readObject(jsonData, Region[].class));
        List<Region> aRegionUpdated = entityService.update(aRegion);
        return regionsToJsonResponse(aRegionUpdated);
    }

    private ResponseEntity regionsToJsonResponse(List<Region> aRegion) {
        for (Region oRegion : aRegion) {
            for (City oCity : oRegion.getCities()) {
                oCity.setRegion(null);
            }
        }

        return JsonRestUtils.toJsonResponse(aRegion);
    }

    @RequestMapping(value = "/getServicesTree", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<String> getServicesTree(
            @RequestParam(value = "sFind", required = false) final String sFind,
            @RequestParam(value = "asID_Place_UA", required = false) final List<String> asID_Place_UA,
            @RequestParam(value = "bShowEmptyFolders", required = false, defaultValue = "false") final boolean bShowEmptyFolders) {
        
        final boolean bTest = generalConfig.bTest();
        
        SerializableResponseEntity<String> entity = cachedInvocationBean
                .invokeUsingCache(new CachedInvocationBean.Callback<SerializableResponseEntity<String>>(
                        GET_SERVICES_TREE, sFind, asID_Place_UA, bTest) {
                    @Override
                    public SerializableResponseEntity<String> execute() {
                        List<Category> aCategory = new ArrayList<>(baseEntityDao.findAll(Category.class));

                        if (!bTest) {
                            filterOutServicesByServiceNamePrefix(aCategory, SERVICE_NAME_TEST_PREFIX);
                        }

                        if (sFind != null) {
                            filterServicesByServiceName(aCategory, sFind);
                        }

                        if (asID_Place_UA != null) {
                            //TODO: Зачем это было добавлено?                    asID_Place_UA.retainAll(SUPPORTED_PLACE_IDS);
                            if (!asID_Place_UA.isEmpty()) {
                                filterServicesByPlaceIds(aCategory, asID_Place_UA);
                            }
                        }

                        if (!bShowEmptyFolders) {
                            hideEmptyFolders(aCategory);
                        }

                        return categoriesToJsonResponse(aCategory);
                    }
                });

        return entity.toResponseEntity();
    }

    private void filterOutServicesByServiceNamePrefix(List<Category> aCategory, String sPrefix) {
        for (Category oCategory : aCategory) {
            for (Subcategory oSubcategory : oCategory.getSubcategories()) {
                for (Iterator<Service> oServiceIterator = oSubcategory.getServices().iterator(); oServiceIterator
                        .hasNext(); ) {
                    Service oService = oServiceIterator.next();
                    if (oService.getName().startsWith(sPrefix)) {
                        oServiceIterator.remove();
                    }
                }
            }
        }
    }

    private void filterServicesByServiceName(List<Category> aCategory, String sFind) {
        for (Category oCategory : aCategory) {
            for (Subcategory oSubcategory : oCategory.getSubcategories()) {
                for (Iterator<Service> oServiceIterator = oSubcategory.getServices().iterator(); oServiceIterator
                        .hasNext(); ) {
                    Service oService = oServiceIterator.next();
                    if (!isTextMatched(oService.getName(), sFind)) {
                        oServiceIterator.remove();
                    }
                }
            }
        }
    }

    private void filterServicesByPlaceIds(List<Category> aCategory, List<String> asID_Place_UA) {
        for (Category oCategory : aCategory) {
            for (Subcategory oSubcategory : oCategory.getSubcategories()) {
                for (Iterator<Service> oServiceIterator = oSubcategory.getServices().iterator(); oServiceIterator
                        .hasNext(); ) {
                    Service oService = oServiceIterator.next();
                    boolean bFound = false;
                    //List<ServiceData> serviceDatas = service.getServiceDataFiltered(generalConfig.bTest());
                    List<ServiceData> aServiceData = oService.getServiceDataFiltered(true);
                    if (aServiceData != null) {
                        for (Iterator<ServiceData> oServiceDataIterator = aServiceData.iterator(); oServiceDataIterator
                                .hasNext(); ) {
                            ServiceData serviceData = oServiceDataIterator.next();

                            City oCity = serviceData.getCity();
                            if (oCity != null && asID_Place_UA.contains(oCity.getsID_UA())) {
                                bFound = true;
                                break;
                            }
                            Region oRegion = serviceData.getRegion();
                            if (oRegion != null && asID_Place_UA.contains(oRegion.getsID_UA())) {
                                bFound = true;
                                break;
                            }
                            if(oCity != null || oRegion != null){
                                oServiceDataIterator.remove();
                            }
                        }
                    }
                    if (!bFound) {
                        oServiceIterator.remove();
                    }
                }
            }
        }
    }

    /**
     * Filter out empty categories and subcategories
     *
     * @param aCategory
     */
    private void hideEmptyFolders(List<Category> aCategory) {
        for (Iterator<Category> oCategoryIterator = aCategory.iterator(); oCategoryIterator.hasNext(); ) {
            Category oCategory = oCategoryIterator.next();

            for (Iterator<Subcategory> oSubcategoryIterator = oCategory.getSubcategories().iterator(); oSubcategoryIterator
                    .hasNext(); ) {
                Subcategory oSubcategory = oSubcategoryIterator.next();
                if (oSubcategory.getServices().isEmpty()) {
                    oSubcategoryIterator.remove();
                }
            }

            if (oCategory.getSubcategories().isEmpty()) {
                oCategoryIterator.remove();
            }
        }
    }

    @RequestMapping(value = "/setServicesTree", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity setServicesTree(@RequestBody String jsonData) {

        List<Category> aCategory = Arrays.asList(JsonRestUtils.readObject(jsonData, Category[].class));
        List<Category> aCategoryUpdated = entityService.update(aCategory);

        return tryClearGetServicesCache(categoriesToJsonResponse(aCategoryUpdated).toResponseEntity());
    }

    @RequestMapping(value = "/getServicesAndPlacesTables", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity getServicesAndPlacesTables() {
        List<TableData> aTableData = tableDataService.exportData(TableDataService.TablesSet.ServicesAndPlaces);
        return JsonRestUtils.toJsonResponse(aTableData);
    }

    @RequestMapping(value = "/setServicesAndPlacesTables", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity setServicesAndPlacesTables(@RequestBody String jsonData) {
        List<TableData> aTableData = Arrays.asList(JsonRestUtils.readObject(jsonData, TableData[].class));
        tableDataService.importData(TableDataService.TablesSet.ServicesAndPlaces, aTableData);
        return JsonRestUtils.toJsonResponse(HttpStatus.OK,
                new ResultMessage("success", "Data successfully imported."));
    }

    @RequestMapping(value = "/downloadServicesAndPlacesTables", method = RequestMethod.GET)
    public
    @ResponseBody
    void downloadServicesAndPlacesTables(HttpServletResponse response) throws IOException {
        List<TableData> aTableData = tableDataService.exportData(TableDataService.TablesSet.ServicesAndPlaces);

        String dateTimeString = DateTimeFormat.forPattern("yyyy-MM-dd_HH-mm-ss").print(new DateTime());

        String fileName = "igov.ua.catalog_" + dateTimeString + ".json";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        JsonRestUtils.writeJsonToOutputStream(aTableData, response.getOutputStream());
    }

    @RequestMapping(value = "/uploadServicesAndPlacesTables", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity uploadServicesAndPlacesTables(@RequestParam("file") MultipartFile file)
            throws IOException {
        List<TableData> tableDataList = Arrays
                .asList(JsonRestUtils.readObject(file.getInputStream(), TableData[].class));

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
