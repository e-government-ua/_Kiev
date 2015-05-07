package org.activiti.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.dao.services.PlacesDao;
import org.wf.dp.dniprorada.dao.services.ServiceDao;
import org.wf.dp.dniprorada.dao.services.ServicesDao;
import org.wf.dp.dniprorada.model.Category;
import org.wf.dp.dniprorada.model.Region;
import org.wf.dp.dniprorada.model.Service;
import org.wf.dp.dniprorada.model.ServiceData;
import org.wf.dp.dniprorada.model.Subcategory;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/services")
public class ActivitiRestServicesController {

	@Autowired
	private ServicesDao serviceDao;

	@Autowired
	private PlacesDao placesDao;

	@Autowired
	private ServiceDao servicesDao;

	@RequestMapping(value = "/getService", method = RequestMethod.POST)
	public @ResponseBody
	Service getService(@RequestParam(value = "nID") String nID) {
		List<Service> res = serviceDao.getAll();
		// nullify some fields which are not required in response
		for (Service s : res) {
			if (s.getId().equals(Integer.valueOf(nID))) {
				s.setSubcategory(null);
				for (ServiceData serviceData : s.getServiceDataList()) {
					serviceData.setService(null);
				}
				return s;
			}
		}
		return null;
	}

	@RequestMapping(value = "/getPlaces", method = RequestMethod.GET)
	public @ResponseBody
	List<Region> getPlaces() {
		List<Region> list = placesDao.getAll();
		return list;
	}

	@RequestMapping(value = "/getServices", method = RequestMethod.GET)
	public @ResponseBody
	List<Category> getServices() {
		List<Category> list = servicesDao.getAll();
		for(Category cat : list){
			for(Subcategory sub : cat.getSubcategoryList()){
				for(Service service : sub.getServiceList()){
					service.setFaq(null);
					service.setInfo(null);
					service.setLaw(null);
					service.setServiceDataList(null);
					service.setSubcategory(null);
				}
			}
		}
		return list;
	}

	@RequestMapping(value = "/setServices", method = RequestMethod.POST)
	public ResponseEntity setServices(
			@RequestParam(value = "json_data") String jsonData)
			throws IOException {

		Service[] services = new ObjectMapper().readValue(jsonData,
				Service[].class);

		System.out.println(services);

		return new ResponseEntity(HttpStatus.OK);
	}

}