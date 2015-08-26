package org.activiti.rest.security;

import org.activity.rest.security.AccessKeyAuthenticationToken;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by diver on 8/24/15.
 */
public class AccessKeyAuthenticationTokenTest {

	@Test
	public void shouldReturnFalseOnHasCorrectAccessKeyAndSubjectIdMethodIfAccessKeyNotSpecified() {
		AccessKeyAuthenticationToken token = new AccessKeyAuthenticationToken(null, "subject_id");

		assertFalse(token.hasCorrectAccessKeyAndSubjectId());
	}

	@Test
	public void shouldReturnFalseOnHasCorrectAccessKeyAndSubjectIdMethodIfSubjectIdNotSpecified() {
		AccessKeyAuthenticationToken token = new AccessKeyAuthenticationToken("access_key", null);

		assertFalse(token.hasCorrectAccessKeyAndSubjectId());
	}

	@Test
	public void shouldReturnFalseOnIsEmptyMethodIfSubjectIdAndAccessKeySpecified() {
		AccessKeyAuthenticationToken token = new AccessKeyAuthenticationToken("access_key", "subject_id");

		assertTrue(token.hasCorrectAccessKeyAndSubjectId());
		assertEquals("subject_id", token.getCredentials());
		assertEquals("access_key", token.getPrincipal());
	}
}
