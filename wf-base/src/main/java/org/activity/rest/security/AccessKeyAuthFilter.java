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
 * @author tasman edited by Olga Turenko & Belyavtsev Vladimir (BW)
 */
public class AccessKeyAuthFilter extends GenericFilterBean {

    private final Logger oLog = LoggerFactory.getLogger(AccessKeyAuthFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        AuthenticationTokenSelector oAuthenticationTokenSelector = new AuthenticationTokenSelector(servletRequest);
        AccessKeyAuthenticationToken oAccessKeyAuthenticationToken = oAuthenticationTokenSelector.createToken();
        if (oAccessKeyAuthenticationToken != null && oAccessKeyAuthenticationToken.isNotEmpty()) {
            oLog.info("[doFilter]:sID&sSecret isNotEmpty!");
            //log.info("oAccessKeyAuthenticationToken!=null:" + (token != null));
            SecurityContextHolder.getContext().setAuthentication(oAccessKeyAuthenticationToken);
        } else {
            oLog.info("[doFilter]:sID||sSecret Empty!!!");
        }
        //oLog.info("[doFilter]");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
