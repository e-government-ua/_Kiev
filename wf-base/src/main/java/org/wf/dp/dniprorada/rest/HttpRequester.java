package org.wf.dp.dniprorada.rest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;

public class HttpRequester {

	public static String post(String url, Map<String, String> list)
			throws Exception {
		String urlParameters = "";

		for (Map.Entry<String, String> entry : list.entrySet())
			urlParameters += entry.getKey() + "="
					+ URLEncoder.encode(entry.getValue(), "UTF-8") + "&";

		URL obj = new URL(url);
		DataOutputStream wr;
		BufferedReader in;
		HttpURLConnection con;
		con = (HttpURLConnection) obj.openConnection();
		con.setRequestProperty("authorization",
				"Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=");
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
	
	public static String get(String url, Map<String, String> list) throws Exception {
		String urlParameters = "";

		if(list != null){
			for (Map.Entry<String, String> entry : list.entrySet())
				urlParameters += entry.getKey() + "="
						+ URLEncoder.encode(entry.getValue(), "UTF-8") + "&";
		}
		

		URL obj = new URL(url + "?" + urlParameters);
		//DataOutputStream wr;
		InputStream in;
		BufferedReader bf;
		HttpURLConnection con;
		con = (HttpURLConnection) obj.openConnection();
		con.setRequestProperty("authorization",
				"Basic YWN0aXZpdGktbWFzdGVyOlVqaHRKbkV2ZiE=");
		con.setRequestMethod(RequestMethod.GET.name());
		con.setDoInput(true); 
		con.setDoOutput(true);
		//wr = new DataOutputStream(con.getOutputStream());
		//wr.writeBytes(urlParameters);
		//wr.flush();
		//wr.close();
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
