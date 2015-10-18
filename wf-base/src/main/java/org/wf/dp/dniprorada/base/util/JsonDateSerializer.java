package org.wf.dp.dniprorada.base.util;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 14:35
 */
public class JsonDateSerializer extends AbstractJsonDateTimeSerializer {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");

    protected JsonDateSerializer() {
        super(DATE_FORMATTER);
    }
}
