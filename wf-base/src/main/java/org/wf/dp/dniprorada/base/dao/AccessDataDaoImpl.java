package org.wf.dp.dniprorada.base.dao;

import org.springframework.beans.factory.annotation.Autowired;
import ua.org.egov.utils.storage.durable.impl.GridFSBytesDataStorage;
import org.wf.dp.dniprorada.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessDataDaoImpl implements AccessDataDao {

    private static final String contentMock = "No content!!!";
    private final Logger log = LoggerFactory.getLogger(AccessKeyAuthProvider.class);

    @Autowired
    private GridFSBytesDataStorage durableBytesDataStorage;

    @Override
    public String setAccessData(String sContent) {
        log.info("[setAccessData]:sContent="+sContent);
        String sKey=durableBytesDataStorage.saveData(Util.contentStringToByte(sContent));
        log.info("[setAccessData]:sKey="+sKey);
        return sKey;
    }

    @Override
    public String setAccessData(byte[] aContent) {
        log.info("[setAccessData]:aContent="+aContent.toString());
        String sKey=durableBytesDataStorage.saveData(aContent);
        log.info("[setAccessData]:sKey="+sKey);
        return sKey;
    }

    @Override
    public String getAccessData(String sKey) {
        byte[] contentByte = durableBytesDataStorage.getData(sKey);
        return contentByte != null ? Util.contentByteToString(contentByte) : contentMock;
    }

    @Override
    public boolean removeAccessData(String sKey) {
        log.info("[removeAccessData]:sKey="+sKey);
        return durableBytesDataStorage.remove(sKey);
    }

}
