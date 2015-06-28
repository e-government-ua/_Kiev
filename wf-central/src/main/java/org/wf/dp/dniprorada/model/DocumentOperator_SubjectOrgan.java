package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.model.document.DocumentAccessHandler;

import javax.persistence.Column;

/**
 *
 * @author dgroup
 * @since 28.06.15
 */
@javax.persistence.Entity
public class DocumentOperator_SubjectOrgan extends org.wf.dp.dniprorada.base.model.NamedEntity {

    // Sorry about variables prefixes, but it's project convention.
    @JsonProperty(value="nID_SubjectOrgan")
    @Column(name="nID_SubjectOrgan", nullable = false)
    private Long    nID_SubjectOrgan;

    /**
     * It represents a handler's full class name.
     * Each handler is a instance of {@link DocumentAccessHandler}.
     **/
    @JsonProperty(value="sHandlerClass")
    @Column(name = "sHandlerClass", nullable = false)
    private String  sHandlerClass;


    public Long getnID_SubjectOrgan() {
        return nID_SubjectOrgan;
    }
    public void setnID_SubjectOrgan(Long nID_SubjectOrgan) {
        this.nID_SubjectOrgan = nID_SubjectOrgan;
    }

    public String getsHandlerClass() {
        return sHandlerClass;
    }
    public void setsHandlerClass(String sHandlerClass) {
        this.sHandlerClass = sHandlerClass;
    }

}