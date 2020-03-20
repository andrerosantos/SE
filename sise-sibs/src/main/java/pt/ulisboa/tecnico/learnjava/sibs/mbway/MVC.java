package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;
import java.util.Scanner;

public class MVC {
	public static void main(String[] args) {
		Controller controller = new Controller();
		
		boolean running = true;
		Scanner s = new Scanner(System.in);
		String input;
		String[] inputs;
		String command;
		
		while (running) {
			
			System.out.println("Insert command: ");
			input = s.nextLine();
			inputs = input.split(" ");
			command = inputs[0];

			switch (command) {
			case "exit":
				running = false;
				System.out.println("You exit the app.");
				break; 
				
			case "associate-mbway":
				controller.createMBWay(inputs[1], Integer.parseInt(inputs[2]));
				
			case "confirm-mbway":
				controller.confirmAccount(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]));
				
			case "mbway-transfer":
				controller.transfer(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]));
				
			case "mbway-split-bill":
				
				int counter = 0;
				int totalAmount = Integer.parseInt(inputs[2]);
				HashMap<Integer, Integer> splits = new HashMap<Integer, Integer>();
				MBWayAccount friend;
								
				for (int i = 0; i < Integer.parseInt(inputs[1]); i++) {
					// first friend already paid and has money to receive
					System.out.println("Insert friend: ");
					input = s.nextLine();
					inputs = input.split(" ");
					command = inputs[0];
					
					if (!command.equals("friend")) {
						// send error
						break;
					} 
					
					friend = MBWayAccount.getMBWayAccount(Integer.parseInt(inputs[1]));
					if (friend == null) {
						
					} else if(!friend.isConfirmed()) {
					
					} else if(friend.getBalance() < Integer.parseInt(inputs[2])){
						
					} else {
						counter += Integer.parseInt(inputs[2]);
						if (counter > totalAmount) {
							
						}
					}
					
					
					
				}
			}
		}
	}
}
