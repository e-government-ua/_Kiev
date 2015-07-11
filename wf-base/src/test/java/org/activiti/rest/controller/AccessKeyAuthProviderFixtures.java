package org.activiti.rest.controller;

import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;

import java.util.*;

/**
 * @author tasman
 */
public class AccessKeyAuthProviderFixtures {
    public static final String ACCESS_KEY = "test_access_key";
    public static final String SUBJECT_ID = "test_subject_id";
    public static final String DIFFERENT_SUBJECT_ID = "different_subject_id";
    public static final String GENERAL_ROLE = "ROLE_USER";

    public static Authentication createAuthToken() {
        return new AccessKeyAuthenticationToken(ACCESS_KEY, SUBJECT_ID);
    }

    public static Authentication createAuthenticatedAuthToken() {
        return new AccessKeyAuthenticationToken(ACCESS_KEY, SUBJECT_ID,
                Collections.<GrantedAuthority>singletonList(new SimpleGrantedAuthority(GENERAL_ROLE)));
    }
}
