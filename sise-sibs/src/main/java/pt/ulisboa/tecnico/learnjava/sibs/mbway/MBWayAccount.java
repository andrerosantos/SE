package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;

public class MBWayAccount {
	private String iban;
	private int phoneNumber;
	private int nConfirmation;
	private boolean confirmed = false;
	
	private static HashMap<Integer, MBWayAccount> accounts = new HashMap<Integer, MBWayAccount>();
	
	public MBWayAccount(String iban, int phoneNumber) {
		this.iban = iban;
		this.phoneNumber = phoneNumber;
		this.nConfirmation = (int) (Math.random() * 100000);
		
		accounts.put(phoneNumber, this);
	}
	
	public String getIban() {
		return this.iban;
	}
	
	public int getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public void setIban(String iban) {
		this.iban = iban;
	}
	
	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public int getConfirmationCode() {
		return this.nConfirmation;
	}
	
	public static MBWayAccount getMBWayAccount(int phoneNumber) {
		return accounts.get(phoneNumber);
	}
	
	public void validateAccount() {
		this.confirmed = true;
	}
	
	public boolean isConfirmed() {
		return this.confirmed;
	}
	
}
