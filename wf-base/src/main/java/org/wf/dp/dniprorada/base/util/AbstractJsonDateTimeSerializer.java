package org.wf.dp.dniprorada.base.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 14:11
 */
public abstract class AbstractJsonDateTimeSerializer extends JsonSerializer<DateTime> {

    private DateTimeFormatter formatter;

    protected AbstractJsonDateTimeSerializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void serialize(DateTime dateTime, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(formatter.print(dateTime));
    }
}
