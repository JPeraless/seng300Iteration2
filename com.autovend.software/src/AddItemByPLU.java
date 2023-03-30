import com.autovend.PriceLookUpCodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.PLUCodedProduct;

import java.math.BigDecimal;


public class AddItemByPLU extends AddItem {
	
	public AddItemByPLU(SelfCheckoutStation station, SellableUnit unit) {
		super(station, unit);
	}
	
	
	public void add(SelfCheckoutStation station) throws Exception {
		  PriceLookUpCodedUnit copyOfUnit = (PriceLookUpCodedUnit) this.unit;
		  PLUCodedProduct pluProduct = getProductFromPLU(copyOfUnit.getPLUCode());
		  
		  // no gui implementation but would have keyboard pop up for user here
		  // prompt user to input PLU code
		  System.out.println("Please enter the PLU code of the item you wish to add");

		// check if PLU code is in database (have to change from copyOfUnit.PLUCode() to the users raw
		  // input then convert that to a PLU code)
      if (ProductDatabases.PLU_PRODUCT_DATABASE.containsKey(copyOfUnit.getPLUCode())) {
          // disable station
          addItemStationDisable();

          // get product/unit info
          double weightInGrams = copyOfUnit.getWeight();
          BigDecimal pricePerKg = pluProduct.getPrice();

          // notify customer
          System.out.println("Please place your item in the bagging area");

          // add item to the baggingArea ElectronicScale
          station.baggingArea.add(copyOfUnit);

          // increment weight total
          this.totalWeight += weightInGrams;

          // calculate price based on weight
          BigDecimal price = pricePerKg.multiply(BigDecimal.valueOf(weightInGrams / 1000));

          // increment price total
          totalPrice.add(price);
          
          // *maybe need different event for adding PLU unit since no barcode*
          // react to barcode scanned event (adds PLUCodedUnit to BillList)
          // reactToBarcodeScannedEvent(station.mainScanner, copyOfUnit.getPLUCode());
          
          addItemStationEnable();
      }
  }

}
