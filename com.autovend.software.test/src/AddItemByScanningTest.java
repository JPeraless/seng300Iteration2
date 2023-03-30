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
import com.autovend.external.*;
import com.autovend.products.BarcodedProduct;




import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.util.Currency;
import java.util.Locale;





import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;



public class AddItemByScanningTest extends BaseTestCase {
	AddItemByScanning useCase;

	
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
	 * 
	 * 
	 */
	@Test
	public void scanWhenDisabled() {
		this.useCase = new AddItemByScanning(super.station, unit0);
		this.station.mainScanner.disable();
		assertThrows(DisabledException.class, () -> this.useCase.add(super.station));
		
	}
	
	/**
	 * Test to ensure totalWeight variable is incremented properly 
	 * when items are added concurrently
	 * @throws Exception
	 */
	@Test
	public void totalWeightTest() throws Exception {
		
		//testing when 1 item has been added
		this.useCase = new AddItemByScanning(super.station, unit0);
		this.useCase.add(station);
		double expectedTotalWeight = unit0.getWeight();
		assertEquals(expectedTotalWeight, station.baggingArea.getCurrentWeight(), 0.00001);
		
		// testing when 2 items have been added
		this.useCase = new AddItemByScanning(super.station, unit1);
		useCase.add(station);
		expectedTotalWeight += unit1.getWeight();
		assertEquals(expectedTotalWeight, station.baggingArea.getCurrentWeight(), 0.00001);
		
	}
		
		
	
	
	/**
	 * function to check if product returned by add() is 
	 * correct
	 * @throws Exception
	 */
	@Test
	public void returnCorrectProductTest() throws Exception {
	    this.useCase = new AddItemByScanning(super.station, unit0);

	    BarcodedProduct expectedProduct = product0;
	    assertEquals(expectedProduct, this.useCase.add(station));

	
	}

	
	/**
	 * function to see if station is successfully reenabled after item has been added
	 * @throws Exception
	 */
	@Test
	public void reEnabledTest() throws Exception {
		this.useCase = new AddItemByScanning(super.station, unit1);
		
		this.useCase.add(station);
		
		assertFalse(this.useCase.station.mainScanner.isDisabled());
		assertFalse(this.useCase.station.handheldScanner.isDisabled());
		assertFalse(this.useCase.station.billInput.isDisabled());
	}
	
	

	

}
