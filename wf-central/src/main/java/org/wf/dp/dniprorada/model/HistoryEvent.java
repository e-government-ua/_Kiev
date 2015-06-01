package org.wf.dp.dniprorada.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import net.sf.brunneng.jom.annotations.Identifier;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.constant.HistoryEventType;

@javax.persistence.Entity
public class HistoryEvent {

    @JsonProperty(value = "nID")
    @Id //@GeneratedValue
    @GeneratedValue
    @Column(name = "nID")
    private Long id;

    @JsonProperty(value = "nID_Subject")
    @Column(name = "nID_Subject", nullable = true)
    private Long subjectKey;

    @JsonProperty(value = "nID_HistoryEventType")
    @Column(name = "nID_HistoryEventType", nullable = true)
    private Long historyEventTypeKey;

    @JsonProperty(value = "sEventName_Custom")
    @Column(name = "sEventName_Custom", nullable = true)
    private String eventNameCustom;

    @JsonProperty(value = "sMessage")
    @Column(name = "sMessage", nullable = false)
    private String sMessage;

    @JsonProperty(value="oDate")
    @Column(name = "sDate", nullable = true)
    private Date date;

    @JsonProperty(value="sDate")
    @Transient
    private String sDate;

    @Identifier
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubjectKey() {
        return (this.subjectKey == null) ? 0 : subjectKey;
    }

    public void setSubjectKey(Long subjectKey) {
        this.subjectKey = subjectKey;
    }

    public Long getHistoryEventTypeKey() {
        return (this.historyEventTypeKey == null) ? 0 : historyEventTypeKey;
    }

    public void setHistoryEventTypeKey(Long historyEventTypeKey) {
        this.historyEventTypeKey = historyEventTypeKey;
    }

    public String getEventNameCustom() {
        return (this.eventNameCustom == null) ? "" : eventNameCustom;
    }
    public String getEventNameCustom(int historyEventTypeKey) {
        switch (historyEventTypeKey){
            case(0) : {
                return this.eventNameCustom;
            }
            case(1) : {
                return HistoryEventType.GET_SERVICE.getsName();
            }
            case(2) : {
                return HistoryEventType.SET_DOCUMENT_INTERNAL.getsName();
            }
            case(3) : {
                return HistoryEventType.SET_DOCUMENT_EXTERNAL.getsName();
            }
            case(4) : {
                return HistoryEventType.SET_DOCUMENT_ACCESS_LINK.getsName();
            }
            case(5) : {
                return HistoryEventType.SET_DOCUMENT_ACCESS.getsName();
            }
            case(6) : {
                return HistoryEventType.ACTIVITY_STATUS_NEW.getsName();
            }
        }

        return "";
    }

    public void setEventNameCustom(String eventNameCustom) {
        this.eventNameCustom = eventNameCustom;
    }

    public String getsMessage() {
        return sMessage;
    }

    public void setsMessage(String sMessage) {
        this.sMessage = sMessage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Transient
    public String getsDate() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(date);
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }
}
