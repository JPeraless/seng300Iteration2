
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.autovend.BarcodedUnit;
import com.autovend.Bill;
import com.autovend.devices.BillDispenser;
import com.autovend.devices.CoinDispenser;
import com.autovend.devices.DisabledException;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;


public class SelfCheckoutSystemLogic {

	private SelfCheckoutStation station;
	private List<BarcodedProduct> billList = new ArrayList<BarcodedProduct>();
	private double changeDispensed = 0;
	private double amountDue = 0;
	private boolean paymentProcess = false;
	
	private double baggingAreaWeight;
	private double expectedBagginAreaWeight;

	private boolean printing;
	
	private boolean unhandledDiscrepancy;
	private boolean unApprovedDiscrepancy;
	private boolean discrepancyActive = false;
	private boolean doNotBagItemActive = false;
	
	private boolean isMember = false;
	private String memberNumber;
	
	private PrintReceipt receiptController;
	private CustomerIO customerio;
	private AttendentStub attendent;
	private BagDispenserStub bagDispenser;
	private final int BAG_DISPENSER_DEFAULT_LIMIT = 100;

	
	public SelfCheckoutSystemLogic(SelfCheckoutStation station, CustomerIO cs, AttendentStub att) {
		this.station = station;
		this.station.handheldScanner.disable();
		this.station.mainScanner.disable();
		this.station.billInput.disable();
		
		customerio = cs;
		attendent = att;
		
		//receiptController = new PrintReceipt(station);
		//receiptController.registerCustomerIO(customerio);
		//receiptController.registerAttendent(attendent);
		
		station.printer.register(receiptController);
	}

	
	public SelfCheckoutSystemLogic(SelfCheckoutStation station) {
		this.station = station;
		this.station.handheldScanner.disable();
		this.station.mainScanner.disable();
		this.station.billInput.disable();
		this.station.coinSlot.disable();
		
		this.customerio = new CustomerIO();
		this.attendent = new AttendentStub();
		this.bagDispenser = new BagDispenserStub(BAG_DISPENSER_DEFAULT_LIMIT);
	}
	
	public void setMemberNumber(String number) {
		memberNumber = number;
	}
	
	public void memberStatus(boolean ism) {
		isMember = ism;
	}



	// sets the system to be ready to take payment, simulates customer indicating they want to pay cash for their bill
	/*
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
			if (amountDue <= 0) {
				paymentProcess = false; // exits the system out of payment
				station.billInput.disable();
				controller.deliverChange(amountDue);
				printing = true; // Set boolean to signal receipt printer to print
			}
		}
		*/

	/**
	 * Prints the receipt and notifies attendant of problems
	 * @throws EmptyException 
	 * @throws OverloadException 
	 */
	//public boolean startPrinting() throws EmptyException, OverloadException{
		//return receiptController.print(billList);
//	}
	
	/*
	public boolean takeMembership(String number) {
		if (customerio.enterMembership(number)) {
			memberNumber = number;
			return true;
		} else {
			return false;
		}
	}
	*/
	
	
	public void purchaseBags() throws OverloadException {
		PurchaseBagsController pb = new PurchaseBagsController(this.station, this);
		this.baggingAreaWeight = this.station.baggingArea.getCurrentWeight();
		pb.addBagsToBill();
	}
	
	
	
	
	public void weightDiscrepency(double expectedWeight) throws OverloadException {
		// weithDiscrep = true
		WeightDiscrepancyController wd = new WeightDiscrepancyController(this.station, this);
		this.station.baggingArea.register(wd);
		this.expectedBagginAreaWeight = expectedWeight;
	}
	
	
	public void checkDiscrepancy(double actualWeight) {
	
		
		// if no discrepancy is found, program can continue
		if (actualWeight == this.expectedBagginAreaWeight) {
			station.handheldScanner.enable();
			station.mainScanner.enable();
			station.billInput.enable();
			return;
		}
		
		// otherwise discrepancy found, disable station
		station.handheldScanner.disable();
		station.mainScanner.disable();
		station.billInput.disable();
		
		this.weightDiscrepancyOptions();
	}
	
	
	
	/**
	 * Method to determine and decide how to handle weight discrepancy
	 * based on attendant and customer decisions
	 * 
	 * 
	 * @return - true if no exception was found
	 * @throws OverloadException - if weight limit in bagging area is exceeded
	 * @throws SimulationException - if attendant does not approve of discrepancy
	 */
	private void weightDiscrepancyOptions() {

		
		// attendant is not approving of discrepancy, will have to go take a look manually
		if (!this.attendent.getDiscrepancyApproved()) {
			this.unApprovedDiscrepancy = true;
			this.discrepancyActive = true;
			return;
		}
		
		// this case will have to react with the "Do Not Bag Item" use case in the future,
		// so let's leave this here for now but just pretend it is resolved by returning true
		if (this.customerio.getCustomerNoBag()) {

			this.doNotBagItemActive = true;
			this.discrepancyActive = true;
			return;
		}
		
		// otherwise if the attendant approved of the weight discrepancy, let's enable the station
		// and get ready for another transaction
		station.handheldScanner.enable();
		station.mainScanner.enable();
		station.billInput.enable();
		this.discrepancyActive = false;
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
	
	public void setCustomerNoBag(boolean value) {
		this.customerio.setCustomerNoBag(value);
	}
	
	public void setAttendantApproval(boolean value) {
		this.attendent.setDiscrepancyApproved(value);
	}
	
	
	public boolean getUnhandledDiscrepancy() {
		return unhandledDiscrepancy;
	}
	
	public boolean getUnApprovedDiscrepancy() {
		return unApprovedDiscrepancy;
	}
	
	public boolean getDiscrepancyActive() {
		return discrepancyActive;
	}
	
	public boolean getDoNotBagItemActive() {
		return this.doNotBagItemActive;
	}
	

	public CustomerIO getCustomerIO() {
		return this.customerio;
	}
	
	public AttendentStub getAttendentStub() {
		return this.attendent;
	}
	
	public BagDispenserStub getBagDispenser() {
		return this.bagDispenser;
	}
	
	public double getBaggingAreaWeight() {
		return this.baggingAreaWeight;
	}
	
}



