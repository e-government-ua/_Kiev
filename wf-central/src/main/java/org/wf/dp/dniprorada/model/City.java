package org.wf.dp.dniprorada.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.base.model.Entity;
import org.wf.dp.dniprorada.base.model.NamedEntity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * User: goodg_000 Date: 04.05.2015 Time: 21:50
 */
@javax.persistence.Entity
public class City extends NamedEntity {

	@JsonProperty(value = "nID_Region")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nID_Region", insertable = false, updatable = false, nullable = false)
	private Region region;

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
}
