package org.wf.dp.dniprorada.dao.place;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.wf.dp.dniprorada.dao.PlaceDao;
import org.wf.dp.dniprorada.model.Place;
import org.wf.dp.dniprorada.base.util.queryloader.QueryLoader;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.Assert.notNull;
import static org.wf.dp.dniprorada.dao.place.PlaceHibernateResultTransformer.toTree;

/**
 * @author dgroup
 * @since  20.07.2015
 */
public class PlaceDaoImpl implements PlaceDao {
    private static final Logger LOG = LoggerFactory.getLogger(PlaceDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

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

    // TODO replace the list of parameters via PlaceHierarchyRecord
    // TODO replace info log level to debug
    @SuppressWarnings("unchecked")
    public PlaceHierarchyTree getTree(PlaceHierarchyRecord rootRecord) {
        notNull(rootRecord, "Root record can't be null");
        String sql = buildQueryForPlaceTree(rootRecord);

        Query query = sessionFactory
            .getCurrentSession()
            .createSQLQuery(sql)
            .setResultTransformer( new PlaceHibernateResultTransformer() );

        if (specified(rootRecord.getPlaceId()))
            query = query.setLong("placeId", rootRecord.getPlaceId());

        if (specified(rootRecord.getTypeId()))
            query = query.setLong("typeId", rootRecord.getTypeId());

//        if (specified(area))
//            query = query.setBoolean("area", area);
//
//        if (specified(root))
//            query = query.setBoolean("root", root);

//        if (specified(deep))
//            query = query.setLong("deep", deep);

        return toTree( query.list() );
    }

    @Cacheable("ext-file-PlaceTree")
    private String buildQueryForPlaceTree(PlaceHierarchyRecord phr) {
        String sql = sqlStorage.get( phr.getPlaceId() > 0
            ? "get_PlaceTree_by_id.sql" : "get_PlaceTree_by_UA-id.sql");

//        if (specified(typeId) || area != null || root != null || specified(deep))
//            sql = sql + " where ";
//
//        if (specified(typeId))
//            sql += " type_id = :typeId";

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

//        if (specified(deep) && (specified(typeId) || area != null || root != null))
//            sql += " and ";
//
//        if (specified(deep))
//            sql += " level <= :deep";

        LOG.debug("SQL query {}", sql);

        return sql;
    }

    private boolean specified(Long value) {
        return value != null && value > 0;
    }
    private boolean specified(Integer value) {
        return value != null && value > 0;
    }
    private boolean specified(Boolean value) {
        return value != null;
    }
}