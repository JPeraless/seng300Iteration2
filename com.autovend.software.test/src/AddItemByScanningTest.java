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

public class AddItemByScanningTest extends BaseTestCase {
	SelfCheckoutStation station;
	AddItem useCase;
	
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
		this.initializeStation();
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
		this.deinitializeStation();
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

	@Test
	public void test() {

	}

}