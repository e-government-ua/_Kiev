package org.wf.dp.dniprorada.base.util;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 14:24
 */
public class JsonDateTimeDeserializer extends AbstractJsonDateTimeDeserializer {

    public JsonDateTimeDeserializer() {
        super(JsonDateTimeSerializer.DATETIME_FORMATTER);
    }
}
