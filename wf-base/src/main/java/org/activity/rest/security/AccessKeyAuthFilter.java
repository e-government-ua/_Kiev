package org.activity.rest.security;

import org.apache.commons.lang3.*;
import org.springframework.security.core.context.*;
import org.springframework.web.filter.*;

import javax.servlet.*;
import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tasman
 */
public class AccessKeyAuthFilter extends GenericFilterBean {

    public static final String ACCESS_KEY = "sAccessKey";
    public static final String SUBJECT_ID = "nID_Subject";

    private final Logger log = LoggerFactory.getLogger(AccessKeyAuthFilter.class);
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String accessKey = servletRequest.getParameter(ACCESS_KEY);
        String subjectId = servletRequest.getParameter(SUBJECT_ID);
        
        log.info("accessKey="+accessKey);
        log.info("subjectId="+subjectId);
        
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
}
