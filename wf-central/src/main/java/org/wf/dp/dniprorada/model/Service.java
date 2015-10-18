package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.wf.dp.dniprorada.util.Util;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: goodg_000
 * Date: 04.05.2015
 * Time: 23:10
 */
@javax.persistence.Entity
public class Service extends org.wf.dp.dniprorada.base.model.NamedEntity {

    private static final String BASE_INFO_PATTERN_FILE_PATH = "patterns/services/Info";
    private static final String BASE_FAQ_PATTERN_FILE_PATH = "patterns/services/FAQ";
    private static final String BASE_LAW_PATTERN_FILE_PATH = "patterns/services/Law";

    @JsonProperty(value = "nOrder")
    @Column(name = "nOrder", nullable = false)
    private Integer order;

    @JsonProperty(value = "oSubcategory")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nID_Subcategory", nullable = false)
    private Subcategory subcategory;

    @JsonProperty(value = "aServiceData")
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<ServiceData> serviceDataList = new ArrayList<>();

    @JsonProperty(value = "sInfo")
    @Column(name = "sInfo", nullable = false)
    private String info;

    /*@JsonProperty(value = "bTest")
    @Column(name = "bTest", nullable = false)
    private boolean bTest;


    public boolean isTest() {
            return bTest;
    }

    public void setTest(boolean b) {
            this.bTest = b;
    }*/
    @Column(name = "sFAQ", nullable = false)
    @JsonProperty("sFAQ")
    private String faq;

    @Column(name = "sLaw", nullable = false)
    @JsonProperty("sLaw")
    private String law;

    @JsonProperty(value = "sSubjectOperatorName")
    @Column(name = "sSubjectOperatorName", nullable = false)
    private String sSubjectOperatorName;

    @Transient
    private int sub = 0;

    @Transient
    private int nStatus = 0;

    public String getSubjectOperatorName() {
        return sSubjectOperatorName;
    }

    public void setSubjectOperatorName(String s) {
        this.sSubjectOperatorName = s;
    }

    @JsonProperty(value = "nSub")
    public int getSub() {
        return sub;
    }

    public void setSub(int n) {
        sub = n;
    }

    @JsonProperty(value = "nStatus")
    public int getStatus() {
        return nStatus;
    }

    public void setStatus(int n) {
        nStatus = n;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    @JsonGetter("aServiceData")
    public List<ServiceData> getServiceDataFiltered(boolean flag) {
        if (serviceDataList == null) {
            return null;
        }

        boolean bTest = flag;
        List<ServiceData> res = new ArrayList<>();
        for (ServiceData oServiceData : serviceDataList) {
            if (!oServiceData.isHidden() && (bTest || !oServiceData.isTest())) {
                res.add(oServiceData);
            }
        }
        return res;
    }

    //0 - none
    //1 - test
    //2 - prod
    @JsonGetter("nID_Status")
    public int getStatusID() {
        if (serviceDataList == null) {
            return 0;
        }

        if (getSub() == 0) {
            for (ServiceData oServiceData : serviceDataList) {
                if (oServiceData.isTest() && !oServiceData.isHidden()) {
                    return 1;
                }
            }
            return 0;
        } else {
            for (ServiceData oServiceData : serviceDataList) {
                if (!oServiceData.isTest() && !oServiceData.isHidden()) {
                    return 2;
                }
            }
            return 1;
        }
    }

    @JsonSetter("nID_Status")
    public void setStatusID(int id) {
        // need to avoid exception in tests.
    }

    @JsonIgnore
    public List<ServiceData> getServiceDataList() {
        return serviceDataList;
    }

    public void setServiceDataList(List<ServiceData> serviceDataList) {
        this.serviceDataList = serviceDataList;
    }

    public String getInfo() {
        //return info;
        return getSmartFieldValue(info, BASE_INFO_PATTERN_FILE_PATH);
    }

    public void setInfo(String info) {
        //this.info = getSmartFieldValue(info, BASE_INFO_PATTERN_FILE_PATH);
        this.info = info;
    }

    public String getFaq() {
        //return faq;
        return getSmartFieldValue(faq, BASE_FAQ_PATTERN_FILE_PATH);
    }

    public void setFaq(String faq) {
        //this.faq = getSmartFieldValue(faq, BASE_FAQ_PATTERN_FILE_PATH);
        this.faq = faq;
    }

    public String getLaw() {
        //return law;
        return getSmartFieldValue(law, BASE_LAW_PATTERN_FILE_PATH);
    }

    public void setLaw(String law) {
        //this.law = getSmartFieldValue(law, BASE_LAW_PATTERN_FILE_PATH);
        this.law = law;
    }

    private String getSmartFieldValue(String value, String basePath) {
        String content = Util.getSmartPathFileContent(value, basePath, getId() + ".html");
        return content != null ? content : value;
    }
}
