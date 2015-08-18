package org.wf.dp.dniprorada.dao.place;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.wf.dp.dniprorada.dao.PlaceDao;
import org.wf.dp.dniprorada.model.Place;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.Assert.notNull;
import static org.wf.dp.dniprorada.dao.place.PlaceHibernateResultTransformer.toTree;
import static org.wf.dp.dniprorada.dao.place.PlaceQueryDaoBuilder.specified;

/**
 * @author dgroup
 * @since  20.07.2015
 */
public class PlaceDaoImpl implements PlaceDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PlaceQueryDaoBuilder sqlBuilder;

    public PlaceDaoImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    // TODO create util method for one parameter
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
    public PlaceHierarchyTree getTreeDown(PlaceHierarchyRecord root) {
        notNull(root, "Root element can't be a null");
        if (!specified(root.getPlaceId()) && isBlank(root.getUaID()))
            throw new IllegalArgumentException("PlaceId and UA id are empty");

        String sql = sqlBuilder.getTreeDown(root);

        Query query = sessionFactory
            .getCurrentSession()
            .createSQLQuery(sql)
            .setResultTransformer( new PlaceHibernateResultTransformer() );

        if (specified(root.getPlaceId()))
            query.setLong("placeId", root.getPlaceId());

        if (specified(root.getTypeId()))
            query.setLong("typeId", root.getTypeId());

        if (specified(root.isArea()))
            query.setBoolean("area", root.isArea());

        if (specified(root.isRoot()))
            query.setBoolean("root", root.isRoot());

        if (specified(root.getDeep()))
            query.setLong("deep", root.getDeep());

        return toTree( query.list() );
    }

    @SuppressWarnings("unchecked")
    public PlaceHierarchyTree getTreeUp(Long placeId, String uaId, Boolean tree) {
        if (!specified(placeId) && !isNotBlank(uaId))
            throw new IllegalArgumentException("One from main parameters doesn't specified");

        String sql = sqlBuilder.getTreeUp(placeId, uaId, tree);
        Query query = sessionFactory
            .getCurrentSession()
            .createSQLQuery(sql)
            .setResultTransformer( new PlaceHibernateResultTransformer() );

        if (specified(placeId))
            query.setLong("placeId", placeId);

        if (isNotBlank(uaId) && !specified(placeId))
            query.setString("ua_id", uaId);

        return toTree( query.list() );
    }

}