package com.autovend.software;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.SellableUnit;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.PLUCodedProduct;

import java.math.BigDecimal;
import java.math.MathContext;


/**
Desmond O'Brien: 30064340
Matthew Cusanelli: 30145324
Saadman Rahman: 30153482
Tanvir Ahamed Himel: 30148868
Victor campos: 30106934
Sean Tan: 30094560
Sahaj Malhotra: 30144405 
Caleb Cavilla: 30145972
Muhtadi Alam: 30150910
Omar Tejada: 31052626
Jose Perales: 30143354
Anna Lee: 30137463
 */
public class AddItemByPLUController extends AddItem {
	
	public AddItemByPLUController(SelfCheckoutStation station, SelfCheckoutSystemLogic system) {
		super(station, system);
	}
	
	
	public void add(SellableUnit currentSelectedItem) throws Exception {
		  PriceLookUpCodedUnit copyOfUnit = (PriceLookUpCodedUnit) currentSelectedItem;
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

          system.weightDiscrepency(this.station.baggingArea.getCurrentWeight() + weightInGrams);
          
          // add item to the baggingArea ElectronicScale
          station.baggingArea.add(copyOfUnit);
          
          this.system.addBillList(pluProduct);


          // increment weight total
          this.system.setBaggingAreaWeight(this.system.getBaggingAreaWeight()+copyOfUnit.getWeight());
          
         
          // mathcontext object to specify decimal precision
          MathContext mc = new MathContext(2);

          // calculate price based on weight
          BigDecimal price = pricePerKg.multiply(BigDecimal.valueOf(weightInGrams), mc);
          
         

          // increment price total
          this.totalPrice = price.plus();
          this.system.setAmountDue(this.system.getAmountDue() + totalPrice.doubleValue());
          
          //System.out.println(this.totalPrice);
          
          // *maybe need different event for adding PLU unit since no barcode*
          // react to barcode scanned event (adds PLUCodedUnit to BillList)
          // reactToBarcodeScannedEvent(station.mainScanner, copyOfUnit.getPLUCode());
          
          addItemStationEnable();
      }
  }

}
