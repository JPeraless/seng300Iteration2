package com.autovend.software;

import java.util.Currency;

import com.autovend.Bill;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillStorage;
import com.autovend.devices.BillValidator;
import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.observers.BillSlotObserver;

public class PayWithCash {

	BillSlot slot = new BillSlot(false);
	BillValidator validator = new BillValidator(Currency.getInstance("CAD"), new int[]{5, 10, 20, 50, 100});
	BillStorage storage = new BillStorage(0);
	
	int amountDue;
	
	
	public PayWithCash(int amountDue) {
		
		this.amountDue = amountDue;
		initialize();
	}
	
	
	public void initialize() {
		slot.enable();
		validator.enable();
		storage.enable();
		
		
	}
	
	
	public void inserted(Bill bill) throws DisabledException, OverloadException {
		if (amountDue > 0) {
			if (slot.accept(bill)) {
				if (validator.accept(bill)) {
					amountDue -= bill.getValue();
				} else {
					slot.emit(bill);
				}
			} else {
				slot.emit(bill);
			}
		} else {
			returnChange();
		}
		
	}
	/**
	 * <p>
	 * Amount due will be a negative number when this method is called, this number represents the total amount of change
	 * owed to the customer. Return change should call on (?) and eject the money through the slot to the customer if 
	 * there is enough change available in the system
	 * </p>
	 *  
	 * <h1>Problems:</h1>
	 * 	<ul>
	 *		<li>Lowest denomination of a bill is $5, what if the change due is <5 ?
	 *		<li>BillStorage has no method to remove a single bill, where are we supposed to get change from? 
	 *	</ul>
	 */
	public void returnChange() {
		
	}
}
