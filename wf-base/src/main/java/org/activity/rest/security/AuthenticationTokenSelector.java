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
					
/**					
 * Created by diver on 8/24/15.	edited by Olga Turenko & Belyavtsev Vladimir (BW)
 */					
public class AuthenticationTokenSelector {					
					
	private final Logger oLog = LoggerFactory.getLogger(AccessKeyAuthFilter.class);				
					
	/* KEYS */				
	public static final String ACCESS_CONTRACT = "sAccessContract";				
	public static final String ACCESS_KEY = "sAccessKey";				
	public static final String SUBJECT_ID = "nID_Subject";				
	public static final String LOGIN = "sLogin";
	/* VALUES */				
	private static final String ACCESS_CONTRACT_REQUEST = "Request";				
					
	private ServletRequest oRequest;				
					
	public AuthenticationTokenSelector(ServletRequest oRequest) {
		this.oRequest = oRequest;			
                if(oRequest instanceof HttpServletRequest && oRequest != null){					
                    oLog.info("[AuthenticationTokenSelector]:getRequestURL()=" + ((HttpServletRequest)oRequest).getRequestURL());
                }					
	}				
					
	public final AccessKeyAuthenticationToken createToken() {				
            return isAccessByContract() ?			
                            createTokenByContract() :	
                            createTokenBySubject();
	}				

	private boolean isAccessByContract() {				
		String sAccessContract = oRequest.getParameter(ACCESS_CONTRACT);			
		oLog.info("[isAccessByContract]:"+ACCESS_CONTRACT + "=" + sAccessContract);			
		return StringUtils.isNoneBlank(sAccessContract) &&			
				ACCESS_CONTRACT_REQUEST.equalsIgnoreCase(sAccessContract);	
	}				
					
	private AccessKeyAuthenticationToken createTokenBySubject() {				
		String sAccessKey = oRequest.getParameter(ACCESS_KEY);			
		oLog.info("[createTokenBySubject]:"+ACCESS_KEY + "=" + sAccessKey);			
		String snID_Subject = oRequest.getParameter(SUBJECT_ID);
		oLog.info("[createTokenBySubject]:"+SUBJECT_ID + "=" + snID_Subject);			
		return new AccessKeyAuthenticationToken(sAccessKey, snID_Subject);			
	}				
					
	private AccessKeyAuthenticationToken createTokenByContract() {				
		String sAccessKey = oRequest.getParameter(ACCESS_KEY);
		oLog.info("[createTokenByContract]:"+ACCESS_KEY + "=" + sAccessKey);			
		String sQuery = getQueryStringWithContextPath();
		oLog.info("[createTokenByContract]:"+"sQuery=" + sQuery);			
		return new AccessKeyAuthenticationToken(sAccessKey, sQuery);			
	}				
					
	private String getQueryStringWithContextPath() {				
		String sPath = getRequestContextPath();			
		String sQuery = getRequestQuery();			
		return sPath.concat("?").concat(sQuery);			
	}				
					
	private String getRequestQuery() {				
		List<BasicNameValuePair> aParameter = new ArrayList<>();			
		Enumeration<String> oaName = oRequest.getParameterNames();			
		while (oaName.hasMoreElements()) {			
			addFilteredParameter(aParameter, oaName.nextElement());		
		}			
		return URLEncodedUtils.format(aParameter, "UTF-8");			
	}				
        					
	private void addFilteredParameter(List<BasicNameValuePair> aParameter, String sName) {				
		if (!ACCESS_KEY.equalsIgnoreCase(sName) && !ACCESS_CONTRACT.equalsIgnoreCase(sName)) {			
			String sValue = oRequest.getParameter(sName);		
			aParameter.add(new BasicNameValuePair(sName, sValue));		
		}			
	}				
					
	private String getRequestContextPath() {				
		if (oRequest instanceof HttpServletRequest) {			
			HttpServletRequest oRequestHTTP = (HttpServletRequest) oRequest;		
			return oRequestHTTP.getContextPath()
                                .concat(oRequestHTTP.getServletPath())
                                .concat(oRequestHTTP.getPathInfo());
		} else {			
			oLog.warn("Can't read context path. Request is not HttpServletRequest object");		
			return StringUtils.EMPTY;		
		}			
	}				
}					
