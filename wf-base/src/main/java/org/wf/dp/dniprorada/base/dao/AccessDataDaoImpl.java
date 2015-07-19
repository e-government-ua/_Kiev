package org.wf.dp.dniprorada.base.dao;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import ua.org.egov.utils.storage.durable.impl.GridFSBytesDataStorage;
import org.wf.dp.dniprorada.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessDataDaoImpl implements AccessDataDao {

    private static final String contentMock = "No content!!!";
    private final static Logger log = LoggerFactory.getLogger(AccessDataDaoImpl.class);

    @Autowired
    private GridFSBytesDataStorage durableBytesDataStorage;

    @Override
    public String setAccessData(String sContent) {
        log.info("[setAccessData]:sContent="+sContent);
        //String sKey=durableBytesDataStorage.saveData(Util.contentStringToByte(sContent));
        //String sKey=durableBytesDataStorage.saveData(sContent.getBytes());
        String sKey=durableBytesDataStorage.saveData(aByte(sContent));
        log.info("[setAccessData]:sKey="+sKey);
        log.info("[setAccessData]:sData(check)="+getAccessData(sKey));
        return sKey;
    } 

    @Override
    public String setAccessData(byte[] aContent) {
        //log.info("[setAccessData]:sContent="+(aContent==null?"null":Util.contentByteToString(aContent)));
        log.info("[setAccessData]:sContent="+(aContent==null?"null":Arrays.toString(aContent))+",sByte(aContent)="+sByte(aContent));
        String sKey=durableBytesDataStorage.saveData(aContent);
        log.info("[setAccessData]:sKey="+sKey);
        return sKey;
    }
    
    public static String sByte(byte[] a){
        //Charset.forName("UTF-8")
        //byte[] b = {(byte) 99, (byte)97, (byte)116};
        String s = "Not convertable!";
        log.info("[sByte]:a.length="+a.length+",Arrays.toString(a)="+Arrays.toString(a));
        try{
            s = new String(a, "UTF-8");
        }catch(Exception oException){
            log.error("[sByte]",oException);
        }
        log.info("[sByte]:s="+s);
        return s;
    }
    public static byte[] aByte(String s){
        log.info("[aByte]:s="+s);
        byte[] a = s.getBytes(Charset.forName("UTF-8"));
        log.info("[aByte]:a.length="+a.length+",Arrays.toString(a)="+Arrays.toString(a));
        return a;
    }
    

    @Override
    public String getAccessData(String sKey) {
        byte[] aContent = durableBytesDataStorage.getData(sKey);
        //return aContent != null ? Util.contentByteToString(aContent) : contentMock;
        String sData = contentMock;
        if(aContent != null){
            log.info("[getAccessData]:sKey="+sKey+",aContent.length()="+aContent.length);
            //sData = Util.contentByteToString(aContent);
            //sData = Arrays.toString(aContent);
            sData = sByte(aContent);
            //log.info("[getAccessData]:TEST:sKey="+sKey+",Arrays.toString(aContent)="+Arrays.toString(aContent));
            if(sData!=null){
                log.info("[getAccessData]:sKey="+sKey+",sData.length()="+sData.length());
            }
        }
        log.info("[getAccessData]:sKey="+sKey+",sData="+sData);
        return sData;
    }

    @Override
    public boolean removeAccessData(String sKey) {
        log.info("[removeAccessData]:sKey="+sKey);
        return durableBytesDataStorage.remove(sKey);
    }

}
