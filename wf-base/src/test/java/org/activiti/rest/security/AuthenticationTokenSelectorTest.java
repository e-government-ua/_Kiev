package org.activiti.rest.security;

import org.activity.rest.security.AccessKeyAuthenticationToken;
import org.activity.rest.security.AuthenticationTokenSelector;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by diver on 8/24/15.
 */
public class AuthenticationTokenSelectorTest {

    private HttpServletRequest request;

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        when(request.getParameter("sAccessContract")).thenReturn("Request");
        when(request.getParameter("nID_Subject")).thenReturn("123456");
        when(request.getParameter("sAccessKey")).thenReturn("654321");
        when(request.getParameter("Data")).thenReturn(null);
        when(request.getContextPath()).thenReturn("/root");
        when(request.getServletPath()).thenReturn("/servlet");
        when(request.getPathInfo()).thenReturn("/path");
        /* REQUEST PARAMETERS */
        Vector<String> names = new Vector<>();
        names.add("sAccessContract");
        names.add("nID_Subject");
        names.add("Data");
        names.add("sAccessKey");
        when(request.getParameterNames()).thenReturn(names.elements());
    }

    @Test
    public void shouldCreateAccessContentTokenIfParameter_sAccessContract_ContainsValue_Request() {
        AuthenticationTokenSelector selector = new AuthenticationTokenSelector(request);
        AccessKeyAuthenticationToken token = selector.createToken();

        String expected = "/root/servlet/path?nID_Subject=123456&Data";

        assertEquals("654321", token.getPrincipal());
        assertEquals(expected, token.getCredentials());
    }

    @Test
    public void shouldCreateSubjectIdTokenIfRequestParameter_sAccessContract_DoesNotSpecified() {
        when(request.getParameter("sAccessContract")).thenReturn(null);

        AuthenticationTokenSelector selector = new AuthenticationTokenSelector(request);
        AccessKeyAuthenticationToken token = selector.createToken();

        assertEquals("654321", token.getPrincipal());
        assertEquals("123456", token.getCredentials());
    }

    @Test
    public void shouldCreateSubjectIdTokenWithNullValuesIfRequestDoesNotContainsAnyParameters() {
        request = mock(HttpServletRequest.class);

        AuthenticationTokenSelector selector = new AuthenticationTokenSelector(request);
        AccessKeyAuthenticationToken token = selector.createToken();

        assertEquals(null, token.getPrincipal());
        assertEquals(null, token.getCredentials());
    }
}
