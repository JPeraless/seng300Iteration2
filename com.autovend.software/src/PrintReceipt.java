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

//	
	public PrintReceipt(SelfCheckoutStation stn) {
		station = stn;
	}
	
	public void registerCustomerIO(CustomerIO anotherCO) {
		CO = anotherCO;
	}

	public void print(SelfCheckoutStation stn, List<BarcodedProduct> billList) throws EmptyException{

		// Tracks the total cost of the customers purchase
		BigDecimal total = new BigDecimal(0);

		// Build the receipt to print
		StringBuilder receiptOutput = new StringBuilder();
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
				station.printer.print(c);
			} catch (OverloadException oe){
				for(PrintReceiptObserver observer : observers) {
					observer.requiresMaintance(station, oe.getMessage());
				}
			} catch (EmptyException e) {
				if (e.getMessage().contains("There is no paper in the printer.")) {
//					for(PrintReceiptObserver observer : observers) {
//						observer.requiresMaintance(station, e.getMessage());
//					}
					currentMessage = e.getMessage();
					reactToOutOfPaperEvent(station.printer);
				} else {
//					for(PrintReceiptObserver observer : observers) {
//						observer.requiresMaintance(station, e.getMessage());
//					}
					currentMessage = e.getMessage();
					reactToOutOfInkEvent(station.printer);

				}

			}
		}
		

		station.printer.cutPaper(); // Cut the paper
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
			observer.requiresMaintance(station, currentMessage);
		}
		CO.errorCall(currentMessage);
	}


	@Override
	public void reactToOutOfInkEvent(ReceiptPrinter printer) {
		for (PrintReceiptObserver observer : observers) {
			observer.requiresMaintance(station, currentMessage);
		}	
		CO.errorCall(currentMessage);
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
