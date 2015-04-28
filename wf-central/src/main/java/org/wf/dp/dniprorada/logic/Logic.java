package org.wf.dp.dniprorada.logic;

import java.util.List;

import org.wf.dp.dniprorada.model.Merchant;

public interface Logic {
	public List<Merchant> getMerchants();
	public void removeMerchant(String sIdOwner, String id);
	public void updateMerchant(String sIdOwner,String sOwnerName,String id);
	public void addMerchant(String sIdOwner,String sOwnerName,String id);
}
