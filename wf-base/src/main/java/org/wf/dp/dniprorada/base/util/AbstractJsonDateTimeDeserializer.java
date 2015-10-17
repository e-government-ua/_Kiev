package org.wf.dp.dniprorada.base.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 14:11
 */
public abstract class AbstractJsonDateTimeDeserializer extends JsonDeserializer<DateTime> {

    private DateTimeFormatter formatter;

    protected AbstractJsonDateTimeDeserializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public DateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        return formatter.parseDateTime(jsonParser.getValueAsString());
    }
}
