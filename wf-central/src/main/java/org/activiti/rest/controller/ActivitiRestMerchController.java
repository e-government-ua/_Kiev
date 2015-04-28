package org.activiti.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.logic.Logic;
import org.wf.dp.dniprorada.model.Merchant;

@Controller
@RequestMapping(value = "/merch")
public class ActivitiRestMerchController {
	@Autowired
	@Qualifier(value = "logic")
	private Logic logic;
	
	@RequestMapping(value = "/getMerchList", method = RequestMethod.GET)
	public @ResponseBody List<Merchant> getMerchants(){
		return logic.getMerchants();
	}
	
	@RequestMapping(value = "/delMerch", method = RequestMethod.POST)
	public void deleteMerchant(@RequestParam(value = "idOwner") String idOwner, @RequestParam(value = "id") String id){
		logic.removeMerchant(idOwner, id);
	}
	
	@RequestMapping(value = "/updMerch", method = RequestMethod.POST)
	public void updateMerchant(@RequestParam(value = "idOwner") String idOwner, @RequestParam(value = "ownerName") String ownerName, @RequestParam(value = "id") String id){
		logic.updateMerchant(idOwner, ownerName, id);
	}	
	
	@RequestMapping(value = "/insMerch", method = RequestMethod.POST)
	public void insertMerchant(@RequestParam(value = "idOwner") String idOwner, @RequestParam(value = "ownerName") String ownerName, @RequestParam(value = "id") String id){
		logic.addMerchant(idOwner, ownerName, id);
	}
}