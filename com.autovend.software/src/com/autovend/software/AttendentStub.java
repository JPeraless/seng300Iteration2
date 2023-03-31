package com.autovend.software;
import java.util.List;

import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.products.BarcodedProduct;

public class AttendentStub implements PrintReceiptObserver {
	
//	public void fillPrinterWithPaper() {
//		
//	}
	PrintReceipt observer;
	ReceiptPrinter printer;
	List<BarcodedProduct> duplicateBill;
	private boolean maintainenceRequired;
	


	private boolean discrepancyApproved;
	
	public AttendentStub(PrintReceipt ob) {
		PrintReceipt observer = ob;
		printer = observer.getPrinter();
	}
	
	public AttendentStub() {};	
	public boolean addPaperToPrinter() {
		try {
			printer.addPaper(1 << 9);
			printDuplicateReceipt();
			return true;
		} catch (OverloadException e) {
//			observer.reactToPaperAddedEvent(printer);
			return false;
		} 
	
		catch (EmptyException f) {
			return false;
		}
	}
	
	public boolean addInkToPrinter()  {
		try {
			printer.addInk(1 << 19);
			printDuplicateReceipt();
			return true;
		} catch (OverloadException e) {
//			observer.reactToInkAddedEvent(printer);
			return false;
		}
		catch (EmptyException f) {
			return false;
		}
	}
	
	public void printDuplicateReceipt() throws EmptyException, OverloadException {
		observer.print(duplicateBill);
	}
		
	public void requiresMaintainence() {
		maintainenceRequired = true;
	}
	
	public boolean isMaintainenceRequired() {
		return maintainenceRequired;
	}

	@Override
	public void sessionComplete(SelfCheckoutStation station) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requiresMaintainence(SelfCheckoutStation station, String message) {
		// TODO Auto-generated method stub
		
	}

	
	public void setDiscrepancyApproved(boolean bool) {
		this.discrepancyApproved = bool;
	}
	
	public boolean getDiscrepancyApproved() {
		return this.discrepancyApproved;
	}

}
