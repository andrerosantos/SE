package pt.ulisboa.tecnico.learnjava.sibs.mbway;

public class Controller {
	private MBWayAccount model;
	private View view = new View();
	
	
	public Controller() { }

	public void createMBWay(String iban, int phoneNumber) {
		this.model = new MBWayAccount(iban, phoneNumber);
		
		view.printConfirmationCode(model.getConfirmationCode());
	}
	
	public int getPhoneNumer() {
		return this.model.getPhoneNumber();
	}
	
	public String getIban() {
		return this.model.getIban();
	}
	
	public void setPhoneNumer(int phoneNumber) {
		this.model.setPhoneNumber(phoneNumber);
	}
	
	public void setIban(String iban) {
		this.model.setIban(iban);
	}
	
	public int getConfirmationCode() {
		return model.getConfirmationCode();
	}
	
	public void confirmAccount(int phoneNumber, int confirmationCode) {
		MBWayAccount account = MBWayAccount.getMBWayAccount(phoneNumber);
		if (confirmationCode == account.getConfirmationCode()) {
			account.validateAccount(confirmationCode);
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
			view.printSuccessfulTransfer();
		}
	}
	
}
