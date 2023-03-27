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

}
