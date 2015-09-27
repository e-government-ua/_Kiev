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
 * @author tasman
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
        if (bAuthByAccessKeyAndSubject() && bAuthByAccessKeyAndSubject_Equal(oAuthentication)) {
            //return authenticatedToken(oAuthentication);
        }else{
            checkAuthByAccessKeyAndData(oAuthentication);
        }
        return createTokenByAuthentication(oAuthentication);
    }

    private void checkAuthByAccessKeyAndData(Authentication oAuthentication) {
        String sAccessKey = oAuthentication.getName();
        //log.info("authentication.getName()="+authentication.getName());
        //log.info("authentication.getPrincipal()="+authentication.getPrincipal());
        String sAccessData = oAccessDataDao.getAccessData(sAccessKey);
        oLog.info("[bAuthByAccessKeyAndData]:sAccessKey="+sAccessKey + ",sAccessData="+sAccessData);
        //log.info("authentication.getCredentials()="+authentication.getCredentials());
        //log.info("authentication.getCredentials() decode ="+URLDecoder.decode((String)authentication.getCredentials()));
        if (sAccessData == null) {
            oLog.warn("[bAuthByAccessKeyAndData]:sAccessData == null");
            throw new BadAccessKeyCredentialsException("Error custom authorization - key is absent");
        }
        sAccessData = sAccessData.replace("&sAccessContract=Request", "").replace("sAccessContract=Request&", "");

        String sAccessDataGenerated = oAuthentication.getCredentials()+"";
        String sAccessDataGeneratedDecoded = null;
        //String sAccessDataGenerated = null;
        try{
            sAccessDataGeneratedDecoded = URLDecoder.decode(sAccessDataGenerated);
        }catch(Exception oException){
            oLog.error("[bAuthByAccessKeyAndData]:sAccessDataGenerated="+sAccessDataGenerated+":on 'URLDecoder.decode' "+oException.getMessage());
            throw oException;
        }
        
        if (!sAccessData.equals(sAccessDataGeneratedDecoded)) {
            oLog.error("[bAuthByAccessKeyAndData]:!sAccessData.equals(sAccessDataGenerated):"
                    + "sAccessData=\n" + sAccessData
                    + ", sAccessDataGenerated=\n" + sAccessDataGenerated
                    + ", sAccessDataGeneratedDecoded=\n" + sAccessDataGeneratedDecoded
                    + "");
            throw new BadAccessKeyCredentialsException("Error custom authorization - key data is wrong");
        }
        //oLog.info("[bAuthByAccessKeyAndData]:sAccessKey="+sAccessKey + ",sPersistentKey="+sPersistentKey);
        if (sAccessKeyUnlimited == null || !sAccessKeyUnlimited.equals(sAccessKey)) {//authentication.getName()
            oLog.info("[bAuthByAccessKeyAndData](sAccessKey="+sAccessKey+"):Remove key!");
            oAccessDataDao.removeAccessData(sAccessKey);//authentication.getName()
        }else{
            oLog.warn("[bAuthByAccessKeyAndData](sAccessKey="+sAccessKey + ",sAccessKeyUnlimited="+sAccessKeyUnlimited+"):Can't remove key, it's Unlimited!");
        }
    }

    private boolean bAuthByAccessKeyAndSubject() {
        //oLog.info("[bAuthByAccessKeyAndSubjectID]:generalAccessKey="+sGeneralAccessKey+",generalSubjectId="+sGeneralSubjectId);
        boolean bTrue = StringUtils.isNotBlank(sGeneralAccessKey) && StringUtils.isNotBlank(sGeneralSubjectId);
        //oLog.info("[bAuthByAccessKeyAndSubjectID]:bReturn="+bTrue);
        oLog.info("[bAuthByAccessKeyAndSubject]:sGeneralAccessKey="+sGeneralAccessKey+",sGeneralSubjectId="+sGeneralSubjectId+",bTrue="+bTrue);
        return bTrue;
    }

    private boolean bAuthByAccessKeyAndSubject_Equal(Authentication oAuthentication) {
        //oLog.info("[bAuthByAccessKeyAndSubjectID_Equal]:generalAccessKey="+sGeneralAccessKey+",generalSubjectId="+sGeneralSubjectId);
        boolean bEqual = sGeneralAccessKey.equals(oAuthentication.getName()) && sGeneralSubjectId.equals(oAuthentication.getCredentials());
        //oLog.info("[bAuthByAccessKeyAndSubjectID_Equal]:bReturn="+bEqual);
        oLog.info("[bAuthByAccessKeyAndSubject_Equal]:sGeneralAccessKey="+sGeneralAccessKey+",sGeneralSubjectId="+sGeneralSubjectId+",bEqual="+bEqual);
        return bEqual;
    }

    private Authentication createTokenByAuthentication(Authentication oAuthentication) {
        //oLog.info("[generalCredentialsExists]:authentication.getName()="+oAuthentication.getName()
        //        +",authentication.getCredentials().toString()="+oAuthentication.getCredentials().toString());
        List<GrantedAuthority> aGrantedAuthority = new ArrayList<>();
        aGrantedAuthority.add(new SimpleGrantedAuthority(GENERAL_ROLE));
        return new AccessKeyAuthenticationToken(oAuthentication.getName(),
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
