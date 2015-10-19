package org.activiti.rest.security;

import org.activity.rest.security.AccessKeyAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

/**
 * @author tasman
 */
public class AccessKeyAuthProviderFixtures {
    public static final String ACCESS_KEY = "test_access_key";
    public static final String ACCESS_DATA = "test_access_data";
    public static final String ACCESS_LOGIN_DEFAULT = "test_access_login_default";
    public static final String DIFFERENT_ACCESS_DATA = "different_subject_id";
    public static final String GENERAL_ROLE = "ROLE_USER";

    public static Authentication createAuthToken() {
        return new AccessKeyAuthenticationToken(ACCESS_KEY, ACCESS_DATA);
    }

    public static Authentication createAuthenticatedAuthToken() {
        return new AccessKeyAuthenticationToken(ACCESS_KEY, ACCESS_DATA,
                Collections.<GrantedAuthority>singletonList(new SimpleGrantedAuthority(GENERAL_ROLE)));
    }
}
