 import static org.junit.Assert.*;

import java.math.BigDecimal;


import com.autovend.Numeral;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.PriceLookUpCode;
import com.autovend.SellableUnit;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.external.*;
import com.autovend.products.PLUCodedProduct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;




public class AddItemByPLUTest extends BaseTestCase {
	AddItemByPLUController useCase;
	
	
	
	PriceLookUpCodedUnit unit0;
	PriceLookUpCodedUnit unit1;
	PriceLookUpCodedUnit unit2;
	PriceLookUpCodedUnit unit3;
	
	PLUCodedProduct product0;
	PLUCodedProduct product1;
	PLUCodedProduct product2;
	PLUCodedProduct product3;
	
	static final PriceLookUpCode PLUCODE_0 = new PriceLookUpCode(new Numeral[] {Numeral.one, Numeral.two, Numeral.three, Numeral.four}); 
	static final PriceLookUpCode PLUCODE_1 = new PriceLookUpCode(new Numeral[] {Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five});
	static final PriceLookUpCode PLUCODE_2 = new PriceLookUpCode(new Numeral[] {Numeral.six, Numeral.seven, Numeral.eight, Numeral.nine});
	static final PriceLookUpCode PLUCODE_3 = new PriceLookUpCode(new Numeral[] {Numeral.five, Numeral.six, Numeral.seven, Numeral.eight, Numeral.nine});
	
	@Before
	public void setUp() throws Exception {
		super.initializeStation();
		unit0 = new PriceLookUpCodedUnit(PLUCODE_0, 1f);
		unit1 = new PriceLookUpCodedUnit(PLUCODE_1, 2f);
		unit2 = new PriceLookUpCodedUnit(PLUCODE_2, 3f);
		unit3 = new PriceLookUpCodedUnit(PLUCODE_3, 4f);
		
		product0 = new PLUCodedProduct(PLUCODE_0, "Item 0", BigDecimal.valueOf(1f));
		product1 = new PLUCodedProduct(PLUCODE_1, "Item 1", BigDecimal.valueOf(2f));
		product2 = new PLUCodedProduct(PLUCODE_2, "Item 2", BigDecimal.valueOf(3f));
		product3 = new PLUCodedProduct(PLUCODE_3, "Item 3", BigDecimal.valueOf(4f));
		
		
		ProductDatabases.PLU_PRODUCT_DATABASE.put(PLUCODE_0, product0);
		ProductDatabases.PLU_PRODUCT_DATABASE.put(PLUCODE_1, product1);
		ProductDatabases.PLU_PRODUCT_DATABASE.put(PLUCODE_2, product2);
		ProductDatabases.PLU_PRODUCT_DATABASE.put(PLUCODE_3, product3);
		
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
		ProductDatabases.PLU_PRODUCT_DATABASE.remove(PLUCODE_0);
		ProductDatabases.PLU_PRODUCT_DATABASE.remove(PLUCODE_1);
		ProductDatabases.PLU_PRODUCT_DATABASE.remove(PLUCODE_2);
		ProductDatabases.PLU_PRODUCT_DATABASE.remove(PLUCODE_3);

	}
	
	@Test
	public void WeightEquals() throws Exception {
		this.system.setCurrentSelectableUnit(unit0);
		
		system.addItemByPLU();
		double weight = system.getBaggingAreaWeight();
		System.out.println(system.getBaggingAreaWeight());
		assertEquals(1f, weight, 0.00001);
		
		// Testing after adding a second item
		this.system.setCurrentSelectableUnit(unit1);
		system.addItemByPLU();
		double weight2 = system.getBaggingAreaWeight();
		assertEquals(1f + 2f,weight2, 0.00001);
	}
	
	@Test
	public void testTotalPrice() throws Exception {
	    this.system.setCurrentSelectableUnit(unit0);
	    system.addItemByPLU();
	    double expectedTotalPrice1 = BigDecimal.valueOf(1f).doubleValue();
	    assertEquals(expectedTotalPrice1, system.getAmountDue(), 0.0001);
        
	}
	
	
	
	@Test (expected = SimulationException.class)
	public void AddingSameItem() throws Exception {
		this.system.setCurrentSelectableUnit(unit0);
		system.addItemByPLU();
		system.addItemByPLU();
	
	}
	

}

