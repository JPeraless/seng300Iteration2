import java.util.ArrayList;
import java.util.List;

import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;


public class VendingSystem {

	private SelfCheckoutStation station;
	private List<BarcodedProduct> billList = new ArrayList<BarcodedProduct>();
	private double amountDue = 0;
	private boolean paymentProcess = false;

	private boolean printing;

	public VendingSystem(SelfCheckoutStation station) {
		this.station = station;
		this.station.handheldScanner.disable();
		this.station.mainScanner.disable();
		this.station.billInput.disable();
	}



	// sets the system to be ready to take payment, simulates customer indicating they want to pay cash for their bill
	public void payWithCash() {
		paymentProcess = true;
		PayWithCashController payWithCashController = new PayWithCashController(station);
		station.billInput.register(payWithCashController);
		station.billInput.enable();
		station.billValidator.register(payWithCashController);
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
		if(printing) {
			try {
				PrintReceipt.print(station, billList);
			} catch (EmptyException e) {

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
	
	
	private boolean attendantApproved = false; 
	
	/**
	 * Changes the value of the attendantApproved variable
	 * 
	 * @param selected: new value for attendantApproved variable
	 */
	public void setAttendantApproved(boolean selected) {
		this.attendantApproved = selected;
	}
	
	/**
	 * This method is called when the bagging area scale announces a change is weight.
	 * 
	 * @param expectedWeight: Expected weight of the bagging area scale
	 * @throws OverloadException: If the weight has overloaded the scale
	 */
	public void checkForWeightDiscrepancy(double expectedWeight) throws OverloadException {
		if (station.baggingArea.getCurrentWeight() != expectedWeight) { //if there is a weight discrepancy
			//a weight discrepancy is detected 
			WeightDiscrepancy detected = new WeightDiscrepancy(attendantApproved, station);
			detected.weightDiscrepancyOptions();
			}
		}
}

