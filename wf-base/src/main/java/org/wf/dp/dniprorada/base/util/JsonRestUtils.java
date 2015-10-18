package org.wf.dp.dniprorada.base.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.wf.dp.dniprorada.base.viewobject.ResultMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * User: goodg_000
 * Date: 09.05.2015
 * Time: 0:40
 */
public final class JsonRestUtils {

    private static final Log LOG = LogFactory.getLog(JsonRestUtils.class);

    private JsonRestUtils() {
    }

    public static <T> T readObject(String jsonData, Class<T> clazz) {
        T res = null;
        try {
            res = new ObjectMapper().readValue(jsonData, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res;
    }

    public static <T> T readObject(InputStream is, Class<T> clazz) {
        T res = null;
        try {
            res = new ObjectMapper().readValue(is, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res;
    }

    public static String toJson(Object res) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        prepareMapperForOutput(mapper);

        return mapper.writeValueAsString(res);
    }

    public static void writeJsonToOutputStream(Object res, OutputStream os) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        prepareMapperForOutput(mapper);

        mapper.writeValue(os, res);
    }

    private static void prepareMapperForOutput(ObjectMapper mapper) {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static ResponseEntity<String> toJsonResponse(HttpStatus httpStatus, Object res) {
        String json;
        try {
            json = toJson(res);
        } catch (JsonProcessingException e) {
            LOG.error("Exception happen during convert object " + res + " to json.", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();

        // UTF-8 needed for correctly encode ukrainian letters.
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        headers.setContentType(mediaType);

        return new ResponseEntity<>(json, headers, httpStatus);
    }

    public static ResponseEntity<String> toJsonResponse(Object res) {
        return toJsonResponse(HttpStatus.OK, res);
    }

    public static ResponseEntity<String> toJsonResponse(HttpStatus httpStatus, ResultMessage resultMessage) {
        String json;
        try {
            json = toJson(resultMessage);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders headers = new HttpHeaders();

        // UTF-8 needed for correctly encode ukrainian letters.
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        headers.setContentType(mediaType);
        return new ResponseEntity<>(json, headers, httpStatus);
    }

}
