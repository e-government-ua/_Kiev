package org.wf.dp.dniprorada.base.util;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 14:37
 */
public class JsonDateTimeSerializer extends AbstractJsonDateTimeSerializer {

    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");

    protected JsonDateTimeSerializer() {
        super(DATETIME_FORMATTER);
    }
}
