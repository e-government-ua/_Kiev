package org.activiti.rest.controller.entity;

/**
 * Created by diver on 4/20/15.
 */
public class LoginResponse implements LoginResponseI {

    private String session;

    public LoginResponse() {
    }

    public LoginResponse(String session) {
        this.session = session;
    }

    @Override
    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        LoginResponse that = (LoginResponse) o;

        if (session != null ? !session.equals(that.session) : that.session != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return session != null ? session.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "session='" + session + '\'' +
                '}';
    }
}
