package org.activity.rest.security;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diver edited by Olga Turenko & Belyavtsev Vladimir (BW)
 */
@Component
public class ActivitiRestAuthProvider implements AuthenticationProvider {

    private static final String GENERAL_ROLE = "ROLE_USER";
    private final Logger oLog = LoggerFactory.getLogger(ActivitiRestAuthProvider.class);
    @Value("${general.auth.login}")
    private String sGeneralUsername;
    @Value("${general.auth.password}")
    private String sGeneralPassword;

    private IdentityService oIdentityService;

    public void setGeneralPassword(String sGeneralPassword) {
        this.sGeneralPassword = sGeneralPassword;
    }

    public void setGeneralUsername(String sGeneralUsername) {
        this.sGeneralUsername = sGeneralUsername;
    }

    private IdentityService getIdentityService() {
        return oIdentityService == null ?
                ProcessEngines.getDefaultProcessEngine().getIdentityService() :
                oIdentityService;
    }

    public void setIdentityService(IdentityService oIdentityService) {
        this.oIdentityService = oIdentityService;
    }

    @Override
    public Authentication authenticate(Authentication oAuthentication) throws AuthenticationException {
        checkAuthByLoginAndPassword(oAuthentication);
        String sUsername = oAuthentication.getName();
        String sPassword = oAuthentication.getCredentials().toString();
        if (!sUsername.equals(sGeneralUsername) || !sPassword.equals(sGeneralPassword)) {
            if (!getIdentityService().checkPassword(sUsername, sPassword)) {
                oLog.warn("[authenticate](sUsername=" + sUsername + "):FAIL!");
                return null;
            }
        }
        return createTokenByUsernameAndPassword(sUsername, sPassword);
    }

    private Authentication createTokenByUsernameAndPassword(String sUsername, String sPassword) {
        oLog.info("[createTokenByUsernameAndPassword]:sUsername=" + sUsername);
        List<GrantedAuthority> aGrantedAuthority = new ArrayList<>();
        aGrantedAuthority.add(new SimpleGrantedAuthority(GENERAL_ROLE));
        return new UsernamePasswordAuthenticationToken(sUsername, sPassword, aGrantedAuthority);
    }

    private void checkAuthByLoginAndPassword(Authentication oAuthentication) throws AuthenticationException {
        boolean bNullAuth = false;
        boolean bInvalid = (bNullAuth = oAuthentication == null);
        boolean bBlankName = false;
        bInvalid = bInvalid || (bBlankName = StringUtils.isBlank(oAuthentication.getName()));
        boolean bNullCredentials = false;
        bInvalid = bInvalid || (bNullCredentials = oAuthentication.getCredentials() == null);
        boolean bBlankCredentials = false;
        bInvalid = bInvalid || (bBlankCredentials = StringUtils.isBlank(oAuthentication.getCredentials().toString()));
        if (bInvalid) {
            oLog.error("[checkAuthByLoginAndPassword]("
                    + "bNullAuth=" + bNullAuth + ""
                    + ",bBlankName=" + bBlankName + ""
                    + ",bNullCredentials=" + bNullCredentials + ""
                    + ",bBlankCredentials=" + bBlankCredentials + "):User or password not valid!");
            throw new BadCredentialsException("User or password not valid");
        }
    }

    @Override
    public boolean supports(Class<?> oAuthentication) {
        boolean bSupport = UsernamePasswordAuthenticationToken.class.equals(oAuthentication);
        //oLog.info("[supports]:bEquals="+bSupport);
        return bSupport;

    }
}
