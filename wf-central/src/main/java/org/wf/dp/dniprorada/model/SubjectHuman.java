package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.wf.dp.dniprorada.base.model.NamedEntity;

import javax.persistence.*;

@javax.persistence.Entity
@AttributeOverrides({ @AttributeOverride(name = "name",
        column = @Column(name = "sName", nullable = true)) })
public class SubjectHuman extends NamedEntity {

    @JsonProperty(value = "oSubject")
    @OneToOne
    @Cascade({ CascadeType.SAVE_UPDATE })
    @JoinColumn(name = "nID_Subject", nullable = false)
    private Subject oSubject;

    @JsonProperty(value = "sINN")
    @Column(name = "sINN", nullable = false)
    private String sINN;

    @JsonProperty(value = "sSB")
    @Column(name = "sSB", nullable = true)
    private String sSB;

    @JsonProperty(value = "sPassportSeria")
    @Column(name = "sPassportSeria", nullable = true)
    private String sPassportSeria;

    @JsonProperty(value = "sPassportNumber")
    @Column(name = "sPassportNumber", nullable = true)
    private String sPassportNumber;

    @JsonProperty(value = "sFamily")
    @Column(name = "sFamily", nullable = true)
    private String sFamily;

    @JsonProperty(value = "sSurname")
    @Column(name = "sSurname", nullable = true)
    private String sSurname;

    public Subject getoSubject() {
        return oSubject;
    }

    public void setoSubject(Subject oSubject) {
        this.oSubject = oSubject;
    }

    public String getsSB() {
        return sSB;
    }

    public void setsSB(String sSB) {
        this.sSB = sSB;
    }

    public String getsINN() {
        return sINN;
    }

    public void setsINN(String sINN) {
        this.sINN = sINN;
    }

    public String getsPassportSeria() {
        return sPassportSeria;
    }

    public void setsPassportSeria(String sPassportSeria) {
        this.sPassportSeria = sPassportSeria;
    }

    public String getsPassportNumber() {
        return sPassportNumber;
    }

    public void setsPassportNumber(String sPassportNumber) {
        this.sPassportNumber = sPassportNumber;
    }

    public String getsFamily() {
        return sFamily;
    }

    public void setsFamily(String sFamily) {
        this.sFamily = sFamily;
    }

    public String getsSurname() {
        return sSurname;
    }

    public void setsSurname(String sSurname) {
        this.sSurname = sSurname;
    }

}
