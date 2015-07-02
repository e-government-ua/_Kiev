/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.activiti.rest.controller.adapter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author olya
 */
public class MultiReadServletFilter implements Filter {

    /*private static final Set<String> MULTI_READ_HTTP_METHODS = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) {{
     // Enable Multi-Read for PUT and POST requests
     add("PUT");
     add("POST");
     }};*/
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            // Check wether the current request needs to be able to support the body to be read multiple times
            //if(MULTI_READ_HTTP_METHODS.contains(request.getMethod())) {
            // Override current HttpServletRequest with custom implementation
            filterChain.doFilter(new MultiReadHttpServletRequest(request), servletResponse);
            return;
            //}
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
