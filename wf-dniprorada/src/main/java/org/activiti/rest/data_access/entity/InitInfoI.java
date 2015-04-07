package org.activiti.rest.data_access.entity;

/**
 * Created by diver on 4/5/15.
 */
public interface InitInfoI {

    /**
     *
     * @return Токен доступа к BankID
     */
    String getAccessToken();

    /**
     *
     * @return Токен оббновления токена доступа к BankID
     */
    String getRefreshToken();

    /**
     *
     * @return Информаци о клиенте
     */
    ClientInfoI getClientInfo();
}