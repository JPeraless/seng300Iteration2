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
package com.autovend;

import java.math.BigDecimal;
import java.util.Calendar;

import com.autovend.Card;
import com.autovend.Card.CardData;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.CardReader;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CardReaderObserver;
import com.autovend.external.CardIssuer;
import com.autovend.software.CustomerIO;
import com.autovend.software.SelfCheckoutSystemLogic;

public class PayWithCardObserver extends SelfCheckoutSystemLogic implements CardReaderObserver {

	private Pay system;
	private SelfCheckoutStation station;
	private double amountDue;
	private Card.CardData data;
	private CardReader reader;
    private Card card;
    private String pin;
    
    private CardIssuer bank;
    private Calendar expiry;
    
    private BigDecimal amountPaid;
    private BigDecimal totalBill;
    private CustomerIO customer;
    
    boolean totalBillPaid = false;
	public PayWithCardObserver(SelfCheckoutStation station, Pay system) {
		super(station);
		this.system = system;
		this.station = station;
		amountDue = super.getAmountDue();
	}

	
	
    @Override
    public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {

    }

    @Override
    public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {

    }



    @Override
    public void reactToCardRemovedEvent(CardReader reader) {
    	customer.sessionComplete();
    }

    @Override
    public void reactToCardInsertedEvent(CardReader reader) {
    	/// exectute
    }
    
    @Override
    public void reactToCardTappedEvent(CardReader reader) {

    }

    @Override
    public void reactToCardSwipedEvent(CardReader reader) {

    }

    @Override
    public void reactToCardDataReadEvent(CardReader reader, Card.CardData data) {
    	this.data = data;
    	
    	bank.addCardData(data.getNumber(), data.getCardholder(), expiry, data.getCVV(), amountPaid);
    	
    	int holdNumber = bank.authorizeHold(data.getNumber(), amountPaid);
    	boolean transactionComplete = bank.postTransaction(data.getNumber(), holdNumber, amountPaid);
    	
    	if (!transactionComplete) {
    		reactToNotEnouighFunds();
    	} else {
        	boolean holdreleased = bank.releaseHold(data.getNumber(), holdNumber);
        	reactToCompletePaymentEvent();
    	}
    }


    public void reactToCompletePaymentEvent() {
    	totalBill = totalBill.subtract(amountPaid);
    }

	public void reactToNotEnouighFunds() {
		// TO DO
	}
}