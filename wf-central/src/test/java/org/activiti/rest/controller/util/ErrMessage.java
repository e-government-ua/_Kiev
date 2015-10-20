package org.activiti.rest.controller.util;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author dgroup
 * @since 30.06.15
 */
public class ErrMessage {

    @JsonProperty
    private String code;

    @JsonProperty
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}