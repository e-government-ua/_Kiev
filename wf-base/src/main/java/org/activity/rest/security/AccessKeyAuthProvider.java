package org.activity.rest.security;

import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.stereotype.*;
import org.wf.dp.dniprorada.base.dao.*;

import java.util.*;

/**
 * @author tasman
 */
@Component
public class AccessKeyAuthProvider implements AuthenticationProvider {

    private static final String GENERAL_ROLE = "ROLE_USER";
    @Value("${general.auth.custom.accessKey}")
    private String generalAccessKey;
    @Value("${general.auth.custom.subjectId}")
    private String generalSubjectId;
    private AccessDataDao accessDataDao;

    @Autowired
    public AccessKeyAuthProvider(AccessDataDao accessDataDao) {
        this.accessDataDao = accessDataDao;
    }

    public void setGeneralAccessKey(String generalAccessKey) {
        this.generalAccessKey = generalAccessKey;
    }

    public void setGeneralSubjectId(String generalSubjectId) {
        this.generalSubjectId = generalSubjectId;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (generalCredentialsExists() &&
                authenticatedByGeneralCredentials(authentication)) {
            return authenticatedToken(authentication);
        }
        authenticateByAccessKey(authentication);
        return authenticatedToken(authentication);
    }

    private void authenticateByAccessKey(Authentication authentication) {
        String accessData = accessDataDao.getAccessData(authentication.getName());
        if (accessData == null) {
            throw new BadAccessKeyCredentialsException("Error custom authorization - key is absent");
        }
        if (!accessData.equals(authentication.getCredentials())) {
            throw new BadAccessKeyCredentialsException("Error custom authorization - key data is wrong");
        }
        accessDataDao.removeAccessData(authentication.getName());
    }

    private boolean authenticatedByGeneralCredentials(Authentication authentication) {
        return generalAccessKey.equals(authentication.getName()) &&
                generalSubjectId.equals(authentication.getCredentials());
    }

    private boolean generalCredentialsExists() {
        return StringUtils.isNotBlank(generalAccessKey) &&
                StringUtils.isNotBlank(generalSubjectId);
    }

    private Authentication authenticatedToken(Authentication authentication) {
        return new AccessKeyAuthenticationToken(authentication.getName(),
                authentication.getCredentials().toString(),
                Arrays.asList(new SimpleGrantedAuthority(GENERAL_ROLE)));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AccessKeyAuthenticationToken.class.equals(authentication);
    }
}
