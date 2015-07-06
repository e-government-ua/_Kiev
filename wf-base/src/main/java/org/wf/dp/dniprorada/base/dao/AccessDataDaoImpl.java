package org.wf.dp.dniprorada.base.dao;

import org.springframework.beans.factory.annotation.Autowired;

import ua.org.egov.utils.storage.durable.impl.GridFSBytesDataStorage;

public class AccessDataDaoImpl implements AccessDataDao{
    
    private static final String contentMock = "No content!!!";

    @Autowired
    private GridFSBytesDataStorage durableBytesDataStorage;

    @Override
    public String setAccessData(String sContent) {
        return durableBytesDataStorage.saveData(sContent.getBytes());
    }

    @Override
    public String setAccessData(byte[] aContent) {
        return durableBytesDataStorage.saveData(aContent);
    }

    @Override
    public byte[] getAccessData(String sKey) {
        byte[] contentByte = durableBytesDataStorage.getData(sKey);
		return contentByte != null ? contentByte : contentMock.getBytes();
    }

    @Override
    public void removeAccessData(String sKey) {
        durableBytesDataStorage.remove(sKey);
    }
    
}
