package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;
import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MVC {
	public static void main(String[] args) throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException {
		
		// Use 'BPICK1', 'BPICK2', 'BPICK3' and 'BPICK4' as IBAN values
		accountsCreation();
		
		boolean running = true;
		Scanner s = new Scanner(System.in);
		String input;
		String[] inputs;
		String command;
		
		Controller controller = new Controller();
		
		HashMap<Integer, Integer> friends = new HashMap<Integer, Integer>();
		int receiver = 0;
		
		while (controller.isRunning()) {
			
			System.out.println("Insert command: ");
			input = s.nextLine();
			inputs = input.split(" ");
			command = inputs[0];
			
			try {
				
				switch (command) {
				case "exit":
					controller.stopRunnging();
					break; 
					
				case "associate-mbway":
					controller.createMBWay(inputs[1], Integer.parseInt(inputs[2]));
					break;
					
				case "confirm-mbway":
					controller.confirmAccount(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]));
					break;
				case "mbway-transfer":
					controller.transfer(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]));
					
				case "friend":
					if (controller.accountExists(Integer.parseInt(inputs[1]))) {
						if (friends.isEmpty()) {
							// save the account that will receive the money
							receiver = Integer.parseInt(inputs[1]);
						}
						friends.put(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]));
					}
					break;
					
				case "mbway-split-bill":
					controller.splitBill(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]), friends, receiver);
					friends.clear();
					break;
					
				default:
					System.out.println("Command not recognised.");
					break;
				}
				
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				System.out.println("Please check yout input.");
			}
			
		}
	}
	
	public static void accountsCreation() throws BankException, ClientException, AccountException {
		Bank bpi = new Bank("BPI");
		
		Client client1 = new Client(bpi, "John", "Doe", "123456789", "987654321", "Street", 25);
		Client client2 = new Client(bpi, "Jane", "Doe", "987654321", "123456789", "Street", 25);
		Client client3 = new Client(bpi, "John", "Smith", "789456123", "321654987", "Street", 25);
		Client client4 = new Client(bpi, "Jane", "Smith", "321654987", "789456123", "Street", 25);
		
		bpi.createAccount(Bank.AccountType.CHECKING, client1, 1000, 0);
		bpi.createAccount(Bank.AccountType.CHECKING, client2, 1000, 0);
		bpi.createAccount(Bank.AccountType.CHECKING, client3, 1000, 0);
		bpi.createAccount(Bank.AccountType.CHECKING, client4, 1000, 0);
	}
	
}
