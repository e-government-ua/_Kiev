package org.wf.dp.dniprorada.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author bw
 */
@Component("generalConfig")
public class GeneralConfig {

    private final static Logger oLog = LoggerFactory.getLogger(GeneralConfig.class);

    
    @Value("${general.sHost}")
    private String sHost; //general.sHost=https://test.region.igov.org.ua    
    
    @Value("${general.sHostCentral}")
    private String sHostCentral; //general.sHost=https://test.igov.org.ua    
    
    @Value("${general.bTest}")
    private String sbTest;
    public static Boolean bTest=null;
    
    
	@Value("${general.auth.login}")
	private String generalUsername;
	@Value("${general.auth.password}")
	private String generalPassword;

    @Value("${general.sURL_DocumentKvitanciiForIgov}")
    private String general_sURL_DocumentKvitanciiForIgov;
    @Value("${general.sURL_DocumentKvitanciiForAccounts}")
    private String general_sURL_DocumentKvitanciiForAccounts;
    @Value("${general.sURL_GenerationSID}")
    private String general_sURL_GenerationSID;
    @Value("${general.sURL_DocumentKvitanciiCallback}")
    private String general_sURL_DocumentKvitanciiCallback;
    @Value("${general.SID_login}")
    private String SID_login;
    @Value("${general.SID_password}")
    private String SID_password;
    
    public String sHost(){
        //general.sHost=https://test-version.region.igov.org.ua    
        return sHost!=null?sHost:"https://test.region.igov.org.ua";
    }
    
    public String sHostCentral(){
        //general.sHost=https://test-version.region.igov.org.ua    
        return sHostCentral!=null?sHostCentral:"https://test.igov.org.ua";
    }
    
    public String sAuthLogin(){
        return generalUsername;
    }
    public String sAuthPassword(){
        return generalPassword;
    }

    public String sURL_DocumentKvitanciiForIgov(){
        return general_sURL_DocumentKvitanciiForIgov;
    }
    public String sURL_DocumentKvitanciiForAccounts(){
        return general_sURL_DocumentKvitanciiForAccounts;
    }
    public String sURL_GenerationSID(){
        return general_sURL_GenerationSID;
    }
    public String sURL_DocumentKvitanciiCallback(){
        return general_sURL_DocumentKvitanciiCallback;
    }
    public String getSID_login() {
        return SID_login;
    }
    public String getSID_password() {
        return SID_password;
    }

    //static public boolean bTest=false;
    public boolean bTest(){
        //return true;
        if (bTest != null) {
            return bTest;
        }
        boolean b = true;
        try {
            
            //Properties oProperties = new Properties();
            //oProperties.load(getClass().getClassLoader().getResourceAsStream("AS.properties"));
            //String sbTest = oProperties.getProperty("general.bTest");
            
            //getProfileProperty("")
            b = (sbTest == null ? b : sbTest.trim().length()>0 ? !"false".equalsIgnoreCase(sbTest.trim()) : true);
            oLog.info("[bTest]:sbTest=" + sbTest);
//            b = true;
            //b = false;
        } catch (Exception oException) {
            oLog.error("[bTest]:sbTest=" + sbTest, oException);
        }
        bTest = b;
        return b;
    }
    
    
    /*public static String getProfileProperty(String sName) throws IOException {
        //String sCase = sCaseDomain("getProfileProperty");
        Properties oProperty = new Properties();
        //try {
            //oProperty.load(new FileInputStream(sPathRoot() + "WEB-INF" + File.separator + "cache" + File.separator + "config.properties"));//getConfDir()
            //oProperty.load(new FileInputStream(sPathConfig() + "Profile.properties"));//getConfDir()
            oProperty.load(new FileInputStream("AS.properties"));//getConfDir()
        //} catch (Exception oException) {
            //com.pb.esc.debug.error.Error.store(oException, sCase, null, TypeMessagePB.GetConfigProperty,
             //       new MsgAttr(DataMessagePB.paramName.name(), sName));
            //loggerStatic.error("[" + sCase + "](sName=" + sName + "):", oException);
        //}
        return oProperty.getProperty(sName);
    }*/
    
}
