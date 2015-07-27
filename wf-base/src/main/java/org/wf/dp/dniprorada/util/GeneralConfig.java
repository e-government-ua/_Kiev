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
    
    
    public String sHost(){
        //general.sHost=https://test-version.region.igov.org.ua    
        return sHost!=null?sHost:"https://test.region.igov.org.ua";
    }
    
    public String sHostCentral(){
        //general.sHost=https://test-version.region.igov.org.ua    
        return sHostCentral!=null?sHostCentral:"https://test.igov.org.ua";
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
