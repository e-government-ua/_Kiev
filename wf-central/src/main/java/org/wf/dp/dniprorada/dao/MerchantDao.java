package org.wf.dp.dniprorada.dao;

import java.sql.SQLException;
import java.util.List;

import org.wf.dp.dniprorada.model.Merchant;

public interface MerchantDao {
	public List<Merchant> getMerchants() throws SQLException;
	public void removeMerchant(String idOwner, String id) throws SQLException;
	public void updateMerchant(Merchant merchant) throws SQLException;
	public void addMerchant(Merchant merchant) throws SQLException;
}
