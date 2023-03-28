


import static org.junit.Assert.*;
import static org.junit.Assert.assertThrows;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.autovend.devices.DisabledException;
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

	
	
	
	
	@Test
	public void addWhileDisabled() {
		this.station.baggingArea.disable();
		
		assertThrows(DisabledException.class, () -> this.useCase.addOwnBags(this.station, 10, true));
		
	}

}
