package org.wf.dp.dniprorada.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.wf.dp.dniprorada.model.Service;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * User: goodg_000
 * Date: 09.05.2015
 * Time: 0:40
 */
public final class JsonRestUtils {

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

   public static ResponseEntity toJsonResponse(Object res) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

      String json;
      try {
         json = mapper.writeValueAsString(res);
      } catch (JsonProcessingException e) {
         return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }

      HttpHeaders headers = new HttpHeaders();

      // UTF-8 needed for correctly encode ukrainian letters.
      MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
      headers.setContentType(mediaType);

      return new ResponseEntity<>(json, headers, HttpStatus.OK);
   }
}
