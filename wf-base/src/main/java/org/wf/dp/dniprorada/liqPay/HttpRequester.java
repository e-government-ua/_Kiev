package org.wf.dp.dniprorada.liqPay;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

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
		con.setDoOutput(true);
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
		con.setRequestMethod("GET");
		con.setDoOutput(true);
		wr = new DataOutputStream(con.getOutputStream());
		// Send get request
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
}
