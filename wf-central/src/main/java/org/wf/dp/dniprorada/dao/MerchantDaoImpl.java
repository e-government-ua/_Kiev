package org.wf.dp.dniprorada.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.Assert;
import org.wf.dp.dniprorada.base.dao.AbstractEntityDao;
import org.wf.dp.dniprorada.model.Merchant;

public class MerchantDaoImpl extends AbstractEntityDao<Merchant> implements MerchantDao {

	protected MerchantDaoImpl() {
		super(Merchant.class);
	}

	@Override
	public Merchant getMerchant(String sID) {
      DetachedCriteria criteria = DetachedCriteria.forClass(getEntityClass());
      criteria.add(Restrictions.eq("sID", sID));

      return (Merchant) criteria.getExecutableCriteria(getSession()).uniqueResult();
   }

   public boolean deleteMerchant(String sID) {
      Merchant merchant = getMerchant(sID);

      boolean deleted = false;
      if (merchant != null) {
         delete(merchant);
         deleted = true;
      }

      return deleted;
   }
}