package org.wf.dp.dniprorada.base.model;

import javax.persistence.Column;

/**
 * Handler Class for FlowProperty
 * <p/>
 * User: goodg_000
 * Date: 14.06.2015
 * Time: 15:11
 */
@javax.persistence.Entity
public class FlowPropertyClass extends NamedEntity {

    /**
     * Fully qualified class name. I.e. package.className
     */
    @Column
    private String sPath;

    /**
     * Optional bean name of bean of corresponding class. Allows to use same class with different bean configurations.
     */
    @Column
    private String sBeanName;

    public String getsPath() {
        return sPath;
    }

    public void setsPath(String sPath) {
        this.sPath = sPath;
    }

    public String getsBeanName() {
        return sBeanName;
    }

    public void setsBeanName(String sBeanName) {
        this.sBeanName = sBeanName;
    }
}
