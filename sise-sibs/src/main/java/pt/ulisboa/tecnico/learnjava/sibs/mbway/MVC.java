package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.HashMap;
import java.util.Scanner;

public class MVC {
	private static Scanner SCANNER = new Scanner(System.in);
	
	public static void main(String[] args) {

	      //fetch student record based on his roll no from the database
	      MBWayAccount model  = retriveMBWayAccount();

	      //Create a view : to write student details on console
	      View view = new View();

	      Controller controller = new Controller();

	      while (true) {
	    	  System.out.println("Insert command: ");
	    	  String input = SCANNER.nextLine();
	    	  String[] inputs = input.split(" ");
	    	  
	    	  
	    	  if(input.equals("exit")) { 
	    		  break; 
	    		  
	    	  } else if (inputs[0].contentEquals("associate-mbway")) {
	    		  controller.createMBWay(inputs[1], Integer.parseInt(inputs[2]));
	    		  
	    	  } else if (inputs[0].contentEquals("confirm-mbway")) {
	    		  controller.confirmAccount(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]));
	    		  
	    	  } else if (inputs[0].contentEquals("mbway-transfer")) {
	    		  // mbway-transfer <SOURCE_PHONE_NUMBER> <TARGET_PHONE_NUMBER> <AMOUNT>
	    		  controller.transfer(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]), Integer.parseInt(inputs[3]));
	    		  
	    	  } else if (inputs[0].contentEquals("mbway-split-bill")) {
	    		  // mbway-split-bill <NUMBER_OF_FRIENDS> <AMOUNT>
	    		  
	    		  int counter = 0;
	    		  HashMap<Integer, Integer> splits = new HashMap<Integer, Integer>();
	    		  for (int i = 0; i < Integer.parseInt(inputs[1]); i++) {
	    			  // friend <PHONE_NUMBER> <AMOUNT>
	    			  
	    		  }
	    		  
	    	  }  
	      }
	   }

	   private static MBWayAccount retriveMBWayAccount(){
	      MBWayAccount mbAccount = new MBWayAccount("BPICK1", 911111111);
	      return mbAccount;
	   }
	   
}
