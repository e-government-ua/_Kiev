package org.wf.dp.dniprorada.base.dao;

public interface AccessDataDao { 
    
    public String setAccessData(String content);
    public String setAccessData(byte[] content);
    public byte[] getAccessData(String sKey);
    public void removeAccessData(String sKey);
}
