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



import static org.junit.Assert.*;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;


public class AddOwnBagsTest extends BaseTestCase {
	private AddOwnBagsController useCase;
	

	// INITIALIZE STATION IS EXTENDED FROM BaseTestCase.java
	@Before
	public void setUp() throws Exception {
		this.initializeStation();
	}

	@After
	public void tearDown() throws Exception {
		this.deinitializeStation();
	}

	
	
	/**
	 * Test to ensure that a DisabledException is thrown
	 * if bagging area scale is disabled.
	 * 
	 * Bug found found with this, code originally allowed
	 * a bag to be added while the scale was disabled
	 * 
	 */
	@Test
	public void addWhileDisabled() {
		this.station.baggingArea.disable();
		this.system.getCustomerIO().setNumberOfPersonalBags(10);
		this.system.getAttendentStub().setDiscrepancyApproved(true);
		assertThrows(DisabledException.class, () -> system.addOwnBags());	
	}
	
	
	
	
	
	/**
	 *	Tests the case when an attendant does not approve the use
	 *	of personal bags (what a hater).
	 *
	 *	This test checks that the weight on the bagging area scale
	 *	is the same before and after the customer tries to add
	 *	their own bags
	 * @throws OverloadException 
	 * 
	 */
	
	@Test
	public void attendantNotApproved() throws OverloadException {
		double weightBefore = this.station.baggingArea.getCurrentWeight();
		
		this.system.getCustomerIO().setNumberOfPersonalBags(10);
		this.system.getAttendentStub().setDiscrepancyApproved(false);
		
		// check in the case that 100 bags have been added
		this.system.addOwnBags();
		assertEquals(weightBefore, this.station.baggingArea.getCurrentWeight(), 0.1f);
		
		
		// check in the case that just 1 bag has been added
		this.system.addOwnBags();
		assertEquals(weightBefore, this.station.baggingArea.getCurrentWeight(), 0.1f);
	}
	
	
	
	/**
	 *  Tests to make sure that the weight on the scale has been updated following 
	 *  a customer adding their own approved bags to the bagging area
	 * @throws Exception 
	 * 
	 */
	
	@Test
	public void attendantApproved() throws Exception {
		double weightBefore = this.station.baggingArea.getCurrentWeight();
		this.system.getAttendentStub().setDiscrepancyApproved(true);
		
		this.system.getCustomerIO().setNumberOfPersonalBags(100);
		// check in the case that 100 bags have been added
		this.system.addOwnBags();
		assertTrue(weightBefore < this.station.baggingArea.getCurrentWeight());
		
		// the bagging area is empty so adding 100 bags with a max of 100 lbs total
		//	should not cause an OverloadException
		
		// make sure that the weight of the added bags is not more than possible
		// given the design of the simulation and getBag() method
		assertTrue(weightBefore - this.station.baggingArea.getCurrentWeight() <= 100);
		
		// clean up simulation
		tearDown();
		setUp();
		
		this.system.getAttendentStub().setDiscrepancyApproved(true);
		// check in the case that just 1 bag has been added
		this.system.getCustomerIO().setNumberOfPersonalBags(1);
		this.system.addOwnBags();
		assertTrue(weightBefore < this.station.baggingArea.getCurrentWeight());
		
		// make sure that the weight of the added bags is not more than possible
		// given the design of the simulation and getBag() method
		assertTrue(this.station.baggingArea.getCurrentWeight() - weightBefore <= 1);
	}
	
	
	
	/**
	 *  Test to make sure that an OverloadExcepiton is thrown if an attendant
	 *  approves an amount of bags that is heavier than the scale alloows
	 * @throws Exception 
	 * 
	 */
	@Test
	public void tooManyBags() throws Exception {
		// fill scale with heavy item
		BarcodedUnit reallyHeavyItem = new BarcodedUnit(new Barcode(new Numeral[] {Numeral.one}), this.weightLimit);
		this.station.baggingArea.add(reallyHeavyItem);
		
		this.system.getAttendentStub().setDiscrepancyApproved(true);
		
		this.system.getCustomerIO().setNumberOfPersonalBags(100);
		
		// make sure that trying to add many bags will cause an overload exception
		assertThrows(OverloadException.class, () -> this.system.addOwnBags());
		
		tearDown();
		setUp();
		
		// make sure that trying to add even one bag will cause an overload exception
		reallyHeavyItem = new BarcodedUnit(new Barcode(new Numeral[] {Numeral.one}), this.weightLimit);
		this.station.baggingArea.add(reallyHeavyItem);
		
		this.system.getAttendentStub().setDiscrepancyApproved(true);
		
		this.system.getCustomerIO().setNumberOfPersonalBags(1);
		assertThrows(OverloadException.class, () -> this.system.addOwnBags());
		
	}
	



	
	@Test
	public void addNoBags() throws Exception {
		
		// make sure that if attendant approval is true, this works
		double weightBefore = this.station.baggingArea.getCurrentWeight();
		this.system.getAttendentStub().setDiscrepancyApproved(true);
		this.system.getCustomerIO().setNumberOfPersonalBags(0);
		this.system.addOwnBags();
		assertEquals(weightBefore, this.station.baggingArea.getCurrentWeight(), 0.00001);
		
		tearDown();
		setUp();
		
		// make sure that if attendant doesn't approve, the same thing happens
		
		// TODO: my thought here is that the attendant doesn't have to worry
		//	about no bags being added, so the check can be skipped
		//	let me know what you think to whoever reads this
		weightBefore = this.station.baggingArea.getCurrentWeight();
		this.system.getAttendentStub().setDiscrepancyApproved(false);
		this.system.getCustomerIO().setNumberOfPersonalBags(0);
		
		this.system.addOwnBags();
		assertEquals(weightBefore, this.station.baggingArea.getCurrentWeight(), 0.00001);
		
	}

}

