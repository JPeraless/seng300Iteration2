import java.util.List;

import com.autovend.SellableUnit;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;

public class PurchaseBagsController {
	
	private SelfCheckoutStation scs;
	private CustomerIO customerIO;
	
	public PurchaseBagsController(SelfCheckoutStation scs, CustomerIO customerIO) {
		this.scs = scs;
		this.customerIO = customerIO;
	}
	
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
