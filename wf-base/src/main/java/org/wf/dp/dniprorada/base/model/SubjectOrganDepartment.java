package org.wf.dp.dniprorada.base.model;

import javax.persistence.Column;

/**
 * User: goodg_000
 * Date: 14.06.2015
 * Time: 14:57
 */
@javax.persistence.Entity
public class SubjectOrganDepartment extends Entity {

    @Column
    private String sName;

    @Column
    private Long nID_SubjectOrgan;

    @Column
    private String sGroup_Activiti;

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public Long getnID_SubjectOrgan() {
        return nID_SubjectOrgan;
    }

    public void setnID_SubjectOrgan(Long nID_SubjectOrgan) {
        this.nID_SubjectOrgan = nID_SubjectOrgan;
    }

    public String getsGroup_Activiti() {
        return sGroup_Activiti;
    }

    public void setsGroup_Activiti(String sGroup_Activiti) {
        this.sGroup_Activiti = sGroup_Activiti;
    }
}
