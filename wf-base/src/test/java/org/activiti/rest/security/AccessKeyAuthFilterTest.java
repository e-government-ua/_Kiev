package org.activiti.rest.security;

import org.activity.rest.security.AccessKeyAuthFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * Created by diver on 8/24/15.
 */
public class AccessKeyAuthFilterTest {

	private HttpServletRequest request;
	private FilterChain filterChain;

	@Before
	public void setUp() {
		filterChain = mock(FilterChain.class);
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

	@After
	public void setDown() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	@Test
	public void shouldInitializeAuthenticationContextWithAccessContentToken() throws IOException, ServletException {
		AccessKeyAuthFilter filter = new AccessKeyAuthFilter();
		filter.doFilter(request, null, filterChain);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		assertEquals("654321", authentication.getName());
		assertEquals("654321", authentication.getPrincipal());
		assertEquals("/root/servlet/path?nID_Subject=123456&Data",
				authentication.getCredentials());

		verify(filterChain).doFilter(request, null);
	}

	@Test
	public void shouldInitializeAuthenticationContextWithSubjectIdToken() throws IOException, ServletException {
		when(request.getParameter("sAccessContract")).thenReturn(null);
		AccessKeyAuthFilter filter = new AccessKeyAuthFilter();
		filter.doFilter(request, null, filterChain);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		assertEquals("654321", authentication.getName());
		assertEquals("654321", authentication.getPrincipal());
		assertEquals("123456",
				authentication.getCredentials());

		verify(filterChain).doFilter(request, null);
	}

	@Test
	public void shouldSkipContextInitializationIfNoSecureParameterSpecified() throws IOException, ServletException {
		request = mock(HttpServletRequest.class);
		AccessKeyAuthFilter filter = new AccessKeyAuthFilter();
		filter.doFilter(request, null, filterChain);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		assertNull(authentication);

		verify(filterChain).doFilter(request, null);
	}
}
