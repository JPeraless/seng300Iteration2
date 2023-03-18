package com.autovend.software;

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
	
	public static void print(SelfCheckoutStation station, List<BarcodedProduct> billList) throws EmptyException {
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
