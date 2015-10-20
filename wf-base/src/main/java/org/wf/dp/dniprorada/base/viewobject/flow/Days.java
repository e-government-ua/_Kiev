package org.wf.dp.dniprorada.base.viewobject.flow;

import java.util.ArrayList;
import java.util.List;

/**
 * User: goodg_000
 * Date: 21.06.2015
 * Time: 14:06
 */
public class Days {

    private List<Day> aDay = new ArrayList<>();

    public Days() {
    }

    public Days(List<Day> aDay) {
        this.aDay = aDay;
    }

    public List<Day> getaDay() {
        return aDay;
    }

    public void setaDay(List<Day> aDay) {
        this.aDay = aDay;
    }
}
