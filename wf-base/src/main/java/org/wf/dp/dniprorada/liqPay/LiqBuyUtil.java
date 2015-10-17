package org.wf.dp.dniprorada.liqPay;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class LiqBuyUtil {
    public static byte[] sha1(String param) {
        try {
            MessageDigest SHA = MessageDigest.getInstance("SHA-1");
            SHA.reset();
            SHA.update(param.getBytes("UTF-8"));
            return SHA.digest();
        } catch (Exception e) {
            throw new RuntimeException("Can't calc SHA-1 hash", e);
        }
    }

    public static String base64_encode(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }

    public static String base64_encode(String data) {
        return base64_encode(data.getBytes());
    }
}
