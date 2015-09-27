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
    @Value("${general.auth.custom.accessKey}")
    private String sGeneralAccessKey;
    @Value("${general.auth.custom.subjectId}")
    private String sGeneralSubjectId;
    @Value("${general.auth.custom.persistentKey}")
    private String sAccessKeyUnlimited;
    private AccessDataDao oAccessDataDao;

    @Value("${general.auth.login}")
    private String sGeneralUsername;
    
    private String sAccessLogin = sGeneralUsername;//"anonymous"
    
    private final Logger oLog = LoggerFactory.getLogger(AccessKeyAuthProvider.class);
    
    @Autowired
    public AccessKeyAuthProvider(AccessDataDao oAccessDataDao) {
        this.oAccessDataDao = oAccessDataDao;
    }

    public void setGeneralAccessKey(String sGeneralAccessKey) {
        this.sGeneralAccessKey = sGeneralAccessKey;
    }

    public void setGeneralSubjectId(String sGeneralSubjectId) {
        this.sGeneralSubjectId = sGeneralSubjectId;
    }

    public void setPersistentKey(String sPersistentKey) {
        this.sAccessKeyUnlimited = sPersistentKey;
    }

    @Override
    public Authentication authenticate(Authentication oAuthentication) throws AuthenticationException {
        if (StringUtils.isNotBlank(sGeneralAccessKey) && StringUtils.isNotBlank(sGeneralSubjectId)
            && sGeneralAccessKey.equals(oAuthentication.getName()) && sGeneralSubjectId.equals(oAuthentication.getCredentials())
            ) {
                //sAccessLogin = "Test";
                oLog.info("[authenticate]:sAccessLogin="+sAccessLogin+",sGeneralSubjectId=="+sGeneralSubjectId+":By General!");
        }else{
            checkAuthByAccessKeyAndData(oAuthentication);
        }
        return createTokenByAuthentication(oAuthentication);
    }

    private void checkAuthByAccessKeyAndData(Authentication oAuthentication) {
        String sAccessKey = oAuthentication.getName();
        String sAccessData = oAccessDataDao.getAccessData(sAccessKey);
        oLog.info("[checkAuthByAccessKeyAndData]:sAccessKey="+sAccessKey + ",sAccessData(Storage)="+sAccessData);
        if (sAccessData == null) {
            oLog.warn("[checkAuthByAccessKeyAndData]:sAccessData == null");
            throw new BadAccessKeyCredentialsException("Error custom authorization - key is absent");
        }
        //sAccessData = sAccessData.replace("&sAccessContract=Request", "").replace("sAccessContract=Request&", "");
        sAccessData = sAccessData.replace("&"+AuthenticationTokenSelector.ACCESS_CONTRACT+"="+AuthenticationTokenSelector.ACCESS_CONTRACT_REQUEST, "")
                .replace(""+AuthenticationTokenSelector.ACCESS_CONTRACT+"="+AuthenticationTokenSelector.ACCESS_CONTRACT_REQUEST+"&", "");
        boolean bContractAndLogin = sAccessData.contains(AuthenticationTokenSelector.ACCESS_CONTRACT+"="+AuthenticationTokenSelector.ACCESS_CONTRACT_REQUEST_AND_LOGIN);
        if(bContractAndLogin){
            sAccessData = sAccessData.replace("&"+AuthenticationTokenSelector.ACCESS_CONTRACT+"="+AuthenticationTokenSelector.ACCESS_CONTRACT_REQUEST_AND_LOGIN, "")
                    .replace(""+AuthenticationTokenSelector.ACCESS_CONTRACT+"="+AuthenticationTokenSelector.ACCESS_CONTRACT_REQUEST_AND_LOGIN+"&", "");
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
        
        if (sAccessKeyUnlimited == null || !sAccessKeyUnlimited.equals(sAccessKey)) {//authentication.getName()
            oLog.info("[checkAuthByAccessKeyAndData](sAccessLogin="+sAccessLogin+",sAccessKey="+sAccessKey+"):Remove key!");
            oAccessDataDao.removeAccessData(sAccessKey);//authentication.getName()
        }else{
            oLog.warn("[checkAuthByAccessKeyAndData](sAccessLogin="+sAccessLogin+",sAccessKey="+sAccessKey + ",sAccessKeyUnlimited="+sAccessKeyUnlimited+"):Can't remove key, it's Unlimited!");
        }
    }

    private Authentication createTokenByAuthentication(Authentication oAuthentication) {
        //oLog.info("[generalCredentialsExists]:authentication.getName()="+oAuthentication.getName()
        //        +",authentication.getCredentials().toString()="+oAuthentication.getCredentials().toString());
        oLog.info("[createTokenByAuthentication]:sAccessLogin="+sAccessLogin);
        List<GrantedAuthority> aGrantedAuthority = new ArrayList<>();
        aGrantedAuthority.add(new SimpleGrantedAuthority(GENERAL_ROLE));
        return new AccessKeyAuthenticationToken(sAccessLogin,//oAuthentication.getName()
                oAuthentication.getCredentials().toString(),
                aGrantedAuthority);
        //Arrays.asList(new SimpleGrantedAuthority(GENERAL_ROLE))
    }

    @Override
    public boolean supports(Class<?> oAuthentication) {
        boolean bSupport = AccessKeyAuthenticationToken.class.equals(oAuthentication);
        //oLog.info("[supports]:bEquals="+bSupport);
        return bSupport;
    }
}
