package org.activiti.rest.security;

import org.activity.rest.security.AccessKeyAuthProvider;
import org.activity.rest.security.BadAccessKeyCredentialsException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.wf.dp.dniprorada.base.dao.AccessDataDao;

import static org.activiti.rest.security.AccessKeyAuthProviderFixtures.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccessKeyAuthProviderTest {
    @Mock
    public AccessDataDao accessDataDao;
    @InjectMocks
    public AccessKeyAuthProvider oAccessKeyAuthProvider;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /*@Test
    public void shouldAuthenticateByGeneralCredentials() throws Exception {
        Authentication expected = createAuthenticatedAuthToken();
        //provider.setGeneralAccessKey(ACCESS_KEY);
        //provider.setGeneralSubjectId(ACCESS_DATA);
        oAccessKeyAuthProvider.setAccessLoginDefault(ACCESS_LOGIN_DEFAULT);
        
        Authentication authentication = createAuthToken();

        Authentication result = oAccessKeyAuthProvider.authenticate(authentication);

        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getCredentials(), result.getCredentials());
        assertEquals(expected.getAuthorities(), result.getAuthorities());
    }*/

    @Test
    public void shouldAuthenticateByDaoCredentials() throws Exception {
        Authentication expected = createAuthenticatedAuthToken();
        when(accessDataDao.getAccessData(ACCESS_KEY)).thenReturn(ACCESS_DATA);

        oAccessKeyAuthProvider.setAccessLoginDefault(ACCESS_LOGIN_DEFAULT);

        Authentication authentication = createAuthToken();

        Authentication result = oAccessKeyAuthProvider.authenticate(authentication);

        assertEquals(ACCESS_LOGIN_DEFAULT, result.getName());//expected.getName()
        assertEquals(expected.getCredentials(), result.getCredentials());
        assertEquals(expected.getAuthorities(), result.getAuthorities());
    }

    @Test
    public void shouldRemoveDaoCredentialsAfterSuccessfulAuthentication() throws Exception {
        when(accessDataDao.getAccessData(ACCESS_KEY)).thenReturn(ACCESS_DATA);

        oAccessKeyAuthProvider.setAccessLoginDefault(ACCESS_LOGIN_DEFAULT);

        Authentication authentication = createAuthToken();

        oAccessKeyAuthProvider.authenticate(authentication);

        verify(accessDataDao).removeAccessData(ACCESS_KEY);
    }

    /*@Test
    public void shouldNotRemoveDaoCredentialsForPersistentKeyAfterSuccessfulAuthentication() throws Exception {
        when(accessDataDao.getAccessData(ACCESS_KEY)).thenReturn(ACCESS_DATA);
        Authentication authentication = createAuthToken();
        oAccessKeyAuthProvider.setPersistentKey(ACCESS_KEY);

        oAccessKeyAuthProvider.authenticate(authentication);

        verify(accessDataDao, times(0)).removeAccessData(ACCESS_KEY);
    }*/

    @Test
    public void shouldThrowExceptionWhenDaoDoesNotContainTheAccessKey() throws Exception {
        Authentication authentication = createAuthToken();
        thrown.expect(BadAccessKeyCredentialsException.class);
        thrown.expectMessage("Error custom authorization - key is absent");

        oAccessKeyAuthProvider.authenticate(authentication);
    }

    @Test
    public void shouldThrowExceptionWhenDaoContainsDifferentAccessData() throws Exception {
        Authentication authentication = createAuthToken();
        when(accessDataDao.getAccessData(ACCESS_KEY)).thenReturn(DIFFERENT_ACCESS_DATA);
        thrown.expect(BadAccessKeyCredentialsException.class);
        thrown.expectMessage("Error custom authorization - key data is wrong");

        oAccessKeyAuthProvider.authenticate(authentication);
    }
}