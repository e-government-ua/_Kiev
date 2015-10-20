package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.wf.dp.dniprorada.base.model.NamedEntity;

import javax.persistence.*;

@javax.persistence.Entity
@AttributeOverrides({ @AttributeOverride(name = "name",
        column = @Column(name = "sName", nullable = true)) })
public class SubjectOrgan extends NamedEntity {

    @JsonProperty(value = "oSubject")
    @OneToOne
    @Cascade({ CascadeType.SAVE_UPDATE })
    @JoinColumn(name = "nID_Subject", nullable = false)
    private Subject oSubject;

    @JsonProperty(value = "sOKPO")
    @Column(name = "sOKPO", nullable = false)
    private String sOKPO;

    @JsonProperty(value = "sFormPrivacy")
    @Column(name = "sFormPrivacy", nullable = true)
    private String sFormPrivacy;

    @JsonProperty(value = "sNameFull")
    @Column(name = "sNameFull", nullable = true)
    private String sNameFull;

    public Subject getoSubject() {
        return oSubject;
    }

    public void setoSubject(Subject oSubject) {
        this.oSubject = oSubject;
    }

    public String getsOKPO() {
        return sOKPO;
    }

    public void setsOKPO(String sOKPO) {
        this.sOKPO = sOKPO;
    }

    public String getsFormPrivacy() {
        return sFormPrivacy;
    }

    public void setsFormPrivacy(String sFormPrivacy) {
        this.sFormPrivacy = sFormPrivacy;
    }

    public String getsNameFull() {
        return sNameFull;
    }

    public void setsNameFull(String sNameFull) {
        this.sNameFull = sNameFull;
    }

}
