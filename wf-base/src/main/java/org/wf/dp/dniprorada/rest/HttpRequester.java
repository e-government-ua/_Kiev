package org.wf.dp.dniprorada.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.RequestMethod;
import org.wf.dp.dniprorada.liqPay.LiqBuyUtil;
import org.wf.dp.dniprorada.util.GeneralConfig;

public class HttpRequester {

    @Autowired
    GeneralConfig generalConfig;
        
	public String post(String url, Map<String, String> params)
			throws Exception {
		String urlParameters = "";

		if(params != null){
			for (Map.Entry<String, String> entry : params.entrySet()){
				if(entry.getValue() != null){
			urlParameters += entry.getKey() + "="
					+ URLEncoder.encode(entry.getValue(), "UTF-8") + "&";
				}
			}
		}

		URL obj = new URL(url);
		DataOutputStream wr;
		BufferedReader in;
		HttpURLConnection con;
		con = (HttpURLConnection) obj.openConnection();
		//con.setRequestProperty("authorization",
		//		"Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=");
                
                
                String sUser=generalConfig.sAuthLogin();
                String sPassword=generalConfig.sAuthPassword();
                String sAuth = LiqBuyUtil.base64_encode(sUser+":"+sPassword);
		con.setRequestProperty("authorization", "Basic "+sAuth);
                
                
		con.setRequestMethod("POST");
		con.setDoOutput(false);
		wr = new DataOutputStream(con.getOutputStream());
		// Send post request
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		in = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
	
	public String get(String url, Map<String, String> params) throws Exception {
		String urlParameters = "";

		if(params != null){
			for (Map.Entry<String, String> entry : params.entrySet()){
				if(entry.getValue() != null){
				urlParameters += entry.getKey() + "="
						+ URLEncoder.encode(entry.getValue(), "UTF-8") + "&";
		}
			}
		}
		
		URL obj = new URL(url + "?" + urlParameters);
		InputStream in;
		BufferedReader bf;
		HttpURLConnection con;
		con = (HttpURLConnection) obj.openConnection();
                
                /*
		con.setRequestProperty("authorization",
				"Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=");
                */
                
                String sUser=generalConfig.sAuthLogin();
                String sPassword=generalConfig.sAuthPassword();
                String sAuth = LiqBuyUtil.base64_encode(sUser+":"+sPassword);
		con.setRequestProperty("authorization", "Basic "+sAuth);
                
		con.setRequestMethod(RequestMethod.GET.name());
		con.setDoInput(true); 
		con.setDoOutput(true);

		if (con.getResponseCode() >= 400) {
			in = con.getErrorStream();
		} else {
			in = con.getInputStream();
		}
		bf = new BufferedReader(new InputStreamReader(in));
		String inputLine;
		StringBuilder response = new StringBuilder();

		while ((inputLine = bf.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
}
