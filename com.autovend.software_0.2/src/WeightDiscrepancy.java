import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;



/*
 * TODO 1. map out three options, 2.
 */


public class WeightDiscrepancy {
	
	static SelfCheckoutStation station;
	double expectedWeight;
	
	
	public WeightDiscrepancy(double expectedWeight) {
		
		this.expectedWeight = expectedWeight;
		
		
	}
	


	public void weightDiscrepancy() throws OverloadException{
		boolean itemBagged = WeightDiscrepancy.doNotBagRequest(station, null, null);
		if (!itemBagged) {
			throw new SimulationException("FIX THIS");
		}
		
	}
	
	
	

	
	
	//Option a
	
	
	// TODO make sure that each check is only carried out if signalled by system for options a, b, and c
	public boolean baggingAreaDiscrepancy(ElectronicScale baggingArea) throws OverloadException {
		return baggingArea.getCurrentWeight() != expectedWeight;
		
		
		
	}
	
	
	
	
	// stub for user decision
	private static boolean bagOrNot(boolean check) {
		return check;
	}
	
	
	// stub for attendant weight oevrride approval
	private static boolean attendantApproval(boolean check) {
		return check;
	}
	
	
	
	//Option b
	
	// return whether or not item has NOT been placed in bagging area
	public static boolean doNotBagRequest(SelfCheckoutStation station, BarcodedUnit item, System customer) throws OverloadException {
		double areaWeight = station.baggingArea.getCurrentWeight();
		
		//customer IO signal
		boolean customerDecision = /*System.*/bagOrNot(true);
		// if dont want to bag
		if (customerDecision) {
			
			// get attendant approval
			if (WeightDiscrepancy.attendantApproval(true)) {
				areaWeight -= item.getWeight();
				return true;
			}
		
		}
			
		// else if the customer tries bagging the item anyway
		// LOCK SYSTEM AND WAIT FOR ATTENDANT APPROVAL?
		station.handheldScanner.disable();
		station.mainScanner.disable();
		station.billInput.disable();
		station.baggingArea.disable();
		station.scale.disable();
		return false;
			

		
		// if they do want to bag it
		// either wait until they 
		
		
	}
	
	//Option c
	
	public static void attendantDiscApproval() {
		
		
		
	}
	
	
	
		
	
	
	
	
	
	
}
