package org.activity.rest.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author tasman
 */
public class AccessKeyAuthenticationToken extends AbstractAuthenticationToken {

    public static final String LOGIN = "sLogin";
    
    private String sAccessKey;
    private String snID_Subject;
    private final Logger oLog = LoggerFactory.getLogger(AccessKeyAuthenticationToken.class);

    public AccessKeyAuthenticationToken(String sAccessKey, String snID_Subject) {
        super(null);
        this.sAccessKey = sAccessKey;
        this.snID_Subject = snID_Subject;
        //log.info("[AccessKeyAuthenticationToken]:sAccessKey="+sAccessKey+",snID_Subject="+snID_Subject);
        setAuthenticated(false);
    }

    public AccessKeyAuthenticationToken(String sAccessKey, String snID_Subject, Collection<? extends GrantedAuthority> aGrantedAuthority) {
        super(aGrantedAuthority);
        this.sAccessKey = sAccessKey;
        this.snID_Subject = snID_Subject;
        //log.info("[AccessKeyAuthenticationToken]:sAccessKey="+sAccessKey+",snID_Subject="+snID_Subject+",aGrantedAuthority!=null:"+(aGrantedAuthority!=null));
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        //log.info("[getCredentials]:snID_Subject=" + snID_Subject);
        return snID_Subject;
    }

    @Override
    public Object getPrincipal() {
        //log.info("[getPrincipal]:sAccessKey="+sAccessKey);
        return sAccessKey;
    }

    public boolean hasCorrectAccessKeyAndSubjectId() {
        return StringUtils.isNotBlank(snID_Subject) && StringUtils.isNotBlank(sAccessKey);
    }
}
