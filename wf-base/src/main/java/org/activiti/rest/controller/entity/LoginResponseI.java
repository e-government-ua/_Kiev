package org.activiti.rest.controller.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by diver on 4/20/15.
 */
@JsonSerialize(as = LoginResponse.class)
@JsonDeserialize(as = LoginResponse.class)
public interface LoginResponseI {

    /**
     * @return Сессиф клиента
     */
    String getSession();
}
