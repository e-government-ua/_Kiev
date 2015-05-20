package ua.org.bankid.oauth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * @author zora.borys
 */
public class BankIdAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null; // TODO
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return  OAuth2Authentication.class.isAssignableFrom(authentication) ||
                PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication) ||
                UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
