/*
 * Anna Lee: 30137463
 * Caleb Cavilla: 30145972
 * Desmond O'Brien: 30064340
 * Jose Perales: 30143354
 * Matthew Cusanelli: 30145324
 * Muhtadi Alam: 30150910
 * Omar Tejada: 31052626
 * Saadman Rahman: 30153482
 * Sahaj Malhotra: 30144405
 * Sean Tan: 30094560
 * Tanvir Ahamed Himel: 30148868
 * Victor campos: 30106934
 */
package com.autovend.software;

import com.autovend.devices.SelfCheckoutStation;

public class CustomerIO implements PrintReceiptObserver {
    private Boolean thanks = false; // Status for if the customer needs to be thanked
    private Boolean ready = false; // Status if ready for new customer session
    
    private String message = "";
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
     
    

}