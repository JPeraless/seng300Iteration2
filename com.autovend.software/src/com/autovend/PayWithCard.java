package com.autovend;

import com.autovend.devices.*;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.CardReaderObserver;


/**
 * PayWithCard as credit and debit need literally the same things and their
 * classes have the same parameters
 */
public class PayWithCard extends AbstractDevice<PayWithCardObserver> {


    protected SelfCheckoutStation checkoutStation;

    /**
     * Amount to be paid by the customer
     */
    protected int totalPayment;

    /**
     * Amount that has been paid by the customer
     */
    protected int amountPaid;


    /**
     * Class constructor
     *
     * @param checkoutStation
     */
    public PayWithCard(SelfCheckoutStation checkoutStation) {
        if (checkoutStation == null) {
            throw new SimulationException("Station cannot not be null");
        }

        this.checkoutStation = checkoutStation;


    }


    /**
     * Make a payment for paymentMade amount, and update toBePaid and amountPaid accordingly
     */
    public void makePayment(int paymentMade) {
        amountPaid += paymentMade;

        if (amountPaid >= totalPayment) {
            for (CardReaderObserver observer : observers) {
                // observer.reactToCompletePaymentEvent(this);
            }
        }

    }


    /**
     *
     * @return amount missing to be paid by customer
     */
    public int getToBePaid() {
        return totalPayment - amountPaid;
    }


    /**
     *
     * @return amount paid so far by customer
     */
    public int getAmountPaid() {
        return amountPaid;
    }

}