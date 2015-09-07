package org.activiti.rest.controller;

import org.activiti.rest.controller.entity.ErrorResponse;
import org.activiti.rest.controller.entity.ErrorResponseI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.wf.dp.dniprorada.base.util.JsonRestUtils;

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
    public @ResponseBody
    ResponseEntity<String> catchActivitiRestException(ActivitiRestException exception) {
        LOGGER.error("REST API Exception: " + exception.getMessage(), exception);
        return JsonRestUtils.toJsonResponse(exception.getHttpStatus(),
                new ErrorResponse(exception.getErrorCode(), exception.getMessage()));
    }

    @ExceptionHandler(value = RuntimeException.class)
    public @ResponseBody ResponseEntity<String> catchRuntimeException(RuntimeException exception) {
        LOGGER.error("REST System Exception: " + exception.getMessage(), exception);
        return JsonRestUtils.toJsonResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                new ErrorResponse(SYSTEM_ERROR_CODE, exception.getMessage()));
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public @ResponseBody ResponseEntity<String> catchMissingServletRequestParameterException(
            MissingServletRequestParameterException exception) {
        LOGGER.error("REST Wrong Input Parameters Exception: " + exception.getMessage(), exception);
        return JsonRestUtils.toJsonResponse(HttpStatus.BAD_REQUEST,
                new ErrorResponse(BUSINESS_ERROR_CODE, exception.getMessage()));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public @ResponseBody ResponseEntity<String> catchHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        LOGGER.error("REST Wrong Input Body Exception: " + exception.getMessage(), exception);
        return JsonRestUtils.toJsonResponse(HttpStatus.BAD_REQUEST,
                new ErrorResponse(BUSINESS_ERROR_CODE, exception.getMessage()));
    }
}
