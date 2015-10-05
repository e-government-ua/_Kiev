package org.activiti.rest.controller;

import com.google.common.collect.Lists;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.redis.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;
import org.wf.dp.dniprorada.constant.Currency;
import org.wf.dp.dniprorada.constant.HistoryEventMessage;
import org.wf.dp.dniprorada.constant.HistoryEventType;
import org.wf.dp.dniprorada.constant.Language;
import org.wf.dp.dniprorada.dao.*;
import org.wf.dp.dniprorada.liqPay.LiqBuy;
import org.wf.dp.dniprorada.model.*;
import org.wf.dp.dniprorada.model.document.HandlerFactory;
import org.wf.dp.dniprorada.util.BankIDConfig;
import org.wf.dp.dniprorada.util.BankIDUtils;
import org.wf.dp.dniprorada.util.GeneralConfig;
import org.wf.dp.dniprorada.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

//import org.wf.dp.dniprorada.base.dao.AccessDataDao;

@Controller
@RequestMapping(value = "/services")
public class ActivitiRestDocumentController {

    private static final Logger log = LoggerFactory.getLogger(ActivitiRestDocumentController.class);
    
    @Autowired
    private DocumentDao documentDao;

    @Autowired
    private SubjectDao subjectDao;
    
    @Autowired
    private SubjectOrganDao subjectOrganDao;

    @Autowired
    private DocumentContentTypeDao documentContentTypeDao;
    
    @Autowired
    private DocumentTypeDao documentTypeDao;
    
    @Autowired
	private HistoryEventDao historyEventDao;
    
    //@Autowired
    //private AccessDataDao accessDataDao;
    
    @Autowired
    LiqBuy liqBuy;

    @Autowired
    private HandlerFactory handlerFactory;
    
    @Autowired
    GeneralConfig generalConfig;
    
    @Autowired
    BankIDConfig bankIDConfig;
    
    @RequestMapping(value = "/getDocument", method = RequestMethod.GET)
    public
    @ResponseBody
    Document getDocument(@RequestParam(value = "nID") Long id,
            @RequestParam(value = "nID_Subject") long nID_Subject) throws ActivitiRestException{
        Document document = documentDao.getDocument(id);
        if(nID_Subject != document.getSubject().getId()){
            throw new ActivitiRestException("401", "You don't have access! Your nID = " + nID_Subject + " Document's Subject's nID = " + document.getSubject().getId());
        } else{
            return  document;
        }
    }



    /**
     * @param accessCode    - строковой код доступа к документу
     * @param organID	    - номер-�?Д субьекта-органа оператора документа
     * @param docTypeID	    - номер-�?Д типа документа (опционально)
     * @param password	    - строка-пароль (опционально)
     * */
    @RequestMapping(value 	= "/getDocumentAccessByHandler",
                    method 	= RequestMethod.GET,
                    headers = { "Accept=application/json" })
    public @ResponseBody
    Document getDocumentAccessByHandler(
            @RequestParam(value = "sCode_DocumentAccess") 				String 	accessCode,
            @RequestParam(value = "nID_DocumentOperator_SubjectOrgan") 	Long 	organID,
            @RequestParam(value = "nID_DocumentType", required = false) Long	docTypeID,
            @RequestParam(value = "sPass", required = false)		    String 	password,
            @RequestParam(value = "nID_Subject", defaultValue = "1")    Long 	nID_Subject,
            HttpServletResponse resp
    ) {

        
        log.info("accessCode = {} ", accessCode);
        //log.info("organID="+organID);
        
        Document document = handlerFactory
                .buildHandlerFor(documentDao.getOperator(organID))
                .setDocumentType(docTypeID)
                .setAccessCode(accessCode)
                .setPassword(password)
                .setWithContent(false)
                .setIdSubject(nID_Subject)
                .getDocument();
        try {
            createHistoryEvent(HistoryEventType.GET_DOCUMENT_ACCESS_BY_HANDLER,
                    document.getSubject().getId(), subjectOrganDao.getSubjectOrgan(organID).getName(), null, document);
        } catch (Exception e){
            log.warn("can`t create history event!", e);
        }
        return document;
    }


    @RequestMapping(value 	= "/getDocumentOperators",
                    method 	= RequestMethod.GET,
                    headers = { "Accept=application/json" })
    public @ResponseBody List<DocumentOperator_SubjectOrgan> getDocumentOperators() {
        return documentDao.getAllOperators();
    }


    @RequestMapping(value = "/getDocumentContent", method = RequestMethod.GET)
    public
    @ResponseBody
    String getDocumentContent(@RequestParam(value = "nID") Long id, 
            @RequestParam(value = "nID_Subject") long nID_Subject) throws ActivitiRestException {
        Document document = documentDao.getDocument(id);
        if(nID_Subject != document.getSubject().getId()){
            throw new ActivitiRestException("401", "You don't have access!");
        } else{
            return Util.contentByteToString(documentDao.getDocumentContent(document.getContentKey())); // ????
        }
    }



    @RequestMapping(value = "/getDocumentFile", method = RequestMethod.GET)
    public
    @ResponseBody
    byte[] getDocumentFile( @RequestParam(value = "nID")                                                Long id,
                            @RequestParam(value = "nID_Subject")                                        Long nID_Subject,
//                            @RequestParam(value = "sCode_DocumentAccess", required = false)             String accessCode,
//                            @RequestParam(value = "nID_DocumentOperator_SubjectOrgan", required = false)Long organID,
//                            @RequestParam(value = "nID_DocumentType", required = false)                 Long docTypeID,
//                            @RequestParam(value = "sPass", required = false)                            String password,

                            HttpServletRequest request, HttpServletResponse httpResponse)
            throws ActivitiRestException{
        Document document = documentDao.getDocument(id);
        if(!nID_Subject.equals(document.getSubject().getId())){
//            if(accessCode!=null){
//                Document oDocument = handlerFactory
//                        .buildHandlerFor(documentDao.getOperator(organID))
//                        .setDocumentType(docTypeID)
//                        .setAccessCode(accessCode)
//                        .setIdSubject(nID_Subject)
//                        .setPassword(password)
//                        .setWithContent(true)
//                        .getDocument();
//                if(oDocument==null){
//                    throw new ActivitiRestException("401", "You don't have access by accessCode!");
//                }
//            }else{
                throw new ActivitiRestException("401", "You don't have access!");
//            }
        }
        byte[] content = documentDao.getDocumentContent(document
                .getContentKey());

        httpResponse.setHeader("Content-disposition", "attachment; filename="
                + document.getFile());

        httpResponse.setHeader("Content-Type", document.getContentType() + ";charset=UTF-8");
        httpResponse.setContentLength(content.length);
        return content;
    }

    @RequestMapping(value = "/getDocumentAbstract", method = RequestMethod.GET)
    public
    @ResponseBody
    byte[] getDocumentAbstract(@RequestParam(value = "nID_Subject", required = false, defaultValue = "1")   Long nID_Subject,
                               @RequestParam(value = "sID", required = false)                                  String sID,
                               @RequestParam(value = "nID_DocumentOperator_SubjectOrgan", required = false)    Long organID,
                               @RequestParam(value = "nID_DocumentType", required = false)                     Long docTypeID,
                               @RequestParam(value = "sPass", required = false)                                String password,

                               HttpServletRequest request, HttpServletResponse httpResponse)
            throws ActivitiRestException {

        Document document = null;
        byte[] content = {};

        try {
            document = handlerFactory
                    .buildHandlerFor(documentDao.getOperator(organID))
                    .setDocumentType(docTypeID)
                    .setAccessCode(sID)
                    .setPassword(password)
                    .setWithContent(true)
                    .setIdSubject(nID_Subject)
                    .getDocument();
            content = document.getFileBody().getBytes();
        } catch (IOException e) {
            throw new ActivitiRestException("500", "Can't read document content!");
        }

        httpResponse.setHeader("Content-Type", document.getContentType() + ";charset=UTF-8");
        httpResponse.setHeader("Content-Disposition", "attachment; filename=" + document.getFile());
        httpResponse.setContentLength(content.length);

        return content;
    }


    @RequestMapping(value = "/getDocuments", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Document> getDocuments(
            @RequestParam(value = "nID_Subject") long nID_Subject) {
        return documentDao.getDocuments(nID_Subject);
    }
    
    @RequestMapping(value = "/getPayButtonHTML_LiqPay", method = RequestMethod.GET)
    public
    @ResponseBody
    String getPayButtonHTML_LiqPay(
    		@RequestParam(value = "sID_Merchant", required = true) String sID_Merchant,
    		@RequestParam(value = "sSum", required = true) String sSum,
    		@RequestParam(value = "oID_Currency", required = true) Currency oID_Currency,
    		@RequestParam(value = "oLanguage", required = true) Language oLanguage,
    		@RequestParam(value = "sDescription", required = true) String sDescription,
    		@RequestParam(value = "sID_Order", required = true) String sID_Order,
    		@RequestParam(value = "sURL_CallbackStatusNew", required = false) String sURL_CallbackStatusNew,
    		@RequestParam(value = "sURL_CallbackPaySuccess", required = false) String sURL_CallbackPaySuccess,
    		@RequestParam(value = "nID_Subject", required = true) Long nID_Subject,
    		@RequestParam(value = "bTest", required = true) boolean bTest) throws Exception {
         
    	return liqBuy.getPayButtonHTML_LiqPay(sID_Merchant, sSum, 
    			oID_Currency, oLanguage, sDescription, sID_Order, 
    			sURL_CallbackStatusNew, sURL_CallbackStatusNew, 
    			nID_Subject, true);
    }

    @RequestMapping(value = "/setDocument", method = RequestMethod.GET)
    public
    @ResponseBody
    Long setDocument(
            @RequestParam(value = "nID_Subject", required = false) long nID_Subject,
            @RequestParam(value = "sID_Subject_Upload") String sID_Subject_Upload,
            @RequestParam(value = "sSubjectName_Upload") String sSubjectName_Upload,
            @RequestParam(value = "sName") String sName,
            //@RequestParam(value = "sFile", required = false) String fileName,
            @RequestParam(value = "nID_DocumentType") Long nID_DocumentType,
            //@RequestParam(value = "nID_DocumentContentType", required = false) Integer nID_DocumentContentType,
            @RequestParam(value = "sDocumentContentType", required = false) String documentContentTypeName,
            @RequestParam(value = "soDocumentContent") String sContent,
            @RequestParam(value = "oSignData", required = false) String oSignData,
            //@RequestParam(value = "oFile", required = false) MultipartFile oFile,
            //@RequestBody byte[] content,
            HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {

        //MultipartFile oFile = new MockMultipartFile("filename.txt", "fullfilename.txt", "text/plain", sContent.getBytes());
        String sFileName = "filename.txt";
        String sFileContentType = "text/plain";
        byte[] aoContent = sContent.getBytes();


        documentContentTypeName = request.getHeader("Content-Type") != null ? request.getHeader("filename") : documentContentTypeName;
        DocumentContentType documentContentType = null;
        if (documentContentTypeName != null) {
            documentContentType = documentContentTypeDao.getDocumentContentType(documentContentTypeName);
            if (documentContentType == null) {
                documentContentType = new DocumentContentType();
                documentContentType.setName(documentContentTypeName);
                documentContentType.setId(documentContentTypeDao.setDocumentContent(documentContentType));
            }
        } else {
            throw new ActivitiObjectNotFoundException(
                    "RequestParam 'nID_DocumentContentType' not found!", DocumentContentType.class);
        }
        
        Subject subject_Upload = syncSubject_Upload(sID_Subject_Upload);

        oSignData = BankIDUtils.checkECP(bankIDConfig.sClientId(), bankIDConfig.sClientSecret(), generalConfig.sHostCentral(), aoContent, sName);
        
        return documentDao.setDocument(
                nID_Subject,
                subject_Upload.getId(),
                sID_Subject_Upload,
                sSubjectName_Upload,
                sName,
                nID_DocumentType,
                documentContentType.getId(),
                sFileName,
                sFileContentType,
                aoContent,
                oSignData);

    }

    @RequestMapping(value = "/setDocumentFile", method = RequestMethod.POST)
    public
    @ResponseBody
    Long setDocumentFile(
            @RequestParam(value = "nID_Subject", required = false) long nID_Subject,
            @RequestParam(value = "sID_Subject_Upload") String sID_Subject_Upload,
            @RequestParam(value = "sSubjectName_Upload") String sSubjectName_Upload,
            @RequestParam(value = "sName") String sName,
            @RequestParam(value = "sFileExtension", required = false) String sFileExtension,
            //@RequestParam(value = "sFile", required = false) String fileName,
            @RequestParam(value = "nID_DocumentType") Long nID_DocumentType,
            @RequestParam(value = "nID_DocumentContentType", required = false) Long nID_DocumentContentType,
            @RequestParam(value = "oFile", required = false) MultipartFile oFile,
            @RequestParam(value = "file", required = false) MultipartFile oFile2,
//            @RequestParam(value = "oSignData", required = true) String soSignData,//todo required?? (issue587)
            //@RequestBody byte[] content,
            HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {

        //String sFileName = oFile.getName();
        //String sFileName = oFile.getOriginalFilename();
         //Content-Disposition:attachment; filename=passport.zip
        
        if(oFile==null){
            oFile=oFile2;
        }        

        String sOriginalFileName = oFile.getOriginalFilename();
        log.info("sOriginalFileName="+sOriginalFileName);
        
        String sOriginalContentType = oFile.getContentType();
        log.info("sOriginalContentType="+sOriginalContentType);

        String sFileName = request.getHeader("filename");
        log.info("sFileName(before)="+sFileName);
        
        if(sFileName==null||"".equals(sFileName.trim())){
            //sFileName = oFile.getOriginalFilename()+".zip";
            log.info("sFileExtension="+sFileExtension);
            if(sFileExtension!=null && !"".equals(sFileExtension.trim())
                    && sOriginalFileName!=null &&  !"".equals(sOriginalFileName.trim())
                    && sOriginalFileName.endsWith(sFileExtension)  ){
                sFileName = sOriginalFileName;
                log.info("sOriginalFileName has equal ext! sFileName(all ok)="+sFileName);
            }else{
                //for(String s : request.getHeaderNames()){
                Enumeration<String> a =  request.getHeaderNames();
                for(int n=0;a.hasMoreElements()&&n<100;n++){
                    String s = a.nextElement();
                    log.info("n="+n+", s="+s+", value="+request.getHeader(s));
                }
                String fileExp = RedisUtil.getFileExp(sOriginalFileName);
                fileExp = fileExp != null ? fileExp : ".zip.zip";
                //fileExp = fileExp.equalsIgnoreCase(sOriginalFileName) ? ".zip" : fileExp;
                fileExp = fileExp.equalsIgnoreCase(sOriginalFileName) ? sFileExtension : fileExp;
                fileExp = fileExp != null ? fileExp.toLowerCase() : ".zip";
                sFileName = sOriginalFileName + (fileExp.startsWith(".")?"":".") + fileExp;                
                log.info("sFileName(after)="+sFileName);
            }
        }
        //String sFileContentType = oFile.getContentType();
        byte[] aoContent = oFile.getBytes();

        Subject subject_Upload = syncSubject_Upload(sID_Subject_Upload);
        
        String soSignData = BankIDUtils.checkECP(bankIDConfig.sClientId(), bankIDConfig.sClientSecret(), generalConfig.sHostCentral(), aoContent, sName);
        
        Long nID_Document = documentDao.setDocument(
                        nID_Subject,
                        subject_Upload.getId(),
                        sID_Subject_Upload,
                        sSubjectName_Upload,
                        sName,
                        nID_DocumentType,
                        nID_DocumentContentType,
                        sFileName,
                        sOriginalContentType,
                        aoContent,
                        soSignData);
        createHistoryEvent(HistoryEventType.SET_DOCUMENT_INTERNAL,
                nID_Subject, sSubjectName_Upload, nID_Document, null);
        return nID_Document;
    }

    private Subject syncSubject_Upload(String sID_Subject_Upload){
    	Subject subject_Upload = subjectDao.getSubject(sID_Subject_Upload);
    	if(subject_Upload == null){
    		subject_Upload = subjectOrganDao.setSubjectOrgan(sID_Subject_Upload).getoSubject();
    	}
    	return subject_Upload;
    }

   

    @RequestMapping(value   = "/getSubjectOrganJoins",
                    method  = RequestMethod.GET,
                    headers = { "Accept=application/json" })
    public  @ResponseBody List<SubjectOrganJoin> getAllSubjectOrganJoins(
            @RequestParam(value = "nID_SubjectOrgan") 				Long    organID,
            @RequestParam(value = "nID_Region", required = false) 	Long    regionID,
            @RequestParam(value = "nID_City", required = false)     Long    cityID,
            @RequestParam(value = "sID_UA", required = false)       String  uaID
    ) {
        return subjectOrganDao.findSubjectOrganJoinsBy(organID, regionID, cityID, uaID);
    }


    @RequestMapping(value   = "/setSubjectOrganJoin",
            method  = RequestMethod.GET,
            headers = { "Accept=application/json" })
    public  @ResponseBody void setSubjectOrganJoin(
            @RequestParam(value = "nID_SubjectOrgan")   Long    organID,
            @RequestParam(value = "sNameUa")            String  nameUA,
            @RequestParam(value = "sNameRu")            String  nameRU,
            @RequestParam(value = "sID_Privat")         String  privateID,
            @RequestParam(value = "sID_Public")         String  publicID,
            @RequestParam(value = "sGeoLongitude")      String  geoLongitude,
            @RequestParam(value = "sGeoLatitude")       String  geoLatitude,
            @RequestParam(value = "sID_UA")             String  uaID,
            @RequestParam(value = "nID_Region", required = false)   Long regionID,
            @RequestParam(value = "nID_City", required = false)     Long cityID
    ){
        SubjectOrganJoin soj = new SubjectOrganJoin();
        soj.setUaId(uaID);
        soj.setSubjectOrganId(organID);
        soj.setNameUa(nameUA);
        soj.setNameRu(nameRU);
        soj.setPrivatId(privateID);
        soj.setPublicId(publicID);
        soj.setGeoLongitude(geoLongitude);
        soj.setGeoLatitude(geoLatitude);
        soj.setRegionId(regionID);
        soj.setCityId(cityID);
        subjectOrganDao.add( soj );
    }
    
    private void createHistoryEvent(HistoryEventType eventType,
			Long nID_Subject, String sSubjectName_Upload, Long nID_Document,
			Document document) {
		Map<String, String> values = new HashMap<>();
		try {
			Document oDocument = document == null ? documentDao
					.getDocument(nID_Document) : document;
			values.put(HistoryEventMessage.DOCUMENT_TYPE, oDocument
					.getDocumentType().getName());
			values.put(HistoryEventMessage.DOCUMENT_NAME, oDocument.getName());
			values.put(HistoryEventMessage.ORGANIZATION_NAME,
					sSubjectName_Upload);
		} catch (Throwable e) {
			log.warn("can't get document info!", e);
		}
		try {
			String eventMessage = HistoryEventMessage.createJournalMessage(
					eventType, values);
			historyEventDao.setHistoryEvent(nID_Subject, eventType.getnID(),
					eventMessage, eventMessage);
		} catch (IOException e) {
			log.error("error during creating HistoryEvent", e);
		}
	}

    @RequestMapping(value   = "/removeSubjectOrganJoins",
                    method  = RequestMethod.GET,
                    headers = { "Accept=application/json" })
    public  @ResponseBody void removeSubjectOrganJoins(
            @RequestParam(value = "nID_SubjectOrgan")   Long     organID,
            @RequestParam(value = "asID_Public")        String[] publicIDs
    ) {
        subjectOrganDao.removeSubjectOrganJoin(organID, publicIDs);
    }

    //################ DocumentType services ###################

    @RequestMapping(value = "/getDocumentTypes", method = RequestMethod.GET)
    public
    @ResponseBody
    List<DocumentType> getDocumentTypes() throws Exception {
        return documentTypeDao.getDocumentTypes();
    }

    @RequestMapping(value   = "/setDocumentType",  method  = RequestMethod.GET)
    public  @ResponseBody
    ResponseEntity setDocumentType (
            @RequestParam(value = "nID")   Long     nID,
            @RequestParam(value = "sName") String sName,
            @RequestParam(value = "bHidden", required = false) Boolean bHidden
    ) {
        ResponseEntity result;
        try {
            DocumentType documentType = documentTypeDao.setDocumentType(nID, sName, bHidden);
            result = JsonRestUtils.toJsonResponse(documentType);
        } catch (RuntimeException e) {
            result = toJsonErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
        }
        return result;
    }

    private ResponseEntity toJsonErrorResponse(HttpStatus httpStatus, String eMessage) {//todo move to JsonRestUtils
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        headers.setContentType(mediaType);
        headers.set("Reason", eMessage);
        return new ResponseEntity<>(headers, httpStatus);
    }

    @RequestMapping(value   = "/removeDocumentType", method  = RequestMethod.GET)
    public  @ResponseBody void removeDocumentType (
            @RequestParam(value = "nID")   Long     nID,
            HttpServletResponse response
    ) {
        try {
            documentTypeDao.removeDocumentType(nID);
        } catch (RuntimeException e) {
            response.setStatus(403);
            response.setHeader("Reason", e.getMessage());
        }
    }

    //################ DocumentContentType services ###################

    @RequestMapping(value = "/getDocumentContentTypes", method = RequestMethod.GET)
    public
    @ResponseBody
    List<DocumentContentType> getDocumentContentTypes()  {
        return documentContentTypeDao.getDocumentContentTypes();
    }

    @RequestMapping(value   = "/setDocumentContentType",  method  = RequestMethod.GET)
    public  @ResponseBody
    ResponseEntity setDocumentContentType (
            @RequestParam(value = "nID")   Long     nID,
            @RequestParam(value = "sName") String sName
    ) {
        ResponseEntity result;
        try {
            DocumentContentType documentType = documentContentTypeDao.setDocumentContentType(nID, sName);
            result = JsonRestUtils.toJsonResponse(documentType);
        } catch (RuntimeException e) {
            result = toJsonErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value   = "/removeDocumentContentType", method  = RequestMethod.GET)
    public  @ResponseBody void removeDocumentContentType (
            @RequestParam(value = "nID")   Long     nID,
            HttpServletResponse response
    ) {
        try {
            documentContentTypeDao.removeDocumentContentType(nID);
        } catch (RuntimeException e) {
            response.setStatus(403);
            response.setHeader("Reason", e.getMessage());
        }
    }

    //################      ###################

}
