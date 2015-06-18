package org.wf.dp.dniprorada.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;



import java.io.InputStream;
import java.util.List;

import javax.activation.DataSource;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author bw
 */
@Component("generalConfig")
public class GeneralConfig {

    private final static Logger oLog = LoggerFactory.getLogger(GeneralConfig.class);

    /*@Autowired
    TaskService taskService;*/

    @Value("${general.bTest}")
    private String sbTest;

    public static Boolean bTest=null;

    //static public boolean bTest=false;
    public boolean bTest(){
        //return true;
        if (bTest != null) {
            return bTest;
        }
        boolean b = true;
        try {
            //getProfileProperty("")
            b = (sbTest == null ? b : sbTest.trim().length()>0 ? !"false".equalsIgnoreCase(sbTest.trim()) : true);
            oLog.info("[bTest]:sbTest=" + sbTest);
            b = true;
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
