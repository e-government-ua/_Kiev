package org.wf.dp.dniprorada.model;

public class OtpPass {
    private String password;

    public OtpPass(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "password:" + password;
    }
}
