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
	private Services service = new Services();
	private Sibs sibs = new Sibs(100, service);
	
	public Controller() { }

	public void createMBWay(String iban, int phoneNumber)  {
		try {
		MBWayAccount model = new MBWayAccount(iban, phoneNumber);
		view.printConfirmationCode(model.getConfirmationCode());
		} catch (MBWayException e) {
			view.printException(e.getMessage());
		}
	}
	
	public void confirmAccount(int phoneNumber, int confirmationCode) {
		MBWayAccount account = MBWayAccount.getMBWayAccount(phoneNumber);
		if (account.validateAccount(confirmationCode)) {
			view.printConfirmationResult(true);
		} else {
			view.printConfirmationResult(false);
		}
	}
	
	public void transfer(int sourcePhoneNumber, int targetPhoneNumber, int amount) throws SibsException, AccountException, OperationException {
		MBWayAccount source = MBWayAccount.getMBWayAccount(sourcePhoneNumber);
		MBWayAccount target = MBWayAccount.getMBWayAccount(targetPhoneNumber);
		
		if(source == null || target == null) {
			view.printNoSuchAccounts();
		} else if (!source.isConfirmed()) {
			view.printAccountNotConfirmed(sourcePhoneNumber);
		} else if (!source.isConfirmed()) {
			view.printAccountNotConfirmed(targetPhoneNumber);
		} else {
			
			sibs.transfer(source.getIban(), target.getIban(), amount);
			view.printSuccessfulTransfer();
			
		}
	}
	
	public void splitBill(int numberOfFriends, int totalAmount, HashMap<Integer, Integer> friends, int receiver) throws SibsException, AccountException, OperationException {
		
		Services service = new Services();
		int counter = 0;
		int amount = 0;
		
		for (int phoneNumber : friends.keySet()) {
			counter++;
			amount += friends.get(phoneNumber);
			
			if (counter > numberOfFriends) {
				view.printTooManyFriends();
			} else if(friends.get(phoneNumber) > service.getAccountByIban(MBWayAccount.getMBWayAccount(phoneNumber).getIban()).getBalance()
					&& phoneNumber != receiver) {
				view.printFriendWithoutMoney();
				return;
			}
		}
		
		
		if (counter < numberOfFriends) {
			view.printNotEnoughFriends();
		} else if (totalAmount != amount) {
			view.printWrongAmount();
		}
		
		String receiverIban = MBWayAccount.getMBWayAccount(receiver).getIban();
		MBWayAccount friendAccount;
		
		for (int phoneNumber : friends.keySet()) {
			if (phoneNumber != receiver) {
				friendAccount = MBWayAccount.getMBWayAccount(phoneNumber);
				this.sibs.transfer(friendAccount.getIban(), receiverIban, friends.get(phoneNumber));
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
		if (MBWayAccount.getMBWayAccount(phoneNumber) == null) {
			view.printNoSuchAccounts(); 
			return true;
		}
		return (true);
	}
}
