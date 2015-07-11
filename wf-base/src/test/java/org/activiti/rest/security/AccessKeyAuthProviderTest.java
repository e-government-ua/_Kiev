package org.activiti.rest.security;

import org.activity.rest.security.*;
import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;
import org.springframework.security.core.*;
import org.wf.dp.dniprorada.base.dao.*;

import static org.activiti.rest.security.AccessKeyAuthProviderFixtures.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccessKeyAuthProviderTest {
    @Mock
    public AccessDataDao accessDataDao;
    @InjectMocks
    public AccessKeyAuthProvider provider;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldAuthenticateByGeneralCredentials() throws Exception {
        Authentication expected = createAuthenticatedAuthToken();
        provider.setGeneralAccessKey(ACCESS_KEY);
        provider.setGeneralSubjectId(SUBJECT_ID);
        Authentication authentication = createAuthToken();

        Authentication result = provider.authenticate(authentication);

        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getCredentials(), result.getCredentials());
        assertEquals(expected.getAuthorities(), result.getAuthorities());
    }

    @Test
    public void shouldAuthenticateByDaoCredentials() throws Exception {
        Authentication expected = createAuthenticatedAuthToken();
        when(accessDataDao.getAccessData(ACCESS_KEY)).thenReturn(SUBJECT_ID);
        Authentication authentication = createAuthToken();

        Authentication result = provider.authenticate(authentication);

        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getCredentials(), result.getCredentials());
        assertEquals(expected.getAuthorities(), result.getAuthorities());
    }

    @Test
    public void shouldRemoveDaoCredentialsAfterSuccessfulAuthentication() throws Exception {
        when(accessDataDao.getAccessData(ACCESS_KEY)).thenReturn(SUBJECT_ID);
        Authentication authentication = createAuthToken();

        provider.authenticate(authentication);

        verify(accessDataDao).removeAccessData(ACCESS_KEY);
    }

    @Test
    public void shouldNotRemoveDaoCredentialsForPersistentKeyAfterSuccessfulAuthentication() throws Exception {
        when(accessDataDao.getAccessData(ACCESS_KEY)).thenReturn(SUBJECT_ID);
        Authentication authentication = createAuthToken();
        provider.setPersistentKey(ACCESS_KEY);

        provider.authenticate(authentication);

        verify(accessDataDao, times(0)).removeAccessData(ACCESS_KEY);
    }

    @Test
    public void shouldThrowExceptionWhenDaoDoesNotContainTheAccessKey() throws Exception {
        Authentication authentication = createAuthToken();
        thrown.expect(BadAccessKeyCredentialsException.class);
        thrown.expectMessage("Error custom authorization - key is absent");

        provider.authenticate(authentication);
    }

    @Test
    public void shouldThrowExceptionWhenDaoContainsDifferentSubjectId() throws Exception {
        Authentication authentication = createAuthToken();
        when(accessDataDao.getAccessData(ACCESS_KEY)).thenReturn(DIFFERENT_SUBJECT_ID);
        thrown.expect(BadAccessKeyCredentialsException.class);
        thrown.expectMessage("Error custom authorization - key data is wrong");

        provider.authenticate(authentication);
    }
}