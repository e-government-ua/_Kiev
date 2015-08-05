package org.activity.rest.security;

import org.springframework.security.authentication.*;
import org.springframework.security.core.*;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tasman
 */
public class AccessKeyAuthenticationToken extends AbstractAuthenticationToken {

    private String accessKey;
    private String subjectId;
    private final Logger log = LoggerFactory.getLogger(AccessKeyAuthenticationToken.class);

    public AccessKeyAuthenticationToken(String accessKey, String subjectId) {
        super(null);
        this.accessKey = accessKey;
        this.subjectId = subjectId;
        log.info("[AccessKeyAuthenticationToken]:accessKey="+accessKey+",subjectId="+subjectId);
        setAuthenticated(false);
    }

    public AccessKeyAuthenticationToken(String accessKey, String subjectId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.accessKey = accessKey;
        this.subjectId = subjectId;
        log.info("[AccessKeyAuthenticationToken]:accessKey="+accessKey+",subjectId="+subjectId+",authorities!=null:"+(authorities!=null));
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        log.info("[getCredentials]:subjectId="+subjectId);
        return subjectId;
    }

    @Override
    public Object getPrincipal() {
        log.info("[getPrincipal]:accessKey="+accessKey);
        return accessKey;
    }
}
