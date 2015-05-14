package org.wf.dp.dniprorada.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.wf.dp.dniprorada.model.Merchant;

public class MerchantRowMapper implements RowMapper<Merchant>{

	@Override
	public Merchant mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new MerchantExtractor().extractData(rs);
	}
}
