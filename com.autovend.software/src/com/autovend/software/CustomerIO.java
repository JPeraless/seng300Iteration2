package com.autovend.software;

import com.autovend.devices.SelfCheckoutStation;

public class CustomerIO implements PrintReceiptObserver {
    private Boolean thanks = false; // Status for if the customer needs to be thanked
    private Boolean ready = false; // Status if ready for new customer session
    
    private String message = "";
//    private String name;
//    private String password;
    

    private boolean customerNoBag;
    
    private int numberOfBagsPurchased;
    private int numberOfPersonalBags;
    

//    private String name;
//    private String password;
    
    public String printToDisplay() {
    	return message;
    }

    // Customer IO returns thank you message to screen ("Thank you for shopping with us today!") or
    // empty string if they shouldn't be thanked yet
    public void thankCustomer(){
        if(thanks){
        	message = "Thank you for shopping with us today!";
        }
        printToDisplay();
    }

    // Sets if ready for new customer (at the start of new customer set to false, and once finished set to true)
    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    // Returns status about customer session
    public Boolean getReady() {
        return ready;
    }

    // System signals Customer I/O through thanks boolean
    public void setThanks(Boolean thanks){
        this.thanks = thanks;
    }

    // Returns if customer should be thanked
    public Boolean getThanks() {
        return thanks;
    }
    
    public void errorCall(String error) {
    	message = error;
        printToDisplay();
    }
    
    public boolean enterMembership(String number) {
    	displayKeyboard();
    	if (MemberDatabase.userExists(number)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public String displayKeyboard() {
    	message = "Please type in your membership number";
    	return printToDisplay();
    }
    
         
     public String sessionComplete() {
    	 message = "Payment Session Complete";
    	 return printToDisplay();
     }


	@Override
	public void requiresMaintainence(SelfCheckoutStation station, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionComplete(SelfCheckoutStation station) {
		// TODO Auto-generated method stub
		
	}
     
    public boolean getCustomerNoBag() {
    	return this.customerNoBag;
    }
    
    public void setCustomerNoBag(boolean val) {
    	this.customerNoBag = val;
    }
    
    public int getNumberOfBagsPurchased() {
    	return this.numberOfBagsPurchased;
    }
    
    public void setNumberOfBagsPurchased(int val) {
    	this.numberOfBagsPurchased = val;
    }
    
    public int getNumberOfPersonalBags() {
    	return this.numberOfPersonalBags;
    }
    
    public void setNumberOfPersonalBags(int val) {
    	this.numberOfPersonalBags = val;
    }

}