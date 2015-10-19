package org.wf.dp.dniprorada.base.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Flow (stored in regional server) related to ServiceData (stored in central server).
 * <p/>
 * User: goodg_000
 * Date: 14.06.2015
 * Time: 15:03
 */
@javax.persistence.Entity
public class Flow_ServiceData extends NamedEntity {

    /**
     * One-to-one soft reference to ServiceData which is stored in central but not present in regional server.
     */
    @Column
    private Long nID_ServiceData;

    /**
     * Many-to-one soft reference to SubjectOrganDepartment which is stored in central but not present in regional server.
     */
    @Column
    private Long nID_SubjectOrganDepartment;

    /**
     * ID of business process definition without version.
     */
    @Column
    private String sID_BP;

    @OneToMany(mappedBy = "oFlow_ServiceData", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<FlowProperty> flowProperties;

    public Long getnID_ServiceData() {
        return nID_ServiceData;
    }

    public void setnID_ServiceData(Long nID_ServiceData) {
        this.nID_ServiceData = nID_ServiceData;
    }

    public Long getnID_SubjectOrganDepartment() {
        return nID_SubjectOrganDepartment;
    }

    public void setnID_SubjectOrganDepartment(Long nID_SubjectOrganDepartment) {
        this.nID_SubjectOrganDepartment = nID_SubjectOrganDepartment;
    }

    public String getsID_BP() {
        return sID_BP;
    }

    public void setsID_BP(String sID_BP) {
        this.sID_BP = sID_BP;
    }

    public List<FlowProperty> getFlowProperties() {
        return flowProperties;
    }

    public void setFlowProperties(List<FlowProperty> flowProperties) {
        this.flowProperties = flowProperties;
    }
}
