package org.wf.dp.dniprorada.logic;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.wf.dp.dniprorada.dao.MerchantDao;
import org.wf.dp.dniprorada.model.Merchant;

public class MerchantLogic implements Logic {

	@Autowired
	@Qualifier(value = "merchantDao")
	private MerchantDao merchantDao;

	@Autowired
	@Qualifier(value = "merchant")
	private Merchant merchant;

	public List<Merchant> getMerchants() {
		System.out.println("Logic Say");
		List<Merchant> list = null;
		try {
			list = merchantDao.getMerchants();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void removeMerchant(String idOwner, String id) {
		try {
			merchantDao.removeMerchant(idOwner, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void updateMerchant(String sIdOwner, String sOwnerName, String id) {
		merchant.setIdOwner(sIdOwner);
		merchant.setOwnerName(sOwnerName);
		merchant.setId(id);
		try {
			merchantDao.updateMerchant(merchant);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void addMerchant(String sIdOwner, String sOwnerName, String id) {
		merchant.setIdOwner(sIdOwner);
		merchant.setOwnerName(sOwnerName);
		merchant.setId(id);
		try {
			merchantDao.addMerchant(merchant);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}