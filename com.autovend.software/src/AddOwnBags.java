import java.util.Random;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;

public class AddOwnBags implements ElectronicScaleObserver {
	boolean attendantApprovedBags;
		
	/**
	 * @param station - SelfCheckoutStation in use
	 * @param bags - Total number of bags customer wants to add
	 * @return - Weight of bags
	 * @throws OverloadException - in case added weight of bags exceeds scale's limit
	 * 
	 */
	
	public void addOwnBags(SelfCheckoutStation station, int bags) throws OverloadException {
		
		this.attendantApprovedBags = attendantApprovedBags;
		
		// assumed that customer was signaled that they want to add bags
		System.out.println("Please add your bags");
	
		double bagWeight = 0;
		for(int i = 0; i < bags; i ++) {
			double weight = addBag(station.baggingArea);
			bagWeight = bagWeight + weight;
		}
		
		// Idk if this counts as signaling system.
		System.out.println("Weight has been changed, weight of bags is: " + bagWeight);
		stationDisable(station);
		
		// Signaling attendant to approve of bags
		System.out.println("Attendant called to approve of bags");
		
		// If attendant does not approve of bags
		if (!attendantApprovedBags) {
			throw new SimulationException("Bags not approved");
		}
		System.out.println("Bags approved");
		
		stationEnable(station); // enables station if bags are approved
		System.out.println("Customer may continue with transaction");
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
	 * disables parts of SelfCheckoutStation that customer can interact with while adding bags
	 * @param station - station to be disabled
	 */
	public static void stationDisable(SelfCheckoutStation station) {
		station.handheldScanner.disable();
		station.billInput.disable();
		station.mainScanner.disable();
	}
	
	/** 
	 * enables parts of SelfCheckoutStation that customer can interact with
	 * @param station - station to be enabled
	 */
	public static void stationEnable(SelfCheckoutStation station) {
		station.handheldScanner.enable();
		station.billInput.enable();
		station.mainScanner.enable();
	}
	

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToWeightChangedEvent(ElectronicScale scale, double weightInGrams) {
		try {
			System.out.println(String.format("New bag added\n Bag weight: %.2f\nCurrent total weight: %.2f\n", weightInGrams, scale.getCurrentWeight()));
		}
		catch(OverloadException e) {
			System.out.println(String.format("OVERLOAD ERROR REFER TO ERROR CODE BELOW:\n%s", e.getStackTrace().toString()));
		}
	}

	@Override
	public void reactToOverloadEvent(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reactToOutOfOverloadEvent(ElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}	
	
}
