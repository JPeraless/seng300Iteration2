import java.util.List;

import com.autovend.SellableUnit;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;

public class PurchaseBagsController {
	
	private SelfCheckoutStation scs;
	private CustomerIO customerIO;
	
	/**
	 * Controls the purchasing of bags for a self checkout station and its current customer.
	 * 
	 * @param scs: Self checkout station to add purchased bags to
	 * @param customerIO: Simulation of IO with customer at self checkout station
	 */
	public PurchaseBagsController(SelfCheckoutStation scs, CustomerIO customerIO) {
		this.scs = scs;
		this.customerIO = customerIO;
	}
	
	/**
	 * This method is called when CustomerIO has indicated that they would like to purchase bag(s)
	 * 
	 * @param bags: The barcoded product representation of the reusable bags
	 * @param bag: The sellable unit representation of the reusable bags
	 */
	public void purchaseBags(BarcodedProduct bags, SellableUnit bag) {
		//Assume that this method is called when customerIO has indicated that they would like to purchase bag(s)
		
		int numberOfBags = customerIO.getWantToPurchaseBags();
		
		//TODO: Add bags to the customer's bill
		
		//Signal to bag dispenser the number of bags to dispense
		
		for (int i=0; i < numberOfBags; i++) {
			scs.baggingArea.add(bag); //Increase the expected weight of the bagging area by the number of bags purchased
		}
		
		customerIO.addBagComplete();
		
	}

}
