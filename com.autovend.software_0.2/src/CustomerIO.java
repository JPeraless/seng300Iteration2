import com.autovend.BarcodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.ElectronicScale;

import java.util.Random;

/**
 * 
 * Pretty much just a dummy class for stubbing
 * @author desmo
 *
 */
public class CustomerIO {
	// boolean indicating when user is done adding bags
	private boolean allBagsAdded;
	
	//number of bags the customer has indicated they want to purchase
	private int wantToPurchasBags;
	
	// getter for abvoe variable, probably redundant? 
	public boolean doneAddingBags() {
		return this.allBagsAdded;
	}
	
	
	
	/*
	 * Function to simulate a user adding bags to a scale
	 * 
	 * Weight of bags is a random weight in range (0, 1)
	 * 
	 */
	public double addBag(ElectronicScale baggingArea) {
		double weight = new Random().nextDouble();
		SellableUnit bag = new BarcodedUnit(null, weight);
		baggingArea.add(bag);
		return weight;
	}
	
	/**
	 * Function that is called when the customer indicates that they want to purchase bags
	 * 
	 * @param numberOfBagsToPurchase: number of bags the customer wants to purchase
	 */
	public void wantToPurchaseBags(int numberOfBagsToPurchase) {
		this.wantToPurchasBags = numberOfBagsToPurchase;
	}
	
	/**
	 * Getter for the number of bags customer wants to purchase
	 * 
	 * @return: returns the number of bags the customer wants to purchase
	 */
	public int getWantToPurchaseBags() {
		return wantToPurchasBags;
	}
	
	/**
	 * Sets the number of bags customer wants to purchase to 0 once the bag purchase is complete
	 */
	public void addBagComplete() {
		this.wantToPurchasBags = 0;
	}

}
