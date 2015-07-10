package org.activiti.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.base.dao.BaseEntityDao;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.dao.MerchantDao;
import org.wf.dp.dniprorada.model.Merchant;
import org.wf.dp.dniprorada.model.SubjectOrgan;
import org.wf.dp.dniprorada.viewobject.MerchantVO;

@Controller
@RequestMapping(value = "/merchant")
public class ActivitiRestMerchantController {

	//final static Logger LOG = Logger.getLogger(ActivitiRestMerchController.class);

	@Autowired	
	@Qualifier(value = "merchantDao")		
	private MerchantDao merchantDao;

	@Autowired
	private BaseEntityDao baseEntityDao;
	
	@RequestMapping(value = "/getMerchants", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity getMerchants(){
		return JsonRestUtils.toJsonResponse(toVO(merchantDao.getAll()));
	}

	@RequestMapping(value = "/getMerchant", method = RequestMethod.GET)
	public @ResponseBody
	ResponseEntity getMerchant(@RequestParam(value = "sID") String sID){
		Merchant merchant = merchantDao.getMerchant(sID);
		if (merchant == null) {
			return new ResponseEntity("Merchant with sID=" + sID + " is not found!", HttpStatus.NOT_FOUND);
		}

		return JsonRestUtils.toJsonResponse(new MerchantVO(merchant));
	}

	@RequestMapping(value = "/removeMerchant", method = RequestMethod.DELETE)
	public ResponseEntity deleteMerchant(@RequestParam(value = "sID") String id){
      return new ResponseEntity(merchantDao.deleteMerchant(id) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value = "/setMerchant", method = RequestMethod.POST)
	public ResponseEntity setMerchant(
			  @RequestParam(value = "nID", required = false) Long nID,
			  @RequestParam(value = "sID", required = false) String sID,
			  @RequestParam(value = "sName", required = false) String sName,
			  @RequestParam(value = "nID_SubjectOrgan", required = false) Long nID_SubjectOrgan,
			  @RequestParam(value = "sURL_CallbackStatusNew", required = false) String sURL_CallbackStatusNew,
			  @RequestParam(value = "sURL_CallbackPaySuccess", required = false) String sURL_CallbackPaySuccess) {

		Merchant merchant = nID != null ? merchantDao.getById(nID) : new Merchant();

		if (merchant == null) {
			merchant = new Merchant();
		}

		if (sID != null) {
			merchant.setsID(sID);
		}

		if (sName != null) {
			merchant.setName(sName);
		}

		if (nID_SubjectOrgan != null) {
			SubjectOrgan subjectOrgan = baseEntityDao.getById(SubjectOrgan.class, nID_SubjectOrgan);
			merchant.setOwner(subjectOrgan);
		}

		if (sURL_CallbackStatusNew != null) {
			merchant.setsURL_CallbackStatusNew(sURL_CallbackStatusNew);
		}

		if (sURL_CallbackPaySuccess != null) {
			merchant.setsURL_CallbackPaySuccess(null);
		}

		merchantDao.saveOrUpdate(merchant);
//		try {
//			merchantDao.saveOrUpdate(merchant);
//		}
//		catch (Exception exc) {
//			LOG.error(exc.getMessage(), exc);
//			return new ResponseEntity(exc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
		return JsonRestUtils.toJsonResponse(new MerchantVO(merchant));
	}

	private List<MerchantVO> toVO(List<Merchant> merchants) {
		List<MerchantVO> res = new ArrayList<>();
		for (Merchant merchant : merchants) {
			res.add(new MerchantVO(merchant));
		}

		return res;
	}
}