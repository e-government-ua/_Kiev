package org.wf.dp.dniprorada.util.rest;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by Dmytro Tsapko on 8/23/2015.
 * This class is needed to disable SSL certificate verifying
 * when performing REST client request to to server with untrusted certificate
 * it should be used only in development process or while testing.
 */

public class SSLCertificateValidation {

    public static void disable() {
        try {
            SSLContext sslc = SSLContext.getInstance("TLS");
            TrustManager[] trustManagerArray = { new NullX509TrustManager() };
            sslc.init(null, trustManagerArray, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostnameVerifier());
        } catch (Exception e) {
            e.printStackTrace();//TODO add logging
        }
    }

    private static class NullX509TrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            /*NOP*/
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            /*NOP*/
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class NullHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}
