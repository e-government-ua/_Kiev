package org.activiti.explorer.conf;

import org.springframework.context.annotation.*;

@Configuration
@PropertySources({
        @PropertySource(value = "classpath:db.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:engine.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:bankID.properties", ignoreResourceNotFound = true) })
@ComponentScan(basePackages = { "org.activiti.explorer.conf",
        "org.egov.web.controller", "org.activiti.rest.controller",
        "org.activiti.rest.conf", "org.activiti.rest.service.api",
        "org.wf.dp.dniprorada.engine.task" })
@ImportResource({ "classpath:context.xml" })
/*@ImportResource({ "classpath:activiti-ui-context.xml",
        "classpath:activiti-login-context.xml",
		"classpath:activiti-custom-context.xml" })*/

public class ApplicationConfiguration {

}
