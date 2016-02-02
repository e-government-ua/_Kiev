package org.activiti.explorer.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.activiti.rest.service.api.PutAwareCommonsMultipartResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

@Configuration
@ComponentScan({"org.activiti.rest.editor", "org.activiti.rest.diagram",
    "org.igov.service.controller", "org.igov.service.conf",
    "org.activiti.rest.service.api"})
@EnableAsync
public class DispatcherServletConfiguration extends WebMvcConfigurationSupport {

    private final Logger LOG = LoggerFactory
            .getLogger(DispatcherServletConfiguration.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public SessionLocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LOG.debug("Configuring localeChangeInterceptor");
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Bean
    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        LOG.debug("Creating requestMappingHandlerMapping");
        RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.setUseSuffixPatternMatch(false);
        Object[] interceptors = {localeChangeInterceptor()};
        requestMappingHandlerMapping.setInterceptors(interceptors);
        return requestMappingHandlerMapping;
    }

    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        addDefaultHttpMessageConverters(converters);
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                jackson2HttpMessageConverter.setObjectMapper(objectMapper);
                break;
            }
        }
    }

    @Override
    protected void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    /* REST API */
    @Bean
    public InternalResourceViewResolver configureInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        /*
        resolver.setPrefix("/WEB-INF/jsp/");
		resolver.setSuffix(".jsp");
         */
        /*
        if(resolver.getApplicationContext()!=null){
                    log.info("!!!!!!!!!!!!!!!resolver.getApplicationContext().getApplicationName()="+resolver.getApplicationContext().getApplicationName());
                }else{
                    log.info("!!!!!!!!!!!!!!!resolver.getApplicationContext()=null");
                }*/
        return resolver;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        PutAwareCommonsMultipartResolver multipartResolver = new PutAwareCommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        return multipartResolver;
    }

}
