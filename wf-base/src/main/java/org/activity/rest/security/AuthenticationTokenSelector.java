package org.activity.rest.security;					
					
import org.apache.commons.lang3.StringUtils;					
import org.apache.http.client.utils.URLEncodedUtils;					
import org.apache.http.message.BasicNameValuePair;					
import org.slf4j.Logger;					
import org.slf4j.LoggerFactory;					
					
import javax.servlet.ServletRequest;					
import javax.servlet.http.HttpServletRequest;					
import java.util.ArrayList;					
import java.util.Enumeration;					
import java.util.List;					
import org.apache.http.client.utils.URIBuilder;					
import org.apache.http.client.utils.URIUtils;					
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
					
/**					
 * Created by diver edited by Olga Turenko & Belyavtsev Vladimir (BW)
 */					
public class AuthenticationTokenSelector {					
				
	private final Logger oLog = LoggerFactory.getLogger(AccessKeyAuthFilter.class);				
					
	/* KEYS */				
	public static final String ACCESS_KEY = "sAccessKey";				
	public static final String SUBJECT_ID = "nID_Subject"; //TODO: remove in future
	public static final String ACCESS_LOGIN = "sAccessLogin";
	public static final String ACCESS_CONTRACT = "sAccessContract";				
	/* VALUES */				
	public static final String ACCESS_CONTRACT_REQUEST = "Request";
	public static final String ACCESS_CONTRACT_REQUEST_AND_LOGIN = "RequestAndLogin";
					
	private ServletRequest oRequest;				
					
	public AuthenticationTokenSelector(ServletRequest oRequest) {
		this.oRequest = oRequest;			
                if(oRequest instanceof HttpServletRequest && oRequest != null){					
                    oLog.info("[AuthenticationTokenSelector]:getRequestURL()=" + ((HttpServletRequest)oRequest).getRequestURL());
                }					
	}				
					
	public final AccessKeyAuthenticationToken createToken() {				
            AccessKeyAuthenticationToken oAccessKeyAuthenticationToken = null;
            String sAccessKey = oRequest.getParameter(ACCESS_KEY);
            String sAccessLogin = oRequest.getParameter(ACCESS_LOGIN);
            String sAccessContract = oRequest.getParameter(ACCESS_CONTRACT);			
            oLog.info("[createToken]:"+ACCESS_KEY + "=" + sAccessKey + ","+ACCESS_LOGIN + "=" + sAccessLogin + "," + ACCESS_CONTRACT + "=" + sAccessContract);			
            if(sAccessLogin!=null){
                Authentication oAuthentication = SecurityContextHolder.getContext().getAuthentication();
                if(oAuthentication!=null){
                    if(oAuthentication.getName()!=null){
                        oLog.info("[createToken]:oAuthentication.getName()="+oAuthentication.getName());			
                        if(!sAccessLogin.equals(oAuthentication.getName())){
                            oLog.error("[createToken]:!sAccessLogin.equals(oAuthentication.getName())");		
                            SecurityContextHolder.getContext().setAuthentication(oAccessKeyAuthenticationToken);
                            throw new BadAccessKeyCredentialsException("[createToken]("
                                    + "sAccessLogin="+sAccessLogin+"):!equals:"+oAuthentication.getName());
                        }//return null;
                    }
                }
            }
            
            if(StringUtils.isNoneBlank(sAccessContract)){
                 String sContextAndQuery = getContextAndQuery();
                 oLog.info("[createToken]:"+"sContextAndQuery=" + sContextAndQuery);
                 if(ACCESS_CONTRACT_REQUEST.equalsIgnoreCase(sAccessContract) || ACCESS_CONTRACT_REQUEST_AND_LOGIN.equalsIgnoreCase(sAccessContract)){
                        oAccessKeyAuthenticationToken = new AccessKeyAuthenticationToken(sAccessKey, sContextAndQuery);			
                 }
            }else{
                oAccessKeyAuthenticationToken = createTokenBySubject();
            }
            return oAccessKeyAuthenticationToken;
	}				

	private String getContextAndQuery() {				
		String sContext = getRequestContextPath();			
		String sQuery = getRequestQuery();			
		return sContext.concat("?").concat(sQuery);			
	}				
					
	private String getRequestContextPath() {				
		if (oRequest instanceof HttpServletRequest) {			
			HttpServletRequest oRequestHTTP = (HttpServletRequest) oRequest;		
			return oRequestHTTP.getContextPath()
                                .concat(oRequestHTTP.getServletPath())
                                .concat(oRequestHTTP.getPathInfo());
		} else {			
			oLog.error("[getRequestContextPath]:Can't read context path. Request is not HttpServletRequest object");		
			return StringUtils.EMPTY;		
		}			
	}				
        
	private String getRequestQuery() {				
		List<BasicNameValuePair> aParameter = new ArrayList<>();			
		Enumeration<String> oaName = oRequest.getParameterNames();			
		while (oaName.hasMoreElements()) {			
                    String sName = oaName.nextElement();
                    if (!ACCESS_KEY.equalsIgnoreCase(sName) && !ACCESS_CONTRACT.equalsIgnoreCase(sName)
                            && !ACCESS_LOGIN.equalsIgnoreCase(sName)) {			
                            String sValue = oRequest.getParameter(sName);		
                            aParameter.add(new BasicNameValuePair(sName, sValue));		
                    }			
		}			
		return URLEncodedUtils.format(aParameter, "UTF-8");			
	}				
        					
	private AccessKeyAuthenticationToken createTokenBySubject() {				
		String sAccessKey = oRequest.getParameter(ACCESS_KEY);			
		String snID_Subject = oRequest.getParameter(SUBJECT_ID);
		oLog.info("[createTokenBySubject]:"+ACCESS_KEY + "=" + sAccessKey+","+SUBJECT_ID + "=" + snID_Subject);			
		return new AccessKeyAuthenticationToken(sAccessKey, snID_Subject);			
	}				
					
}					
