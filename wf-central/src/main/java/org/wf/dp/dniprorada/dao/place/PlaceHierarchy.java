package org.wf.dp.dniprorada.dao.place;

/**
 * This is interface marker.
 * <p/>
 * In scope of
 * - {@link org.activiti.rest.controller.PlaceController#getPlacesTree}
 * - {@link org.activiti.rest.controller.PlaceController#getPlace}
 * system can return a different types of hierarchy structure.
 * <p/>
 * These different types can't be linked via inheritance.
 * Polymorphism is only one way to meet the requirements.
 *
 * @author dgroup
 * @since 26.09.2015
 */
public interface PlaceHierarchy {
}
