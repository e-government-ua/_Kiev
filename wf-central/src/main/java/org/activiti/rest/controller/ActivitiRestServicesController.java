package org.activiti.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.wf.dp.dniprorada.dao.services.CategoryDao;
import org.wf.dp.dniprorada.dao.services.PlacesDao;
import org.wf.dp.dniprorada.dao.services.ServiceDao;
import org.wf.dp.dniprorada.model.Category;
import org.wf.dp.dniprorada.model.Region;
import org.wf.dp.dniprorada.model.Service;
import org.wf.dp.dniprorada.model.ServiceData;
import org.wf.dp.dniprorada.model.Subcategory;
import org.wf.dp.dniprorada.util.JsonRestUtils;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/services")
public class ActivitiRestServicesController {

	@Autowired
	private PlacesDao placesDao;

	@Autowired
	private ServiceDao serviceDao;

	@Autowired
	private CategoryDao categoryDao;

	@RequestMapping(value = "/getService", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity getService(@RequestParam(value = "nID") Integer nID) {
		Service service = serviceDao.getById(nID);
		return toJsonResponse(service);
	}

	@RequestMapping(value = "/setService", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity setService(@RequestBody String jsonData) throws IOException {

		Service service = JsonRestUtils.readObject(jsonData, Service.class);

		serviceDao.saveOrUpdate(service);
		return toJsonResponse(service);
	}

	private ResponseEntity toJsonResponse(Service service) {
		service.setSubcategory(null);
		for (ServiceData serviceData : service.getServiceDataList()) {
			serviceData.setService(null);
		}
		return JsonRestUtils.toJsonResponse(service);
	}

	@RequestMapping(value = "/getPlaces", method = RequestMethod.GET)
	public @ResponseBody List<Region> getPlaces() {
		List<Region> list = placesDao.getAll();
		return list;
	}

	@RequestMapping(value = "/getServices", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity getServices() {
		List<Category> list = categoryDao.getAll();
		for(Category cat : list){
			for(Subcategory sub : cat.getSubcategoryList()){
				sub.setCategory(null);

				for(Service service : sub.getServiceList()){
					service.setFaq(null);
					service.setInfo(null);
					service.setLaw(null);
					service.setServiceDataList(null);
					service.setSubcategory(null);
				}
			}
		}

		return JsonRestUtils.toJsonResponse(list);
	}

	@RequestMapping(value = "/setServices", method = RequestMethod.POST)
	public Category[] setServices(@RequestParam(value = "json_data") String jsonData) throws IOException {

		Category[] categories = new ObjectMapper().readValue(jsonData,
				  Category[].class);

		categoryDao.saveOrUpdateAll(categories);

		return categories;
	}

}