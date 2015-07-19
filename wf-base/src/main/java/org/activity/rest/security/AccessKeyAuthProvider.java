package org.activity.rest.security;

import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.stereotype.*;
import org.wf.dp.dniprorada.base.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    @Value("${general.auth.custom.persistentKey}")
    private String persistentKey;
    private AccessDataDao accessDataDao;

    private final Logger log = LoggerFactory.getLogger(AccessKeyAuthProvider.class);
    
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

    public void setPersistentKey(String persistentKey) {
        this.persistentKey = persistentKey;
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
        log.info("authentication.getName()="+authentication.getName());
        log.info("authentication.getPrincipal()="+authentication.getPrincipal());
        
        String accessData = accessDataDao.getAccessData(authentication.getName());
        log.info("accessData="+accessData);
        log.info("authentication.getCredentials()="+authentication.getCredentials());
        
        if (accessData == null) {
            log.warn("accessData == null");
            throw new BadAccessKeyCredentialsException("Error custom authorization - key is absent");
        }
        if (!accessData.equals(authentication.getCredentials())) {
            log.warn("!accessData.equals(authentication.getCredentials()");
            throw new BadAccessKeyCredentialsException("Error custom authorization - key data is wrong");
        }
        log.info("persistentKey="+persistentKey);
        if (persistentKey == null || !persistentKey.equals(authentication.getName())) {
            log.info("persistentKey == null || !persistentKey.equals(authentication.getName())");
            accessDataDao.removeAccessData(authentication.getName());
        }else{
            log.warn("Can't remove key");
        }
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
