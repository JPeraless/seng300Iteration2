package com.autovend;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import com.autovend.Card.CardData;
import com.autovend.devices.*;
import com.autovend.external.CardIssuer;
import com.autovend.software.CustomerIO;

/**
 * PayWithCard as credit and debit need literally the same things and their
 * classes have the same parameters
 */
public class PayWithCard extends Pay<PayWithCardObserver> {
    CardReader reader;
    Card card;
    String pin;
    
    CardIssuer bank;
    Calendar expiry;
    
    BigDecimal amountPaid;
    BigDecimal totalBill;
    CustomerIO customer;
    
    boolean totalBillPaid = false;
    
    ArrayList<PayWithCardObserver> observers = new ArrayList<PayWithCardObserver>();
    
    

    public PayWithCard(SelfCheckoutStation checkoutStation, PayWithCardObserver controller, Card cd, CustomerIO cus) {
        super(checkoutStation, controller);
        reader = new CardReader();
        card = cd;
        
        customer = cus;
    }
    
    public void setBank(CardIssuer bk) {
    	bank = bk;
    }
    
    public void setExpiry(Calendar date) {
    	expiry = date;
    }

    
    
    /**
     * Make a payment for paymentMade amount, and update toBePaid and amountPaid
     * accordingly
     */
    @Override
    public void makePayment(double paymentMade) {
        reduceAmountDue(paymentMade);

        if (getAmountPaid() > getTotalAmountDue()) {
            // for (CardReaderObserver observer : observers) {
            // observer.reactToCompletePaymentEvent(this);
            // }
        }
    }
    
    public void payWithCard(Card card, double amount, String type) throws IOException {
    	
    	reader.enable();
    	BufferedImage image = new BufferedImage(null, null, false, null);
    	CardData data;
    	
    	if (type.equals("insert")) {
        	data = reader.insert(card,  pin);
    	} else if (type.equals("tap")) {
        	data = reader.tap(card);
    	} else {
        	data = reader.swipe(card, image);
    	}
    	
    	bank.addCardData(data.getNumber(), data.getCardholder(), expiry, data.getCVV(), amountPaid);
    	int holdNumber = bank.authorizeHold(data.getNumber(), amountPaid);
    	//something to do with system. see usecase scenario number 7 and 8
    	
    	boolean transactionComplete = bank.postTransaction(data.getNumber(), holdNumber, amountPaid);
    	
    	if (!transactionComplete) {
    		for (PayWithCardObserver observer : observers) {
        		observer.reactToNotEnouighFunds();
    		}
    	} else {
        	boolean holdreleased = bank.releaseHold(data.getNumber(), holdNumber);
        	
        	totalBill = totalBill.subtract(amountPaid);
    	}
    	
    	if (reader.remove()) {
    		customer.paymentSessionComplete();
    	}
    	
    	int res = totalBill.compareTo(new BigDecimal("0")); 
    	
    	if (res == 1) {
    		totalBillPaid = false;
    	} else if (res == 2) {
    		totalBillPaid = true;
    		//requires change
    	} else {
    		totalBillPaid = true;
    	}
    	
    }
    
    public void setPin(String num) {
    	pin = num;
    }
}