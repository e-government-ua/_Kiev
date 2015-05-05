package org.wf.dp.dniprorada.dao;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.wf.dp.dniprorada.model.Merchant;



public class MerchantDaoImpl implements MerchantDao{
	
    private JdbcTemplate jdbcTemplate;
    
    public void setDataSource(DataSource dataSource){
    	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	public List<Merchant> getMerchants() {
		return jdbcTemplate.query("SELECT sIdOwner, sOwnerName, sId FROM merchants", new MerchantRowMapper());
	}

	public void removeMerchant(String idOwner, String id) {
		if(idOwner == null){
			jdbcTemplate.update("DELETE FROM merchants WHERE sId=?", id);
		} else {
			jdbcTemplate.update("DELETE FROM merchants WHERE sIdOwner=?", idOwner);
		}		
	}

	public void updateMerchant(Merchant merchant) {
		jdbcTemplate.update("UPDATE merchants SET sIdOwner=?, sOwnerName=? WHERE sId=?",
				merchant.getIdOwner(), merchant.getOwnerName(), merchant.getId());
	}

	public void addMerchant(Merchant merchant) {
		jdbcTemplate.update("INSERT INTO merchants (sIdOwner, sOwnerName, sId) VALUES (?,?,?)",
				merchant.getIdOwner(), merchant.getOwnerName(), merchant.getId());
	}
}