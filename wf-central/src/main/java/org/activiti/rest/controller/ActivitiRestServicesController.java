package org.activiti.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.brunneng.jom.MergingContext;
import net.sf.brunneng.jom.diff.ChangeType;
import net.sf.brunneng.jom.diff.apply.IBeanFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.wf.dp.dniprorada.dao.BaseEntityDao;
import org.wf.dp.dniprorada.model.*;
import org.wf.dp.dniprorada.service.EntityService;
import org.wf.dp.dniprorada.util.JsonRestUtils;

import java.io.IOException;
import java.io.Serializable;
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
		return toJsonResponse(service);
	}

	@RequestMapping(value = "/setService", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity setService(@RequestBody String jsonData) throws IOException {

		Service service = JsonRestUtils.readObject(jsonData, Service.class);

		Service updatedService = entityService.update(service);
		return toJsonResponse(updatedService);
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
		List<Region> list = baseEntityDao.getAll(Region.class);
		return list;
	}

	@RequestMapping(value = "/getServices", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity getServices() {
		List<Category> list = baseEntityDao.getAll(Category.class);
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
	public Category[] setServices(@RequestBody String jsonData) throws IOException {

		Category[] categories = new ObjectMapper().readValue(jsonData, Category[].class);

		baseEntityDao.saveOrUpdateAll(categories);

		return categories;
	}

}