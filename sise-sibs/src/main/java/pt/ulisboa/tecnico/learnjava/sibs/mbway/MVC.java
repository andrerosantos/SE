package pt.ulisboa.tecnico.learnjava.sibs.mbway;

import java.util.Scanner;

public class MVC {
	private static Scanner SCANNER = new Scanner(System.in);
	
	public static void main(String[] args) {

	      //fetch student record based on his roll no from the database
	      MBWayAccount model  = retriveMBWayAccount();

	      //Create a view : to write student details on console
	      View view = new View();

	      Controller controller = new Controller(model, view);

	      while (true) {
	    	  System.out.println("Insert command: ");
	    	  String input = SCANNER.nextLine();
	    	  
	    	  if(input.equals("exit")) { 
	    		  break; 
	    	  } else if (input.substring(0, input.indexOf(" ")).contentEquals("associate-mbway")) {
	    		  String newInput = input.substring(input.indexOf(" ")+1);
	    		  System.out.println("iban: " + newInput.substring(0, newInput.indexOf(" "))); 
	    		  System.out.println("phone number: " + newInput.substring(newInput.indexOf(" ") + 1)); 
	    		  
	    		  
	    	  } else if (input.substring(0, input.indexOf(" ")).contentEquals("confirm-mbway")) {
	    		  //check if input equals confirmation code
	    		  
	    	  } else if (input.substring(0, input.indexOf(" ")).contentEquals("mbway-transfer")) {
	    		  // mbway-transfer <SOURCE_PHONE_NUMBER> <TARGET_PHONE_NUMBER> <AMOUNT>
	    		  
	    		  String sourceNumber = input.substring(input.indexOf(" ")+1);
	    		  System.out.println("source phone number: " + sourceNumber.substring(0, sourceNumber.indexOf(" "))); 
	    		  
	    		  String targetNumber = input.substring(input.indexOf(" ")+1);
	    		  System.out.println("source phone number: " + targetNumber.substring(0, targetNumber.indexOf(" "))); 
	    		  
	    		  //por acabar!!!!!
	    		  
	    	  } else if (input.substring(0, input.indexOf(" ")).contentEquals("mbway-split-bill")) {
	    		  // mbway-split-bill <NUMBER_OF_FRIENDS> <AMOUNT>
	    		  String numberOfFriends = input.substring(input.indexOf(" "), input.indexOf(" ", input.indexOf(" ")));
	    		  
	    		  System.out.println(numberOfFriends);
	    		  
	    		  String[] inputs = input.split("\\s+");
	    		  
	    		  System.out.println(inputs[1]);
	    		  
	    		  // friend <PHONE_NUMBER> <AMOUNT>
	    		  
	    		  
	    	  }  
	      }
	   }

	   private static MBWayAccount retriveMBWayAccount(){
	      MBWayAccount mbAccount = new MBWayAccount("BPICK1", 911111111);
	      return mbAccount;
	   }
	   
}
