package ua.org.bankid.oauth.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import ua.org.bankid.oauth.BankIdStorageService;

/**
 * @author zora.borys
 */
public class BankIdStorageServiceRedisImpl implements BankIdStorageService {

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        return null;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) {
        return null;
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return null;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        return null;
    }
}
