package org.wf.dp.dniprorada.base.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.util.Util;
import ua.org.egov.utils.storage.durable.impl.GridFSBytesDataStorage;

import java.util.Arrays;

@Repository
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
        String sKey=durableBytesDataStorage.saveData(Util.aData(sContent));
        log.info("[setAccessData]:sKey="+sKey);
        //log.info("[setAccessData]:sData(check)="+getAccessData(sKey));
        return sKey;
    } 

    @Override
    public String setAccessData(byte[] aContent) {
        //log.info("[setAccessData]:sContent="+(aContent==null?"null":Util.contentByteToString(aContent)));
        log.info("[setAccessData]:sContent="+(aContent==null?"null":Arrays.toString(aContent))+",sByte(aContent)="+Util.sData(aContent));
        String sKey=durableBytesDataStorage.saveData(aContent);
        log.info("[setAccessData]:sKey="+sKey);
        return sKey;
    }
    

    

    @Override
    public String getAccessData(String sKey) {
        byte[] aContent = durableBytesDataStorage.getData(sKey);
        //return aContent != null ? Util.contentByteToString(aContent) : contentMock;
        String sData = contentMock;
        if(aContent != null){
//            log.info("[getAccessData]:sKey="+sKey+",aContent.length()="+aContent.length);
            //sData = Util.contentByteToString(aContent);
            //sData = Arrays.toString(aContent);
            sData = Util.sData(aContent);
            //log.info("[getAccessData]:TEST:sKey="+sKey+",Arrays.toString(aContent)="+Arrays.toString(aContent));
            /*if(sData!=null){
                log.info("[getAccessData]:sKey="+sKey+",sData.length()="+sData.length());
            }*/
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
