package ua.org.bankid.oauth;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author zora.borys
 */
public interface BankIdStorageService {

    ClientDetails loadClientByClientId(String clientId);

    OAuth2Authentication loadAuthentication(String accessToken);

    OAuth2AccessToken readAccessToken(String accessToken);

    Authentication authenticate(Authentication authentication);
}
