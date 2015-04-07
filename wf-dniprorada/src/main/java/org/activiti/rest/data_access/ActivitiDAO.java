package org.activiti.rest.data_access;

import org.activiti.rest.data_access.entity.InitInfoI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

/**
 * Created by diver on 4/5/15.
 */
public final class ActivitiDAO implements ActivitiDAOI {

    private static final String SESSION = "SESSION";

    private RedisTemplate redisTemplate;

    private void throwRuntimeExceptionInParameterNotSpecified(Object parameter, String parameterName) {
        if (parameter == null) {
            throwRuntimeExceptionInParameterNotSpecified(StringUtils.EMPTY, parameterName);
        }
    }

    private void throwRuntimeExceptionInParameterNotSpecified(String parameter, String parameterName) {
        if (StringUtils.isBlank(parameter)) {
            throw new NullPointerException("Incoming parameter '" + parameterName + "' not specified");
        }
    }

    @Override
    public InitInfoI getInitInfo(String session) throws DataAccessException {
        throwRuntimeExceptionInParameterNotSpecified(session, SESSION);
        try {
            if (redisTemplate.hasKey(session)) {
                Map<String, String> initInfoData = redisTemplate.boundHashOps(session).entries();
                InitInfoBuilder initInfoBuilder = InitInfoBuilder.create()
                        .addAccessToken(initInfoData.get("STAccessToken"))
                        .addRefreshToken(initInfoData.get("STRefreshToken"));
                return initInfoBuilder.build();
            } else {
                throw new DataAccessException(DataAccessException.ErrorCode.DB_ERR_B_0001, "Session not found");
            }
        } catch (RuntimeException ex) {
            throw new DataAccessException(DataAccessException.ErrorCode.DB_ERR_S_0001, ex.getMessage());
        }
    }

    @Override
    public void putInitInfo(String session, InitInfoI sessionInfo) throws DataAccessException {

    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

}
