package org.wf.dp.dniprorada.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.wf.dp.dniprorada.base.dao.AbstractEntityDao;
import org.wf.dp.dniprorada.base.dao.BaseEntityDao;
import org.wf.dp.dniprorada.model.Place;
import org.wf.dp.dniprorada.model.PlaceTree;
import org.wf.dp.dniprorada.model.PlaceType;
import org.wf.dp.dniprorada.util.Tree;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author dgroup
 * @since  20.07.2015
 */
public class PlaceDaoImpl implements PlaceDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private BaseEntityDao baseEntityDao;

    public PlaceDaoImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @SuppressWarnings("unchecked")
    public List<Place> findBy(Long placeId, String uaId, Boolean tree) {
        Criteria places = sessionFactory
            .getCurrentSession()
            .createCriteria(Place.class);

        if(isNotBlank(uaId))
            places = places.add( Restrictions.eq("uaId", uaId) );

        return places.list();
    }

    @SuppressWarnings("unchecked")
    public Tree<Place> findPlacesTreeBy(Long placeId, String uaId, Long typeId, Boolean area, Boolean root, Integer deep) {
        List<Object[]> results = sessionFactory
            .getCurrentSession()
            .createSQLQuery("")
            .list();

        return null;
    }

    private boolean specified(Long value) {
        return value != null && value > 0;
    }


}
