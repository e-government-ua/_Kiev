package org.activiti.explorer.conf;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Joram Barrez
 */
@Configuration
public class JacksonConfiguration {

    @Bean()
    public ObjectMapper objectMapper() {
        // To avoid instantiating and configuring the mapper everywhere
        ObjectMapper mapper = new ObjectMapper();
        
        /*15.03.2015 maxtmn add some features*/
        mapper.disable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        mapper.disable(SerializationFeature.WRAP_ROOT_VALUE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        return mapper;
    }

}
