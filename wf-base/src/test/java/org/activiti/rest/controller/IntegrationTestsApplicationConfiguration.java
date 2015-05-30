package org.activiti.rest.controller;

import org.activiti.explorer.conf.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Created by diver on 4/12/15.
 */
@EnableWebMvc
@Configuration
@Import(ApplicationConfiguration.class)
@ImportResource("classpath:mock-beans.xml")
public class IntegrationTestsApplicationConfiguration {

}
