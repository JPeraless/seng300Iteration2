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
import com.autovend.external.ProductDatabases;





/**
 * 
 * Class to implement the "Weight Discrepancy" use case
 * 
 * 
 * 
 * @author desmo
 *
 */
public class WeightDiscrepancyController implements ElectronicScaleObserver {
	
	// declare fields
	private SelfCheckoutStation station;
	private double expectedWeight;
	private CustomerIO customerStub;
	private AttendentStub attendantStub;
	private BarcodedUnit currentItem;
	private SelfCheckoutSystemLogic system;
	
	/**
	 * Constructor
	 * 
	 * 
	 * @param station - SelfCheckoutStation used by system
	 * @param item - item that has to be checked
	 * @param noBag - choice of customer to bag or not bag item
	 * @param attendantApproved - boolean condition of attendant approval
	 * @throws OverloadException - if current weight of scale is too high
	 */
	public WeightDiscrepancyController (SelfCheckoutStation station, SelfCheckoutSystemLogic system) {
		


		
		this.station = station;
		this.system = system;
		
		//disable the selfcheckout system since a weight discrepancy is detected
		station.handheldScanner.disable();
		station.mainScanner.disable();
		station.billInput.disable();
		
		
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
		this.system.checkDiscrepancy(weightInGrams);
		
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
