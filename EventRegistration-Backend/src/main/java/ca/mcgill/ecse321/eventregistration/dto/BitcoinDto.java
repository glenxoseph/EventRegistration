package ca.mcgill.ecse321.eventregistration.dto;

public class BitcoinDto {
	
	private String userID;
	private int amount;
	
	public BitcoinDto() {
		
	}
	public BitcoinDto(String userID) {
		this.userID = userID;
		this.amount = 0;
	}
	public BitcoinDto(String userID, int amount) {
		this.userID = userID;
		this.amount = amount;
	}
	public String getUserID () {
		return this.userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public int getAmount() {
		return this.amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
}