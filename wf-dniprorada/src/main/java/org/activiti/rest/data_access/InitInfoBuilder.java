package org.activiti.rest.data_access;

import org.activiti.rest.data_access.entity.InitInfo;
import org.activiti.rest.data_access.entity.InitInfoI;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by diver on 4/6/15.
 */
final class InitInfoBuilder {

    private InitInfo initInfo;

    private InitInfoBuilder() {
        initInfo = new InitInfo();
    }

    public InitInfoBuilder addAccessToken(String accessToken) {
        initInfo.setAccessToken(accessToken);
        return this;
    }

    public static InitInfoBuilder create() {
        return new InitInfoBuilder();
    }

    private void throwRuntimeExceptionIfMandatoryParameterNotSpecified(String parameter, String parameterName) {
        if (StringUtils.isBlank(parameter)) {
            throw new NullPointerException("Can't build init info object without parameter: '" + parameterName + "'");
        }
    }

    public InitInfoI build() {
        throwRuntimeExceptionIfMandatoryParameterNotSpecified(initInfo.getAccessToken(), "Access Token");
        return initInfo;
    }

    public InitInfoBuilder addRefreshToken(String refreshToken) {
        initInfo.setRefreshToken(refreshToken);
        return this;
    }
}
