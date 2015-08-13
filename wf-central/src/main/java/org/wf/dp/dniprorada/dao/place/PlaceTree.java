package org.wf.dp.dniprorada.dao.place;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.model.Place;

import java.util.List;

/**
 * @author dgroup
 * @since  22.07.2015
 */
public class PlaceTree  {
    @JsonProperty
    private Long levelOfArea;

    @JsonProperty
    private Long level;

    @JsonProperty
    private Place place;

    @JsonProperty
    private List<PlaceTree> children;


    public Long getLevelOfArea() {
        return levelOfArea;
    }
    public void setLevelOfArea(Long levelOfArea) {
        this.levelOfArea = levelOfArea;
    }

    public Long getLevel() {
        return level;
    }
    public void setLevel(Long level) {
        this.level = level;
    }

    public Place getPlace() {
        return place;
    }
    public void setPlace(Place place) {
        this.place = place;
    }

    public List<PlaceTree> getChildren() {
        return children;
    }
    public void setChildren(List<PlaceTree> children) {
        this.children = children;
    }
}