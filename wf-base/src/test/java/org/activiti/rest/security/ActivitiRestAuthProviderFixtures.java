package org.activiti.rest.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diver on 6/26/15.
 */
public class ActivitiRestAuthProviderFixtures {

    private ActivitiRestAuthProviderFixtures() {
    }

    public static Authentication getGeneralAuthInfo() {
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken("general_username", "general_password", grantedAuths);
    }

    public static Authentication getActivitiAuthInfo() {
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken("activiti_username", "activiti_password", grantedAuths);
    }
}
