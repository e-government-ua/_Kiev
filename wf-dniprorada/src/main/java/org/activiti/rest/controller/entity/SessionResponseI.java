package org.activiti.rest.controller.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by diver on 4/6/15.
 */
@JsonSerialize(as = SessionResponse.class)
@JsonDeserialize(as = SessionResponse.class)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public interface SessionResponseI {

    /**
     *
     * @return Идентификатор сессии Activiti Rest API
     */
    String getSessionId();
}
