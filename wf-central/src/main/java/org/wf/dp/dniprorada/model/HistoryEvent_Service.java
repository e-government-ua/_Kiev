package org.wf.dp.dniprorada.model;

import org.wf.dp.dniprorada.base.model.Entity;

import javax.persistence.Column;

@javax.persistence.Entity
public class HistoryEvent_Service extends Entity {

    @Column
    private String sID;

    @Column
    private Long nID_Protected;

    @Column
    private Long nID_Task;

    @Column
    private Long nID_Subject;

    @Column
    private String sStatus;

    @Column
    private String sID_Status;

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public Long getnID_Protected() {
        return nID_Protected;
    }

    public void setnID_Protected(Long nID_Protected) {
        this.nID_Protected = nID_Protected;
    }

    public Long getnID_Task() {
        return nID_Task;
    }

    public void setnID_Task(Long nID_Task) {
        this.nID_Task = nID_Task;
    }

    public Long getnID_Subject() {
        return nID_Subject;
    }

    public void setnID_Subject(Long nID_Subject) {
        this.nID_Subject = nID_Subject;
    }

    public String getsStatus() {
        return sStatus;
    }

    public void setsStatus(String sStatus) {
        this.sStatus = sStatus;
    }

    public String getsID_Status() {
        return sID_Status;
    }

    public void setsID_Status(String sID_Status) {
        this.sID_Status = sID_Status;
    }
}
