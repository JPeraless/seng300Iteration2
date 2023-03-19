package com.autovend.software;

import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;

import java.util.List;

/*
 * Members for Iteration 1:
 * Michelle Loi (30019557)
 * James Hayward (30149513)
 * Caleb Cavilla (30145972)
 * Amandeep Kaur (30153923)
 * Ethan Oke (30142180)
 * Winjoy Tiop (30069663)
 */

/**
 * Dummy class to simulate attendant actions
 */
public class Attendant {
    private boolean informed = false;

    public Attendant(){}

    /**
     * Allows the attendant to invoke the receipt printer when an error occurs.
     * Either the attendant will successfully print a receipt or a null receipt will occur do to an exception
     * @param selfCheckoutStation current self-checkout station
     * @param billList bill list to print
     */
    public void invokeReceiptPrinter(SelfCheckoutStation selfCheckoutStation, List<BarcodedProduct> billList) {
        try {
            PrintReceipt.print(selfCheckoutStation, billList);
            informed = false;
        } catch (EmptyException e) {
            informed = true; // Keep informed boolean as true so attendant can continue to try and print receipt
        }
    }

    /**
     * Attendant refills ink and the printer is ready to go
     * @param rp receipt printer attendant needs to maintain
     * @param amount the amount to refill
     * @throws OverloadException Too much ink in receipt printer
     */
    public void fillInk(ReceiptPrinter rp, Integer amount) throws OverloadException {
        rp.addInk(amount);
        informed = false; // Turns off informing the attendant
    }

    /**
     * Attendant refills paper and the printer is ready to go
     * @param rp receipt printer attendant needs to maintain
     * @throws OverloadException Too much paper in receipt printer
     */
    public void fillPaper(ReceiptPrinter rp, Integer amount) throws OverloadException{
        rp.addPaper(amount);
        informed = false; // Turns off informing the attendant
    }

    // Return if the attendant is informed to determine next course of action
    public boolean isInformed() {
        return informed;
    }

    // Set if attendant is informed
    public void setInformed(boolean informed) {
        this.informed = informed;
    }
}
