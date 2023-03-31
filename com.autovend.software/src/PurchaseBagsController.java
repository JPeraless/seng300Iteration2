/*
 * Anna Lee: 30137463
 * Caleb Cavilla: 30145972
 * Desmond O'Brien: 30064340
 * Jose Perales: 30143354
 * Matthew Cusanelli: 30145324
 * Muhtadi Alam: 30150910
 * Omar Tejada: 31052626
 * Saadman Rahman: 30153482
 * Sahaj Malhotra: 30144405
 * Sean Tan: 30094560
 * Tanvir Ahamed Himel: 30148868
 * Victor campos: 30106934
 */
import java.math.BigDecimal;
import java.util.ArrayList;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.DisabledException;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;

/**
 * Class that implements the "Purchase Bags" use case
 * 
 * 
 * @author desmo
 *
 */
public class PurchaseBagsController {
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

private SelfCheckoutSystemLogic system;

	


	/**
	 * Constructor
	 * 
	 * @param station - station in use
	 * @param numOfBags - customer's desired number of purchased bags
	 * @throws OverloadException - in case of a baggiage area overload
	 */
	public PurchaseBagsController(SelfCheckoutStation station, SelfCheckoutSystemLogic system) {
		this.station = station;
		this.system = system;
		
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
	public void addBagsToBill() throws OverloadException, EmptyException {
		
		if (this.station.baggingArea.isDisabled()) {
			throw new DisabledException();
		}
		// instantiate an ArrayList of bags to be returned by method
		
		// dispense desired number of bags
		for (int i = 0; i < this.system.getCustomerIO().getNumberOfBagsPurchased(); ++i) {
			
			// signal to bag dispenser number of bags to dispense
			BarcodedUnit dispensedBag = this.system.getBagDispenser().dispenseBag();
			BarcodedProduct bagProduct = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(dispensedBag.getBarcode());
			
			// add price of bag to ammount due
			this.system.setAmountDue(this.system.getAmountDue()+ bagProduct.getPrice().doubleValue());
			
			// add item to bill
			this.system.addBillList(bagProduct);
			
			// add bag to weight, check weight discrepancy by registering weightDiscrepancy listener
			double expectedWeight = this.system.getBaggingAreaWeight() + bagProduct.getExpectedWeight();
			this.system.weightDiscrepency(expectedWeight);
			this.station.baggingArea.add(dispensedBag);
		}
	}



	


	




}
