package org.wf.dp.dniprorada.dao.place;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.dao.GenericEntityDao;
import org.wf.dp.dniprorada.dao.PlaceDao;
import org.wf.dp.dniprorada.model.Place;

import static org.apache.commons.lang3.StringUtils.*;
import static org.springframework.util.Assert.*;
import static org.wf.dp.dniprorada.dao.place.PlaceHibernateResultTransformer.toTree;
import static org.wf.dp.dniprorada.dao.place.PlaceQueryDaoBuilder.specified;

/**
 * @author dgroup
 * @since  20.07.2015
 */
@Repository
public class PlaceDaoImpl extends GenericEntityDao<Place> implements PlaceDao {

    @Autowired
    private PlaceQueryDaoBuilder sqlBuilder;

    public PlaceDaoImpl() {
        super(Place.class);
    }


    @SuppressWarnings("unchecked")
    public PlaceHierarchyTree getTreeDown(PlaceHierarchyRecord root) {
        notNull(root, "Root element can't be a null");
        if (!specified(root.getPlaceId()) && isBlank(root.getUaID()))
            throw new IllegalArgumentException("PlaceId and UA id are empty");

        String sql = sqlBuilder.getTreeDown(root);

        Query query = getSession()
            .createSQLQuery(sql)
            .setResultTransformer( new PlaceHibernateResultTransformer() );

        if (specified(root.getPlaceId()))
            query.setLong("placeId", root.getPlaceId());

        if (!specified(root.getPlaceId()) && isNotBlank(root.getUaID()))
            query.setString("ua_id", root.getUaID());

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
        if (!specified(placeId) && isBlank(uaId)) {
            notNull(placeId, "PlaceId can't be empty");
            isTrue(isBlank(uaId), "UA id can't empty.");
        }

        String sql = sqlBuilder.getTreeUp(placeId, uaId, tree);
        Query query = getSession()
            .createSQLQuery(sql)
            .setResultTransformer( new PlaceHibernateResultTransformer() );

        if (specified(placeId))
            query.setLong("placeId", placeId);

        if (isNotBlank(uaId) && !specified(placeId))
            query.setString("ua_id", uaId);

        return toTree( query.list() );
    }

}