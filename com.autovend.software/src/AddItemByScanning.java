import java.math.BigDecimal;
import java.util.ArrayList;

import com.autovend.BarcodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.ElectronicScale;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.Product;

public class AddItemByScanning extends AddItem<BarcodedProduct> {

	public AddItemByScanning(SelfCheckoutStation station, SellableUnit currentUnit) {
		super(station, currentUnit);
		this.station.mainScanner.enable();
	
	}
	
	
	public BarcodedProduct add(SelfCheckoutStation station) throws Exception {
		BarcodedUnit copyOfUnit = (BarcodedUnit) this.unit;
		BarcodedProduct barcodedProduct = getProductFromBarcode(copyOfUnit.getBarcode());
		
		

		
		if(station.mainScanner.scan(copyOfUnit)) {
			//WeightDiscrepancy wd = new WeightDiscrepancy(station);
			
			//disable station
			addItemStationDisable();
			
			
			//get product/unit info
			double weightInGrams = copyOfUnit.getWeight();
			
			
			//notify customer
			System.out.println("Please place your item in the bagging area");
			
			//add item to the baggingArea ElectronicScale
			station.baggingArea.add(copyOfUnit);
			
			//this.products.add(barcodedProduct);
			
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

	















}


