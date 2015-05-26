package org.activiti.rest.controller;


import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.dao.DocumentAccessDao;
import org.wf.dp.dniprorada.model.DocumentAccess;
import org.wf.dp.dniprorada.model.SURL;

@Controller
public class ActivitiDocumentAccessController {

	@Autowired
	private DocumentAccessDao documentAccessDao;
	
	
	@RequestMapping(value = "/setDocumentLink", method = RequestMethod.GET, headers = {"Accept=application/json"})
	public @ResponseBody SURL setDocumentAccess(
			@RequestParam(value = "nID_Document") Integer nID_Document,
			@RequestParam(value = "sFIO") String sFIO,
			@RequestParam(value = "sTarget") String sTarget,
			@RequestParam(value = "sTelephone") String sTelephone,
			@RequestParam(value = "nDays") String nDays,
			@RequestParam(value = "sMail") String sMail, HttpServletResponse response) {
		SURL url = new SURL();
		try{
		url.setValue(documentAccessDao.setDocumentLink(nID_Document, sFIO,
				sTarget, sTelephone, Long.parseLong(nDays), sMail));
		url.setName("sURL");
		} catch (Exception e){
			response.setStatus(400);
			response.setHeader("Reason", e.getMessage());
		}
		return url;
	}

	@RequestMapping(value = "/getDocumentLink", method = RequestMethod.GET, headers = {"Accept=application/json"})
	public @ResponseBody DocumentAccess getDocumentAccess(
			@RequestParam(value = "nID_Access") String nID_Access,
			@RequestParam(value = "sSecret") String sSecret,
			HttpServletResponse response) {
		DocumentAccess da;
		try{
			da = documentAccessDao.getDocumentLink(nID_Access, sSecret);
		} catch (Exception e){
			response.setStatus(403);
			response.setHeader("Reason", "Access not found");
			return null;
		}				
		
		if(da.getsDays()+da.getsDateCreate().getTime() < new Date().getTime()){
			response.setStatus(403);
			response.setHeader("Reason", "Access expired");
		}		
		if(!sSecret.equals(da.getsSecret())){
			response.setStatus(403);
			response.setHeader("Reason", "Access to another document");
		}
		return da;
	}

}