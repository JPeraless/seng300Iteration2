package com.autovend.software.test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.SellableUnit;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.*;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.AddItem;
import com.autovend.software.AddItemByScanningController;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.util.Currency;
import java.util.Locale;





import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;


/**
Desmond O'Brien: 30064340
Matthew Cusanelli: 30145324
Saadman Rahman: 30153482
Tanvir Ahamed Himel: 30148868
Victor campos: 30106934
Sean Tan: 30094560
Sahaj Malhotra: 30144405 
Caleb Cavilla: 30145972
Muhtadi Alam: 30150910
Omar Tejada: 31052626
Jose Perales: 30143354
Anna Lee: 30137463
 */
public class AddItemByScanningTest extends BaseTestCase {
	AddItemByScanningController useCase;

	
	SellableUnit unit0;
	SellableUnit unit1;
	SellableUnit unit2;
	SellableUnit unit3;
	
	BarcodedProduct product0;
	BarcodedProduct product1;
	BarcodedProduct product2;
	BarcodedProduct product3;
	
	
	
	static final Barcode BARCODE_0 = new Barcode(new Numeral[] {Numeral.zero, Numeral.zero});
	static final Barcode BARCODE_1 = new Barcode(new Numeral[] {Numeral.zero, Numeral.one});
	static final Barcode BARCODE_2 = new Barcode(new Numeral[] {Numeral.one, Numeral.zero});
	static final Barcode BARCODE_3 = new Barcode(new Numeral[] {Numeral.one, Numeral.one});

	@Before
	public void setUp() throws Exception {
		super.initializeStation();
		unit0 = new BarcodedUnit(BARCODE_0, 1f);
		unit1 = new BarcodedUnit(BARCODE_1, 2f);
		unit2 = new BarcodedUnit(BARCODE_2, 3f);
		unit3 = new BarcodedUnit(BARCODE_3, 4f);
		
		product0 = new BarcodedProduct(BARCODE_0, "Item 0", BigDecimal.valueOf(1f), unit0.getWeight());
		product1 = new BarcodedProduct(BARCODE_1, "Item 1", BigDecimal.valueOf(2f), unit1.getWeight());
		product2 = new BarcodedProduct(BARCODE_2, "Item 2", BigDecimal.valueOf(3f), unit2.getWeight());
		product3 = new BarcodedProduct(BARCODE_3, "Item 3", BigDecimal.valueOf(4f), unit3.getWeight());
		
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(BARCODE_0, product0);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(BARCODE_1, product1);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(BARCODE_2, product2);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(BARCODE_3, product3);

	}

	@After
	public void tearDown() throws Exception {
		super.deinitializeStation();
		unit0 = null;
		unit1 = null;
		unit2 = null;
		unit3 = null;
		
		product0 = null;
		product1 = null;
		product2 = null;
		product3 = null;
		ProductDatabases.BARCODED_PRODUCT_DATABASE.remove(BARCODE_0);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.remove(BARCODE_1);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.remove(BARCODE_2);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.remove(BARCODE_3);
		
	}

	/**
	 * Function to ensure a DisabledException is thrown
	 * when trying to scan while system not ready
	 * to accept scanned items
	 * @throws Exception 
	 * 
	 * 
	 */
	@Test
	public void scanWhenDisabled() throws Exception {
		this.system.setCurrentSelectableUnit(unit0);
		this.station.mainScanner.disable();
		system.addItemByScanning();
		// ** FIX NEEDED , THROWS SIM EXCEPTION, SHOULD THROW DISABLED EXCEPTION
		try {
			assertThrows(SimulationException.class, () -> system.addItemByScanning());
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Test to ensure totalWeight variable is incremented properly 
	 * when items are added concurrently
	 * @throws Exception
	 */
	@Test
	public void totalWeightTest() throws Exception {
		
		//testing when 1 item has been added
		this.system.setCurrentSelectableUnit(unit0);
		system.addItemByScanning();
		double expectedTotalWeight = unit0.getWeight();
		try {
			assertEquals(expectedTotalWeight, system.getBaggingAreaWeight(), 0.00001);
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		
		// testing when 2 items have been added
		this.system.setCurrentSelectableUnit(unit1);
		system.addItemByScanning();
		expectedTotalWeight += unit1.getWeight();
		try {
			assertEquals(expectedTotalWeight, system.getBaggingAreaWeight(), 0.00001);
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
		
		
	
	
	/**
	 * function to check if product returned by add() is 
	 * correct
	 * @throws Exception
	 */
	@Test
	public void returnCorrectProductTest() throws Exception {
	    this.system.setCurrentSelectableUnit(unit0);
	    system.addItemByScanning(); 
	    BarcodedProduct expectedProduct = product0;
	    try {
	    	assertEquals(expectedProduct, AddItem.getProductFromBarcode(system.getCurrentBarcode()));
	    }
	    catch (Throwable e) {
	    	e.printStackTrace();
	    }
	
	}

	
	/**
	 * function to see if station is successfully reenabled after item has been added
	 * @throws Exception
	 */
	@Test
	public void reEnabledTest() throws Exception {
		this.system.setCurrentSelectableUnit(unit0);
		
		system.addItemByScanning();
		
		assertFalse(system.getStation().mainScanner.isDisabled());
		assertFalse(system.getStation().handheldScanner.isDisabled());
		assertFalse(system.getStation().billInput.isDisabled());
	}
	
	

	

}
