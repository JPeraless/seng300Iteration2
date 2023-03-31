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

public interface  PrintReceiptObserver {
	    /**
	     * Announces that the Customer Session is complete.
	     * 
	     * @param station
	     *            The device whose session is complete.
	     *            tasks for iteration2: signals customer io that session is complete
	     */
	    void sessionComplete(SelfCheckoutStation station);

	    /**
	     * Announces that the machine requires Maintanence.
	     * 
	     * @param station
	     *            The device who requires Maintances.
	     *            tasks for iteration 2: signals attendent stub that printer requires maintainence
	     */
	    void requiresMaintainence(SelfCheckoutStation station, String message);
	    

}
