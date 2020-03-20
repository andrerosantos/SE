package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;

public class Controller {
	private View view = new View();
	private boolean running = true;
	
	
	public Controller() { }

	public void createMBWay(String iban, int phoneNumber) {
		MBWayAccount model = new MBWayAccount(iban, phoneNumber);
		
		view.printConfirmationCode(model.getConfirmationCode());
	}
	
	public void confirmAccount(int phoneNumber, int confirmationCode) {
		MBWayAccount account = MBWayAccount.getMBWayAccount(phoneNumber);
		if (account.validateAccount(confirmationCode)) {
			view.printConfirmationResult(true);
		} else {
			view.printConfirmationResult(false);
		}
	}
	
	public void transfer(int sourcePhoneNumber, int targetPhoneNumber, int amount) {
		MBWayAccount source = MBWayAccount.getMBWayAccount(sourcePhoneNumber);
		MBWayAccount target = MBWayAccount.getMBWayAccount(targetPhoneNumber);
		
		if(source == null || target == null) {
			view.printNoSuchAccounts();
		} else if (!source.isConfirmed()) {
			view.printAccountNotConfirmed(sourcePhoneNumber);
		} else if (!source.isConfirmed()) {
			view.printAccountNotConfirmed(targetPhoneNumber);
		} else {
			
			//ToDo
			//transfer money
			
			
			
			view.printSuccessfulTransfer();
		}
	}
	
	public void splitBill(int numberOfFriends, int totalAmount, HashMap<Integer, Integer> friends, int friendsCounter, int friendsMoneyCounter) {
		if (numberOfFriends < friendsCounter) {
			view.printTooManyFriends();
		} else if (numberOfFriends > friendsCounter) {
			view.printNotEnoughFriends();
		} else if (totalAmount != friendsMoneyCounter) {
			view.printWrongAmount();
		}
		
		Services service = new Services();
		for (int key : friends.keySet()) {
			if(friends.get(key) > service.getAccountByIban(MBWayAccount.getMBWayAccount(key).getIban()).getBalance()) {
				view.printFriendWithoutMoney();
			}
		}

		//ToDo split the bill
		//ToDo split the bill
		//ToDo split the bill
		//ToDo split the bill
		//ToDo split the bill
		//ToDo split the bill
		
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
