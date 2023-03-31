package com.autovend.software;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;

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
public abstract class Pay<T extends AbstractDeviceObserver> {
    protected final SelfCheckoutStation checkoutStation;
    protected final T controller;
    private double totalAmountDue;
    private double amountPaid;

    public Pay(SelfCheckoutStation checkoutStation, T controller) {
        if (checkoutStation == null)
            throw new SimulationException("Station cannot not be null");
        if (controller == null)
            throw new SimulationException("Controller cannot be null");

        this.checkoutStation = checkoutStation;
        this.controller = controller;

        PayWithCardObserver cardObserver = new PayWithCardObserver(checkoutStation, this);
        
        checkoutStation.cardReader.register(cardObserver);
        
        amountPaid = 0;
    }

    // TODO: Decide how to handle 'indicating the mode of payment'
    // public abstract void choosePaymentMethod();

    public abstract void makePayment(double paymentMade);

    protected void reduceAmountDue(double paymentMadeAmount) {
        amountPaid += paymentMadeAmount;
    }

    public double getTotalAmountDue() {
        return totalAmountDue;
    }

    public double getAmountPaid() {
        return amountPaid;
    }
}
