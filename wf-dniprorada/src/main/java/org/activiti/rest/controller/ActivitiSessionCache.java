package org.activiti.rest.controller;

import net.jodah.expiringmap.ExpiringMap;
import org.activiti.rest.data_access.ActivitiDAOI;
import org.activiti.rest.data_access.entity.InitInfoI;

import java.util.concurrent.TimeUnit;

/**
 * Created by diver on 4/6/15.
 */
public class ActivitiSessionCache implements ExpiringMap.ExpirationListener<String, InitInfoI>, ExpiringMap.EntryLoader<String, InitInfoI> {

    private ExpiringMap<String, InitInfoI> sessionInfoExpiringMap;
    private ActivitiDAOI sessionInfoDAO;
    private long expiration = 3;

    public ActivitiSessionCache() {
        sessionInfoExpiringMap = ExpiringMap.builder().
                expiration(expiration, TimeUnit.MINUTES).
                expirationListener(this).
                entryLoader(this).
                build();
    }

    @Override
    public InitInfoI load(String session) {
        //Ged and refresh data from database
        return null;
    }

    @Override
    public void expired(String session, InitInfoI sessionInfo) {
        //Refresh token
    }

    public void put(String session, InitInfoI sessionInfo) {
        sessionInfoExpiringMap.put(session, sessionInfo);
    }

    public InitInfoI get(String session) {
        return sessionInfoExpiringMap.get(session);
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public void setSessionInfoDAO(ActivitiDAOI sessionInfoDAO) {
        this.sessionInfoDAO = sessionInfoDAO;
    }
}
