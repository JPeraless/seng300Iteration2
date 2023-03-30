import java.math.BigDecimal;
import java.util.ArrayList;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;

/**
 * Class that implements the "Purchase Bags" use case
 * 
 * 
 * @author desmo
 *
 */
public class PurchaseBags {
// some public constants that define the properties of purchased bags, as well as the default bag dispenser capacity
public static final int DEFAULT_NUMBER_OF_BAGS = 100;
public static final double BAG_WEIGHT = 1f;
public static final Barcode PURCHASEDBAGBARCODE = new Barcode(new Numeral[] {Numeral.six,Numeral.nine});
public static final BigDecimal PURCHASED_BAG_PRICE = BigDecimal.valueOf(0.5);

// user's desired number of bags

private int desiredNumberOfBags;
// system's self checkout station

private SelfCheckoutStation station;
 
// rolling count of current bags available in dispenser
public int currentBagsAvailable;

// when adding the weight of bags, USE CASE DESCRIPTION EXCEPTIONS SHOW THAT 
// THE SYSTEM WILL HAVE TO CHECK A WEIGHT DISCREPENCY
private WeightDiscrepancy discrep;

	


	/**
	 * Constructor
	 * 
	 * @param station - station in use
	 * @param numOfBags - customer's desired number of purchased bags
	 * @throws OverloadException - in case of a baggiage area overload
	 */
	public PurchaseBags(SelfCheckoutStation station, int numOfBags) throws OverloadException {
		
		// instantiate fields
		this.station = station; 
		this.desiredNumberOfBags = numOfBags;
		this.currentBagsAvailable = DEFAULT_NUMBER_OF_BAGS;
		
		
		// instantiate WeightDiscrepancy field
		this.discrep = new WeightDiscrepancy(this.station, true, true);
	}
	
	/**
	 * Method that allows for the changing of WeightDiscrepency paramters in the absence
	 * of AttendantIO and CustomerIO implementations.
	 * 
	 * This method will be obsolete in future iterations once IO has been developed,
	 * until then this method will be useful for testing cases of different
	 * weight discrepancy approvals
	 * 
	 * @param noBag - customer does not want to bag the item (bag)
	 * @param attendantApproval - attendant does not approve the weight discrepancy that has been found
	 */
	public void setDiscrepancyParameters(boolean noBag, boolean attendantApproval) {
		this.discrep.setCustomerNoBag(noBag);
		this.discrep.setAttendantApproval(attendantApproval);
		
	}
	
	/**
	 * 	Method to fulfill "Purchase Bags" use case
	 * 
	 * @return - ArrayList of bags to add to bill. Calling code (System) can
	 * iterate over bags, adding each as an item to bill. Bags are added to the bagging
	 * area during this method call, so do not need to add bags again
	 * @throws OverloadException  - If weight is overloaded in weight discrepancy
	 * @throws SimulationException  - If attendand does not approve weight discrepancy
	 */
	public ArrayList<BarcodedUnit> purchaseBags() throws SimulationException, OverloadException, DisabledException {
		
		if (this.station.baggingArea.isDisabled()) {
			throw new DisabledException();
		}
		// instantiate an ArrayList of bags to be returned by method
		ArrayList<BarcodedUnit> bags = new ArrayList<>();
		
		// dispense desired number of bags
		for (int i = 0; i < this.desiredNumberOfBags; ++i) {
			
			// signal to bag dispenser number of bags to dispense
			BarcodedUnit dispensedBag = dispenseBag();
			
			// adjust bagging area's expected weight to include weight of purchased bags
			this.station.baggingArea.add(dispensedBag);
			
			// check for and handle weight discrepancy
			if (dispensedBag.getWeight() != ProductDatabases.BARCODED_PRODUCT_DATABASE.get(dispensedBag.getBarcode()).getExpectedWeight()) {
				this.discrep.weightDiscrepancyOptions();
			}
			
			// signals weight change TODO: Should this be done with an observer reaction?
		}
		// return arraylist of bags, calling code can then add these items to a bill
		return bags;
	}
	
	
	
	
	/**
	 * Stub for Bag Dispenser
	 * This method will be obsolete when Bag Dispenser is implemented in hardware
	 * 
	 * @return - If bags are still available in dispensers, return a new bage
	 * 			otherwise, throw a simulation exception
	 */
	private BarcodedUnit dispenseBag() throws SimulationException {
		// if bags are available, dispense a new bag
		if (this.currentBagsAvailable > 0) {
			BarcodedUnit bag = new BarcodedUnit(PURCHASEDBAGBARCODE, BAG_WEIGHT);
			// decrement number of available bags
			--this.currentBagsAvailable;
			// dispense bag
			return bag; 
		}
		// if no bags available, throw exception as per use case description
		throw new SimulationException("Bag Dispenser is out of bags");
	}
	




}
