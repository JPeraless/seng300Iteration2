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


/*
 * System
 * ....
 * 
 * 	if (weightDiscrepancy) {
 * 		
 * 		//customer wants to bad
 * 
 * 		boolean outcome = new WeightDiscrepancy(expectedWeight, bagTrue, approved).weightDiscrepancy();
 * 
 		if (outcome) {
 		disable
 		}
 		else {
 		
 		exceptions
 		}
 * 
 * }
 * 
 * 
 * 
 * 
 * ....
 * 
 * 
 * 
 * 
 * 
 */






public class WeightDiscrepancy {
	
	private static SelfCheckoutStation station;
	private double expectedWeight;
	private boolean customerNoBag;
	private boolean attendantApproved;
	
	
	public WeightDiscrepancy(SelfCheckoutStation station, boolean noBag, boolean attendantApproved) throws OverloadException {
		
		//this.customerNoBag = noBag;
		
		// CustomerIO and AttendantIO not implemented, 
		this.attendantApproved = attendantApproved;
		this.customerNoBag = noBag;
		this.station = station;
		this.expectedWeight = station.baggingArea.getCurrentWeight();
		
		//disable the selfcheckout system since a weight discrepancy is detected
		station.handheldScanner.disable();
		station.mainScanner.disable();
		station.billInput.disable();
		
	}
	
	
	void setCustomerNoBag(boolean choice) {
		this.customerNoBag = choice;
	}
	
	void setAttendantApproval(boolean choice) {
		this.attendantApproved = choice;
	}

	// return: true -> attendant is helping or approval was good, false -> weight change in response to customerIO
	
	/*
	 * TODO: 
	 * 
	 */
	public void weightDiscrepancyOptions() throws OverloadException, SimulationException {
		boolean discrepancyResolved = false;
		
		// case a: if the weight is back to expected weight 
		if (this.baggingAreaDiscrepancy(station.baggingArea)) {
			discrepancyResolved = true;
		}
		
		// case c: if attendant approves the discrepancy
		if (attendantApproved) {
			discrepancyResolved = true;
		}
		
		if (discrepancyResolved) {
			//enable the station when if the discrepancy is resolved
			station.handheldScanner.enable();
			station.mainScanner.enable();
			station.billInput.enable();
		}
		else {
			throw new SimulationException("Attendant assistance required, please hold...\n");
		}
		
	}
	
	
	
	
	// TODO make sure that each check is only carried out if signalled by system for options a, b, and c
	public boolean baggingAreaDiscrepancy(ElectronicScale baggingArea) throws OverloadException {
		return baggingArea.getCurrentWeight() == expectedWeight;
	}
	
	

	
	
		
	
	
	
	
	
	
}
