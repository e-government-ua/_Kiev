package org.activiti.rest.controller.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by diver on 4/6/15.
 */
@JsonSerialize(as = ErrorResponse.class)
@JsonDeserialize(as = ErrorResponse.class)
public interface ErrorResponseI {

    /**
     * @return Код ошибки
     */
    String getCode();

    /**
     * @return Сообщение об ошибке
     */
    String getMessage();
}
