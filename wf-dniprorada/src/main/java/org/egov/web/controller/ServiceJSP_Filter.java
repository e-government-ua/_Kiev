package org.egov.web.controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "ProxyFilter", urlPatterns = {"*/index","*/startForm","*/startedProcess"})
public class ServiceJSP_Filter implements Filter {

    @Override
    public void init(FilterConfig config)
            throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
            HttpServletRequest oRequest = (HttpServletRequest) request;
            String sRequestURL = oRequest.getRequestURL().toString();
            //String sPath = "wf-dniprorada/";
            //String sPath = "wf-dniprorada/";
            String sPath = "service/";
            int nAt=sRequestURL.indexOf(sPath);
            System.out.println("Before_sRequestURL="+sRequestURL);
            if(nAt>=0){
                //sRequestURL = ((sRequestURL.replaceAll(sPath, "/"+sPath+"WEB-INF/jsp"))+".jsp").substring(nAt);
                //sRequestURL = ((sRequestURL.replaceAll(sPath, "/WEB-INF/jsp"+sPath+""))+".jsp").substring(nAt);
                sRequestURL = ((sRequestURL.replaceAll("service/rest/", "WEB-INF/jsp/"))+".jsp");
                ((HttpServletResponse) response).sendRedirect(sRequestURL);//"/index.jsp"
                System.out.println("After_sRequestURL="+sRequestURL);
            }else{
                chain.doFilter(request, response);
            }
            //-resolver.setPrefix("/WEB-INF/jsp/");
            //-resolver.setSuffix(".jsp");
            
    }

    @Override
    public void destroy() {
    }
}
