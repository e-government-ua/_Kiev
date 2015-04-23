package ua.org.bankid.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import ua.org.bankid.oauth.BankIdStorageService;

/**
 * @author zora.borys
 */
@Service("clientDetailsService")
public class BankIdClientDetailsService implements ClientDetailsService {

    @Autowired
    private BankIdStorageService bankIdStorageService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return bankIdStorageService.loadClientByClientId(clientId);
    }
}
