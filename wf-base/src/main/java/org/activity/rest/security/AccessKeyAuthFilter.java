package org.activity.rest.security;

import org.apache.commons.lang3.*;
import org.springframework.security.core.context.*;
import org.springframework.web.filter.*;

import javax.servlet.*;
import java.io.*;

/**
 * @author tasman
 */
public class AccessKeyAuthFilter extends GenericFilterBean {

    public static final String ACCESS_KEY = "sAccessKey";
    public static final String SUBJECT_ID = "nID_Subject";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String accessKey = servletRequest.getParameter(ACCESS_KEY);
        String subjectId = servletRequest.getParameter(SUBJECT_ID);
        if (StringUtils.isNotBlank(accessKey) && StringUtils.isNotBlank(subjectId)) {
            SecurityContextHolder.getContext().setAuthentication(new AccessKeyAuthenticationToken(accessKey, subjectId));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
