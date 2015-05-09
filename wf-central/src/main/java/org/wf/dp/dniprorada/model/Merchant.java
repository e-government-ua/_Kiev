package org.wf.dp.dniprorada.model;

public class Merchant {
	//OKPO
	private String idOwner;
	private String ownerName; 
	private String id;
	
	public void setIdOwner(String idOwner) {
		this.idOwner = idOwner;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getIdOwner() {
		return idOwner;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "{"+idOwner+":"+ownerName+":"+id+"}";
	}
}
