package com.autovend;

import com.autovend.devices.*;

/**
 * PayWithCard as credit and debit need literally the same things and their
 * classes have the same parameters
 */
public class PayWithCard extends Pay<PayWithCardObserver> {
    public PayWithCard(SelfCheckoutStation checkoutStation, PayWithCardObserver controller) {
        super(checkoutStation, controller);
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
}
