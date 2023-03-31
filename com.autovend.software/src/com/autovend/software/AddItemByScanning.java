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
package com.autovend.software;
import com.autovend.Barcode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.products.BarcodedProduct;


/**
 * Permits customer to scan a barcode of an item to add it to their bill for
purchase.
 */


public class AddItemByScanning extends SelfCheckoutSystemLogic implements BarcodeScannerObserver{

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
	public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) throws SimulationException{
		super.addBillList(barcodedProduct);
	}

}
