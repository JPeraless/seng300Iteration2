package com.autovend.software;
	import java.util.Scanner;
	/**
	Desmond O'Brien: 30064340
	Matthew Cusanelli: 30145324
	Saadman Rahman: 30153482
	Tanvir Ahamed Himel: 30148868
	Victor campos: 30106934
	Sean Tan: 30094560
	Sahaj Malhotra: 30144405 
	Caleb Cavilla: 30145972
	Muhtadi Alam: 30150910
	Omar Tejada: 31052626
	Jose Perales: 30143354
	Anna Lee: 30137463
	 */
public class MembershipCard {

	   private Scanner scanner;

		    public void MembershipNumberInput() {
		        scanner = new Scanner(System.in);
		    }

		    
		    /* public String enterMembershipNumber() {
		        String membershipNumber = "";
		        boolean isValid = false;

		        while (!isValid) {
		            System.out.print("Please enter your membership number: ");
		            membershipNumber = scanner.nextLine();


	// Check if the membership number is valid  (contains only digits, is of correct length, And is in database)
		             
		            if (isValidMembershipNumber(membershipNumber)) {
		                isValid = true;
		            } else {
		                System.out.println("Invalid membership number. Please try again.");
		            }
		        }

		        return membershipNumber;
		    }


		**/


		    // Need to create seperate string for customer name, and if tap is enabled or not ???
		    
		    
		    
		    //Still have to work on Exceptions:
		    //1. Bad membership numbers should be detected and the customer informed about this. It is possible
		    //that they made a typographical error, or that what they believed to be their membership number is not correct.
		    	
		    //2. The customer should be able to cancel the operation, rather than being forced to provide a valid number


}
