package org.activiti.rest.controller;

import org.activiti.rest.controller.entity.ErrorResponse;
import org.activiti.rest.controller.entity.ErrorResponseI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by diver on 4/6/15.
 */
@Controller
@ControllerAdvice
public class ActivitiExceptionController {

    private static final String SYSTEM_ERROR_CODE = "API_ERR_S_0000";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ActivitiRestException.class)
    public @ResponseBody ErrorResponseI catchActivitiRestException(ActivitiRestException exception) {
        return new ErrorResponse(exception.getErrorCode(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public @ResponseBody ErrorResponseI catchRuntimeException(RuntimeException exception) {
        return new ErrorResponse(SYSTEM_ERROR_CODE, exception.getMessage());
    }
}
