package org.wf.dp.dniprorada.model;

import org.wf.dp.dniprorada.base.model.NamedEntity;

import javax.persistence.Column;

/**
 * Store information specific to SubjectOrgan when it acts like ServiceOperator.
 * This entity stored in regional server.
 * <p/>
 * User: goodg_000
 * Date: 14.06.2015
 * Time: 14:31
 */
@javax.persistence.Entity
public class ServiceOperator_SubjectOrgan extends NamedEntity {

    /**
     * One-to-one soft reference to entity which is stored in central but not present in regional server.
     */
    @Column
    private Long nID_SubjectOrgan;

    public Long getnID_SubjectOrgan() {
        return nID_SubjectOrgan;
    }

    public void setnID_SubjectOrgan(Long nID_SubjectOrgan) {
        this.nID_SubjectOrgan = nID_SubjectOrgan;
    }
}
