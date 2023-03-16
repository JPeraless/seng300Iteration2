package com.autovend.software;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import com.autovend.Bill;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillStorage;
import com.autovend.devices.BillValidator;
import com.autovend.devices.DisabledException;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.BillSlotObserver;

public class PayWithCash {

	SelfCheckoutStation station = new SelfCheckoutStation(Currency.getInstance("CAD"), new int[]{5, 10, 20, 50, 100}, new BigDecimal[]{new BigDecimal("0")}, 1, 1);
	BillSlot inputSlot = station.billInput;
	BillValidator validator = station.billValidator;
	BillStorage storage = station.billStorage;
	BillSlot outputSlot = station.billOutput;
	Map<Integer, BillDispenser> dispensers = station.billDispensers;
	
	
	double amountDue;
	
	
	public PayWithCash(int amountDue) {
		
		this.amountDue = amountDue;
		initialize();
	}
	
	
	public void initialize() {
		inputSlot.enable();
		validator.enable();
		storage.enable();
	}
	
	/**
	 * <p>
	 * Accepts a bill inserted into the billSlot if 
	 * </p>
	 * @throws InterruptedException 
	 *  
	 */
	public void inserted(Bill bill) throws DisabledException, OverloadException {
		if (amountDue > 0) {
			 inputSlot.accept(bill);
		}else {
			returnChange();
		}
		
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
	public void returnChange()  {
		amountDue = ((amountDue + 4) / 5) * 5;; // round up to the nearest multiple of 5 to avoid under paying customer
		List<Integer> keyList = new ArrayList<Integer>(dispensers.keySet());
		Collections.reverse(keyList);
		for (int i : keyList) {
			int numberOfBills = (int) (amountDue / i);
			for (int j = 0; j < numberOfBills; j++) {
					try {
						dispensers.get(i).emit();
					} catch (DisabledException | EmptyException | OverloadException e) {
						e.printStackTrace();
					}		

				
			amountDue = amountDue % i;
			}
		}
	}
}
