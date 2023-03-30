package com.autovend.software;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.autovend.Bill;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.products.BarcodedProduct;


public class SelfCheckoutSystemLogic {

	private SelfCheckoutStation station;
	private List<BarcodedProduct> billList = new ArrayList<BarcodedProduct>();
	private double changeDispensed = 0;
	private double amountDue = 0;
	private boolean paymentProcess = false;

	private boolean printing;
	
	private boolean isMember = false;
	private String memberNumber;
	
	private PrintReceipt receiptController;
	private CustomerIO customerio;
	private AttendentStub attendent;

	public SelfCheckoutSystemLogic(SelfCheckoutStation station, CustomerIO cs, AttendentStub att) {
		this.station = station;
		this.station.handheldScanner.disable();
		this.station.mainScanner.disable();
		this.station.billInput.disable();
		
		customerio = cs;
		attendent = att;
		
		receiptController = new PrintReceipt(station);
		receiptController.registerCustomerIO(customerio);
		receiptController.registerAttendent(attendent);
		
		station.printer.register(receiptController);
	}
	
	public SelfCheckoutSystemLogic(SelfCheckoutStation station) {
		this.station = station;
		this.station.handheldScanner.disable();
		this.station.mainScanner.disable();
		this.station.billInput.disable();
		this.station.coinSlot.disable();
	}
	
	public void setMemberNumber(String number) {
		memberNumber = number;
	}
	
	public void memberStatus(boolean ism) {
		isMember = ism;
	}



	// sets the system to be ready to take payment, simulates customer indicating they want to pay cash for their bill
		public void payWithCash() throws SimulationException, OverloadException {
			paymentProcess = true;
			PayWithCashController payWithCashController = new PayWithCashController(station, this);
			station.billInput.register(payWithCashController);
			station.billInput.enable();
			station.billValidator.register(payWithCashController);
			station.coinSlot.register(payWithCashController);
			station.billInput.enable();
			station.coinValidator.register(payWithCashController);
			for(BillDispenser dispenser : station.billDispensers.values())
				dispenser.register(payWithCashController);
			for(CoinDispenser dispenser : station.coinDispensers.values())
				dispenser.register(payWithCashController);
		}
		
		public void changeAmountDue(double d, PayWithCashController controller) {
			amountDue -= d;
			if (amountDue <= 0 && paymentProcess) {
				paymentProcess = false; // exits the system out of payment
				station.billInput.disable();
				station.billValidator.disable();
				station.coinSlot.disable();
				station.coinValidator.disable();
				controller.deliverChange(amountDue);
				printing = true; // Set boolean to signal receipt printer to print
			}
		}

	/**
	 * Prints the receipt and notifies attendant of problems
	 * @throws EmptyException 
	 * @throws OverloadException 
	 */
	public boolean startPrinting() throws EmptyException, OverloadException{
		return receiptController.print(billList);
	}
	
	public boolean takeMembership(String number) {
		if (customerio.enterMembership(number)) {
			memberNumber = number;
			return true;
		} else {
			return false;
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
		this.amountDue = amountDue;
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


