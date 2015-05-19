package org.wf.dp.dniprorada.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.wf.dp.dniprorada.model.Merchant;

public class MerchantExtractor implements ResultSetExtractor<Merchant> {

	@Override
	public Merchant extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		Merchant merchant = new Merchant();
		merchant.setIdOwner(rs.getString("sIdOwner"));
		merchant.setOwnerName(rs.getString("sOwnerName"));
		merchant.setId(rs.getString("sId"));
		return merchant;
	}
	
}