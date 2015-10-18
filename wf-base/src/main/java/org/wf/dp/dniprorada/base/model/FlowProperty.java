package org.wf.dp.dniprorada.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The property of flow. Stored in regional server.
 * <p/>
 * User: goodg_000
 * Date: 14.06.2015
 * Time: 15:18
 */
@javax.persistence.Entity
public class FlowProperty extends Entity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nID_Flow_ServiceData")
    private Flow_ServiceData oFlow_ServiceData;

    @JsonProperty(value = "nID_FlowPropertyClass")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nID_FlowPropertyClass")
    private FlowPropertyClass oFlowPropertyClass;

    @Column
    private String sData;
    @Column(nullable = true)
    private Boolean bExclude;
    @Column(nullable = true)
    private String sName;
    @Column(nullable = true)
    private String sRegionTime;
    @Column(nullable = true)
    private String saRegionWeekDay;
    @Column(nullable = true)
    private String sDateTimeAt;
    @Column(nullable = true)
    private String sDateTimeTo;

    @JsonProperty(value = "nLen")
    @Column(nullable = true)
    private Integer nLen;

    @JsonProperty(value = "sLenType")
    @Column(nullable = true)
    private String sLenType;

    public Flow_ServiceData getoFlow_ServiceData() {
        return oFlow_ServiceData;
    }

    public void setoFlow_ServiceData(Flow_ServiceData oFlow_ServiceData) {
        this.oFlow_ServiceData = oFlow_ServiceData;
    }

    public FlowPropertyClass getoFlowPropertyClass() {
        return oFlowPropertyClass;
    }

    public void setoFlowPropertyClass(FlowPropertyClass oFlowPropertyClass) {
        this.oFlowPropertyClass = oFlowPropertyClass;
    }

    public String getsData() {
        return sData;
    }

    public void setsData(String sData) {
        this.sData = sData;
    }

    public Boolean getbExclude() {
        return bExclude;
    }

    public void setbExclude(Boolean bExclude) {
        this.bExclude = bExclude;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsRegionTime() {
        return sRegionTime;
    }

    public void setsRegionTime(String sRegionTime) {
        this.sRegionTime = sRegionTime;
    }

    public String getSaRegionWeekDay() {
        return saRegionWeekDay;
    }

    public void setSaRegionWeekDay(String saRegionWeekDay) {
        this.saRegionWeekDay = saRegionWeekDay;
    }

    @JsonProperty(value = "nLen")
    public Integer getLen() {
        return nLen;
    }

    public void setLen(Integer nLen) {
        this.nLen = nLen;
    }

    @JsonProperty(value = "sLenType")
    public String getLenType() {
        return sLenType;
    }

    public void setLenType(String sLenType) {
        this.sLenType = sLenType;
    }

    public String getsDateTimeAt() {
        return sDateTimeAt;
    }

    public void setsDateTimeAt(String sDateTimeAt) {
        this.sDateTimeAt = sDateTimeAt;
    }

    public String getsDateTimeTo() {
        return sDateTimeTo;
    }

    public void setsDateTimeTo(String sDateTimeTo) {
        this.sDateTimeTo = sDateTimeTo;
    }
}
