package ua.org.egov.service.model;

import java.io.Serializable;

public class PublicCitizenInfo implements Serializable {

    private String name;

    private String lastName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
