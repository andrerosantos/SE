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
		
		HashMap<Integer, Integer> friends = new HashMap<Integer, Integer>();
		int friendsCounter = 0;
		int moneyCounter = 0;
		
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
						friends.put(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]));
						friendsCounter++;
						moneyCounter += Integer.parseInt(inputs[2]);
					}
					break;
					
				case "mbway-split-bill":
					controller.splitBill(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]), friends, friendsCounter, moneyCounter);
					friendsCounter = 0;
					moneyCounter = 0;
					friends.clear();
					break;
					
				default:
					System.out.println("Command not recognised.");
					break;
				}
				
			} catch (Exception e) {
				System.out.println("Please check yout input.");
			}
		}
	}
}
