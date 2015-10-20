package org.activiti.rest.controller.entity;

/**
 * Created by diver on 4/6/15.
 */
public class ErrorResponse implements ErrorResponseI {

    private String code;
    private String message;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ErrorResponse that = (ErrorResponse) o;

        if (code != null ? !code.equals(that.code) : that.code != null)
            return false;
        if (message != null ? !message.equals(that.message) : that.message != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
