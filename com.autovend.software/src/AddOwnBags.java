import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ElectronicScaleObserver;




public class AddOwnBags implements ElectronicScaleObserver {
	
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
			double bagWeight = io.addBag(station.baggingArea);
			this.reactToWeightChangedEvent(station.baggingArea , bagWeight);
			done = io.doneAddingBags();
		}
		
		// return weight of bags once all bags have been added
		return station.baggingArea.getCurrentWeight() - initialWeight;
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
