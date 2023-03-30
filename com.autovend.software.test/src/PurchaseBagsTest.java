import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;

public class PurchaseBagsTest extends BaseTestCase {
	private PurchaseBags useCase;


	@Before
	public void setUp() throws Exception {
		this.initializeStation();
		BarcodedProduct productBag = new BarcodedProduct(PurchaseBags.PURCHASEDBAGBARCODE, "Purchased bag", PurchaseBags.PURCHASED_BAG_PRICE, PurchaseBags.BAG_WEIGHT);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(PurchaseBags.PURCHASEDBAGBARCODE, productBag);
		
	}

	@After
	public void tearDown() throws Exception {
		this.deinitializeStation();
		ProductDatabases.BARCODED_PRODUCT_DATABASE.remove(PurchaseBags.PURCHASEDBAGBARCODE);
	}

	@Test
	public void purchaseWhileBaggingAreaDisabled() throws OverloadException {
		this.station.baggingArea.disable();
		this.useCase = new PurchaseBags(this.station, 10);
		assertThrows(DisabledException.class, () -> this.useCase.purchaseBags());
	}

	
	@Test
	public void notEnoughBags() throws Exception {
		this.useCase = new PurchaseBags(this.station, 101);
		assertThrows(SimulationException.class, () -> this.useCase.purchaseBags());
		
		tearDown();
		setUp();
		
		this.useCase = new PurchaseBags(this.station, 50);
		// this is good, still 50 bags available after this
		this.useCase.purchaseBags();
		// this is also good, but now 0 bags available after this
		this.useCase.purchaseBags();
		
		//this should throw a SimulationException, no more bags are available
		assertThrows(SimulationException.class, () -> this.useCase.purchaseBags());
	
	}
	
	@Test
	public void incorrectWeight() throws OverloadException {
		BarcodedProduct productBag = new BarcodedProduct(PurchaseBags.PURCHASEDBAGBARCODE, "Purchased bag", PurchaseBags.PURCHASED_BAG_PRICE, 100f);
		
		// simulate a change in expected weight of bag in the product database
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(PurchaseBags.PURCHASEDBAGBARCODE, productBag);
		
		// any calls to purchase a bag will now produce a weight discrepancy
		
		// a weight discrepency here should not cause any issue, if the attendant approves
		this.useCase = new PurchaseBags(this.station, 10);
		this.useCase.setDiscrepancyParameters(true, true);
		this.useCase.purchaseBags();
		
		
		// a weight discrepency here SHOULD cause any issue, the attendant does not approve
		this.useCase = new PurchaseBags(this.station, 10);
		this.useCase.setDiscrepancyParameters(true, false);
		assertThrows(SimulationException.class, () -> this.useCase.purchaseBags());	
	}
	
	@Test
	public void baggingAreaWeightUpdated() throws OverloadException {
		this.useCase = new PurchaseBags(this.station, 10);
		double weightBefore = this.station.baggingArea.getCurrentWeight();
		
		this.useCase.purchaseBags();
		
		double expected = weightBefore + (10 * PurchaseBags.BAG_WEIGHT);
		double actual = this.station.baggingArea.getCurrentWeight();
		
		assertEquals(expected, actual, 0.00001);
	}
	
}
