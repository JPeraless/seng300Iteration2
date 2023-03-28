import com.autovend.Barcode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.products.BarcodedProduct;
import com.autovend.BarcodedUnit;


/**
 * Permits customer to scan a barcode of an item to add it to their bill for
purchase.
 */


public class AddItem extends VendingSystem implements BarcodeScannerObserver{
	private BarcodedUnit barcodedUnit;
	private BarcodedProduct barcodedProduct;


	public AddItem(BarcodedProduct currentBarcodedProduct,BarcodedUnit currentBarcodedUnit, SelfCheckoutStation station){
		super(station);
		this.barcodedProduct = currentBarcodedProduct;
		this.barcodedUnit = currentBarcodedUnit; 

	}
	
	public void AddItemByScanning(BarcodeScanner scanner) throws Exception{
		if(scanner.scan(barcodedUnit)) {
			
			addItemStationDisable();
		}
	}

	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	// Reacts to scanner and adds item which is detected
	public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) throws SimulationException{
		super.addBillList(barcodedProduct);
	}

}
