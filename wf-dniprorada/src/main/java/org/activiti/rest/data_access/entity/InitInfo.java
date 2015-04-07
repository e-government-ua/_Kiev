package org.activiti.rest.data_access.entity;

/**
 * Created by diver on 4/6/15.
 */
public class InitInfo implements InitInfoI {

    private String accessToken;
    private String refreshToken;
    private ClientInfoI clientInfo;

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public ClientInfoI getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfoI clientInfo) {
        this.clientInfo = clientInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InitInfo initInfo = (InitInfo) o;

        if (accessToken != null ? !accessToken.equals(initInfo.accessToken) : initInfo.accessToken != null)
            return false;
        if (clientInfo != null ? !clientInfo.equals(initInfo.clientInfo) : initInfo.clientInfo != null) return false;
        if (refreshToken != null ? !refreshToken.equals(initInfo.refreshToken) : initInfo.refreshToken != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = accessToken != null ? accessToken.hashCode() : 0;
        result = 31 * result + (refreshToken != null ? refreshToken.hashCode() : 0);
        result = 31 * result + (clientInfo != null ? clientInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InitInfo{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", clientInfo=" + clientInfo +
                '}';
    }
}
