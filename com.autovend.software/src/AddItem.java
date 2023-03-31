import java.math.BigDecimal;
import java.util.ArrayList;

import com.autovend.Barcode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.BarcodedUnit;
import com.autovend.PriceLookUpCode;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.SellableUnit;


/**
 * Permits customer to scan a barcode of an item to add it to their bill for
purchase.
 */

//TODO: change constructor implementation, instead use barcode to create unit/product from database and pass into functions 


public class AddItem<T extends Product> implements BarcodeScannerObserver{
	//private BarcodedUnit barcodedUnit;
	//private BarcodedProduct barcodedProduct;
	protected double totalWeight = 0;
	protected BigDecimal totalPrice = BigDecimal.ZERO;
	//protected ArrayList<T> products;
	protected SellableUnit unit;
	protected SelfCheckoutStation station;
	protected SelfCheckoutSystemLogic system;
 
	
	
	
	/**
	 * 
	 * 
	 * 
	 * @param staion - the station being used by the system
	 * @param currentUnit - Unit to be added (PLU or barcoded)
	 */
	public AddItem(SelfCheckoutStation station, SelfCheckoutSystemLogic system){
		
		this.station = station;
		this.system = system;

	}

	public BarcodedProduct getProductFromBarcode(Barcode barcode) {
		return ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
	}
	

	public PLUCodedProduct getProductFromPLU(PriceLookUpCode plu) {
		return ProductDatabases.PLU_PRODUCT_DATABASE.get(plu);
	}

	protected void addItemStationDisable() {
		station.mainScanner.disable();
		station.handheldScanner.disable();
		station.billInput.disable();
	}
	
	protected void addItemStationEnable() {
		station.mainScanner.enable();
		station.handheldScanner.enable();
		station.billInput.enable();
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

	}

}
