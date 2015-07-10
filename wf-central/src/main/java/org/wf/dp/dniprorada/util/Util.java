package org.wf.dp.dniprorada.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

public final class Util {

	private Util() {
	}

	public static String contentByteToString(byte[] contentByte) {
		BASE64Encoder encoder = new BASE64Encoder();
		String contentString = encoder.encode(contentByte);
		return contentString;
	}

	public static byte[] contentStringToByte(String contentString){
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] contentByte;
		try{
			contentByte = decoder.decodeBuffer(contentString);	
		}catch(Exception ex){
			contentByte = new byte[1];
		}
		
		return contentByte;
	}

	public static boolean isNotEmpty(String str){
		return str != null && !str.isEmpty();
	}
	
	public static String httpAnswer (String urlName, String sData) throws Exception{
		
		URL url = new URL(urlName);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("content-type", "application/json;charset=UTF-8");
		con.setDoOutput(true);
		DataOutputStream dos = new DataOutputStream(con.getOutputStream());
		dos.writeBytes(sData);
		dos.flush();
		dos.close();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String inputLine;
		while((inputLine = br.readLine()) != null){
			sb.append(inputLine);
		}
		br.close();
		return sb.toString();
	}
}
