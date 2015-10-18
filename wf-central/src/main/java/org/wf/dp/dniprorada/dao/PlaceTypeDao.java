package org.wf.dp.dniprorada.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.dao.GenericEntityDao;
import org.wf.dp.dniprorada.model.PlaceType;

import java.util.List;

/**
 * @author dgroup
 * @since 28.08.2015
 */
@Repository
public class PlaceTypeDao extends GenericEntityDao<PlaceType> {

    protected PlaceTypeDao() {
        super(PlaceType.class);
    }

    @SuppressWarnings("unchecked")
    public List<PlaceType> getPlaceTypes(Boolean area, Boolean root) {
        Criteria crt = createCriteria();

        if (area != null)
            crt.add(Restrictions.eq("bArea", area));

        if (root != null)
            crt.add(Restrictions.eq("bRoot", root));

        return crt.list();
    }
}