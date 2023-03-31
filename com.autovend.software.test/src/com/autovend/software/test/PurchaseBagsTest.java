package com.autovend.software.test;
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
import com.autovend.products.Product;
import com.autovend.software.PurchaseBagsController;


/**
 * Test suite for the Purchase Bags use case
 * 
 * @author desmo
 *
 */
public class PurchaseBagsTest extends BaseTestCase {
	private PurchaseBagsController useCase;
	
	/**
	 * Setup, partially inherited from BaseTestCase
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.initializeStation();
		BarcodedProduct productBag = new BarcodedProduct(PurchaseBagsController.PURCHASEDBAGBARCODE, "Purchased bag", 
				PurchaseBagsController.PURCHASED_BAG_PRICE, PurchaseBagsController.BAG_WEIGHT);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(PurchaseBagsController.PURCHASEDBAGBARCODE, productBag);
		
	}

	
	/**
	 * Tear down, partially inherited from BaseTestCase
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.deinitializeStation();
		ProductDatabases.BARCODED_PRODUCT_DATABASE.remove(PurchaseBagsController.PURCHASEDBAGBARCODE);
	}
	
	/**
	 * Test case to assure that a disabled exception is thrown if
	 * a bag is attempted to be purchased while the system is disabled
	 * 
	 * 
	 * @throws OverloadException
	 */
	@Test
	public void purchaseWhileBaggingAreaDisabled() throws OverloadException {
		// disable everything
		this.station.baggingArea.disable();
		this.station.handheldScanner.disable();
		this.station.mainScanner.disable();
		this.station.billInput.disable();
		this.station.coinSlot.disable();
		
		// catch expected exception
		this.system.getCustomerIO().setNumberOfBagsPurchased(10);
		assertThrows(DisabledException.class, () -> this.system.purchaseBags());
	}
	
	/**
	 * Test case to ensure that a Bag Dispenser throws an empty exception
	 * when it runs out of bags to dispense
	 * 
	 * As of this iteration, the Bag Dispenser device
	 * is stubbed in BagDispenserStub.java
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void notEnoughBags() throws Exception {
		
		// default limit in this implementation is 100, so let's try to purchase 101 bags all at once
		this.system.getCustomerIO().setNumberOfBagsPurchased(101);
		// exception should be caught
		assertThrows(EmptyException.class, () -> this.system.purchaseBags());
		
		
		// reset the station
		tearDown();
		setUp();
		
		// let's try to purchase 50 bags, should be fine as there are 100 available
		this.system.getCustomerIO().setNumberOfBagsPurchased(50);
		this.system.purchaseBags();
		
		// let's purchase another 50 bags, should also be ok as there are exactly 50 bags available
		// this is also good, but now 0 bags available after this
		this.system.purchaseBags();
		
		// try to purchase another 50 bags, make sure that empty exception is thrown
		assertThrows(EmptyException.class, () -> this.system.purchaseBags());

		// ensure that an exception is thrown if even 1 bag is to be purchased
		this.system.getCustomerIO().setNumberOfBagsPurchased(1);
		assertThrows(EmptyException.class, () -> this.system.purchaseBags());
	}
	
	
	/**
	 * Test to ensure that the Purchase Bag use case
	 * properly attaches a WeightDiscrepancy instance
	 * to the bagging area scale to react to weight changed events
	 * and handle bag weight discrepancies
	 *
	 * @throws OverloadException
	 * @throws EmptyException
	 */
	@Test
	public void incorrectWeight() throws OverloadException, EmptyException {
		// let's generate a new bag product for our database, with an updated price
		BarcodedProduct productBag = new BarcodedProduct(PurchaseBagsController.PURCHASEDBAGBARCODE, "Purchased bag", 
				PurchaseBagsController.PURCHASED_BAG_PRICE, 100f);
		
		//let's add it to the database
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(PurchaseBagsController.PURCHASEDBAGBARCODE, productBag);
		
		// any attempt to purchase bags should now cause a weight discrepancy, as no actual bags in the dispenser
		// will have the updated weight of 100 lbs as above
		
		// try to purchase 5 bags, lets say that the attendant eventually approves
		// the discrepancy and the customer doesn't want to add the bag to the scale
		this.system.getCustomerIO().setNumberOfBagsPurchased(5);
		this.system.setAttendantApproval(true);
		this.system.setCustomerNoBag(false);
		this.system.purchaseBags();
		
		// assure that the discrepancy was approved and dealt with, no active discrepancy
		assertEquals(false, this.system.getUnApprovedDiscrepancy());
		assertEquals(false, this.system.getDiscrepancyActive());
		
		
		// now let's try purchasing another 5 bags, this time without attendant approval
		// and the customer still does not want to add the bag to the scale
		this.system.setAttendantApproval(false);
		this.system.setCustomerNoBag(false);
		this.system.purchaseBags();
		
		// there should be an unapproved discrepancy deteced,
		// and the system should be in a state of an active discrepancy
		assertEquals(true, this.system.getUnApprovedDiscrepancy());
		assertEquals(true, this.system.getDiscrepancyActive());
		
		// if the customer doesn't want to add the bag to the scale and the attendant approves,
		// a Do Not Bag Item use case should be active
		this.system.setAttendantApproval(true);
		this.system.setCustomerNoBag(true);
		this.system.purchaseBags();
		
		// assure that the Do Not Bag Item use case is in effect and the discrepancy is still
		// in effect as of the beginning of this new use case
		assertEquals(true, this.system.getDoNotBagItemActive());
		assertEquals(true, this.system.getDiscrepancyActive());
	}
	
	/**
	 * Test case to ensure that if there are no
	 * weight discrepancies, that the weight of the bagging
	 * area is being corrected to include the weight of the new bags
	 * 
	 * @throws OverloadException
	 * @throws EmptyException
	 */
	@Test
	public void baggingAreaWeightUpdated() throws OverloadException, EmptyException {
		// let's record the weight on the scale before adding any bags
		double weightBefore = this.station.baggingArea.getCurrentWeight();
		
		// lets purchase 50 bags and add the, to the scale
		this.system.getCustomerIO().setNumberOfBagsPurchased(50);
		this.system.purchaseBags();
		
		// the weight that the bagging area should have
		double expected = weightBefore + (50 * PurchaseBagsController.BAG_WEIGHT);
		
		// the actual weight on the bagging area
		double actual = this.system.getStation().baggingArea.getCurrentWeight();
		// ensure that all weight are equal
		assertEquals(expected, actual, 0.000001);
		
	}
	
	/**
	 * Test case to ensure that after purchasing bags,
	 * the customer's bill includes the proper number of bags
	 * with the proper price, description, and weight
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void billListUpdated() throws Exception {
		
		// let's purchase 25 bags
		this.system.getCustomerIO().setNumberOfBagsPurchased(25);
		system.purchaseBags();
		
		// we should expect a bill size of 25 if nothing else was purchased
		int expectedBillSize = 25;
		int actualBillSize = this.system.getBillList().size();
		
		// ensure that this is accurate
		assertEquals(expectedBillSize, actualBillSize);
		
		// reset the simulation
		tearDown();
		setUp();
		
		// now let's simulate a case when there are already items on the bill
		ArrayList<Product> billList = new ArrayList<>();
		for (int i = 0; i < 31; ++i) {
			billList.add(new BarcodedProduct(new Barcode(new Numeral[] {Numeral.eight}), "Arbitrary test item", BigDecimal.valueOf(100f) , 10f));
		}
		
		// set the arraylist above as the bill list of the transaction
		this.system.setBillList(billList);
		
		// now lets purchase some bags
		this.system.getCustomerIO().setNumberOfBagsPurchased(57);
		system.purchaseBags();
		
		// our expected bill size should include the previous items
		expectedBillSize = 31 + 57;
		actualBillSize = this.system.getBillList().size();
		
		// ensure that this happens
		assertEquals(expectedBillSize, actualBillSize);
		
		// now let's go through our bill list and inspect every item 
		// with a purchased bag barcode
		// lets ensure that all of the information on the item is correct
		for (Product product : this.system.getBillList()) {
			if (((BarcodedProduct) product).getBarcode().equals(PurchaseBagsController.PURCHASEDBAGBARCODE)) {
				assertEquals(((BarcodedProduct)product).getDescription(), "Purchased bag");
				assertEquals(product.getPrice(), PurchaseBagsController.PURCHASED_BAG_PRICE);
				assertEquals(((BarcodedProduct)product).getExpectedWeight(), PurchaseBagsController.BAG_WEIGHT, 0.0001);
			}
		}
	}
	
	/**
	 * Test case to ensure that the amount due
	 * on the bill includes the cost
	 * of all purchased bags after purchasing bags
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void totalAmountDueUpdated() throws Exception {
		
		// let's purchase only 25 bags
		this.system.getCustomerIO().setNumberOfBagsPurchased(25);
		system.purchaseBags();
		
		// lets ensure that the amount due in the system is correct
		double expectedAmountDue = 25f * PurchaseBagsController.PURCHASED_BAG_PRICE.doubleValue();
		double actualAmountDue = this.system.getAmountDue();
		assertEquals(expectedAmountDue, actualAmountDue, 0.000001);
	
		// reset the simulation
		tearDown();
		setUp();
		
		
		// now lets purchase some items before we purchase the bags
		ArrayList<Product> billList = new ArrayList<>();
		for (int i = 0; i < 31; ++i) {
			billList.add(new BarcodedProduct(new Barcode(new Numeral[] {Numeral.eight}), "Arbitrary test item", BigDecimal.valueOf(100f) , 10f));
		}
		
		// set the price and bill list manually in the system
		this.system.setAmountDue(100f * 31f);
		this.system.setBillList(billList);
		
		// now lets purchase some bags
		this.system.getCustomerIO().setNumberOfBagsPurchased(57);
		system.purchaseBags();
		
		// ensure that the total amount due is equal to the total amount
		// due of the previous items as well as the bags that we have purchased
		expectedAmountDue = (100f * 31) + (57f * PurchaseBagsController.PURCHASED_BAG_PRICE.doubleValue());
		actualAmountDue = this.system.getAmountDue();
		assertEquals(expectedAmountDue, actualAmountDue, 0.0000001);
	}
}

