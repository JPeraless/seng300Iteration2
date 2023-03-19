/*
 * Members for Iteration 1:
 * Michelle Loi (30019557)
 * James Hayward (30149513)
 * Caleb Cavilla (30145972)
 * Amandeep Kaur (30153923)
 * Ethan Oke (30142180)
 * Winjoy Tiop (30069663)
 */
package com.autovend.software;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillValidator;
import com.autovend.devices.DisabledException;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillSlotObserver;
import com.autovend.devices.observers.BillValidatorObserver;

public class PayWithCashController extends System implements BillSlotObserver, BillValidatorObserver {
	
	double amountDue;
	SelfCheckoutStation station;
	
	public PayWithCashController(SelfCheckoutStation station) {
		super(station);
		this.station = station;
		amountDue = super.getAmountDue();
	}

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void reactToBillInsertedEvent(BillSlot slot) {
	
	}

	@Override
	public void reactToBillEjectedEvent(BillSlot slot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillRemovedEvent(BillSlot slot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value) {
		changeAmountDue(value, this);	
	}

	/**
	 * <p>
	 * Amount due will be a negative number when this method is called, this number represents the total amount of change
	 * owed to the customer. Return change will dispense the smallest number of bills possible in-order to return the full change to the customer
	 * For example, if the amountDue is -35, this method will dispense a 20, 10, and 5. (rather than, say, seven 5's)
	 * </p>
	 *  
	 * <h1>Problems:</h1>
	 * 	<ul>
	 *		<li>Lowest denomination of a bill is $5, what if the change due is <5 ?
	 *	</ul>
	 * @throws InterruptedException 
	 */
	public void deliverChange(double amountDue)  {
		amountDue = ((amountDue + 4) / 5) * 5;; // round up to the nearest multiple of 5 to avoid under paying customer
		List<Integer> keyList = new ArrayList<Integer>(station.billDispensers.keySet());
		Collections.reverse(keyList);
		for (int i : keyList) {
			int numberOfBills = (int) (amountDue / i);
			for (int j = 0; j < numberOfBills; j++) {
					try {
						station.billDispensers.get(i).emit();
					} catch (DisabledException | EmptyException | OverloadException e) {
						e.printStackTrace();
					}		

				
			amountDue = amountDue % i;
			}
		}
	}
	
	@Override
	public void reactToInvalidBillDetectedEvent(BillValidator validator) {
		// TODO Auto-generated method stub
		
	}

}
