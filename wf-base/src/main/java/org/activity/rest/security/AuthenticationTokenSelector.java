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
 * Created by diver on 8/24/15.
 */
public class AuthenticationTokenSelector {

	private final Logger LOGGER = LoggerFactory.getLogger(AccessKeyAuthFilter.class);

	/* KEYS */
	public static final String ACCESS_CONTRACT = "sAccessContract";
	public static final String ACCESS_KEY = "sAccessKey";
	public static final String SUBJECT_ID = "nID_Subject";
	/* VALUES */
	public static final String REQUEST = "Request";

	private ServletRequest request;

	public AuthenticationTokenSelector(ServletRequest request) {
		this.request = request;
                if(request instanceof HttpServletRequest && request != null){
                    LOGGER.info("[AuthenticationTokenSelector] URL: " + ((HttpServletRequest)request).getRequestURL());
                }
	}

	public final AccessKeyAuthenticationToken createToken() {
		return isRequestContainAccessContractParameter() ?
				createAccessContractToken() :
				createSubjectIdToken();
	}

	private boolean isRequestContainAccessContractParameter() {
		String accessContract = request.getParameter(ACCESS_CONTRACT);
		LOGGER.info("accessContract=" + accessContract);
		return StringUtils.isNoneBlank(accessContract) &&
				REQUEST.equalsIgnoreCase(accessContract);
	}

	private AccessKeyAuthenticationToken createSubjectIdToken() {
		String accessKey = request.getParameter(ACCESS_KEY);
		LOGGER.info("accessKey=" + accessKey);
		String subjectId = request.getParameter(SUBJECT_ID);
		LOGGER.info("subjectId=" + subjectId);
		return new AccessKeyAuthenticationToken(accessKey, subjectId);
	}

	private AccessKeyAuthenticationToken createAccessContractToken() {
		String accessKey = request.getParameter(ACCESS_KEY);
		LOGGER.info("accessKey=" + accessKey);
		String queryString = getQueryStringWithContextPath();
		LOGGER.info("queryString=" + queryString);
		return new AccessKeyAuthenticationToken(accessKey, queryString);
	}

	private String getQueryStringWithContextPath() {
		String path = createRequestContextPath();
		String query = createRequestQueryString();
		return path.concat("?").concat(query);
	}

	private String createRequestQueryString() {
		List<BasicNameValuePair> parameters = new ArrayList<>();
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			addParameterByFilter(parameters, names.nextElement());
		}
                //return getParameters(parameters);
		return URLEncodedUtils.format(parameters, "UTF-8");
		//return URIBuilder.format(parameters, "UTF-8");
	}

	private String getParameters(List<BasicNameValuePair> parameters) {
                String s="";
		for (BasicNameValuePair parameter : parameters) {
			s+=(s.length()>0?"&":"")+parameter.getName()+"="+parameter.getValue();
		}
                return s;
	}
        
	private void addParameterByFilter(List<BasicNameValuePair> parameters, String name) {
		if (!ACCESS_KEY.equalsIgnoreCase(name)) {
			String value = request.getParameter(name);
			parameters.add(new BasicNameValuePair(name, value));
		}
	}

	private String createRequestContextPath() {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			return httpRequest.getContextPath().concat(httpRequest.getServletPath()).
					concat(httpRequest.getPathInfo());
		} else {
			LOGGER.warn("Can't read context path. Request is not HttpServletRequest object");
			return StringUtils.EMPTY;
		}
	}
}
