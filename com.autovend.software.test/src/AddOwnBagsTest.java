


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

import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;


public class AddOwnBagsTest {
	AddOwnBags useCase;
	private SelfCheckoutStation station;
	
	
	
	
	
	
	
	
/*	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
*/
	
	
	@Before
	public void setUp() throws Exception {
		Currency curr = Currency.getInstance(Locale.CANADA);
		
		int[] billDenoms = {5,10,20,50,100};
		
		double[] coinDenomFloats = {0.01f, 0.5f, 0.10f, 0.25f, 1f, 2f};
		BigDecimal[] coinDenoms = new BigDecimal[coinDenomFloats.length];
		for (int i = 0; i < coinDenomFloats.length; ++i) {
			coinDenoms[i] = BigDecimal.valueOf(coinDenomFloats[i]);
		}
		
		int scaleMax = 100;
		int scaleSensitivity = 1;
		
		this.station = new SelfCheckoutStation(curr, billDenoms, coinDenoms, scaleMax, scaleSensitivity);
		this.useCase = new AddOwnBags();
		
	}

	@After
	public void tearDown() throws Exception {
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
		assertThrows(DisabledException.class, () -> this.useCase.addOwnBags(this.station, 10, true));	
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
		
		// check in the case that 100 bags have been added
		this.useCase.addOwnBags(this.station, 100, false);
		assertEquals(weightBefore, this.station.baggingArea.getCurrentWeight(), 0.1f);
		
		// check in the case that just 1 bag has been added
		this.useCase.addOwnBags(this.station, 1, false);
		assertEquals(weightBefore, this.station.baggingArea.getCurrentWeight(), 0.1f);
		
	}

}
