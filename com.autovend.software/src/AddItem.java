import java.math.BigDecimal;

import com.autovend.Barcode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;
import com.autovend.BarcodedUnit;
import com.autovend.SellableUnit;


/**
 * Permits customer to scan a barcode of an item to add it to their bill for
purchase.
 */

//TODO: change constructor implementation, instead use barcode to create unit/product from database and pass into functions 


public class AddItem extends VendingSystem implements BarcodeScannerObserver{
	//private BarcodedUnit barcodedUnit;
	//private BarcodedProduct barcodedProduct;
	double totalWeight = 0;
	BigDecimal totalPrice = BigDecimal.ZERO;
	private SellableUnit unit;
	

	/**
	 * 
	 * 
	 * 
	 * @param staion - the station being used by the system
	 * @param currentUnit - Unit to be added (PLU or barcoded)
	 */
	public AddItem(SelfCheckoutStation station, SellableUnit currentUnit){
		super(station);

		this.unit = currentUnit; 

	}
		
	/*
	 *  DON'T THINK THIS METHOD IS NECESSARY, UNIT IS ALREADY PASSED IN AS 
	 * 
	 */
	/*
	public BarcodedUnit getUnitFromBarcode (Barcode barcode) {
		for(Barcode barcode1: ProductDatabases.BARCODED_PRODUCT_DATABASE.keySet()) {
			if(barcode == barcode1) {
				BarcodedUnit item = new BarcodedUnit(barcode1, ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode1).getExpectedWeight());
				return item;
			}
			
		}
		return null;
	}
	*/
	
	public BarcodedProduct getProductFromBarcode(Barcode barcode) {
		return ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
	}
	
	
	
	public void AddItemByScanning(SelfCheckoutStation station) throws Exception {
		BarcodedUnit copyOfUnit = (BarcodedUnit) this.unit;
		BarcodedProduct barcodedProduct = getProductFromBarcode(copyOfUnit.getBarcode());
		addItemStationEnable();
		if(station.mainScanner.scan(copyOfUnit)) {
			
			//disable station
			addItemStationDisable();
			
			//get product/unit info
			double weightInGrams = copyOfUnit.getWeight();
			BigDecimal price = barcodedProduct.getPrice();
			
			//notify customer
			System.out.println("Please place your item in the bagging area");
			
			//add item to the baggingArea ElectronicScale
			station.baggingArea.add(copyOfUnit);
			
			//increment weight total
			totalWeight += weightInGrams;
			
			//check for weight discrepancy using totalWeight
			checkForWeightDiscrepancy(totalWeight);
			
			//increment price total
			totalPrice.add(price);
			
			//react to barcode scanned event (adds barcodedUnit to BillList)
			reactToBarcodeScannedEvent(station.mainScanner, copyOfUnit.getBarcode());
			
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
		super.addBillList(getProductFromBarcode(barcode));
	}

}
