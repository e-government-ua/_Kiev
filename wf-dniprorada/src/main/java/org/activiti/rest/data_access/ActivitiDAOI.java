package org.activiti.rest.data_access;

import org.activiti.rest.data_access.entity.InitInfoI;

/**
 * Created by diver on 4/5/15.
 */
public interface ActivitiDAOI {

    /**
     * Получить развернутую информаци по идентификатору сессии
     *
     * @param session Идентификатор сессии
     * @return Развернутая информация о сессии
     * @throws DataAccessException
     */
    InitInfoI getInitInfo(String session) throws DataAccessException;

    /**
     * Записать/Перезаписать развернутую информацию о сессии
     *
     * @param session Идентификатор сессии
     * @param sessionInfo Развернутая информация о сессии
     * @throws DataAccessException
     */
    void putInitInfo(String session, InitInfoI sessionInfo) throws DataAccessException;
}
