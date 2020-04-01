package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;
import java.util.Set;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MBWayException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MBWayAccount {
	private String iban;
	private int phoneNumber;
	private int nConfirmation;
	private boolean confirmed = false;
	
	private Services services;
	private Sibs sibs;
	
	private static HashMap<Integer, MBWayAccount> accounts = new HashMap<Integer, MBWayAccount>();
	
	public MBWayAccount(String iban, int phoneNumber, Services services) throws MBWayException {
		this.iban = iban;
		this.phoneNumber = phoneNumber;
		this.nConfirmation = (int) (Math.random() * 900000) + 100000;
		this.services = services;
		this.sibs = new Sibs(100, this.services);
		
		if (accounts.containsKey(phoneNumber)) {
			throw new MBWayException("Phone number already in use.");
		} else if (phoneNumber<100000000 || phoneNumber > 999999999){
			throw new MBWayException("This phone number is not valid.");
		} else if (services.getAccountByIban(iban) == null) {
			throw new MBWayException("There is no such iban.");
		}
		
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
	
	public void transferMoney(MBWayAccount tragetAccount, int amount) throws SibsException, AccountException, OperationException, MBWayException {
		if(!isConfirmed() || !tragetAccount.isConfirmed()) {
			throw new MBWayException("One or more MBWay Accounts are not confirmed!");
		}
		this.sibs.transfer(getIban(), tragetAccount.getIban(), amount);
	}
	
	public void splitBill(HashMap<Integer, Integer> friends, int totalAmount) throws MBWayException, SibsException, AccountException, OperationException {
		Set<Integer> keys = friends.keySet();
		int counter = 0;
		for (int phoneNumber : keys) {
			if (phoneNumber != getPhoneNumber()) {
				if (MBWayAccount.getMBWayAccount(phoneNumber).getBalance() < friends.get(phoneNumber)) {
					throw new MBWayException("A friend doesn't have enough money.");
				}
			}
			counter += friends.get(phoneNumber);
		}
		
		if (counter != totalAmount) {
			throw new MBWayException("Something is wrong. Did you set the bill amount right?");
		}
		
		for (int phoneNumber : keys) {
			MBWayAccount friend = MBWayAccount.getMBWayAccount(phoneNumber);
			friend.transferMoney(this, friends.get(phoneNumber));
		}
	}
	
	public static MBWayAccount getMBWayAccount(int phoneNumber) throws MBWayException {
		MBWayAccount result = accounts.get(phoneNumber);
		if (result == null) {
			throw new MBWayException("This phone number doesn't have an associated MBWay account.");
		}
		return result;
	}
	
	public static void clearMBWayAccounts() {
		accounts.clear();
	}
	
}
