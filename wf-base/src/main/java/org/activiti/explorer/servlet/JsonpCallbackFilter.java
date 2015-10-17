package org.activiti.explorer.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class JsonpCallbackFilter implements Filter {

    private static Logger log = LoggerFactory.getLogger(JsonpCallbackFilter.class);

    public void init(FilterConfig fConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String sRequestURL = httpRequest.getRequestURL().toString();
        //String sPath = "wf/";
        //String sPath = "wf/";
        //"*/index","*/startForm","*/startedProcess"
        if (sRequestURL.endsWith("/index")
                || sRequestURL.endsWith("/startForm")
                || sRequestURL.endsWith("/startedProcess")) {
            String sPath = "service/";
            int nAt = sRequestURL.indexOf(sPath);
            log.info("Before_sRequestURL {} ", sRequestURL);
            //        System.out.println("Before_sRequestURL="+sRequestURL);
            if (nAt >= 0) {
                //sRequestURL = ((sRequestURL.replaceAll(sPath, "/"+sPath+"WEB-INF/jsp"))+".jsp").substring(nAt);
                //sRequestURL = ((sRequestURL.replaceAll(sPath, "/WEB-INF/jsp"+sPath+""))+".jsp").substring(nAt);
                sRequestURL = ((sRequestURL.replaceAll("service/rest/", "WEB-INF/jsp/")) + ".jsp");
                //            System.out.println("After_sRequestURL="+sRequestURL);
                log.info("After_sRequestURL {} ", sRequestURL);
                ((HttpServletResponse) response).sendRedirect(sRequestURL);//"/index.jsp"
        /*}else{
            chain.doFilter(request, response);*/
            }
        }

        //-resolver.setPrefix("/WEB-INF/jsp/");
        //-resolver.setSuffix(".jsp");

        Map<String, String[]> parms = httpRequest.getParameterMap();

        if (parms.containsKey("callback")) {
            if (log.isDebugEnabled())
                log.debug("Wrapping response with JSONP callback '" + parms.get("callback")[0] + "'");

            OutputStream out = httpResponse.getOutputStream();

            GenericResponseWrapper wrapper = new GenericResponseWrapper(httpResponse);

            chain.doFilter(request, wrapper);

            //handles the content-size truncation
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(new String(parms.get("callback")[0] + "(").getBytes());
            outputStream.write(wrapper.getData());
            outputStream.write(new String(");").getBytes());
            byte jsonpResponse[] = outputStream.toByteArray();

            wrapper.setContentType("text/javascript;charset=UTF-8");
            wrapper.setContentLength(jsonpResponse.length);

            out.write(jsonpResponse);

            out.close();

        } else {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }
}
