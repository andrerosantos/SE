package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.MBWayException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Controller {
	private View view = new View();
	private boolean running = true;
	private Services services;
	private Sibs sibs;
	
	public Controller(Services services) { 
		this.services = services;
		this.sibs = new Sibs(100, this.services);
	}

	public void createMBWay(String iban, int phoneNumber)  {
		try {
		MBWayAccount model = new MBWayAccount(iban, phoneNumber, this.services);
		view.printConfirmationCode(model.getConfirmationCode());
		} catch (MBWayException e) {
			view.printException(e.getMessage());
		}
	}
	
	public void confirmAccount(int phoneNumber, int confirmationCode) {
		try {
			MBWayAccount account = MBWayAccount.getMBWayAccount(phoneNumber);
			
			if (account.validateAccount(confirmationCode)) {
				view.printConfirmationResult(true);
			} else {
				view.printConfirmationResult(false);
			}
			
		} catch (Exception e) {
			view.printException(e.getMessage());
		}
	}
	
	public void transfer(int sourcePhoneNumber, int targetPhoneNumber, int amount) {
		try {
			MBWayAccount source = MBWayAccount.getMBWayAccount(sourcePhoneNumber);
			MBWayAccount target = MBWayAccount.getMBWayAccount(targetPhoneNumber);
			
			source.transferMoney(target, amount);
			
		} catch (Exception e) {
			view.printException(e.getMessage());
		}
	}
	
	public void splitBill(int numberOfFriends, int totalAmount, HashMap<Integer, Integer> friends, int receiver) throws SibsException, AccountException, OperationException {
		Services service = new Services();
		int counter = 0;
		int amount = 0;
		
		if (friends.size() < numberOfFriends) {
			view.printNotEnoughFriends();
		} else if (friends.size() > numberOfFriends) {
			view.printTooManyFriends();
		} else {
			try {
				MBWayAccount receiverAcc = MBWayAccount.getMBWayAccount(receiver);
				receiverAcc.splitBill(friends, totalAmount, numberOfFriends);
				view.printSuccessSplitBill();
			} catch (Exception e) {
				view.printException(e.getMessage());
			}
		}
	}
	
	public boolean isRunning() {
		return this.running;
	}
	
	public void stopRunnging() {
		this.running = false;
		view.printStopRunning();
	}
	
	public boolean accountExists(int phoneNumber) {
		try {
			MBWayAccount.getMBWayAccount(phoneNumber);
			return true;
		} catch (Exception e) {
			view.printNoSuchAccounts();
			return false;
		}
	}
}
