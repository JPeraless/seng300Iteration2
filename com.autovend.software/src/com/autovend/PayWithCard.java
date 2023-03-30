package com.autovend;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;

import com.autovend.devices.*;

/**
 * PayWithCard as credit and debit need literally the same things and their
 * classes have the same parameters
 */
public class PayWithCard extends Pay<PayWithCardObserver> {
    public PayWithCard(SelfCheckoutStation checkoutStation, PayWithCardObserver controller) {
        super(checkoutStation, controller);
    }

    
    CardReader reader = new CardReader();
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
    
    public void payWithCard(Card card, double amount) throws IOException {
    	
    	
    	Scanner scan = new Scanner(System.in);
    	BufferedImage image = new BufferedImage(null, null, false, null);
    	String pin = scan.nextLine();
    	
    	reader.swipe(card, image);
    	
    	
    	reader.insert(card, pin);
    	
    	System.out.println("PIN: " + pin);
    	
    	reader.tap(card);
    }
}
