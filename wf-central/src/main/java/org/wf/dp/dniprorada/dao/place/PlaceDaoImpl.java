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

    @SuppressWarnings("unchecked")
    public PlaceHierarchyTree getTree(PlaceHierarchyRecord root) {
        notNull(root, "Root element can't be a null");
        String sql = buildQueryForPlaceTree(root);

        Query query = sessionFactory
            .getCurrentSession()
            .createSQLQuery(sql)
            .setResultTransformer( new PlaceHibernateResultTransformer() );

        if (specified(root.getPlaceId()))
            query = query.setLong("placeId", root.getPlaceId());

        if (specified(root.getTypeId()))
            query = query.setLong("typeId", root.getTypeId());

        if (specified(root.isArea()))
            query = query.setBoolean("area", root.isArea());

        if (specified(root.isRoot()))
            query = query.setBoolean("root", root.isRoot());

        if (specified(root.getDeep()))
            query = query.setLong("deep", root.getDeep());

        return toTree( query.list() );
    }

    @Cacheable("ext-file-PlaceTree")
    private String buildQueryForPlaceTree(PlaceHierarchyRecord phr) {
        String sql = sqlStorage.get( phr.getPlaceId() > 0
            ? "get_PlaceTree_by_id.sql" : "get_PlaceTree_by_UA-id.sql");

        if (specified(phr.getTypeId()) ||
            specified(phr.isArea()) ||
            specified(phr.isRoot()) ||
            specified(phr.getDeep()))
            sql = sql + " where ";

        if (specified(phr.getTypeId()))
            sql += " type_id = :typeId";

        if (specified(phr.isArea()) && specified(phr.getTypeId()))
            sql += " and ";

        if (specified(phr.isArea()))
            sql += " area = :area";

        if (specified(phr.isRoot()) && (
            specified(phr.getTypeId()) ||
            specified(phr.isArea())))
            sql += " and ";

        if (specified(phr.isRoot()))
            sql += " root = :root";

        if (specified(phr.getDeep()) && (
            specified(phr.getTypeId()) ||
            specified(phr.isArea()) ||
            specified(phr.isRoot())))
            sql += " and ";

        if (specified(phr.getDeep()))
            sql += " level <= :deep";

        LOG.debug("SQL query {}", sql); // TODO decrease log level to debug

        return sql;
    }

    private boolean specified(Long value) {
        return value != null && value > 0;
    }
    private boolean specified(Boolean value) {
        return value != null;
    }
}