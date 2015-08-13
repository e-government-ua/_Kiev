package org.wf.dp.dniprorada.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.wf.dp.dniprorada.base.dao.BaseEntityDao;
import org.wf.dp.dniprorada.model.Place;
import org.wf.dp.dniprorada.util.Tree;
import org.wf.dp.dniprorada.util.queryloader.QueryLoader;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.wf.dp.dniprorada.dao.PlaceHibernateResultTransformer.toTree;

/**
 * @author dgroup
 * @since  20.07.2015
 */
public class PlaceDaoImpl implements PlaceDao {
    private static final Logger LOG = LoggerFactory.getLogger(PlaceDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private BaseEntityDao baseEntityDao;

    @Autowired
    private QueryLoader sqlStorage;

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
    public Tree<PlaceHierarchyRecord> getPlaces(Long placeId,
                                 String uaId,
                                 Long typeId,
                                 Boolean area,
                                 Boolean root,
                                 Integer deep) {
        String sql = buildQueryForPlaceTree(placeId, uaId, typeId, area, root, deep);
        LOG.info("Got sql for execution: \n\r {}", sql);

        Query query = sessionFactory
            .getCurrentSession()
            .createSQLQuery(sql)
            .setResultTransformer( new PlaceHibernateResultTransformer() );

        if (specified(placeId))
            query = query.setLong("placeId", placeId);

        if (specified(typeId))
            query = query.setLong("typeId", typeId);

        return toTree( query.list() );
    }

    @Cacheable("ext-file-PlaceTree")
    private String buildQueryForPlaceTree(Long placeId, String uaId,  Long typeId,
                                          Boolean area, Boolean root, Integer deep) {
        String sql = sqlStorage.get(
            placeId != null ? "get_PlaceTree_by_id.sql" :
            isNotBlank(uaId)? "get_PlaceTree_by_UA-id.sql" : "get_PlaceTree-s.sql");

//        if (specified(typeId) || area != null || root != null || specified(deep))
//            sql = sql + " where ";
//
//        if (specified(typeId))
//            sql += " type_id = :typeId";
//
//        if (area != null && specified(typeId))
//            sql += " and ";
//
//        if (area != null)
//            sql += " area = :area";
//
//        if (root != null && (specified(typeId) || area !=null) )
//            sql += " and ";
//
//        if (root != null)
//            sql += " root = :root";
//
//        if (specified(deep) && (specified(typeId) || area != null || root != null))
//            sql += " and ";
//
//        if (specified(deep))
//            sql += " and level <= :deep";

        LOG.debug("Final query {}", sql);

        return sql;
    }

    private boolean specified(Long value) {
        return value != null && value > 0;
    }
}