package org.activiti.rest.data_access;

import org.activiti.rest.controller.ActivitiRestException;

/**
 * Created by diver on 4/5/15.
 */
public class DataAccessException extends ActivitiRestException {

    public enum ErrorCode {
        DB_ERR_B_0001,//Session not found in remote store
        DB_ERR_S_0001//Can't access to remote store
    }
    
    private ErrorCode errorCode;

    public DataAccessException(ErrorCode errorCode, String message) {
        super(errorCode.name(), message);
    }

    public DataAccessException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode.name(), throwable);
    }

    public DataAccessException(ErrorCode errorCode, String message, Throwable throwable) {
        super(errorCode.name(), message, throwable);
    }

    @Override
    public String toString() {
        return getErrorCode() + ": " + super.toString();
    }
}
