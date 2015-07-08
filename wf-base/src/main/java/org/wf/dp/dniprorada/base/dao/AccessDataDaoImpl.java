package org.wf.dp.dniprorada.base.dao;

import org.springframework.beans.factory.annotation.Autowired;
import ua.org.egov.utils.storage.durable.impl.GridFSBytesDataStorage;
import org.wf.dp.dniprorada.util.Util;

public class AccessDataDaoImpl implements AccessDataDao{
    
    private static final String contentMock = "No content!!!";

    @Autowired
    private GridFSBytesDataStorage durableBytesDataStorage;

    @Override
    public String setAccessData(String sContent) {
        return durableBytesDataStorage.saveData(Util.contentStringToByte(sContent));
    }

    @Override
    public String setAccessData(byte[] aContent) {
        return durableBytesDataStorage.saveData(aContent);
    }

    @Override
    public String getAccessData(String sKey) {
        byte[] contentByte = durableBytesDataStorage.getData(sKey);
		return contentByte != null ? Util.contentByteToString(contentByte) : contentMock;
    }

    @Override
    public boolean removeAccessData(String sKey) {
        return durableBytesDataStorage.remove(sKey);
    }
    
}
