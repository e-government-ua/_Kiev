package org.wf.dp.dniprorada.util;

import java.io.Serializable;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author dgroup
 * @since  23.07.2015
 */
public class Parameter {

    private String name;
    private Serializable value;
    private String errorMsg;

    public Parameter() {
        // nothing required here
    }

    public Parameter(String name, Serializable value) {
        setName(name);
        setValue(value);
    }

    public Parameter(String name, Serializable value, String errorMsg) {
        this(name, value);
        setErrorMsg(errorMsg);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (isBlank(name))
            throw new IllegalArgumentException("'Name' can't be empty or null");
        this.name = name;
    }

    public Serializable getValue() {
        return value;
    }
    public void setValue(Serializable value) {
        Objects.requireNonNull(value, "'Value' can't be a null");
        this.value = value;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        if (isBlank(errorMsg))
            throw new IllegalArgumentException("Error message can't be empty or null");
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}