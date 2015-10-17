package org.wf.dp.dniprorada.model;

import org.wf.dp.dniprorada.base.model.NamedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * User: goodg_000
 * Date: 12.07.2015
 * Time: 21:17
 */
@Entity
public class SubjectMessageType extends NamedEntity {

    public static final SubjectMessageType DEFAULT;

    static {
        DEFAULT = new SubjectMessageType();
        DEFAULT.setId(0L);
    }

    @Column
    private String sDescription;

    public String getsDescription() {
        return sDescription;
    }

    public void setsDescription(String sDescription) {
        this.sDescription = sDescription;
    }
}
