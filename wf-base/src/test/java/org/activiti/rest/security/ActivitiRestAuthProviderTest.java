package org.activiti.rest.security;

import org.activiti.engine.IdentityService;
import org.activity.rest.security.ActivitiRestAuthProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by diver on 6/26/15.
 */
public class ActivitiRestAuthProviderTest {

    private ActivitiRestAuthProvider provider;
    private Authentication authentication;

    @Before
    public void setUpProvider() {
        provider = new ActivitiRestAuthProvider();
        provider.setGeneralUsername("general_username");
        provider.setGeneralPassword("general_password");
        authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("general_username");
        when(authentication.getCredentials()).thenReturn("general_password");
        IdentityService identityService = mock(IdentityService.class);
        when(identityService.checkPassword("activiti_username", "activiti_password")).thenReturn(Boolean.TRUE);
        provider.setIdentityService(identityService);
    }

    @Test
    public void shouldReturnErrorIfUserNotSpecified() {
        when(authentication.getName()).thenReturn(null);
        try {
            provider.authenticate(authentication);
        } catch (AuthenticationException e) {
            assertEquals("User or password not valid", e.getMessage());
        }
    }

    @Test
    public void shouldReturnErrorIfPasswordNotSpecified() {
        when(authentication.getCredentials()).thenReturn(null);
        try {
            provider.authenticate(authentication);
        } catch (AuthenticationException e) {
            assertEquals("User or password not valid", e.getMessage());
        }
    }

    @Test
    public void shouldSuccessfullyAuthIfRequestContainsGeneralUserAndPassword() {
        assertEquals(ActivitiRestAuthProviderFixtures.getGeneralAuthInfo(), provider.authenticate(authentication));
    }

    @Test
    public void shouldSuccessfullyAuthIfRequestContainsActivitiUserAndPassword() {
        when(authentication.getName()).thenReturn("activiti_username");
        when(authentication.getCredentials()).thenReturn("activiti_password");
        assertEquals(ActivitiRestAuthProviderFixtures.getActivitiAuthInfo(), provider.authenticate(authentication));
    }

    @Test
    public void shouldReturnErrorIfUserAndPasswordNotValidOrDontHaveAnyRoles() {
        when(authentication.getName()).thenReturn("my_username");
        when(authentication.getCredentials()).thenReturn("my_password");
        try {
            provider.authenticate(authentication);
        } catch (AuthenticationException e) {
            assertEquals("User or password not valid", e.getMessage());
        }
    }
}
