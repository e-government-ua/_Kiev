package org.wf.dp.dniprorada.util;

import java.io.IOException;

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
}
