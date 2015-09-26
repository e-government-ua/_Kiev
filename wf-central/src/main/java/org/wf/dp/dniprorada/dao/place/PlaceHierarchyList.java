package org.wf.dp.dniprorada.dao.place;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.wf.dp.dniprorada.model.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dgroup
 * @since  26.09.2015
 */
public class PlaceHierarchyList implements PlaceHierarchy {

    @JsonProperty("a")
    private List<Place> places = new ArrayList<>();

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public boolean add(Place place) {
        return places.add(place);
    }
}
