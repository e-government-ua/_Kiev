package org.wf.dp.dniprorada.base.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.model.Flow_ServiceData;

/**
 * User: goodg_000
 * Date: 20.09.2015
 * Time: 19:21
 */
@Repository
public class FlowServiceDataDaoImpl extends GenericEntityDao<Flow_ServiceData> implements FlowServiceDataDao {

    public FlowServiceDataDaoImpl() {
        super(Flow_ServiceData.class);
    }

    @Override
    public Long findFlowId(String sID_BP, Long nID_SubjectOrganDepartment) {

        Criteria criteria = createCriteria();

        criteria.add(Restrictions.eq("sID_BP", sID_BP));
        criteria.add(Restrictions.eq("nID_SubjectOrganDepartment", nID_SubjectOrganDepartment));
        criteria.setProjection(Projections.id());

        return (Long) criteria.uniqueResult();
    }
}
