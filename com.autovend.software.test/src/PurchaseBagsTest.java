import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.DisabledException;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;

public class PurchaseBagsTest extends BaseTestCase {
	private PurchaseBagsController useCase;

	@Before
	public void setUp() throws Exception {
		this.initializeStation();
		BarcodedProduct productBag = new BarcodedProduct(PurchaseBagsController.PURCHASEDBAGBARCODE, "Purchased bag", 
				PurchaseBagsController.PURCHASED_BAG_PRICE, PurchaseBagsController.BAG_WEIGHT);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(PurchaseBagsController.PURCHASEDBAGBARCODE, productBag);
		
	}

	@After
	public void tearDown() throws Exception {
		this.deinitializeStation();
		ProductDatabases.BARCODED_PRODUCT_DATABASE.remove(PurchaseBagsController.PURCHASEDBAGBARCODE);
	}
	
	@Test
	public void purchaseWhileBaggingAreaDisabled() throws OverloadException {
		this.station.baggingArea.disable();
		this.system.getCustomerIO().setNumberOfBagsPurchased(10);
		assertThrows(DisabledException.class, () -> this.system.purchaseBags());
	}
	
	
	@Test
	public void notEnoughBags() throws Exception {
		// default limit in this implementation is 100
		this.system.getCustomerIO().setNumberOfBagsPurchased(101);
		assertThrows(EmptyException.class, () -> this.system.purchaseBags());
		
		tearDown();
		setUp();
		
		this.system.getCustomerIO().setNumberOfBagsPurchased(50);
		// this is good, still 50 bags available after this
		this.system.purchaseBags();
		// this is also good, but now 0 bags available after this
		this.system.purchaseBags();
		
		//this should throw a SimulationException, no more bags are available
		assertThrows(EmptyException.class, () -> this.system.purchaseBags());
	
	}
	
	@Test
	public void incorrectWeight() throws OverloadException, EmptyException {
		BarcodedProduct productBag = new BarcodedProduct(PurchaseBagsController.PURCHASEDBAGBARCODE, "Purchased bag", PurchaseBagsController.PURCHASED_BAG_PRICE, 100f);
		
		// simulate a change in expected weight of bag in the product database
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(PurchaseBagsController.PURCHASEDBAGBARCODE, productBag);
		
		// any calls to purchase a bag will now produce a weight discrepancy
		
		// a weight discrepency here should not cause any issue, if the attendant approves and customer
		// wants to bag the item
		this.system.getCustomerIO().setNumberOfBagsPurchased(5);
		this.system.setAttendantApproval(true);
		this.system.setCustomerNoBag(false);
		this.system.purchaseBags();
		assertEquals(false, this.system.getUnApprovedDiscrepancy());
		assertEquals(false, this.system.getDiscrepancyActive());
		
		
		// a weight discrepency here SHOULD cause any issue, the attendant does not approve
		this.system.setAttendantApproval(false);
		this.system.setCustomerNoBag(false);
		this.system.purchaseBags();
		assertEquals(true, this.system.getUnApprovedDiscrepancy());
		assertEquals(true, this.system.getDiscrepancyActive());
		
		// if the customer doesn't want to add the bag to the scale and the attendant approves,
		// a Do Not Bag Item use case should be active
		this.system.setAttendantApproval(true);
		this.system.setCustomerNoBag(true);
		this.system.purchaseBags();
		assertEquals(true, this.system.getDoNotBagItemActive());
		assertEquals(true, this.system.getDiscrepancyActive());
	}
	
	@Test
	public void baggingAreaWeightUpdated() throws OverloadException, EmptyException {
		double weightBefore = this.station.baggingArea.getCurrentWeight();
		
		this.system.getCustomerIO().setNumberOfBagsPurchased(50);
		this.system.purchaseBags();
		
		double expected = weightBefore + (50 * PurchaseBagsController.BAG_WEIGHT);
		double actual = this.system.getStation().baggingArea.getCurrentWeight();
		double systemStoredWeight = this.system.getBaggingAreaWeight();
		
		assertEquals(expected, actual, 0.000001);
		assertEquals(expected, systemStoredWeight, 0.000001);
		
	}
	
	@Test
	public void billListUpdated() throws Exception {

		this.system.getCustomerIO().setNumberOfBagsPurchased(25);
		system.purchaseBags();
		
		int expectedBillSize = 25;
		int actualBillSize = this.system.getBillList().size();
		
		assertEquals(expectedBillSize, actualBillSize);
		
		
		tearDown();
		setUp();
		
		ArrayList<BarcodedProduct> billList = new ArrayList<>();
		for (int i = 0; i < 31; ++i) {
			billList.add(new BarcodedProduct(new Barcode(new Numeral[] {Numeral.eight}), "Arbitrary test item", BigDecimal.valueOf(100f) , 10f));
		}
		
		this.system.setBillList(billList);
		
		this.system.getCustomerIO().setNumberOfBagsPurchased(57);
		system.purchaseBags();
		
		expectedBillSize = 31 + 57;
		actualBillSize = this.system.getBillList().size();
		assertEquals(expectedBillSize, actualBillSize);
		
		
		for (BarcodedProduct product : this.system.getBillList()) {
			if (product.getBarcode().equals(PurchaseBagsController.PURCHASEDBAGBARCODE)) {
				assertEquals(product.getDescription(), "Purchased bag");
				assertEquals(product.getPrice(), PurchaseBagsController.PURCHASED_BAG_PRICE);
				assertEquals(product.getExpectedWeight(), PurchaseBagsController.BAG_WEIGHT, 0.0001);
			}
		}
	}
	
	@Test
	public void totalAmountDueUpdated() throws Exception {

		this.system.getCustomerIO().setNumberOfBagsPurchased(25);
		system.purchaseBags();
		
		double expectedAmountDue = 25f * PurchaseBagsController.PURCHASED_BAG_PRICE.doubleValue();
		double actualAmountDue = this.system.getAmountDue();
		assertEquals(expectedAmountDue, actualAmountDue, 0.000001);
	
	
		tearDown();
		setUp();
		
		ArrayList<BarcodedProduct> billList = new ArrayList<>();
		for (int i = 0; i < 31; ++i) {
			billList.add(new BarcodedProduct(new Barcode(new Numeral[] {Numeral.eight}), "Arbitrary test item", BigDecimal.valueOf(100f) , 10f));
		}
		
		this.system.setAmountDue(100f * 31f);
		
		this.system.setBillList(billList);
		
		this.system.getCustomerIO().setNumberOfBagsPurchased(57);
		system.purchaseBags();
		
		expectedAmountDue = (100f * 31) + (57f * PurchaseBagsController.PURCHASED_BAG_PRICE.doubleValue());
		actualAmountDue = this.system.getAmountDue();
		assertEquals(expectedAmountDue, actualAmountDue, 0.0000001);
	}
}
/*
	


	
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
*/
