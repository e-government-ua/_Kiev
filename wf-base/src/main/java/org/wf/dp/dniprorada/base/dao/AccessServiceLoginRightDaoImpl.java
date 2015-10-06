package org.wf.dp.dniprorada.base.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.model.AccessServiceLoginRight;
import org.wf.dp.dniprorada.base.model.EscalationRule;

import java.util.List;

/**
 * User: goodg_000
 * Date: 06.10.2015
 * Time: 22:34
 */
@Repository
public class AccessServiceLoginRightDaoImpl extends GenericEntityDao<AccessServiceLoginRight>
        implements AccessServiceLoginRightDao {


   protected AccessServiceLoginRightDaoImpl() {
      super(AccessServiceLoginRight.class);
   }

   @Override
   public AccessServiceLoginRight getAccessServiceLoginRight(String sLogin, String sService) {
      Criteria criteria = createCriteria();

      criteria.add(Restrictions.eq("sLogin", sLogin));
      criteria.add(Restrictions.eq("sService", sService));

      return (AccessServiceLoginRight) criteria.uniqueResult();
   }

   @Override
   public List<String> getAccessibleServices(String sLogin) {
      Criteria criteria = createCriteria();

      criteria.add(Restrictions.eq("sLogin", sLogin));
      criteria.setProjection(Projections.property("sService"));

      return criteria.list();
   }
}
