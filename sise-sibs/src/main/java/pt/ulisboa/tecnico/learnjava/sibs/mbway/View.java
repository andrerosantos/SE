package pt.ulisboa.tecnico.learnjava.sibs.mbway;

public class View {
	
	public View() { }
	
	public void printSomething(String comething){
	      System.out.println("The something: ");
	   }
	
	public void printConfirmationCode(int code) {
		System.out.println("Code: " + code);
	}
	
	public void printConfirmationResult(boolean result) {
		if (result) {
			System.out.println("MBWAY association confirmed successfully!");
		} else {
			System.out.println("Wrong confirmation code. Try association again.");
		}
	}
	
	public void printNoSuchAccounts() {
		System.out.println("The phone number(s) you entered are not valid.");
	}
	
	public void printAccountNotConfirmed(int phoneNumber) {
		System.out.println("The phone number '" + phoneNumber + "' is not confirmed.");
	}
	
	public void printSuccessfulTransfer() {
		System.out.println("Transfer performed successfully!");
	}
	
	public void printNotEnoughMoney() {
		System.out.println("Not enough money on the source account.");
	}
	
	public void printStopRunning() {
		System.out.println("You exit the app.");
	}
	
	public void printTooManyFriends() {
		System.out.println("Oh no! Too many friends.");
	}
	
	public void printNotEnoughFriends() {
		System.out.println("Oh no! One friend is missing.");
	}
	
	public void printWrongAmount() {
		System.out.println("Something is wrong. Did you set the bill amount right?");
	}
	
	public void printFriendWithoutMoney() {
		System.out.println("Oh no! One friend does not have money to pay!");
	}
	
	public void printException(String message) {
		System.out.println(message);
	}
	
	public void printSuccessSplitBill() {
		System.out.println("Bill payed successfully!");
	}
}
