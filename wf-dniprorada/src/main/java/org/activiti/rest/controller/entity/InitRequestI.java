package org.activiti.rest.controller.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by diver on 4/6/15.
 */
@JsonSerialize(as = InitRequest.class)
@JsonDeserialize(as = InitRequest.class)
public interface InitRequestI {

    /**
     *
     * @return Токен доступа к BankID
     */
    //TODO diver: Add validation
    String getAccessToken();

    /**
     *
     * @return Токен оббновления токена доступа к BankID
     */
    //TODO diver: Add validation
    String getRefreshToken();
}
