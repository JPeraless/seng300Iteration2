package com.autovend.software;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.products.BarcodedProduct;

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
/**
 * Class to print receipts once customer's session is complete
 */
public class PrintReceipt implements ReceiptPrinterObserver {

	/**
	 * Prints customer's bill record once payment has been received in full where, the bill record contains
	 * BarcodedProducts. The receipt is formatted as follows:
	 * 						Item ______Barcode______ $Price
	 *
	 * 						Total: $......
	 * @param station The self-checkout station the customer is using
	 * @param billList The list of items the customer has paid for in full
	 * @return A string representing the receipt
	 * @throws EmptyException Receipt printer contains no ink
	 */
	
	private ArrayList<PrintReceiptObserver> observers = new ArrayList<>();
	private SelfCheckoutStation station;
	private String currentMessage;
	private CustomerIO CO;
//	private String currentBillPrinted;
	private AttendentStub attendent;

//	
	public PrintReceipt(SelfCheckoutStation stn) {
		station = stn;
	}
	
	public void registerObserver(PrintReceiptObserver observer) {
		observers.add(observer);
	}
	
	public void registerCustomerIO(CustomerIO anotherCO) {
		CO = anotherCO;
		observers.add(anotherCO);
	}
	
	public void registerAttendent(AttendentStub att) {
		attendent = att;
		observers.add(att);
	}
	
	public ReceiptPrinter getPrinter() {
		return station.printer;
	}
	
	public boolean print(List<BarcodedProduct> billList) throws EmptyException, OverloadException{

		// Tracks the total cost of the customers purchase
		BigDecimal total = new BigDecimal(0);

		// Build the receipt to print
		StringBuilder receiptOutput = new StringBuilder();
		System.out.println(billList.getClass());
		for (BarcodedProduct bp : billList) {
			receiptOutput.append(bp.getDescription());
			receiptOutput.append("      ");
			receiptOutput.append(bp.getBarcode());
			receiptOutput.append("      ");
			receiptOutput.append("$");
			receiptOutput.append(bp.getPrice());
			receiptOutput.append("\n");
			total = total.add(bp.getPrice());
		}
		receiptOutput.append("\nTotal: $");
		receiptOutput.append(total);

		// Convert String to char and use receipt printer to print character by character
		char[] receipt = receiptOutput.toString().toCharArray();
		for (char c : receipt) {
			try{
//				currentBillPrinted += c;
				station.printer.print(c);
			} catch (OverloadException oe){
				for(PrintReceiptObserver observer : observers) {
					observer.requiresMaintainence(station, oe.getMessage());
				}
				return false;
			} catch (EmptyException e) {
				if (e.getMessage().contains("There is no paper in the printer.")) {
//					for(PrintReceiptObserver observer : observers) {
//						observer.requiresMaintance(station, e.getMessage());
//					}
					station.printer.cutPaper(); // Cut the paper

					currentMessage = e.getMessage();
					reactToOutOfPaperEvent(station.printer);
//					if (added) {
//						station.printer.print(c);
//					}
					return false;
				} else {
//					for(PrintReceiptObserver observer : observers) {
//						observer.requiresMaintance(station, e.getMessage());
//					}
					station.printer.cutPaper(); // Cut the paper

					currentMessage = e.getMessage();
					reactToOutOfInkEvent(station.printer);
//					if (added) {
//						station.printer.print(c);
//					}
					return false;
				}

			}
		}
		station.printer.cutPaper(); // Cut the paper
		for (PrintReceiptObserver observer : observers) {
			observer.sessionComplete(station);
		}
		return true;
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
	public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
		for (PrintReceiptObserver observer : observers) {
			observer.requiresMaintainence(station, currentMessage);
		}
		CO.errorCall(currentMessage);
		attendent.requiresMaintainence();
	}


	@Override
	public void reactToOutOfInkEvent(ReceiptPrinter printer) {
		for (PrintReceiptObserver observer : observers) {
			observer.requiresMaintainence(station, currentMessage);
		}	
		CO.errorCall(currentMessage);
		attendent.requiresMaintainence();
	
	}

	@Override
	public void reactToPaperAddedEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToInkAddedEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
	}
	
}
