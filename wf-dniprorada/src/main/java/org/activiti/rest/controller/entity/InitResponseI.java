package org.activiti.rest.controller.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by diver on 4/6/15.
 */
@JsonSerialize(as = InitResponse.class)
@JsonDeserialize(as = InitResponse.class)
public interface InitResponseI {

    /**
     *
     * @return Идентификатор сессии Activiti Rest API
     */
    String getSessionId();
}
