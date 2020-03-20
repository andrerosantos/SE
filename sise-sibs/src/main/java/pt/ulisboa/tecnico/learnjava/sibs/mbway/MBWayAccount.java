package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MBWayException;

public class MBWayAccount {
	private String iban;
	private int phoneNumber;
	private int nConfirmation;
	private boolean confirmed = false;
	
	private static HashMap<Integer, MBWayAccount> accounts = new HashMap<Integer, MBWayAccount>();
	private static Services services = new Services();
	
	public MBWayAccount(String iban, int phoneNumber) throws MBWayException {
		if (accounts.containsKey(phoneNumber)) {
			throw new MBWayException("Phone number already in use.");
		} else if (services.getAccountByIban(iban) == null) {
			throw new MBWayException("There is no such iban.");
		}
		
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
	
	public boolean validateAccount(int confirmationNumber) {
		if (confirmationNumber == this.nConfirmation) {
			this.confirmed = true;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isConfirmed() {
		return this.confirmed;
	}
	
	public int getBalance() {
		Services service = new Services();
		return service.getAccountByIban(this.iban).getBalance();
	}
	
	public static MBWayAccount getMBWayAccount(int phoneNumber) {
		return accounts.get(phoneNumber);
	}
	
}
