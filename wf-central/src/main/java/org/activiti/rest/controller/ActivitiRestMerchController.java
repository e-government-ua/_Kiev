package org.activiti.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.dao.MerchantDao;
import org.wf.dp.dniprorada.model.Merchant;

@Controller
@RequestMapping(value = "/merchant")
public class ActivitiRestMerchController {
	@Autowired	
	@Qualifier(value = "merchantDao")		
	private MerchantDao merchantDao;
	
	@RequestMapping(value = "/getMerchants", method = RequestMethod.GET)
	public @ResponseBody List<Merchant> getMerchants(){
		return merchantDao.getMerchants();
	}
	
	@RequestMapping(value = "/removeMerchant", method = RequestMethod.POST)
	public void deleteMerchant(@RequestParam(value = "idOwner") String idOwner, @RequestParam(value = "id") String id){
		merchantDao.removeMerchant(idOwner, id);
	}
	
	@RequestMapping(value = "/setMerchant", method = RequestMethod.POST)
	public void updateMerchant(@RequestParam(value = "idOwner") String idOwner, @RequestParam(value = "ownerName") String ownerName, @RequestParam(value = "id") String id){
		Merchant merchant = new Merchant();
		merchant.setIdOwner(idOwner);
		merchant.setId(id);
		merchant.setOwnerName(ownerName);
		merchantDao.updateMerchant(merchant);
	}	
	
	@RequestMapping(value = "/addMerchant", method = RequestMethod.POST)
	public void insertMerchant(@RequestParam(value = "idOwner") String idOwner, @RequestParam(value = "ownerName") String ownerName, @RequestParam(value = "id") String id){
		Merchant merchant = new Merchant();
		merchant.setIdOwner(idOwner);
		merchant.setId(id);
		merchant.setOwnerName(ownerName);
		merchantDao.addMerchant(merchant);
	}
}