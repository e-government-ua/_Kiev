package org.wf.dp.dniprorada.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.wf.dp.dniprorada.liqPay.LiqBuyUtil;
import org.wf.dp.dniprorada.util.GeneralConfig;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpRequester {

    @Autowired
    GeneralConfig generalConfig;

    public String post(String url, Map<String, String> params)
            throws Exception {
        String urlParameters = "";

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    urlParameters += entry.getKey() + "="
                            + URLEncoder.encode(entry.getValue(), "UTF-8") + "&";
                }
            }
        }

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        String sUser = generalConfig.sAuthLogin();
        String sPassword = generalConfig.sAuthPassword();
        String sAuth = LiqBuyUtil.base64_encode(sUser + ":" + sPassword);
        con.setRequestProperty("authorization", "Basic " + sAuth);

        con.setRequestMethod(RequestMethod.POST.name());
        con.setDoOutput(false);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        // Send post request
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public String get(String url, Map<String, String> params) throws Exception {
        URL obj = new URL(getFullURL(url, params));
        InputStream in;
        BufferedReader bf;
        HttpURLConnection con;
        con = (HttpURLConnection) obj.openConnection();

        String sUser = generalConfig.sAuthLogin();
        String sPassword = generalConfig.sAuthPassword();
        String sAuth = LiqBuyUtil.base64_encode(sUser + ":" + sPassword);
        con.setRequestProperty("authorization", "Basic " + sAuth);

        con.setRequestMethod(RequestMethod.GET.name());
        con.setDoInput(true);
        con.setDoOutput(true);

        if (con.getResponseCode() >= HttpStatus.BAD_REQUEST.value()) {
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

    public String getFullURL(String url, Map<String, String> params) throws UnsupportedEncodingException {
        String urlParameters = "";
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    urlParameters += entry.getKey() + "="
                            + URLEncoder.encode(entry.getValue(), "UTF-8") + "&";
                }
            }
        }

        String fullUrl = url;
        if (urlParameters.length() > 0) {
            fullUrl += "?" + urlParameters;
        }
        return fullUrl;
    }
}
