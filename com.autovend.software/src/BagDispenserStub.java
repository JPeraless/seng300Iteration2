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

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.EmptyException;
import com.autovend.devices.SimulationException;

public class BagDispenserStub {
	public static final double BAG_WEIGHT = 1f;
	public static final Barcode PURCHASEDBAGBARCODE = new Barcode(new Numeral[] {Numeral.six,Numeral.nine});
	public static final BigDecimal PURCHASED_BAG_PRICE = BigDecimal.valueOf(0.5);
	
	private int maxNumberOfBags;
	private int currentBagsAvailable;
	
	public BagDispenserStub(int maxNumberOfBags) {
		this.maxNumberOfBags = maxNumberOfBags;
		this.currentBagsAvailable = maxNumberOfBags;
	}
	
	
	/**
	 * Stub for Bag Dispenser
	 * This method will be obsolete when Bag Dispenser is implemented in hardware
	 * but it is useful to simulate the rest of the use case implementation
	 * as of this iteration
	 * 
	 * @return - If bags are still available in dispensers, return a new bage
	 * 			otherwise, throw a simulation exception
	 * @throws EmptyException 
	 */
	public BarcodedUnit dispenseBag() throws SimulationException, EmptyException {
		// if bags are available, dispense a new bag
		if (this.currentBagsAvailable > 0) {
			BarcodedUnit bag = new BarcodedUnit(PURCHASEDBAGBARCODE, BAG_WEIGHT);
			// decrement number of available bags
			--this.currentBagsAvailable;
			// dispense bag
			return bag; 
		}
		// if no bags available, throw exception as per use case description
		throw new EmptyException("Bag Dispenser is out of bags");
	}



	public int getMaxNumberOfBags() {
		return this.maxNumberOfBags;
	}
	
	public int getCurrentBagsAvailable() {
		return this.currentBagsAvailable;
	}
}
