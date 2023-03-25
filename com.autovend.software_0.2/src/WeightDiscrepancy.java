import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;



/*
 * 
 * System
 * 
 * 
 * \add item here/
 * 
 * if (weightDiscrepancyFound() {
 * 	boolean customerChoice = CustomerIO.bagOrNot();
 * 	boolean attendantApproval = AttendantIO.approveDiscrepancy();
 * 
 * 	WeightDiscrepancy discrep = new WeightDiscrepancy(this.baggingArea.getCurrentWeight(), customerChoice, attendantApproval);
 * 	discrep.weightDiscrepancy(this.station);
 * }
 * 
 * 
 * 
 * 
 */


public class WeightDiscrepancy {
	
	static SelfCheckoutStation station;
	double expectedWeight;
	boolean customerNoBag;
	boolean attendantApproved;
	
	
	public WeightDiscrepancy(double expectedWeight, boolean noBag, boolean attendantApproved) {
		
		this.expectedWeight = expectedWeight;
		this.customerNoBag = noBag;
		this.attendantApproved = attendantApproved;
		
	}
	

	// return: true -> attendant is helping or approval was good, false -> weight change in response to customerIO
	
	public boolean weightDiscrepancy(SelfCheckoutStation staion) throws OverloadException {
		// case a
		if (this.baggingAreaDiscrepancy(station.baggingArea)) {
			return false;
		}
		
		// case c
		if (!attendantApproved) {
			//notify attendant, they have to investigate
			throw new SimulationException("Weight override not approved, please wait for attendant assistance...\n");
		}
		
		// case b
		else if (customerNoBag && attendantApproved) {
			return true;
		}
		
		
		// system requirements unclear about this case.
		else {
			return false;
		}
		
		
		
	}
	
	
	
	
	// TODO make sure that each check is only carried out if signalled by system for options a, b, and c
	public boolean baggingAreaDiscrepancy(ElectronicScale baggingArea) throws OverloadException {
		return baggingArea.getCurrentWeight() != expectedWeight;
	}
	
	

	
	
		
	
	
	
	
	
	
}
