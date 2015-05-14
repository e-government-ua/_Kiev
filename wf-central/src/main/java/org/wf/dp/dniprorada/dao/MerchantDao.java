package org.wf.dp.dniprorada.dao;

import java.util.List;

import org.wf.dp.dniprorada.model.Merchant;

public interface MerchantDao {
	public List<Merchant> getMerchants();
	public void removeMerchant(String idOwner, String id);
	public void updateMerchant(Merchant merchant);
	public void addMerchant(Merchant merchant);
}