package org.activity.rest.security;

import org.springframework.security.authentication.*;
import org.springframework.security.core.*;

import java.util.*;

/**
 * @author tasman
 */
public class AccessKeyAuthenticationToken extends AbstractAuthenticationToken {

    private String accessKey;
    private String subjectId;

    public AccessKeyAuthenticationToken(String accessKey, String subjectId) {
        super(null);
        this.accessKey = accessKey;
        this.subjectId = subjectId;
        setAuthenticated(false);
    }

    public AccessKeyAuthenticationToken(String accessKey, String subjectId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.accessKey = accessKey;
        this.subjectId = subjectId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return subjectId;
    }

    @Override
    public Object getPrincipal() {
        return accessKey;
    }
}
