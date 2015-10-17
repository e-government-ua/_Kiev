package org.wf.dp.dniprorada.dao;

import org.springframework.stereotype.Repository;
import org.wf.dp.dniprorada.base.dao.GenericEntityDao;
import org.wf.dp.dniprorada.model.Merchant;

@Repository
public class MerchantDaoImpl extends GenericEntityDao<Merchant> implements MerchantDao {

    public static final String S_ID = "sID";

    protected MerchantDaoImpl() {
        super(Merchant.class);
    }

    @Override
    public Merchant getMerchant(String sID) {
        return findBy(S_ID, sID).orNull();
    }

    public boolean deleteMerchant(String sID) {
        return deleteBy(S_ID, sID) > 0;
    }
}