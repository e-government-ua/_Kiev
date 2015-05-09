package org.activiti.rest.conf;

import org.activiti.rest.common.application.ContentTypeResolver;
import org.activiti.rest.common.application.DefaultContentTypeResolver;
import org.activiti.rest.service.api.RestResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Joram Barrez
 */
@Configuration
public class RestConfiguration {
	private final Logger log = LoggerFactory
			.getLogger(RestConfiguration.class);

	
	@Bean()
	public RestResponseFactory restResponseFactory() {
		RestResponseFactory restResponseFactory = new RestResponseFactory();
		log.info("restResponseFactory init");
		return restResponseFactory;
	}

	@Bean()
	public ContentTypeResolver contentTypeResolver() {
		ContentTypeResolver resolver = new DefaultContentTypeResolver();
		return resolver;
	}
}
