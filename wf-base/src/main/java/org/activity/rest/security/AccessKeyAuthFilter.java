package org.activity.rest.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author tasman
 */
public class AccessKeyAuthFilter extends GenericFilterBean {

    public static final String ACCESS_CONTROL = "sAccessContract";
    public static final String ACCESS_KEY = "sAccessKey";
    public static final String SUBJECT_ID = "nID_Subject";

    public static final String REQUEST = "Request";
    public static final String QUERY_DELIMITER = "?";
    public static final String VALUE_DELIMITER = "=";
    public static final String PARAM_DELIMITER = "&";

    private final Logger log = LoggerFactory.getLogger(AccessKeyAuthFilter.class);
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String accessControl = servletRequest.getParameter(ACCESS_CONTROL);
        String accessKey = servletRequest.getParameter(ACCESS_KEY);
        String subjectId = servletRequest.getParameter(SUBJECT_ID);

        log.info("accessControl="+accessControl);
        log.info("accessKey="+accessKey);
        log.info("subjectId="+subjectId);

        String query = generateQueryStringByServletRequest(servletRequest);
        log.info("currentQuery()="+query);
        if (StringUtils.isNoneBlank(accessControl) && REQUEST.equalsIgnoreCase(accessControl)) {
            subjectId = query;
            log.info("subjectId(NEW)="+subjectId);
        }
        if (StringUtils.isNotBlank(accessKey) && StringUtils.isNotBlank(subjectId)) {
            log.info("accessKey&subjectId is Not Blank");
            AccessKeyAuthenticationToken oAccessKeyAuthenticationToken=new AccessKeyAuthenticationToken(accessKey, subjectId);
            log.info("oAccessKeyAuthenticationToken!=null:"+(oAccessKeyAuthenticationToken!=null));
            SecurityContextHolder.getContext().setAuthentication(oAccessKeyAuthenticationToken);
        }else{
            log.info("accessKey&subjectId is Blank!!!");
        }
        log.info("filterChain.doFilter");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String generateQueryStringByServletRequest(ServletRequest servletRequest) throws ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            return request.getContextPath().concat(request.getServletPath()).
                    concat(request.getPathInfo()).concat(QUERY_DELIMITER).
                    concat(getQueryStringWithoutAccessKey(request.getQueryString(), request));
        } else {
            throw new ServletException("Subject ID generation error. Can't detect servlet request");
        }
    }
    //FIXME Potential bug. If parameter will be in the first place it would never be replaced by this method!
    private String getQueryStringWithoutAccessKey(String queryString, HttpServletRequest request) {
        String key = request.getParameter(ACCESS_KEY);
        if (StringUtils.isBlank(key)) {
            return queryString.replaceAll(PARAM_DELIMITER.concat(ACCESS_KEY), StringUtils.EMPTY);
        } else {
            return queryString.replaceAll(PARAM_DELIMITER.concat(ACCESS_KEY).concat(VALUE_DELIMITER).
                    concat(key), StringUtils.EMPTY);
        }
    }
}
