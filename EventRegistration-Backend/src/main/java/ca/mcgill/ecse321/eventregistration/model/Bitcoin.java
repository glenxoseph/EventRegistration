package ca.mcgill.ecse321.eventregistration.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Bitcoin {
	private String userID;

	public void setUserID(String value) {
		this.userID = value;
	}

	@Id
	public String getUserID() {
		return this.userID;
	}
	
	private int amount;

	public void setAmount(int value) {
		this.amount = value;
	}

	public int getAmount() {
		return this.amount;
	}
	
	public String getBitcoin() {
		return this.userID;
	}
	
	public void setBitcoin(String value) {
		this.userID = value;
	}
}
