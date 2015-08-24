package org.activity.rest.security;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author tasman
 */
public class AccessKeyAuthFilter extends GenericFilterBean {

    public static final String ACCESS_CONTROL = "sAccessContract";
    public static final String ACCESS_KEY = "sAccessKey";
    public static final String SUBJECT_ID = "nID_Subject";

    public static final String REQUEST = "Request";

    private final Logger log = LoggerFactory.getLogger(AccessKeyAuthFilter.class);
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String accessControl = servletRequest.getParameter(ACCESS_CONTROL);
        String accessKey = servletRequest.getParameter(ACCESS_KEY);
        String subjectId = servletRequest.getParameter(SUBJECT_ID);

        log.info("accessControl="+accessControl);
        log.info("accessKey="+accessKey);
        log.info("subjectId="+subjectId);

        String sRequest = URLEncodedUtils.format(createNameValueList(servletRequest), "UTF-8");
        log.info("sRequest="+sRequest);
        if (StringUtils.isNoneBlank(accessControl) && REQUEST.equalsIgnoreCase(accessControl)) {
            subjectId = sRequest;
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

    private List<? extends NameValuePair> createNameValueList(ServletRequest servletRequest) {
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        Enumeration<String> names = servletRequest.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (!ACCESS_KEY.equalsIgnoreCase(name)) {
                parameters.add(new BasicNameValuePair(name, servletRequest.getParameter(name)));
            }
        }
        return parameters;
    }
}
