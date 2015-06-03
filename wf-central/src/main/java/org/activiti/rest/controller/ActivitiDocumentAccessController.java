package org.activiti.rest.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.wf.dp.dniprorada.model.AccessURL;

@Controller
public class ActivitiDocumentAccessController {

	@Autowired
	private DocumentAccessDao documentAccessDao;
	
	
	@RequestMapping(value = "/setDocumentLink", method = RequestMethod.POST, headers = {"Accept=application/json"})
	public @ResponseBody AccessURL setDocumentAccessLink(
			@RequestParam(value = "nID_Document") Long nID_Document,
			@RequestParam(value = "sFIO") String sFIO,
			@RequestParam(value = "sTarget") String sTarget,
			@RequestParam(value = "sTelephone") String sTelephone,
			@RequestParam(value = "nMS") Long nMS,
			@RequestParam(value = "sMail") String sMail, HttpServletResponse response) {
		AccessURL oAccessURL = new AccessURL();
		try{
		oAccessURL.setValue(documentAccessDao.setDocumentLink(nID_Document, sFIO,
				sTarget, sTelephone, nMS, sMail));
		oAccessURL.setName("sURL");
		} catch (Exception e){
			response.setStatus(400);
			response.setHeader("Reason", e.getMessage());
		}
		return oAccessURL;
	}

	@RequestMapping(value = "/getDocumentLink", method = RequestMethod.POST, headers = {"Accept=application/json"})
	public @ResponseBody DocumentAccess getDocumentAccessLink(
			@RequestParam(value = "nID_Access") Long nID_Access,
			@RequestParam(value = "sSecret") String sSecret,
			HttpServletResponse response) {
		DocumentAccess da;
		try{
			da = documentAccessDao.getDocumentLink(nID_Access, sSecret);
		} catch (Exception e){
			response.setStatus(403);
			response.setHeader("Reason", "Access not found\n"+e.getMessage());
			return null;
		}				
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		Date d = null;
		try {
			d = sdf.parse(da.getDateCreate());
		} catch (ParseException e) {
			response.setStatus(400);
			response.setHeader("Reason", e.getMessage());
		}
		if(da.getMS()+ d.getTime() < new Date().getTime()){
			response.setStatus(403);
			response.setHeader("Reason", "Access expired");
		}		
		if(!sSecret.equals(da.getSecret())){
			response.setStatus(403);
			response.setHeader("Reason", "Access to another document");
		}
		return da;
	}
        
        
	@RequestMapping(value = "/getDocumentAccess", method = RequestMethod.POST, headers = {"Accept=application/json"})
	public @ResponseBody AccessURL setDocumentAccess(
			@RequestParam(value = "nID_Access") Long nID_Access,
			@RequestParam(value = "sSecret") String sSecret,
                    HttpServletResponse response) {
		AccessURL oAccessURL = new AccessURL();
		try{
                    oAccessURL.setValue(documentAccessDao.getDocumentAccess(nID_Access, sSecret));
                    oAccessURL.setName("sURL");
		} catch (Exception e){
			response.setStatus(400);
			response.setHeader("Reason", e.getMessage());
		}
		return oAccessURL;
	}
        
	@RequestMapping(value = "/setDocumentAccess", method = RequestMethod.POST, headers = {"Accept=application/json"})
	public @ResponseBody AccessURL setDocumentAccess(
			@RequestParam(value = "nID_Access") Long nID_Access,
			@RequestParam(value = "sSecret") String sSecret,
			@RequestParam(value = "sAnswer") String sAnswer,
                    HttpServletResponse response) {
		AccessURL oAccessURL = new AccessURL();
		try{
		oAccessURL.setValue(documentAccessDao.setDocumentAccess(nID_Access, sSecret, sAnswer));
		oAccessURL.setName("sURL");
		} catch (Exception e){
			response.setStatus(400);
			response.setHeader("Reason", e.getMessage());
		}
		return oAccessURL;
	}    
}