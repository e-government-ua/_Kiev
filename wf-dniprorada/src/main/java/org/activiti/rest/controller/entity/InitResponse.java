package org.activiti.rest.controller.entity;

/**
 * Created by diver on 4/6/15.
 */
public class InitResponse implements InitResponseI {

    private String session;

    public InitResponse(String session) {
        this.session = session;
    }

    @Override
    public String getSessionId() {
        return session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InitResponse that = (InitResponse) o;

        if (session != null ? !session.equals(that.session) : that.session != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return session != null ? session.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "InitResponse{" +
                "session='" + session + '\'' +
                '}';
    }
}
