package org.wf.dp.dniprorada.liqPay;

import static org.wf.dp.dniprorada.liqPay.LiqBuyUtil.base64_encode;
import static org.wf.dp.dniprorada.liqPay.LiqBuyUtil.sha1;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.dp.dniprorada.base.dao.AccessDataDao;
import org.wf.dp.dniprorada.constant.Currency;
import org.wf.dp.dniprorada.constant.Language;
import org.wf.dp.dniprorada.rest.HttpRequester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component()
public class LiqBuy {

    @Autowired
    private AccessDataDao accessDataDao;

    private static final Logger log = LoggerFactory.getLogger(LiqBuy.class);
    private static final String URL = "https://www.liqpay.com/api/checkout";
    private static final String version = "3";
    public static final Language DEFAULT_LANG = Language.RUSSIAN;
    private static final String sandbox = "1";
    private static final String payButtonHTML = new StringBuilder()
            .append("<form method=\"POST\" action=\"")
            .append(URL)
            .append("\" ")
            .append("accept-charset=\"utf-8\">")
            .append("<input type=\"hidden\" name=\"data\" value=\"%s\"/>")
            .append("<input type=\"hidden\" name=\"signature\" value=\"%s\"/>")
            .append("<input type=\"image\" src=\"//static.liqpay.com/buttons/p1%s.radius.png\"/>")
            .append("</form>").toString();

    public String getPayButtonHTML_LiqPay(String sID_Merchant, String sSum,
            Currency oID_Currency, Language oLanguage, String sDescription,
            String sID_Order, String sURL_CallbackStatusNew,
            String sURL_CallbackPaySuccess, Long nID_Subject, boolean bTest) throws Exception {
        if (oLanguage == null) {
            oLanguage = DEFAULT_LANG;
        }
        
        String publicKey = sID_Merchant;

        Map<String, String> paramMerchant = new HashMap<String, String>();
        paramMerchant.put("sID", sID_Merchant);
        paramMerchant.put("nID_Subject", String.valueOf(nID_Subject));
        String merchant = HttpRequester.get("https://test.igov.org.ua/wf-central/service/merchant/getMerchant", paramMerchant);
        
        log.info("merchant="+merchant);
        
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(merchant);
        
        String privateKey = (String) jsonObject.get("sPrivateKey");
        if (privateKey == null) {
            privateKey = "test";
        }
        if (sURL_CallbackStatusNew == null) {
            if(jsonObject.get("sURL_CallbackStatusNew")!=null){
                sURL_CallbackStatusNew = (String) jsonObject.get("sURL_CallbackStatusNew");
            }else{
                sURL_CallbackStatusNew = "";
            }
        }
        log.info("sURL_CallbackStatusNew="+sURL_CallbackStatusNew);
        
        
        if (sURL_CallbackPaySuccess == null) {
            if(jsonObject.get("sURL_CallbackPaySuccess")!=null){
                sURL_CallbackPaySuccess = (String) jsonObject.get("sURL_CallbackPaySuccess");
            }else{
                sURL_CallbackPaySuccess = "https://igov.org.ua";
            }
        }
        log.info("sURL_CallbackPaySuccess="+sURL_CallbackPaySuccess);
        
        
        if (sURL_CallbackStatusNew != null) {
            nID_Subject=new Long(0);
            
            String snID_Subject="0";log.info("accessDataDao!=null:"+(accessDataDao!=null));
            //String nID_Access = accessDataDao.setAccessData(String.valueOf(nID_Subject));
            String nID_Access = accessDataDao.setAccessData(snID_Subject);
            
            sURL_CallbackStatusNew = new StringBuilder(sURL_CallbackStatusNew)
                    .append(sURL_CallbackStatusNew.indexOf("?")>-1?"&":"?")
                    .append("nID_Subject=").append(nID_Subject)
                    .append("&nID_Access=").append(nID_Access).toString();
        }
        log.info("sURL_CallbackStatusNew(with security-key)="+sURL_CallbackStatusNew);
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("version", version);
        params.put("amount", sSum);
        params.put("currency", oID_Currency.name());
        params.put("language", oLanguage.getShortName());
        params.put("description", sDescription);
        params.put("order_id", sID_Order);
        params.put("server_url", sURL_CallbackStatusNew);
        params.put("result_url", sURL_CallbackPaySuccess);
        params.put("public_key", publicKey);

        if (bTest) {
            params.put("sandbox", sandbox);
        }
        
        log.info("getPayButtonHTML_LiqPay params: " + params + " privateKey: " + privateKey);
        String result = getForm(params, privateKey, oLanguage);
        log.info("getPayButtonHTML_LiqPay ok!: " + result);
        
        // LiqPay liqpay = new LiqPay(publicKey, privateKey);
        // String result = liqpay.cnb_form(params);
        // System.out.println(result);
        return result;
    }

    private String getForm(Map<String, String> params, String privateKey, Language oLanguage) {
        String data = base64_encode(JSONObject.toJSONString(params));
        String signature = createSignature(data, privateKey);
        return String.format(payButtonHTML, data, signature, oLanguage.getShortName());
    }

    private String str_to_sign(String str) {
        return base64_encode(sha1(str));
    }

    private String createSignature(String base64EncodedData, String privateKey) {
        return str_to_sign(privateKey + base64EncodedData + privateKey);
    }
}
