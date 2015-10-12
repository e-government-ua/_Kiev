package org.activity.rest.security;

import java.net.URLDecoder;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.stereotype.*;
import org.wf.dp.dniprorada.base.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author tasman edited by Olga Turenko & Belyavtsev Vladimir (BW)
 */
@Component
public class AccessKeyAuthProvider implements AuthenticationProvider {

    private static final String GENERAL_ROLE = "ROLE_USER";

    @Value("${general.auth.login}")
    private String sAccessLogin; // = sGeneralUsername; // == null ? "anonymous" : sGeneralUsername;

    private AccessDataDao oAccessDataDao;
    
    private final Logger oLog = LoggerFactory.getLogger(AccessKeyAuthProvider.class);
    
    @Autowired
    public AccessKeyAuthProvider(AccessDataDao oAccessDataDao) {
        this.oAccessDataDao = oAccessDataDao;
    }

    public void setAccessLoginDefault(String sAccessLogin) {
        this.sAccessLogin = sAccessLogin;
    }
    
    @Override
    public Authentication authenticate(Authentication oAuthentication) throws AuthenticationException {
        checkAuthByAccessKeyAndData(oAuthentication);
        return createTokenByAccessKeyAndData(oAuthentication);
    }

    private void checkAuthByAccessKeyAndData(Authentication oAuthentication) {
        String sAccessKey = oAuthentication.getName();
        String sAccessData = oAccessDataDao.getAccessData(sAccessKey);
        oLog.info("[checkAuthByAccessKeyAndData]:sAccessKey="+sAccessKey + ",sAccessData(Storage)="+sAccessData);
        if (sAccessData == null) {
            oLog.warn("[checkAuthByAccessKeyAndData]:sAccessData == null");
            throw new BadAccessKeyCredentialsException("Error custom authorization - key is absent");
        }
        sAccessData = sAccessData.replace("&"+AuthenticationTokenSelector.ACCESS_CONTRACT+"="+AuthenticationTokenSelector.ACCESS_CONTRACT_REQUEST, "")
                .replace(""+AuthenticationTokenSelector.ACCESS_CONTRACT+"="+AuthenticationTokenSelector.ACCESS_CONTRACT_REQUEST+"&", "");
        boolean bContractAndLogin = sAccessData.contains(AuthenticationTokenSelector.ACCESS_CONTRACT+"="+AuthenticationTokenSelector.ACCESS_CONTRACT_REQUEST_AND_LOGIN);
        if(bContractAndLogin){
            sAccessData = sAccessData.replace("&"+AuthenticationTokenSelector.ACCESS_CONTRACT+"="+AuthenticationTokenSelector.ACCESS_CONTRACT_REQUEST_AND_LOGIN, "")
                    .replace(""+AuthenticationTokenSelector.ACCESS_CONTRACT+"="+AuthenticationTokenSelector.ACCESS_CONTRACT_REQUEST_AND_LOGIN+"&", "");
        }
        if(sAccessData.contains(AuthenticationTokenSelector.ACCESS_LOGIN)){
            sAccessData = sAccessData.substring(0, sAccessData.indexOf("&" + AuthenticationTokenSelector.ACCESS_LOGIN)); //&sAccessLogin=activiti-master  
        }
        
        String sAccessDataGenerated = oAuthentication.getCredentials()+"";
        String sAccessDataGeneratedDecoded = null;
        try{
            sAccessDataGeneratedDecoded = URLDecoder.decode(sAccessDataGenerated);
            if(bContractAndLogin){
                String sStartWith = AuthenticationTokenSelector.ACCESS_LOGIN+"=";
                String[] as = sAccessDataGeneratedDecoded.split("\\&");
                for(String s : as){
                    if(s.startsWith(sStartWith)){
                        String[] asWord = s.split("\\=");
                        sAccessLogin=asWord[1];
                        break;
                    }
                }
            }
        }catch(Exception oException){
            oLog.error("[checkAuthByAccessKeyAndData]:sAccessDataGenerated="+sAccessDataGenerated+":on 'URLDecoder.decode' "+oException.getMessage());
            throw oException;
        }
        
        if (!sAccessData.equals(sAccessDataGeneratedDecoded)) {
            oLog.error("[checkAuthByAccessKeyAndData]:!sAccessData.equals(sAccessDataGenerated):"
                    + "sAccessData(FromStorage)=\n" + sAccessData
                    //+ ", sAccessDataGenerated=\n" + sAccessDataGenerated
                    + ", sAccessDataGeneratedDecoded=\n" + sAccessDataGeneratedDecoded
                    + "");
            throw new BadAccessKeyCredentialsException("Error custom authorization - key data is wrong");
        }
        
        oAccessDataDao.removeAccessData(sAccessKey);
        oLog.info("[checkAuthByAccessKeyAndData](sAccessLogin="+sAccessLogin+",bContractAndLogin="+bContractAndLogin+",sAccessKey="+sAccessKey+"):Removed key!");
    }

    private Authentication createTokenByAccessKeyAndData(Authentication oAuthentication) {
        oLog.info("[createTokenByAccessKey]:sAccessLogin="+sAccessLogin);//+",oAuthentication.getName()="+oAuthentication.getName()//+",authentication.getCredentials().toString()="+oAuthentication.getCredentials().toString());
        List<GrantedAuthority> aGrantedAuthority = new ArrayList<>(); //Arrays.asList(new SimpleGrantedAuthority(GENERAL_ROLE))
        aGrantedAuthority.add(new SimpleGrantedAuthority(GENERAL_ROLE));
        return new AccessKeyAuthenticationToken(sAccessLogin, //sAccessLogin == null ? oAuthentication.getName() : sAccessLogin
                oAuthentication.getCredentials().toString(), aGrantedAuthority);
    }

    @Override
    public boolean supports(Class<?> oAuthentication) {
        boolean bSupport = AccessKeyAuthenticationToken.class.equals(oAuthentication);
        //oLog.info("[supports]:bEquals="+bSupport);
        return bSupport;
    }
}
