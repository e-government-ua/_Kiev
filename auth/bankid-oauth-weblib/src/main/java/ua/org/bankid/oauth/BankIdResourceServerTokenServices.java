package ua.org.bankid.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Service;
import ua.org.bankid.oauth.BankIdStorageService;

/**
 * @author zora.borys
 */
@Service("tokenServices")
public class BankIdResourceServerTokenServices implements ResourceServerTokenServices {

    @Autowired
    private BankIdStorageService bankIdStorageService;

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        return bankIdStorageService.loadAuthentication(accessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return bankIdStorageService.readAccessToken(accessToken);
    }
}
