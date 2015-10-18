package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.sf.brunneng.jom.annotations.Identifier;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.wf.dp.dniprorada.base.util.JsonDateTimeDeserializer;
import org.wf.dp.dniprorada.base.util.JsonDateTimeSerializer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "DocumentAccess")
public class DocumentAccess extends org.wf.dp.dniprorada.base.model.Entity {

    @Column
    private Long nID_Document;

    @JsonSerialize(using = JsonDateTimeSerializer.class)
    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    @Type(type = DATETIME_TYPE)
    @Column
    private DateTime sDateCreate;

    @Column
    private Long nMS;

    @Column
    private String sFIO;

    @Column
    private String sTarget;

    @Column
    private String sTelephone;

    @Column
    private String sMail;

    @Column
    private String sSecret;

    @Column
    private String sAnswer;

    @JsonSerialize(using = JsonDateTimeSerializer.class)
    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    @Type(type = DATETIME_TYPE)
    @Column
    private DateTime sDateAnswerExpire;

    @Column
    private String sCode;

    @Column
    private String sCodeType;

    @Identifier
    public Long getID_Document() {
        return nID_Document;
    }

    public void setID_Document(Long nID_Document) {
        this.nID_Document = nID_Document;
    }

    public DateTime getDateCreate() {
        return sDateCreate;
    }

    public void setDateCreate(DateTime sDateCreate) {
        this.sDateCreate = sDateCreate;
    }

    public Long getMS() {
        return nMS;
    }

    public void setMS(Long n) {
        this.nMS = n;
    }

    public String getFIO() {
        return sFIO;
    }

    public void setFIO(String sFIO) {
        this.sFIO = sFIO;
    }

    public String getTarget() {
        return sTarget;
    }

    public void setTarget(String sTarget) {
        this.sTarget = sTarget;
    }

    public String getTelephone() {
        return sTelephone;
    }

    public void setTelephone(String sTelephone) {
        this.sTelephone = sTelephone;
    }

    public String getMail() {
        return sMail;
    }

    public void setMail(String sMail) {
        this.sMail = sMail;
    }

    public String getSecret() {
        return sSecret;
    }

    public void setSecret(String sSecret) {
        this.sSecret = sSecret;
    }

    public String getAnswer() {
        return sAnswer;
    }

    public void setAnswer(String s) {
        this.sAnswer = s;
    }

    public DateTime getDateAnswerExpire() {
        return sDateAnswerExpire;
    }

    public void setDateAnswerExpire(DateTime sDateAnswerExpire) {
        this.sDateAnswerExpire = sDateAnswerExpire;
    }

    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    public String getsCodeType() {
        return sCodeType;
    }

    public void setsCodeType(String sCodeType) {
        this.sCodeType = sCodeType;
    }

    @Override
    public String toString() {
        return "{\n" + "nID:" + getId() + "\nnID_Document:" + nID_Document + "sDateCreate:" + sDateCreate +
                "\nnMS:" + nMS + "\nsFIO:" + sFIO + "\nsTarget:" + sTarget + "\nsTelephone:" + sTelephone +
                "\nsMail:" + sMail + "\nsSecret:" + sSecret + "\nsAnswer:" + sAnswer + "" +
                "\nsDateAnswerExpire:" + sDateAnswerExpire +
                "\nsCode:" + sCode + "\nsCodeType:" + sCodeType + "}";
    }
}