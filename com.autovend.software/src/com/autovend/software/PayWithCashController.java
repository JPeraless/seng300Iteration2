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

import static java.lang.System.out;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillValidator;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.CoinSlot;
import com.autovend.devices.CoinValidator;
import com.autovend.devices.DisabledException;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillDispenserObserver;
import com.autovend.devices.observers.BillSlotObserver;
import com.autovend.devices.observers.BillValidatorObserver;
import com.autovend.devices.observers.CoinDispenserObserver;
import com.autovend.devices.observers.CoinSlotObserver;
import com.autovend.devices.observers.CoinValidatorObserver;

public class PayWithCashController extends SelfCheckoutSystemLogic implements BillSlotObserver, BillValidatorObserver, BillDispenserObserver, CoinSlotObserver, CoinValidatorObserver, CoinDispenserObserver {
	
	SelfCheckoutSystemLogic system;
	double amountDue;
	SelfCheckoutStation station;
	
	public PayWithCashController(SelfCheckoutStation station, SelfCheckoutSystemLogic system) {
		super(station);
		this.system = system;
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
		system.changeAmountDue(value, this);	
	}

	
	@Override
	public void reactToInvalidBillDetectedEvent(BillValidator validator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillsFullEvent(BillDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillsEmptyEvent(BillDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillAddedEvent(BillDispenser dispenser, Bill bill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillRemovedEvent(BillDispenser dispenser, Bill bill) {
		system.setChangeDispensed(system.getChangeDispensed() + bill.getValue());
		
	}

	@Override
	public void reactToBillsLoadedEvent(BillDispenser dispenser, Bill... bills) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToBillsUnloadedEvent(BillDispenser dispenser, Bill... bills) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToCoinsFullEvent(CoinDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToCoinsEmptyEvent(CoinDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToCoinAddedEvent(CoinDispenser dispenser, Coin coin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToCoinRemovedEvent(CoinDispenser dispenser, Coin coin) {
		system.setChangeDispensed(system.getChangeDispensed() +  coin.getValue().doubleValue());
		
	}

	@Override
	public void reactToCoinsLoadedEvent(CoinDispenser dispenser, Coin... coins) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToCoinsUnloadedEvent(CoinDispenser dispenser, Coin... coins) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToValidCoinDetectedEvent(CoinValidator validator, BigDecimal value) {
		system.changeAmountDue(value.doubleValue(), this);
		
	}

	@Override 
	public void reactToInvalidCoinDetectedEvent(CoinValidator validator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToCoinInsertedEvent(CoinSlot slot) {
		// TODO Auto-generated method stub
		
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
		System.out.println("amount Due: " + amountDue);
		amountDue = (int) Math.ceil((-1*amountDue)/0.05) * 0.05; // round up to the nearest multiple of 5 to avoid under paying customer
		System.out.println("amount Due: " + amountDue);
		
		// Split the amount due down into the least amount of bills possible
		List<Integer> billKeyList = new ArrayList<Integer>(station.billDispensers.keySet());
		Collections.reverse(billKeyList);
		for (int i : billKeyList) {
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
		
		System.out.println("amount Due Inbetween: " + amountDue);
		// Split the remaining amount due into the least amount of coins possible
		
		BigDecimal fiveCent = new BigDecimal("0.05");
        BigDecimal tenCent = new BigDecimal("0.10");
        BigDecimal twentyFiveCent = new BigDecimal("0.25");
        BigDecimal loonie = new BigDecimal("1");
        BigDecimal toonie = new BigDecimal("2");
		List<BigDecimal> coinKeyList = new ArrayList<BigDecimal>(Arrays.asList(fiveCent, tenCent, twentyFiveCent, loonie, toonie));
		Collections.reverse(coinKeyList);
		for (BigDecimal i : coinKeyList) {
			System.out.println("I = " + i);
			System.out.println("amount Due before divison: " + amountDue);
			System.out.println("Divisor: " + i.doubleValue());
			int numberOfCoins = (int) (amountDue / i.doubleValue());
			System.out.println("number of coins: "+ numberOfCoins);
			for (int j = 0; j < numberOfCoins; j++) {
					try {
						station.coinDispensers.get(i).emit();
					} catch (DisabledException | EmptyException | OverloadException e) {
						e.printStackTrace();
					}		
					
				
			amountDue = amountDue % i.doubleValue();
			}
		}
	}
	
	
}
