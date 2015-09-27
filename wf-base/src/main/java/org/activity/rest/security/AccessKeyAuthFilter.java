package org.activity.rest.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author tasman
 */
public class AccessKeyAuthFilter extends GenericFilterBean {

    private final Logger log = LoggerFactory.getLogger(AccessKeyAuthFilter.class);
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        AuthenticationTokenSelector oAuthenticationTokenSelector = new AuthenticationTokenSelector(servletRequest);
        AccessKeyAuthenticationToken oAccessKeyAuthenticationToken = oAuthenticationTokenSelector.createToken();
        if (oAccessKeyAuthenticationToken.hasCorrectAccessKeyAndSubjectId()) {
            log.info("sAccessKey is Not Blank");
            //log.info("oAccessKeyAuthenticationToken!=null:" + (token != null));
            SecurityContextHolder.getContext().setAuthentication(oAccessKeyAuthenticationToken);
        } else {
            log.info("sAccessKey is Blank!!!");
        }
        log.info("filterChain.doFilter");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
