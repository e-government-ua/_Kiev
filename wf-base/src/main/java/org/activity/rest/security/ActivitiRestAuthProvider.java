package org.activity.rest.security;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.apache.commons.lang3.StringUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by diver on 6/26/15.
 */
@Component
public class ActivitiRestAuthProvider implements AuthenticationProvider {

	private final Logger oLog = LoggerFactory.getLogger(ActivitiRestAuthProvider.class);
    
	private static final String GENERAL_ROLE = "ROLE_USER";

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

	public void setIdentityService(IdentityService oIdentityService) {
		this.oIdentityService = oIdentityService;
	}

	private IdentityService getIdentityService() {
		return oIdentityService == null ? ProcessEngines.getDefaultProcessEngine().getIdentityService() : oIdentityService;
	}

	@Override
	public Authentication authenticate(Authentication oAuthentication) throws AuthenticationException {
                //oLog.info("sGeneralUsername="+sGeneralUsername+",sGeneralPassword="+sGeneralPassword);
		validateAuthenticationInformation(oAuthentication);
		String sUsername = oAuthentication.getName();
		String sPassword = oAuthentication.getCredentials().toString();
                //oLog.info("[authenticate]:sUsername="+sUsername+",sPassword="+sPassword);
//                oLog.info("[authenticate]:sUsername="+sUsername);
		if (sUsername.equals(sGeneralUsername) && sPassword.equals(sGeneralPassword)) {
                        /*log.info("sGeneralUsername!!!1test: tech_mvd,getIdentityService="+getIdentityService().getUserInfo("tech_mvd", "tech_mvd"));
                        Authentication oAuthentication=createBasicAuthUsernameAndPasswordToken("tech_mvd", "tech_mvd");
                        log.info("sGeneralUsername!!!2test: tech_mvd,oAuthentication!=mull:"+(oAuthentication!=null));
                        if(oAuthentication!=null){
                            log.info("sGeneralUsername!!!3test: tech_mvd"
                                    + ",oAuthentication.getName()="+oAuthentication.getName()
                                    + ",oAuthentication.getDetails="+oAuthentication.getDetails()
                            );
                        }*/
                        //oLog.info("[authenticate]:sUsername.equals(generalUsername) && sPassword.equals(generalPassword)");
                        oLog.info("[authenticate](sUsername="+sUsername+"):General - Ok!");
			return createBasicAuthUsernameAndPasswordToken(sUsername, sPassword);
		} else {
                        boolean bCheckPassword = getIdentityService().checkPassword(sUsername, sPassword);
                        //oLog.info("[authenticate](sUsername="+sUsername+"):Custom - "+(bCheckPassword? "Ok!":"Fail!!!"));//bCheckPassword
			if (bCheckPassword) {
                            oLog.info("[authenticate](sUsername="+sUsername+"):Custom - Ok!");//bCheckPassword
                            //oLog.info("[authenticate](sUsername="+sUsername+"):getIdentityService="+getIdentityService().getUserInfo(sUsername, sPassword));
                            return createBasicAuthUsernameAndPasswordToken(sUsername, sPassword);
			} else {
                            oLog.warn("[authenticate](sUsername="+sUsername+"):Custom - FAIL!");//bCheckPassword
                            return null;
			}
		}
	}

	private Authentication createBasicAuthUsernameAndPasswordToken(String username, String password) {
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority(GENERAL_ROLE));
		return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
	}

	private void validateAuthenticationInformation(Authentication oAuthentication) throws AuthenticationException {
		boolean bNullAuth = false;
		boolean bInvalid = (bNullAuth = oAuthentication == null);
                //log.info("isAuthInfoInvalid0="+isAuthInfoInvalid);
		boolean bBlankName = false;
		bInvalid = bInvalid || (bBlankName = StringUtils.isBlank(oAuthentication.getName()));
                //log.info("isAuthInfoInvalid1="+isAuthInfoInvalid);
		boolean bNullCredentials = false;
		bInvalid = bInvalid || (bNullCredentials = oAuthentication.getCredentials() == null);
                //log.info("isAuthInfoInvalid2="+isAuthInfoInvalid);
		boolean bBlankCredentials = false;
		bInvalid = bInvalid || (bBlankCredentials = StringUtils.isBlank(oAuthentication.getCredentials().toString()));
                //log.info("isAuthInfoInvalid3="+isAuthInfoInvalid);
		if (bInvalid) {
                        oLog.error("[validateAuthenticationInformation]("
                                + "bNullAuth="+bNullAuth + ""
                                + ",bBlankName="+bBlankName + ""
                                + ",bNullCredentials="+bNullCredentials + ""
                                + ",bBlankCredentials="+bBlankCredentials + "):User or password not valid!");
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
