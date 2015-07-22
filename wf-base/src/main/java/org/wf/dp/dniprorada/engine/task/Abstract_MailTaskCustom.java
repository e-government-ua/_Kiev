package org.wf.dp.dniprorada.engine.task;

import java.net.URL;
import java.util.Date;
import javax.activation.DataHandler;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.activiti.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.wf.dp.dniprorada.base.dao.AccessDataDao;
import org.wf.dp.dniprorada.constant.Currency;
import org.wf.dp.dniprorada.constant.Language;
import org.wf.dp.dniprorada.liqPay.LiqBuy;
import org.wf.dp.dniprorada.util.GeneralConfig;
import org.wf.dp.dniprorada.util.Mail;

public abstract class Abstract_MailTaskCustom implements JavaDelegate {

    
    @Autowired
    GeneralConfig generalConfig;
    
    private static final String TAG_PAYMENT_BUTTON_LIQPAY = "[paymentButton_LiqPay]";
    

    //private static final String LIQPAY_CALLBACK_URL = "https://test.region.igov.org.ua/wf-region/service/setPaymentStatus_TaskActiviti?sID_Order={0}&sID_PaymentSystem=Liqpay&sData=";
    private static final String LIQPAY_CALLBACK_URL = new GeneralConfig().sHost() + "/wf-region/service/setPaymentStatus_TaskActiviti?sID_Order={0}&sID_PaymentSystem=Liqpay&sData=";
    private static final String TAG_nID_SUBJECT = "[nID_Subject]";
    private static final String TAG_sACCESS_KEY = "[sAccessKey]";
    private static final String TAG_sURL_SERVICE_MESSAGE = "[sURL_ServiceMessage]";
    //private static final String URL_SERVICE_MESSAGE = "https://test.igov.org.ua/wf-central/service/messages/setMessage";
    private static final String URL_SERVICE_MESSAGE = new GeneralConfig().sHostCentral() + "/wf-central/service/messages/setMessage";

    
    
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
    
    protected Expression sID_Merchant;
    protected Expression sSum;
    protected Expression sID_Currency;
    protected Expression sLanguage;
    protected Expression sDescription;
    protected Expression nID_Subject;

    static final transient Logger LOG = LoggerFactory.getLogger(Abstract_MailTaskCustom.class);
    
    protected String replaceTags(String textStr, DelegateExecution execution) throws Exception {
        if (textStr == null) {
            return null;
        }
        String textWithoutTags = textStr;
        if (textStr.contains(TAG_PAYMENT_BUTTON_LIQPAY)) {
            String sID_Merchant = execution.getVariable("sID_Merchant").toString();
            LOG.info("sID_Merchant="+sID_Merchant);
            //sID_Merchant = getStringFromFieldExpression(this.sID_Merchant, execution);
            //LOG.info("sID_Merchant="+sID_Merchant);
            
            //String sSum = getStringFromFieldExpression(this.sSum, execution);
            String sSum = execution.getVariable("sSum").toString();
            LOG.info("sSum="+sSum);
            
            
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
            textWithoutTags = StringUtils.replace(textStr, TAG_PAYMENT_BUTTON_LIQPAY, htmlButton);
        }

        if (textWithoutTags.contains(TAG_nID_SUBJECT)) {
            textWithoutTags = textWithoutTags.replaceAll(TAG_nID_SUBJECT, "" + nID_Subject);
        }
        if (textWithoutTags.contains(TAG_sACCESS_KEY)) {
            textWithoutTags = textWithoutTags.replaceAll(TAG_sACCESS_KEY, accessDataDao.setAccessData("" + nID_Subject));
        }
        if (textWithoutTags.contains(TAG_sURL_SERVICE_MESSAGE)) {
            textWithoutTags = textWithoutTags.replaceAll(TAG_sURL_SERVICE_MESSAGE, URL_SERVICE_MESSAGE);
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
        
        Mail oMail = new Mail();
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

        oMail
        ._BodyAsText()
        ;
        
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
