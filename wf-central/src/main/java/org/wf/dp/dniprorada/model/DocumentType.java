package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.base.model.NamedEntity;

import javax.persistence.Column;

@javax.persistence.Entity
public class DocumentType extends NamedEntity {

    @JsonProperty("bHidden")
    @Column
    private boolean bHidden;

    public boolean isbHidden() {
        return bHidden;
    }

    public void setbHidden(boolean bHidden) {
        this.bHidden = bHidden;
    }
}
