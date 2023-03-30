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
	AddItemByPLU useCase;
	
	SellableUnit unit0;
	SellableUnit unit1;
	SellableUnit unit2;
	SellableUnit unit3;
	
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
		super.initializeStation();
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
		this.useCase = new AddItemByPLU(super.station, unit0);
		useCase.add(station);
		double weight = useCase.totalWeight;
		assertEquals(weight,station.baggingArea.getCurrentWeight(), 0.00001);
		
		// Testing after adding a second item
		this.useCase = new AddItemByPLU(super.station, unit1);
		useCase.add(station);
		double weight2 = useCase.totalWeight + weight;
		assertEquals(weight2,station.baggingArea.getCurrentWeight(), 0.00001);
	}
	
	@Test
	public void testTotalPrice() throws Exception {
	    AddItemByPLU useCase1 = new AddItemByPLU(station, unit0);
	    useCase1.add(station);
	    BigDecimal expectedTotalPrice1 = BigDecimal.valueOf(1f);
	    assertEquals(expectedTotalPrice1, useCase1.totalPrice);

	}
	
	
	
	@Test (expected = SimulationException.class)
	public void AddingSameItem() throws Exception {
		this.useCase = new AddItemByPLU(super.station, unit0);
		useCase.add(station);
		this.useCase = new AddItemByPLU(super.station, unit0);
		useCase.add(station);
	
	}
	

}

