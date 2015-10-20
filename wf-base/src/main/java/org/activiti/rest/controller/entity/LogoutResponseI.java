package org.activiti.rest.controller.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by diver on 4/20/15.
 */
@JsonSerialize(as = LogoutResponseI.class)
@JsonDeserialize(as = LogoutResponseI.class)
public interface LogoutResponseI {

    /**
     * @return Сессия пользователя
     */
    String getSession();
}
