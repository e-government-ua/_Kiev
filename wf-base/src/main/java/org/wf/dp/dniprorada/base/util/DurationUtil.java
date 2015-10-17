package org.wf.dp.dniprorada.base.util;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 19:56
 */
public class DurationUtil {

    private static DatatypeFactory durationFactory;

    static {
        try {
            durationFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Duration parseDuration(String durationString) {
        return durationFactory.newDuration(durationString);
    }
}
