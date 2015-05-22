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
	public @ResponseBody ResponseEntity getService(@RequestParam(value = "nID") Integer nID) {
		Service service = baseEntityDao.getById(Service.class, nID);
		return regionsToJsonResponse(service);
	}

	@RequestMapping(value = "/setService", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity setService(@RequestBody String jsonData) throws IOException {

		Service service = JsonRestUtils.readObject(jsonData, Service.class);

		Service updatedService = entityService.update(service);
		return regionsToJsonResponse(updatedService);
	}

	private ResponseEntity regionsToJsonResponse(Service service) {
		service.setSubcategory(null);
		for (ServiceData serviceData : service.getServiceDataList()) {
			serviceData.setService(null);
			if (serviceData.getCity() != null) {
				serviceData.getCity().setRegion(null);
			}
			if (serviceData.getRegion() != null) {
				serviceData.getRegion().setCities(null);
			}

		}
		return JsonRestUtils.toJsonResponse(service);
	}

	@RequestMapping(value = "/getPlaces", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity getPlaces() {
		List<Region> regions = baseEntityDao.getAll(Region.class);
		return regionsToJsonResponse(regions);
	}

	@RequestMapping(value = "/setPlaces", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity setPlaces(@RequestBody String jsonData) {

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
	public @ResponseBody ResponseEntity getServicesTree(@RequestParam(value = "sFind", required = false) String partOfName) {
		List<Category> categories = new ArrayList<>(baseEntityDao.getAll(Category.class));

		if (partOfName != null) {
			filterCategories(categories, partOfName);
		}

		return categoriesToJsonResponse(categories);
	}

	private void filterCategories(List<Category> categories, @RequestParam(value = "sFind", required = false) String partOfName) {
		for (Iterator<Category> i1 = categories.iterator(); i1.hasNext(); ) {
         Category c = i1.next();

         for (Iterator<Subcategory> i2 = c.getSubcategories().iterator(); i2.hasNext(); ) {
            Subcategory sc = i2.next();

            for (Iterator<Service> i3 = sc.getServices().iterator(); i3.hasNext(); ) {
               Service s = i3.next();
               if (!isNameMatched(s.getName(), partOfName)) {
                  i3.remove();
               }
            }

            if (sc.getServices().isEmpty()) {
               i2.remove();
            }
         }

         if (c.getSubcategories().isEmpty()) {
            i1.remove();
         }
      }
	}

	@RequestMapping(value = "/setServicesTree", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity setServicesTree(@RequestBody String jsonData) {

		List<Category> categories = Arrays.asList(JsonRestUtils.readObject(jsonData, Category[].class));
		List<Category> updatedCategories = entityService.update(categories);

		return categoriesToJsonResponse(updatedCategories);
	}

	private boolean isNameMatched(String name, String partOfName) {
		return name.toLowerCase().contains(partOfName.toLowerCase());
	}

	private ResponseEntity categoriesToJsonResponse(List<Category> categories) {
		for(Category c : categories){
			for(Subcategory sc : c.getSubcategories()){
				sc.setCategory(null);

				for(Service service : sc.getServices()){
					service.setFaq(null);
					service.setInfo(null);
					service.setLaw(null);
                                        service.setSub(service.getServiceDataList().size());
					service.setServiceDataList(null);
					service.setSubcategory(null);
				}
			}
		}

		return JsonRestUtils.toJsonResponse(categories);
	}

}