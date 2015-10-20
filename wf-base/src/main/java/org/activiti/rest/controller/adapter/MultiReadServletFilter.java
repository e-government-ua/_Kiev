/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.activiti.rest.controller.adapter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author olya
 */
public class MultiReadServletFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            MultiReadHttpServletRequest requestMultiRead = new MultiReadHttpServletRequest(request);
            MultiReaderHttpServletResponse responseMultiRead = new MultiReaderHttpServletResponse(response,
                    requestMultiRead);
            requestMultiRead.setAttribute("responseMultiRead", responseMultiRead);
            filterChain.doFilter(requestMultiRead, responseMultiRead);
            return;
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
