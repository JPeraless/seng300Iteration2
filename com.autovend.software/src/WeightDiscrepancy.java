import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;





/**
 * 
 * Class to implement the "Weight Discrepancy" use case
 * 
 * 
 * 
 * @author desmo
 *
 */
public class WeightDiscrepancy {
	
	// declare fields
	private SelfCheckoutStation station;
	private double expectedWeight;
	private boolean customerNoBag;
	private boolean attendantApproved;
	private BarcodedUnit currentItem;
	boolean thisIsABoolean;
	
	/**
	 * Constructor
	 * 
	 * 
	 * @param station - SelfCheckoutStation used by system
	 * @param item - item that has to be checked
	 * @param noBag - choice of customer to bag or not bag item
	 * @param attendantApproved - boolean condition of attendant approval
	 * @throws OverloadException - if current weight of scale is too high
	 */
	public WeightDiscrepancy(SelfCheckoutStation station, BarcodedUnit item, boolean noBag, boolean attendantApproved) throws OverloadException {
		
		// CustomerIO and AttendantIO not implemented, 
		this.attendantApproved = attendantApproved;
		this.customerNoBag = noBag;
		this.station = station;
		this.expectedWeight = station.baggingArea.getCurrentWeight();
		
		
		//disable the selfcheckout system since a weight discrepancy is detected
		station.handheldScanner.disable();
		station.mainScanner.disable();
		station.billInput.disable();
		
		this.currentItem = item;
	}
	
	
	/**
	 * Check for weight discrepancy, should be called whenever a BarcodedUnit is
	 * added to the scale
	 * 
	 * @throws SimulationException - In the case of an attendant not approving of a discrepancy
	 * @throws OverloadException - In the case that the max weight limit of the scale is surpassed
	 */
	public void checkDiscrepancy() throws SimulationException, OverloadException {
		// get expected weight from database
		double expectedWeight = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(this.currentItem.getBarcode()).getExpectedWeight();		
		
		// if no discrepancy is found, program can continue
		if (expectedWeight == currentItem.getWeight()) {
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
	 * Essentially a stub for getting the customer's decision
	 * on on bagging the item or not
	 * 
	 * This method will be deleted when CustomerIO is implemented,
	 * but for now it is effective especially for testing
	 * 
	 * @param choice - customer's decision to bag or not
	 */
	void setCustomerNoBag(boolean choice) {
		this.customerNoBag = choice;
	}
	
	
	
	/**
	 * Essentially a stub for getting the attendant's decision
	 * on on bagging the item or not
	 * 
	 * This method will be deleted when AttendantIO is implemented,
	 * but for now it is effective especially for testing
	 * 
	 * @param choice - attendant's decision to approve or disapprove discrepancy
	 */
	void setAttendantApproval(boolean choice) {
		this.attendantApproved = choice;
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
	private boolean weightDiscrepancyOptions() throws OverloadException, SimulationException {
		
		// customer goofed and added an item / and or took it off after being prompted of a weight change
		// attendant needs to come fix this
		if (this.baggingAreaDiscrepancy(station.baggingArea)) {
			throw new SimulationException("Additional item removed/added, assistance required, please hold\n...");
		}
		
		// attendant is not approving of discrepancy, will have to go take a look manually
		else if (!attendantApproved) {
			throw new SimulationException("Attendant assistance required, please hold...\n");
		}
		
		// this case will have to react with the "Do Not Bag Item" use case in the future,
		// so let's leave this here for now but just pretend it is resolved by returning true
		if (this.customerNoBag) {
			// THE DO NOT PLACE ITEM IN BAGGING AREA SHOULD BE IMPLEMENTED HERE
			// FOR NOW WE WILL ASSUME THAT SUCH A CASE IS HANDLED AS EXPECTED
			// WILL HAVE TO CHANGE FOR FUTURE ITERATION
			// So lets leave this if statement for future implementation
			station.handheldScanner.enable();
			station.mainScanner.enable();
			station.billInput.enable();
			return true;
		}
		
		// otherwise if the attendant approved of the weight discrepancy, let's enable the station
		// and get ready for another transaction
		station.handheldScanner.enable();
		station.mainScanner.enable();
		station.billInput.enable();
		return true;
	}
	
	
	
	
	
	// TODO make sure that each check is only carried out if signalled by system for options a, b, and c
	public boolean baggingAreaDiscrepancy(ElectronicScale baggingArea) throws OverloadException {
		return baggingArea.getCurrentWeight() == expectedWeight;
	}
	
	

	
	
		
	
	
	
	
	
	
}
