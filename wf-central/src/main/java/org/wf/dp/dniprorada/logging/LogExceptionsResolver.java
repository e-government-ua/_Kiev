package org.wf.dp.dniprorada.logging;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: goodg_000
 * Date: 11.05.2015
 * Time: 20:11
 */
public class LogExceptionsResolver extends AbstractHandlerMethodExceptionResolver {
    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Exception ex) {
        String requestInfo = "Undefined";
        if (request != null) {
            requestInfo = "" + request.getMethod() + " " + request.getContextPath() + request.getPathInfo();
            if (request.getQueryString() != null) {
                requestInfo += "?" + request.getQueryString();
            }
        }

        logger.error("Exception while execution request " + requestInfo, ex);

        return null;
    }
}
