package org.wf.dp.dniprorada.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author askosyr
 */
@Component("bankIDConfig")
public class BankIDConfig {

    @Value("#{bankId_clientId:testIgov}")
	private String clientId;
    
    @Value("#{bankId_clientSecret:testIgovSecret}")
	private String clientSecret;
    
    
    public String sClientId(){
        return clientId;
    }
    
    public String sClientSecret(){
        return clientSecret;
    }
    
}
