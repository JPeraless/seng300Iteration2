package com.autovend.software;

import java.math.BigDecimal;
import java.util.List;

import com.autovend.devices.AbstractDevice;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;
import com.autovend.products.BarcodedProduct;

public class PrintReceipt implements ReceiptPrinterObserver {

/*	public static void print(SelfCheckoutStation station, List<BarcodedProduct> billList) throws EmptyException {
		StringBuilder sb = new StringBuilder();
//		1. System: The bill record will be updated with details of the payment(s).
		for (BarcodedProduct bp : billList) {
			sb.append("Item: ");
			sb.append(bp.getDescription());
			sb.append("\nPrice: ");
			sb.append(bp.getPrice());
			sb.append("\n\n");
		}
//		2. System: Signals to the Receipt Printer to print the bill record.
		char[] receipt = sb.toString().toCharArray();
		for (char c : receipt) {
			try {
				station.printer.print(c);
			} catch (OverloadException oe) {
				try {
					station.printer.print('\n');
				} catch (OverloadException ignored) {}
			} catch (EmptyException ee) {
				throw ee;
			}
		}
//		3. ReceiptPrinter: Printsthereceipt.
		station.printer.cutPaper();
//		4. System: Signals to Customer I/O that the customer’s session is complete.
//		5. Customer I/O: Thanks the Customer.
//		6. Customer I/O: Ready for a new customer session.
	}*/

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
	 * @throws OverloadException Receipt printer contains too much paper
	 */
	public static String print(SelfCheckoutStation station, List<BarcodedProduct> billList)
			throws EmptyException, OverloadException {

		// Tracks the total cost of the customers purchase
		BigDecimal total = new BigDecimal(0);

		// Build the receipt to print
		StringBuilder receiptOutput = new StringBuilder();
		for (BarcodedProduct bp : billList) {
			receiptOutput.append(bp.getDescription());
			receiptOutput.append("______");
			receiptOutput.append(bp.getBarcode());
			receiptOutput.append("______");
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
			station.printer.print(c);
		}

		// Print the receipt
		station.printer.cutPaper(); // Cut the paper
		return station.printer.removeReceipt(); // Return the removed receipt
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToOutOfInkEvent(ReceiptPrinter printer) {
		// TODO Auto-generated method stub
		
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
