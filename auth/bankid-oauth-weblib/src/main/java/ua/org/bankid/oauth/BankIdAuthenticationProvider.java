package ua.org.bankid.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import ua.org.bankid.oauth.BankIdStorageService;

/**
 * @author zora.borys
 */
@Service("authenticationProvider")
public class BankIdAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private BankIdStorageService bankIdStorageService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return bankIdStorageService.authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
