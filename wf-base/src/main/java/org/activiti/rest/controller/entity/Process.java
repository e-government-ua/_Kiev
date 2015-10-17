package org.activiti.rest.controller.entity;

/**
 * Created by diver on 4/12/15.
 */
public class Process implements ProcessI {

    private String id;

    public Process() {
    }

    public Process(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Process process = (Process) o;

        if (id != null ? !id.equals(process.id) : process.id != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id='" + id + '\'' +
                '}';
    }
}
