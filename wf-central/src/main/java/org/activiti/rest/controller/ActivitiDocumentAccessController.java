package org.activiti.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wf.dp.dniprorada.constant.HistoryEventType;
import org.wf.dp.dniprorada.dao.DocumentAccessDao;
import org.wf.dp.dniprorada.dao.DocumentDao;
import org.wf.dp.dniprorada.dao.HistoryEventDao;
import org.wf.dp.dniprorada.model.AccessURL;
import org.wf.dp.dniprorada.model.Document;
import org.wf.dp.dniprorada.model.DocumentAccess;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ActivitiDocumentAccessController {

    private final Logger log = LoggerFactory.getLogger(ActivitiDocumentAccessController.class);

	@Autowired
	private DocumentAccessDao documentAccessDao;
    @Autowired
    private HistoryEventDao historyEventDao;
    @Autowired
    private DocumentDao documentDao;

    @RequestMapping(value = "/setDocumentLink", method = RequestMethod.GET, headers = {"Accept=application/json"})
    public
    @ResponseBody
    AccessURL setDocumentAccessLink(
			@RequestParam(value = "nID_Document") Long nID_Document,
			@RequestParam(value = "sFIO", required = false) String sFIO,
			@RequestParam(value = "sTarget", required = false) String sTarget,
			@RequestParam(value = "sTelephone", required = false) String sTelephone,
			@RequestParam(value = "nMS") Long nMS,
			@RequestParam(value = "sMail", required = false) String sMail,
			HttpServletResponse response) {
		AccessURL oAccessURL = new AccessURL();
		try {
			oAccessURL.setValue(documentAccessDao.setDocumentLink(nID_Document,
					sFIO, sTarget, sTelephone, nMS, sMail));
			oAccessURL.setName("sURL");
		} catch (Exception e) {
			response.setStatus(400);
			response.setHeader("Reason", e.getMessage());
		}
        createHistoryEvent(4L, nID_Document, sFIO, sTelephone);
        return oAccessURL;
    }

	@RequestMapping(value = "/getDocumentLink", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody
	DocumentAccess getDocumentAccessLink(
			@RequestParam(value = "nID_Access") Long nID_Access,
			@RequestParam(value = "sSecret") String sSecret,
			HttpServletResponse response) {
		DocumentAccess da = null;
		try {
			da = documentAccessDao.getDocumentLink(nID_Access, sSecret);
		} catch (Exception e) {
			response.setStatus(403);
			response.setHeader("Reason", "Access not found\n" + e.getMessage());
		}
		if (da == null) {
			response.setStatus(403);
			response.setHeader("Reason", "Access not found");
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			Date d = null;
            boolean isSuccessAccess = true;
            try {
                d = sdf.parse(da.getDateCreate());
			} catch (ParseException e) {
                isSuccessAccess = false;
                response.setStatus(400);
                response.setHeader("Reason", e.getMessage());
			}
			if (da.getMS() + d.getTime() < new Date().getTime()) {
                isSuccessAccess = false;
                response.setStatus(403);
                response.setHeader("Reason", "Access expired");
			}
			if (!sSecret.equals(da.getSecret())) {
                isSuccessAccess = false;
                response.setStatus(403);
                response.setHeader("Reason", "Access to another document");
			}
            if (isSuccessAccess) {
                createHistoryEvent(5L, da.getID_Document(), da.getFIO(), da.getTelephone());
            }
        }

		return da;
	}

	@RequestMapping(value = "/getDocumentAccess", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody
	AccessURL getDocumentAccess(
			@RequestParam(value = "nID_Access") Long nID_Access,
			@RequestParam(value = "sSecret") String sSecret,
			HttpServletResponse response) {
		String str = "";
		AccessURL oAccessURL = new AccessURL();
		try {
			oAccessURL.setName("sURL");
			str = documentAccessDao.getDocumentAccess(nID_Access,sSecret);
			oAccessURL.setValue(str);
		} catch (Exception e) {
			response.setStatus(403);
			response.setHeader("Reason", "Access not found");
			oAccessURL.setValue(e.getMessage());
		}
		return oAccessURL;
	}

	@RequestMapping(value = "/setDocumentAccess", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody
	AccessURL setDocumentAccess(
			@RequestParam(value = "nID_Access") Long nID_Access,
			@RequestParam(value = "sSecret") String sSecret,
			@RequestParam(value = "sAnswer") String sAnswer,
			HttpServletResponse response) {
		AccessURL oAccessURL = new AccessURL();
		try {
			oAccessURL.setValue(documentAccessDao.setDocumentAccess(nID_Access,
					sSecret, sAnswer));
			oAccessURL.setName("sURL");
			if(oAccessURL.getValue().isEmpty() || oAccessURL.getValue() == null){
				response.setStatus(403);
				response.setHeader("Reason", "Access not found");
			}
		} catch (Exception e) {
			response.setStatus(400);
			response.setHeader("Reason", e.getMessage());
		}
		return oAccessURL;
	}

    private void createHistoryEvent(Long nID_HistoryEventType, Long nID_Document,
                                    String sFIO, String sPhone) {
        String sDocumentName = "";
        String sDocumentType = "";
        try {
            Document oDocument = documentDao.getDocument(nID_Document);
            sDocumentName = oDocument.getName();
            sDocumentType = oDocument.getDocumentType().getName();
        } catch (Throwable e) {
            log.warn("can't get document info!", e);
        }
        try {
            HistoryEventType eventType = HistoryEventType.getById(nID_HistoryEventType);
            String eventMessage = eventType.getsTemplate();//"Ви надаєте доступ до документу %Назва документу% іншій людині: %Ім’я того, кому надають доступ% (телефон: %телефон%)")
            //%Ім’я того, кому надають доступ% скористався доступом, який Ви надали, та переглянув документ %Тип документу% %Назва документу%
            eventMessage = eventMessage.replaceAll("%Ім’я того, кому надають доступ%", sFIO)
                    .replaceAll("%телефон%", sPhone)
                    .replaceAll("%Тип документу%", sDocumentType)
                    .replaceAll("%Назва документу%", sDocumentName);

            historyEventDao.setHistoryEvent(nID_Document, nID_HistoryEventType,
                    eventType.getsName(), eventMessage);
        } catch (IOException e) {
            log.error("error during creating HistoryEvent", e);
        } catch (Throwable e) {
            log.warn(e.getMessage());//???
        }
    }
}