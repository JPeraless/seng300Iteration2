import java.util.ArrayList;
import java.util.Random;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.SellableUnit;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.DisabledException;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;

public class AddOwnBagsController {
	//TODO: I THINK THIS FIELD CAN BE CUT, SHOULD REALLY ONLY BE USED AS PARAMETER I THINK
	private boolean attendantApprovedBags;
	private int numberOfBagsOwned;
	
	// TODO: ITEMS CAN'T HAVE A NULL BARCODE, SO JUST MADE A CONSTANT BARCODE HERE FOR BAGS
	//		 THAT THE WHOLE SYSTEM CAN ACCESS
	public static final Barcode BAG_BARCODE = new Barcode(new Numeral[] {Numeral.nine, Numeral.nine, Numeral.nine, Numeral.nine});

	
	private SelfCheckoutStation station;
	private SelfCheckoutSystemLogic system;
		
	
	public AddOwnBagsController(SelfCheckoutStation station, SelfCheckoutSystemLogic system) {
		this.station = station;
		this.system = system;
		this.numberOfBagsOwned = this.system.getCustomerIO().getNumberOfPersonalBags();
	}
	
	
	/**
	 * @param station - SelfCheckoutStation in use
	 * @param bags - Total number of bags customer wants to add
	 * @return - Weight of bags
	 * @throws OverloadException - in case added weight of bags exceeds scale's limit
	 * 
	 */
	
	public void addOwnBags() throws OverloadException {
		// if disabled handle exception
		if (station.baggingArea.isDisabled()) {
			throw new DisabledException();
		}
		
		if (this.numberOfBagsOwned == 0) {
			return;
		}
		
		
		// get the attendant approval flag
		this.attendantApprovedBags = this.system.getAttendentStub().getDiscrepancyApproved();
		
		// assumed that customer was signaled that they want to add bags
		System.out.println("Please add your bags");
		
		// array to store bags in memory
		BarcodedUnit[] bags = new BarcodedUnit[this.numberOfBagsOwned];
		
		// add bags, increment running weight of bags
		double bagWeight = 0;
		for(int i = 0; i < this.numberOfBagsOwned; i ++) {
			bags[i] = getBag();
			
			bagWeight += bags[i].getWeight();
			
			this.system.weightDiscrepency(this.system.getBaggingAreaWeight() + bags[i].getWeight());
			station.baggingArea.add(bags[i]);
			
			// calling this is literally the only way to get an overload exception from
			// hardware unless we implement our own function. I think this works tho
			station.baggingArea.getCurrentWeight();
		}
		
		
		// Idk if this counts as signaling system.
		System.out.println("Weight has been changed, weight of bags is: " + bagWeight);
		stationDisable(station);
		
		// Signaling attendant to approve of bags
		System.out.println("Attendant called to approve of bags");
		
		// If attendant does not approve of bags
		if (!attendantApprovedBags) {
			this.system.setDiscrepancyActive(true);
			for (BarcodedUnit bag : bags) {
				station.baggingArea.remove(bag);
			}
			System.out.println("Bags removed\n");

		}
		else {
			// otherwise
			System.out.println("Bags approved");
		}
		stationEnable(station); // enables station if bags are approved
		System.out.println("Customer may continue with transaction");
		
	}
	
	/*
	 * Function to simulate a user adding bags to a scale
	 * 
	 * Weight of bags is a random weight in range (0, 1)
	 * 
	 */
	public BarcodedUnit getBag() {
		double weight = new Random().nextDouble(0.1, 1);
		return new BarcodedUnit(AddOwnBagsController.BAG_BARCODE, weight);
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
	

	
}
