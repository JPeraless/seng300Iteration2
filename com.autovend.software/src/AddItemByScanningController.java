/*
 * Anna Lee: 30137463
 * Caleb Cavilla: 30145972
 * Desmond O'Brien: 30064340
 * Jose Perales: 30143354
 * Matthew Cusanelli: 30145324
 * Muhtadi Alam: 30150910
 * Omar Tejada: 31052626
 * Saadman Rahman: 30153482
 * Sahaj Malhotra: 30144405
 * Sean Tan: 30094560
 * Tanvir Ahamed Himel: 30148868
 * Victor campos: 30106934
 */
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
	
	
	public BarcodedProduct add(SellableUnit currentSelectedUnit) throws Exception {
		

		
		if(station.mainScanner.scan((BarcodedUnit) currentSelectedUnit)) {
			BarcodedUnit copyOfUnit = (BarcodedUnit) currentSelectedUnit;
			BarcodedProduct barcodedProduct = getProductFromBarcode(this.system.getCurrentBarcode());
			//disable station
			addItemStationDisable();
			
			
			//get product/unit info
			double weightInGrams = copyOfUnit.getWeight();
			
			//notify customer
			System.out.println("Please place your item in the bagging area");
			
			this.system.weightDiscrepency(this.station.baggingArea.getCurrentWeight() + weightInGrams);
			//add item to the baggingArea ElectronicScale
			station.baggingArea.add(copyOfUnit);
			
		
			
			//increment weight total
			this.system.setBaggingAreaWeight(this.system.getBaggingAreaWeight() + weightInGrams);
			

			

			
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


