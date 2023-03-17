package com.autovend.software;

import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.products.BarcodedProduct;

public class PrintReceipt implements ReceiptPrinterObserver {
	
	
	
	public void print(SelfCheckoutStation station) throws EmptyException {
		StringBuilder sb = new StringBuilder();
//		1. System: The bill record will be updated with details of the payment(s).
		List<BarcodedProduct> billList = station.getBillList();
		for (BarcodedProduct bp : billList) {
			sb.append("Item: ");
			sb.append(bp.getDescription());
			sb.append("\nPrice: ");
			sb.append(bp.getPrice());
			sb.append("\n\n");
		}
//		2. System: Signals to the Receipt Printer to print the bill record.
		String receipt = sb.toString();
		for (char c : receipt) {
			try {
				station.printer.print(c);
			} catch (OverloadException oe) {
				station.printer.print("\n");
			} catch (EmptyException ee) {
				throw ee;
			}
		}
//		3. ReceiptPrinter: Printsthereceipt.
//		4. System: Signals to Customer I/O that the customer’s session is complete.
//		5. Customer I/O: Thanks the Customer.
//		6. Customer I/O: Ready for a new customer session.
	}
	
	/**
	 * Announces that the indicated printer is out of paper.
	 * 
	 * @param printer
	 *            The device from which the event emanated.
	 */
	void reactToOutOfPaperEvent(ReceiptPrinter printer) {
		
	}

	/**
	 * Announces that the indicated printer is out of ink.
	 * 
	 * @param printer
	 *            The device from which the event emanated.
	 */
	void reactToOutOfInkEvent(ReceiptPrinter printer) {
		
	}

	/**
	 * Announces that paper has been added to the indicated printer.
	 * 
	 * @param printer
	 *            The device from which the event emanated.
	 */
	void reactToPaperAddedEvent(ReceiptPrinter printer) {
		
	}

	/**
	 * Announces that ink has been added to the indicated printer.
	 * 
	 * @param printer
	 *            The device from which the event emanated.
	 */
	void reactToInkAddedEvent(ReceiptPrinter printer) {
		
	}
	
}
