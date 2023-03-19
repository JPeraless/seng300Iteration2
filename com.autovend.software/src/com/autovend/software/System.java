package com.autovend.software;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.autovend.Bill;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.products.BarcodedProduct;

/*
 * Members for Iteration 1:
 * Michelle Loi (30019557)
 * James Hayward (30149513)
 * Caleb Cavilla (30145972)
 * Amandeep Kaur (30153923)
 * Ethan Oke (30142180)
 * Winjoy Tiop (30069663)
 */

public class System {

	private double changeDispensed = 0;
	private SelfCheckoutStation station;
	private List<BarcodedProduct> billList = new ArrayList<BarcodedProduct>();
	private double amountDue = 0;
	private boolean paymentProcess = false;

	private boolean printing;
	Attendant attendant = new Attendant();
	CustomerIO customerIO = new CustomerIO();

	public System(SelfCheckoutStation station) {
		this.station = station;
		this.station.handheldScanner.disable();
		this.station.mainScanner.disable();
		this.station.billInput.disable();
	}

	public System(SelfCheckoutStation station, Attendant attendant, CustomerIO customerIO){
		this.station = station;
		this.attendant = attendant;
		this.customerIO = customerIO;
	}

	// sets the system to be ready to take payment, simulates customer indicating they want to pay cash for their bill
	public void payWithCash() throws SimulationException, OverloadException {
		paymentProcess = true;
		PayWithCashController payWithCashController = new PayWithCashController(station, this);
		station.billInput.register(payWithCashController);
		station.billInput.enable();
		station.billValidator.register(payWithCashController);
		for(BillDispenser dispenser : station.billDispensers.values())
			dispenser.register(payWithCashController);
		
		for(int x : station.billDispensers.keySet()) {
			Bill bill = new Bill(x, Currency.getInstance("CAD"));
			station.billDispensers.get(x).load(bill, bill, bill, bill, bill);
		}
	}
	
	public void changeAmountDue(int value, PayWithCashController controller) {
		amountDue -= value;
		if (amountDue <= 0) {
			paymentProcess = false; // exits the system out of payment
			station.billInput.disable();
			controller.deliverChange(amountDue);
			printing = true; // Set boolean to signal receipt printer to print
		}
	}

	/**
	 * Prints the receipt and notifies attendant of problems
	 */
	public void startPrinting(){
		if(printing){
			try {
				PrintReceipt.print(station, billList);
				customerIO.setThanks(true); // signal to customer session is complete
			} catch (EmptyException e) {
				// Print to screen that attendant has been flagged
				attendant.setInformed(true);
			}
		}
	}
	
	/**
	 * Adds an item to the end of the current bill list
	 */
	public void addBillList(BarcodedProduct product) {
		billList.add(product);
	}
	/**
	 * removes the item in the bill list at the specified index
	 */
	public void removeBillList(int index) {
		billList.remove(index);
	}

	/**
	 * gets the current bill List
	 */
	public List<BarcodedProduct> getBillList() {
		return billList;
	}

	/**
	 * sets the current bill list
	 */
	public void setBillList(List<BarcodedProduct> billList) {
		this.billList = billList;
	}

	/**
	 * gets the total amount currently due based on the bill list
	 */
	public double getAmountDue() {
		return amountDue;
	}

	/**
	 * sets the total amount due
	 */
	public void setAmountDue(double amountDue) {
		this.amountDue = this.amountDue + amountDue;
	}
	
	/**
	 * gets the total amount currently due based on the bill list
	 */
	public SelfCheckoutStation getStation() {
		return station;
	}

	public void setPrinting(boolean printing) {
		this.printing = printing;
	}

	public double getChangeDispensed() {
		return changeDispensed;
	}

	public void setChangeDispensed(double changeDispensed) {
		this.changeDispensed = changeDispensed;
	}
}


