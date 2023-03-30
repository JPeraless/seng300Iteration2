import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.autovend.BarcodedUnit;
import com.autovend.Numeral;



import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import com.autovend.Barcode;

import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;

public class WeightDiscrepancyTest extends BaseTestCase {
	


	@Before
	public void setUp() throws Exception {
		this.initializeStation();
		
	}

	@After
	public void tearDown() throws Exception {
		this.deinitializeStation();
	}

	@Test
	public void itemWeightNotAsExpectedNoApproval() throws SimulationException, OverloadException {
		BarcodedUnit unit1 = new BarcodedUnit(new Barcode(new Numeral[] {Numeral.one}), 5f);
		BarcodedProduct product1 = new BarcodedProduct(new Barcode(new Numeral[] {Numeral.one}), "Test item 1", BigDecimal.valueOf(100f), 10f);
		//ProductDatabases.BARCODED_PRODUCT_DATABASE.put(new Barcode(new Numeral[] {Numeral.one}), product1);
		
		this.system.weightDiscrepency(5f);
		this.system.setCustomerNoBag(true);
		this.system.setAttendantApproval(false);
		this.station.baggingArea.add(unit1);
		
		assertEquals(true, this.system.getUnApprovedDiscrepancy());
		
		
	}

}
