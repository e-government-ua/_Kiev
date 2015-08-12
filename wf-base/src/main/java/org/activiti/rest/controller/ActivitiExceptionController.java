package org.activiti.rest.controller;

import org.activiti.rest.controller.entity.ErrorResponse;
import org.activiti.rest.controller.entity.ErrorResponseI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by diver on 4/6/15.
 */
@Controller
@ControllerAdvice
public class ActivitiExceptionController {

    public static final String SYSTEM_ERROR_CODE = "SYSTEM_ERR";
    public static final String BUSINESS_ERROR_CODE = "BUSINESS_ERR";
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiExceptionController.class);

    @ExceptionHandler(value = ActivitiRestException.class)
    public @ResponseBody ErrorResponseI catchActivitiRestException(ActivitiRestException exception, HttpServletResponse response) {
        LOGGER.error("REST API Exception: " + exception.getMessage(), exception);
        response.setStatus(exception.getHttpStatus().value());
        return new ErrorResponse(exception.getErrorCode(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = RuntimeException.class)
    public @ResponseBody ErrorResponseI catchRuntimeException(RuntimeException exception) {
        LOGGER.error("REST System Exception: " + exception.getMessage(), exception);
        return new ErrorResponse(SYSTEM_ERROR_CODE, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public @ResponseBody ErrorResponseI catchMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        LOGGER.error("REST Wrong Input Parameters Exception: " + exception.getMessage(), exception);
        return new ErrorResponse(BUSINESS_ERROR_CODE, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public @ResponseBody ErrorResponseI catchHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        LOGGER.error("REST Wrong Input Body Exception: " + exception.getMessage(), exception);
        return new ErrorResponse(BUSINESS_ERROR_CODE, exception.getMessage());
    }
}
