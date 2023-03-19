/*
 * Members for Iteration 1:
 * Michelle Loi (30019557)
 * James Hayward (30149513)
 * Caleb Cavilla (30145972)
 * Amandeep Kaur (30153923)
 * Ethan Oke (30142180)
 * Winjoy Tiop (30069663)
 */

package com.autovend.software;

import java.math.BigDecimal;

import com.autovend.Barcode;
import com.autovend.SellableUnit;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;


/**
 * Permits customer to scan a barcode of an item to add it to their bill for
purchase.
 */


public class AddItemByScanning extends System implements BarcodeScannerObserver{
	
	private BarcodedProduct barcodedProduct; 
	
	
	public AddItemByScanning(BarcodedProduct currentBarcodedProduct, SelfCheckoutStation station){
		super(station);
		this.barcodedProduct = currentBarcodedProduct;
		
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
		public void addItem(Barcode barcode, SellableUnit unit, SelfCheckoutStation station) throws SimulationException, OverloadException{
			super.addBillList(barcodedProduct);	
			
			//Getting the current weight at station
			double currentWeight = station.scale.getCurrentWeight();
			
			//Getting the expected weight of product
			double expectedProductWeight = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode).getExpectedWeight();
			
			//Determining if the expected weight is equal to the current weight
			if (expectedProductWeight != currentWeight) {
				throw new SimulationException("Weight discrepancy with item's actual weight and expected weight!");
			}
			
			try {
				station.baggingArea.add(unit);
			} catch (Exception e) {
				throw new SimulationException(e);
			}
			
			//determining the price of the product
			BigDecimal expectedPrice = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode).getPrice();
			//Setting the price of the product to a double
			double expectedPriceDouble = expectedPrice.doubleValue();
			super.setAmountDue(expectedPriceDouble);
			
		}

		@Override
		public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
			// TODO Auto-generated method stub
			
		}
	
}
