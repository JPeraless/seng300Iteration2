import java.math.BigDecimal;
import java.util.ArrayList;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;

public class AddItemByScanningController extends AddItem<BarcodedProduct> implements BarcodeScannerObserver {

	public AddItemByScanningController(SelfCheckoutStation station, SelfCheckoutSystemLogic system) {
		super(station, system);
		this.station.mainScanner.enable();
	
	}
	
	
	public BarcodedProduct add(SelfCheckoutStation station) throws Exception {
		BarcodedUnit copyOfUnit = (BarcodedUnit) this.unit;
		BarcodedProduct barcodedProduct = getProductFromBarcode(copyOfUnit.getBarcode());

		
		if(station.mainScanner.scan(copyOfUnit)) {
		
			//disable station
			addItemStationDisable();
			
			
			//get product/unit info
			double weightInGrams = copyOfUnit.getWeight();
			
			
			//notify customer
			System.out.println("Please place your item in the bagging area");
			
			//add item to the baggingArea ElectronicScale
			station.baggingArea.add(copyOfUnit);
			
		
			
			//increment weight total
			this.totalWeight += weightInGrams;
			
			//boolean discrepancyCheck = wd.weightDiscrepancyOptions();
			
			//if (!discrepancyCheck) {
			//	throw new SimulationException("User added or removed item in repsonse");
			//}
			
			
			
			//react to barcode scanned event (adds barcodedUnit to BillList)
			reactToBarcodeScannedEvent(station.mainScanner, copyOfUnit.getBarcode());
			
			addItemStationEnable();
			
			return barcodedProduct;
		}
		
		return null;
		
		
	
		
	}

	@Override
	public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
		this.system.setCurrentBarcode(barcode);
	}
	
	















}


