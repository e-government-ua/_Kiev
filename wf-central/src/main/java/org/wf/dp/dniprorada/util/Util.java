package org.wf.dp.dniprorada.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Util {

        private final static Logger log = LoggerFactory.getLogger(Util.class);
    
	private Util() {
	}

    public static String sData(byte[] a){
        //Charset.forName("UTF-8")
        //byte[] b = {(byte) 99, (byte)97, (byte)116};
        String s = "Not convertable!";
        log.info("[sData]:a.length="+a.length+",Arrays.toString(a)="+Arrays.toString(a));
        try{
            s = new String(a, "UTF-8");
        }catch(Exception oException){
            log.error("[sData]",oException);
        }
        log.info("[sData]:s="+s);
        return s;
    }
    public static byte[] aData(String s){
        log.info("[aData]:s="+s);
        byte[] a = s.getBytes(Charset.forName("UTF-8"));
        log.info("[aData]:a.length="+a.length+",Arrays.toString(a)="+Arrays.toString(a));
        return a;
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
		con.setRequestMethod("GET");
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
