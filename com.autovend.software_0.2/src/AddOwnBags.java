import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;




public class AddOwnBags {

	/**
	 * Method implementing Add Own Bags use case
	 * 
	 * Returns weight of bags, once they've been added, to calling code (System)
	 * 
	 * @param station - SelfCheckoutStation in use
	 * @param io	- Customer IO interface
	 * @return - 	Weight of bags
	 * @throws OverloadException - in case added weight of bags exceeds scale's limit
	 */
	public double addOwnBags(SelfCheckoutStation station, CustomerIO io) throws OverloadException {
		// assumed that customer has signalled that they want to add bags
		// assumaed that system has signalled that customer should add own bags
		
		
		// record scale's weight before bags were added
		double initialWeight = station.baggingArea.getCurrentWeight();
		
		
		// allow customer to add as many bags as they would like
		// TODO: Deal with exception here? Custom exception message? idk
		boolean done = false;
		while (!done) {
			io.addBag(station.baggingArea);
			done = io.doneAddingBags();
		}
		
		// return weight of bags once all bags have been added
		return station.baggingArea.getCurrentWeight() - initialWeight;
	}
	
	
	
	
	
}
