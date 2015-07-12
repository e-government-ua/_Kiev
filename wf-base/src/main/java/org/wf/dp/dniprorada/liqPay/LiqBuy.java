package org.wf.dp.dniprorada.liqPay;

import static org.wf.dp.dniprorada.liqPay.LiqBuyUtil.base64_encode;
import static org.wf.dp.dniprorada.liqPay.LiqBuyUtil.sha1;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.wf.dp.dniprorada.constant.Currency;
import org.wf.dp.dniprorada.constant.Language;

public class LiqBuy {

	private static final String URL = "https://www.liqpay.com/api/checkout";
	private static final String version = "version";
	private static final String languageDefault = "version";
	private static final String sandbox = "1";
	private static final String payButtonHTML = new StringBuilder()
			.append("<form method=\"POST\" action=\"")
			.append(URL)
			.append("\"")
			.append("accept-charset=\"utf-8\">")
			.append("<input type=\"hidden\" name=\"data\" value=\"${%s}\"/>")
			.append("<input type=\"hidden\" name=\"signature\" value=\"${%s}\"/>")
			.append("<input type=\"image\" src=\"//static.liqpay.com/buttons/p1{%s}.radius.png\"/>")
			.append("</form>").toString();

	public String getPayButtonHTML_LiqPay(String sID_Merchant, String sSum,
			Currency oID_Currency, Language oLanguage, String sDescription,
			String sID_Order, String sURL_CallbackStatusNew,
			String sURL_CallbackPaySuccess, Long nID_Subject, boolean bTest) throws Exception {

		bTest = true; //пока не отладим
		String publicKey = sID_Merchant;
        Map<String, String> paramMerchant= new HashMap<String, String>();
        paramMerchant.put("sID_Merchant", sID_Merchant);
        System.out.println("!!!!!!!!!!!!!!!!!merchant start");
		String merchant = HttpRequester.get("https://test.igov.org.ua/wf-central/service/merchant/getMerchant", paramMerchant);
		System.out.println("!!!!!!!!!!!!!!!!!merchant ok: " + merchant);
		String privateKey = "test"; //Пока не реализовали возврат в ответе ключа
		if (sURL_CallbackPaySuccess == null) {
			sURL_CallbackPaySuccess = "https://test.igov.org.ua/wf-central/service/merchant/getMerchants";//пока не реализовали в ответе URL merchant.getsURL_CallbackPaySuccess(); 
		}

		Long nID_Access = new Long(10);// !!!!!!!!!!!!!!!AccessData.setAccessData(nID_Subject);
		if (sURL_CallbackPaySuccess != null) {
			sURL_CallbackPaySuccess = new StringBuilder(sURL_CallbackPaySuccess)
					.append("?nID_Subject=").append(nID_Subject)
					.append("&nID_Access=").append(nID_Access).toString();
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("version", version);
		params.put("amount", sSum);
		params.put("currency", oID_Currency.name());
		params.put("language", oLanguage.name());
		params.put("description", sDescription);
		params.put("order_id", sID_Order);
		params.put("server_url", sURL_CallbackStatusNew);
		params.put("result_url", sURL_CallbackPaySuccess);
		
		params.put("public_key", publicKey);
		
		if (bTest) {
			params.put("sandbox", sandbox);
		}

		// LiqPay liqpay = new LiqPay(publicKey, privateKey);
		// String result = liqpay.cnb_form(params);
		// System.out.println(result);

		String result = getForm(params, privateKey);
		System.out.println("*************************************************result: " + result);
        return result;
	}
	
	private String getForm(Map<String, String> params, String privateKey) {
		String data = base64_encode(JSONObject.toJSONString(params));
		String signature = createSignature(data, privateKey);
		String language = params.get("language") != null ? params.get("language") : languageDefault;
		return String.format(payButtonHTML, data, signature, language);
	}

	private String str_to_sign(String str) {
		return base64_encode(sha1(str));
	}

	private String createSignature(String base64EncodedData, String privateKey) {
		return str_to_sign(privateKey + base64EncodedData + privateKey);
	}
}
