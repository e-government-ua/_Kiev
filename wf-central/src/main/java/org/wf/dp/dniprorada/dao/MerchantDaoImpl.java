package org.wf.dp.dniprorada.dao;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.activiti.engine.ProcessEngines;
import org.wf.dp.dniprorada.model.Merchant;



public class MerchantDaoImpl implements MerchantDao{
	
	private Connection connect = null;
	
	private static Connection getConnection() throws IOException, SQLException{
		DataSource ds = ProcessEngines.getDefaultProcessEngine().getProcessEngineConfiguration().getDataSource();
		return ds.getConnection();
	}
	
	public MerchantDaoImpl() {
		try {
			connect = getConnection();
		} catch (SQLException e) {
			//Change
			e.printStackTrace();
		} catch (IOException ex){
			//Change
			ex.printStackTrace();
		}
	}
	
	public List<Merchant> getMerchants() throws SQLException {
		PreparedStatement ps = connect.prepareStatement("SELECT sIdOwner, sOwnerName, sId FROM merchants");
		ResultSet rs = ps.executeQuery();
		List<Merchant> list = new ArrayList<Merchant>();
		while(rs.next()){
			Merchant merch = new Merchant();
			merch.setIdOwner(rs.getString("sIdOwner"));
			merch.setOwnerName(rs.getString("sOwnerName"));
			merch.setId(rs.getString("id"));
			list.add(merch);
		}
		return list;
	}

	public void removeMerchant(String idOwner, String id) throws SQLException {
		PreparedStatement ps = null;
		if(idOwner == null){
			ps = connect.prepareStatement("DELETE FROM merchants WHERE sId=?");
			ps.setString(1, id);
			ps.executeUpdate();
		} else {
			ps = connect.prepareStatement("DELETE FROM merchants WHERE sIdOwner=?");
			ps.setString(1, idOwner);
			ps.executeUpdate();
		}		
	}

	public void updateMerchant(Merchant merchant) throws SQLException {
		PreparedStatement ps = null;
		ps = connect.prepareStatement("UPDATE merchants SET sIdOwner=?, sOwnerName=? WHERE sId=?");
		ps.setString(1, merchant.getIdOwner());
		ps.setString(2, merchant.getOwnerName());
		ps.setString(3, merchant.getId());
		ps.executeUpdate();
	}

	public void addMerchant(Merchant merchant) throws SQLException {
		PreparedStatement ps = null;
		ps = connect.prepareStatement("INSERT INTO merchants (sIdOwner, sOwnerName, sId) VALUES (?,?,?)");
		ps.setString(1, merchant.getIdOwner());
		ps.setString(2, merchant.getOwnerName());
		ps.setString(3, merchant.getId());
		ps.executeUpdate();		
	}
}