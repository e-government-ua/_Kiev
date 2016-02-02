package org.activiti.explorer.servlet;

import org.activiti.explorer.conf.ApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
public class WebConfigurer implements ServletContextListener {

    private final Logger LOG = LoggerFactory.getLogger(WebConfigurer.class);

    public ApplicationContext context;

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        LOG.debug("Configuring Spring root application context");

        ApplicationContext rootContext;

        if (context == null) {
            AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
            ctx.register(ApplicationConfiguration.class);
            ctx.setServletContext(servletContext);
            ctx.refresh();
            rootContext = ctx;
        } else {
            rootContext = context;
        }

        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootContext);

        initSpring(servletContext, rootContext);

        LOG.debug("Web application fully configured");
    }

    /**
     * Initializes Spring and Spring MVC.
     */
    private ServletRegistration.Dynamic initSpring(ServletContext servletContext,
            ApplicationContext rootContext) {
        LOG.debug("Configuring Spring Web application context");
        AnnotationConfigWebApplicationContext dispatcherServletConfiguration = new AnnotationConfigWebApplicationContext();
        dispatcherServletConfiguration.setParent(rootContext);
        dispatcherServletConfiguration.register(DispatcherServletConfiguration.class);

        LOG.debug("Registering Spring MVC Servlet");
        ServletRegistration.Dynamic dispatcherServlet = servletContext
                .addServlet("dispatcher", new DispatcherServlet(dispatcherServletConfiguration));
        dispatcherServlet.addMapping("/service/*");
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.setAsyncSupported(true);

        return dispatcherServlet;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOG.info("Destroying Web application");
        WebApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
        AnnotationConfigWebApplicationContext gwac = (AnnotationConfigWebApplicationContext) ac;
        gwac.close();
        LOG.debug("Web application destroyed");
    }
}
