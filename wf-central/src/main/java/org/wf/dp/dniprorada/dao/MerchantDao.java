package org.wf.dp.dniprorada.dao;

import org.wf.dp.dniprorada.base.dao.EntityDao;
import org.wf.dp.dniprorada.model.Merchant;

import java.util.List;

public interface MerchantDao extends EntityDao<Merchant> {

   Merchant getMerchant(String sID);
   boolean deleteMerchant(String sID);

}