package org.wf.dp.dniprorada.engine.task;

import org.activiti.engine.EngineServices;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.wf.dp.dniprorada.base.dao.AccessDataDao;
import org.wf.dp.dniprorada.constant.Currency;
import org.wf.dp.dniprorada.constant.Language;
import org.wf.dp.dniprorada.liqPay.LiqBuy;
import org.wf.dp.dniprorada.util.GeneralConfig;
import org.wf.dp.dniprorada.util.Mail;
import org.wf.dp.dniprorada.util.Util;

import java.util.LinkedList;
import java.util.List;

import static org.activiti.rest.controller.ActivitiRestApiController.parseEnumProperty;
import static org.wf.dp.dniprorada.util.luna.AlgorithmLuna.getProtectedNumber;

public abstract class Abstract_MailTaskCustom implements JavaDelegate {

    
    @Autowired
    GeneralConfig generalConfig;
    
    @Autowired
    Mail oMail;
    
    private static final String TAG_PAYMENT_BUTTON_LIQPAY = "[paymentButton_LiqPay]";
    private static final String TAG_CANCEL_TASK = "[cancelTask]";

    //private static final String LIQPAY_CALLBACK_URL = "https://test.region.igov.org.ua/wf-region/service/setPaymentStatus_TaskActiviti?sID_Order={0}&sID_PaymentSystem=Liqpay&sData=";
    private static final String TAG_nID_Protected = "[nID_Protected]";
    private static final String TAG_nID_SUBJECT = "[nID_Subject]";
    private static final String TAG_sACCESS_KEY = "[sAccessKey]";
    private static final String TAG_sURL_SERVICE_MESSAGE = "[sURL_ServiceMessage]";
    //[sURL_ServiceMessage]?nID_Subject=[nID_Subject]&amp;sAccessKey=[sAccessKey]&amp;sData=�������� ������&amp;sMail= &amp;nID_SubjectMessageType=1
    //private static final String queryParamPattern = "?nID_Subject=%s&amp;sData=�������� ������&amp;sMail= &amp;nID_SubjectMessageType=1&amp;sAccessContract=Request"; //sAccessKey=%s&amp;
    //private static final String accessKeyPattern = "&amp;sAccessKey=%s";
    private static final String queryParamPattern = "?sHead=Отзыв&sBody=Отзыв&sData=Название Услуги&sMail= &nID_SubjectMessageType=1&sAccessContract=Request"; //sAccessKey=%s&amp;
    private static final String accessKeyPattern = "&sAccessKey=%s";
    //private static final String URL_SERVICE_MESSAGE = "https://test.igov.org.ua/wf-central/service/messages/setMessage";
    private static final String TAG_Function_AtEnum = "enum{[";
    private static final String TAG_Function_To = "]}";

    
    
    @Autowired
    public TaskService taskService;

    @Value("${mailServerHost}")
    public String mailServerHost;

    @Value("${mailServerPort}")
    public String mailServerPort;

    @Value("${mailServerDefaultFrom}")
    public String mailServerDefaultFrom;

    @Value("${mailServerUsername}")
    public String mailServerUsername;

    @Value("${mailServerPassword}")
    public String mailServerPassword;

    @Value("${mailAddressNoreply}")
    public String mailAddressNoreplay;

    public Expression from;
    public Expression to;
    public Expression subject;
    public Expression text;    
    
    

    @Autowired
    AccessDataDao accessDataDao;

    @Autowired
    LiqBuy liqBuy;
    @Autowired
    private CancelTaskUtil cancelTaskUtil;

    protected Expression sID_Merchant;
    protected Expression sSum;
    protected Expression sID_Currency;
    protected Expression sLanguage;
    protected Expression sDescription;
    protected Expression nID_Subject;

    static final transient Logger LOG = LoggerFactory.getLogger(Abstract_MailTaskCustom.class);
    
    protected String replaceTags(String textStr, DelegateExecution execution) throws Exception {
        String LIQPAY_CALLBACK_URL = generalConfig.sHost() + "/wf-region/service/setPaymentStatus_TaskActiviti?sID_Order={0}&sID_PaymentSystem=Liqpay&sData=";
        String URL_SERVICE_MESSAGE = generalConfig.sHostCentral() + "/wf-central/service/messages/setMessage";
        
        if (textStr == null) {
            return null;
        }
        String textWithoutTags = textStr;
        if (textWithoutTags.contains(TAG_PAYMENT_BUTTON_LIQPAY)) {
            String sID_Merchant = execution.getVariable("sID_Merchant").toString();
            LOG.info("sID_Merchant="+sID_Merchant);
            //sID_Merchant = getStringFromFieldExpression(this.sID_Merchant, execution);
            //LOG.info("sID_Merchant="+sID_Merchant);
            
            //String sSum = getStringFromFieldExpression(this.sSum, execution);
            String sSum = execution.getVariable("sSum").toString();
            LOG.info("sSum="+sSum);
            if(sSum!=null){
                sSum=sSum.replaceAll(",", ".");
            }
            
            
            //String sID_Currency = getStringFromFieldExpression(this.sID_Currency, execution);
            String sID_Currency = execution.getVariable("sID_Currency").toString();
            LOG.info("sID_Currency="+sID_Currency);
            Currency oID_Currency = Currency.valueOf(sID_Currency==null?"UAH":sID_Currency);
            LOG.info("oID_Currency.name()="+oID_Currency.name());
            
            Language sLanguage = LiqBuy.DEFAULT_LANG;
            //String sDescription = getStringFromFieldExpression(this.sDescription, execution);
            String sDescription = execution.getVariable("sDescription").toString();
            LOG.info("sDescription="+sDescription);
            
            String sID_Order = "TaskActiviti_" + execution.getId();  // TODO: not sure about id
            String sURL_CallbackStatusNew = StringUtils.replace(LIQPAY_CALLBACK_URL, "{0}", sID_Order); 
            String sURL_CallbackPaySuccess = null;
            //Long nID_Subject = getLongFromFieldExpression(this.nID_Subject, execution);
            Long nID_Subject = Long.valueOf(execution.getVariable("nID_Subject").toString());
            nID_Subject = (nID_Subject==null?0:nID_Subject);
            LOG.info("nID_Subject="+nID_Subject);
            boolean bTest = generalConfig.bTest();
            String htmlButton = liqBuy.getPayButtonHTML_LiqPay(sID_Merchant, sSum, oID_Currency, sLanguage, sDescription, sID_Order, sURL_CallbackStatusNew, sURL_CallbackPaySuccess, nID_Subject, bTest);
            textWithoutTags = StringUtils.replace(textWithoutTags, TAG_PAYMENT_BUTTON_LIQPAY, htmlButton);
        }

        
    //private static final String TAG_Function_AtEnum = "enum{[";
    //private static final String TAG_Function_To = "]}";
        
        //if (textWithoutTags.contains(TAG_Function_AtEnum)) {
        int nLimit=10;
        boolean bCashed = false;
        List<FormProperty> aProperty=new LinkedList();
        while (nLimit>0 && textWithoutTags.contains(TAG_Function_AtEnum)) {
            nLimit--;
            int nAt = textWithoutTags.indexOf(TAG_Function_AtEnum);
            LOG.info("sTAG_Function_AtEnum,nAt="+nAt);
            int nTo = textWithoutTags.indexOf(TAG_Function_To);
            LOG.info("sTAG_Function_AtEnum,nTo="+nTo);
            String sTAG_Function_AtEnum = textWithoutTags.substring(nAt+TAG_Function_AtEnum.length(), nTo);
            LOG.info("sTAG_Function_AtEnum="+sTAG_Function_AtEnum);
            
            if(!bCashed){
                EngineServices oEngineServices = execution.getEngineServices();
                //RuntimeService oRuntimeService = oEngineServices.getRuntimeService();
                LOG.info("execution.getId()="+execution.getId());
                TaskFormData oTaskFormData = oEngineServices
                        .getFormService()
                        .getTaskFormData(execution.getId());//task.getId()
                for (FormProperty property : oTaskFormData.getFormProperties()) {
                    //List<FormProperty> aProperty=new LinkedList();
                    aProperty.add(property);
                    //String sType=property.getType().getName();
                    //String snID=property.getId();
                    LOG.info(String.format("Matching property %s:%s:%s with fieldNames", property.getId(), property.getName(), property.getType().getName()));
                }
                bCashed = true;
            }
                //TaskFormData oTaskFormData = formService.getTaskFormData(curTask.getId());
                boolean bReplaced=false;
                //for (FormProperty property : oTaskFormData.getFormProperties()) {
                for (FormProperty property : aProperty) {
                        String sType=property.getType().getName();
                        String snID=property.getId();
//                        LOG.info(String.format("Matching property %s:%s:%s with fieldNames", snID, property.getName(), sType));
                        //if (currentRow.contains("${" + property.getId() + "}")) {
                        //LOG.info("sType="+sType + ",snID="+snID);
                        //LOG.info("snID="+snID);
                        if (!bReplaced && "enum".equals(sType) && sTAG_Function_AtEnum.equals(snID)) {
                                LOG.info(String.format("Found field! Matching property %s:%s:%s with fieldNames", snID, property.getName(), sType));
                                //LOG.info(String.format("Found field with id %s in the pattern. Adding value to the result", "${" + property.getId() + "}"));
//                                LOG.info("Found field!");
                                String sValue = parseEnumProperty(property);
                                LOG.info("sValue="+sValue);
                                
                                textWithoutTags = textWithoutTags.replaceAll("\\Q"+TAG_Function_AtEnum+sTAG_Function_AtEnum+TAG_Function_To+"\\E", sValue);
                                bReplaced=true;
                                //resume
                                /*
                                String sValue = "";
                                String sType=property.getType().getName();
                                log.info("sType="+sType);
                                if ("enum".equalsIgnoreCase(sType)) {
                                        sValue = parseEnumProperty(property);
                                } else {
                                        sValue = property.getValue();
                                }*/
                                /*log.info("sValue="+sValue);
                                if (sValue != null){
                                        log.info(String.format("Replacing field with the value %s", sValue));
                                        currentRow = currentRow.replace("${" + property.getId() + "}", sValue);
                                }*/

                        }
                }            
            
//            textWithoutTags = textWithoutTags.replaceAll(TAG_Function_AtEnum+sTAG_Function_AtEnum+TAG_Function_To, TAG_Function_To);
            //textWithoutTags = textWithoutTags.replaceAll(TAG_nID_SUBJECT, "" + nID_Subject);
        }
                
        //[sURL_ServiceMessage]?nID_Subject=[nID_Subject]&amp;sAccessKey=[sAccessKey]&amp;sData=�������� ������&amp;sMail= &amp;nID_SubjectMessageType=1       
        LOG.info("execution.getProcessInstanceId()="+execution.getProcessInstanceId());
        Long nID_Protected = getProtectedNumber(Long.valueOf(execution.getProcessInstanceId()));
        LOG.info("nID_Protected="+nID_Protected);
        //if (textWithoutTags.contains(TAG_nID_Protected)) {
        if (textWithoutTags.contains(TAG_nID_Protected)) {
            LOG.info("TAG_nID_Protected:Found");
            textWithoutTags = textWithoutTags.replaceAll("\\Q"+TAG_nID_Protected+"\\E", "" + nID_Protected);
        }else{
            //LOG.info("TAG_nID_Protected:Not Found");
        }
        if (textWithoutTags.contains(TAG_CANCEL_TASK)){
            LOG.info("TAG_CANCEL_TASK:Found");
            try{
                String cancelTaskBtn = cancelTaskUtil.getCancelFormHTML(nID_Protected);

                LOG.info(">>>>cancel button = " + cancelTaskBtn);
                textWithoutTags = textWithoutTags.replaceAll("\\Q"+TAG_CANCEL_TASK+"\\E", cancelTaskBtn);
            } catch (Exception e){
                LOG.error("ex during creating cancel task!", e);
            }
        }else{
            LOG.info("TAG_CANCEL_TASK:Not Found");
        }
        if (textWithoutTags.contains(TAG_nID_SUBJECT)) {
            textWithoutTags = textWithoutTags.replaceAll("\\Q"+TAG_nID_SUBJECT+"\\E", "" + nID_Subject);
        }
        if (textWithoutTags.contains(TAG_sACCESS_KEY)) {
            textWithoutTags = textWithoutTags.replaceAll("\\Q"+TAG_sACCESS_KEY+"\\E", accessDataDao.setAccessData("" + nID_Subject));
        }
        if (textWithoutTags.contains(TAG_sURL_SERVICE_MESSAGE)) {
            String URI = Util.deleteContextFromURL(URL_SERVICE_MESSAGE);
            String queryParam = String.format(queryParamPattern);
            if(nID_Subject != null){
                queryParam = queryParam + "&nID_Subject=" + nID_Subject;
            }
            String accessKey = accessDataDao.setAccessData(URI + queryParam);
            String replacemet = URL_SERVICE_MESSAGE + queryParam 
                    + String.format(accessKeyPattern, accessKey);
            LOG.info("textWithoutTags" + textWithoutTags);
            LOG.info("replacemet " + TAG_sURL_SERVICE_MESSAGE + ": " + replacemet);
            //textWithoutTags = textWithoutTags.replaceAll(TAG_sURL_SERVICE_MESSAGE, replacemet);
            textWithoutTags = StringUtils.replace(textWithoutTags, TAG_sURL_SERVICE_MESSAGE, replacemet);
            LOG.info("textWithoutTags after replase" + textWithoutTags);
        }

        return textWithoutTags;
    }

    protected String getStringFromFieldExpression(Expression expression, DelegateExecution execution) {
        if (expression != null) {
            Object value = expression.getValue(execution);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }
    
    protected Long getLongFromFieldExpression(Expression expression, DelegateExecution execution) {
        if (expression != null) {
            Object value = expression.getValue(execution);
            if (value != null) {
                return Long.valueOf(value.toString());
            }
        }
        return null;
    }
    
    
    //public MultiPartEmail MultiPartEmail_BaseFromTask(DelegateExecution oExecution) throws Exception {
    public Mail Mail_BaseFromTask(DelegateExecution oExecution) throws Exception {
    
        String sFromMail = getStringFromFieldExpression(this.from, oExecution);
        String saToMail = getStringFromFieldExpression(this.to, oExecution);
        String sHead = getStringFromFieldExpression(this.subject, oExecution);
        String sBodySource = getStringFromFieldExpression(this.text, oExecution);
        String sBody = replaceTags(sBodySource, oExecution);
        
        //Mail oMail = new Mail();
        oMail
        .reset();
        
        oMail
        ._From(mailAddressNoreplay)
        ._To(saToMail)
        ._Head(sHead)
        ._Body(sBody)
        ._AuthUser(mailServerUsername)
        ._AuthPassword(mailServerPassword)
        ._Host(mailServerHost)
        ._Port(Integer.valueOf(mailServerPort))
        ._SSL(true)
        ._TLS(true)
        ;

        /*oMail
        ._BodyAsHTML()
        ;*/
        
//        return oMultiPartEmail;
        return oMail;
        //MultiPartEmail oMultiPartEmail = MultiPartEmail_BaseFromTask();    
    }
    
    
    //sFromMail="HelpDesk <helpdesk@privatbank.ua>"
    /*private void sendMessageHTML(String sFromMail, String saToMail, String sHead, String sBody, String[] aPictureFilename) throws Exception {
    
        Date start = new Date();
        final ClassLoader oClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(MimeMessage.class.getClassLoader());
            final MimeMessage oMimeMessage = mailSender.createMimeMessage();
            final MimeMessageHelper oMMH = new MimeMessageHelper(oMimeMessage, true);
            oMMH.setTo(saToMail);
            oMMH.setFrom(sFromMail);
            oMMH.setSubject(sHead);

            final MimeMultipart oMimeMultipart = new MimeMultipart("related");
            final BodyPart oBodyPart = new MimeBodyPart();
            oBodyPart.setContent(sBody, "text/html; charset=\"utf-8\"");
            oMimeMultipart.addBodyPart(oBodyPart);

            //Loop for ataching all pics
            for (int i = 0; i < aPictureFilename.length; i++) {
                final MimeBodyPart oMimeBodyPart_Image = new MimeBodyPart();
                final URL oURL_Image = oClassLoader.getResource("email_pic/" + aPictureFilename[i]);
                oMimeBodyPart_Image.setDataHandler(new DataHandler(new URLDataSource(oURL_Image)));
                oMimeBodyPart_Image.setHeader("Content-ID", "<pic" + i + ">");
                oMimeMultipart.addBodyPart(oMimeBodyPart_Image);
            }

            oMimeMessage.setContent(oMimeMultipart);
            mailSender.send(oMimeMessage);
        } finally {
            Thread.currentThread().setContextClassLoader(oClassLoader);
            LOG.info("sendMessage " + Long.toString(new Date().getTime() - start.getTime()));
        }
    }*/
    
    

}
