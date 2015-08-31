package org.wf.dp.dniprorada.util.rest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dmytro Tsapko on 8/26/2015.
 */

public class ResponseErrHandler implements ResponseErrorHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseErrHandler.class);
    private static final List<Integer> statuses = Arrays.asList(200, 301, 302, 303);

    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !statuses.contains(response.getRawStatusCode());
    }

    public void handleError(ClientHttpResponse response) throws IOException {
        String theString = IOUtils.toString(response.getBody());

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("code", response.getStatusCode().toString());
        properties.put("body", theString);
        properties.put("header", response.getHeaders());
        LOG.error("REST request error: ", properties);
        throw new RestClientException("REST request error " + properties);
    }
}
