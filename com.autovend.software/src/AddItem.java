import java.math.BigDecimal;

import com.autovend.Barcode;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.products.PLUCodedProduct;
import com.autovend.products.Product;
import com.autovend.BarcodedUnit;
import com.autovend.PriceLookUpCode;
import com.autovend.PriceLookUpCodedUnit;
import com.autovend.SellableUnit;


/**
 * Permits customer to scan a barcode of an item to add it to their bill for
purchase.
 */

//TODO: change constructor implementation, instead use barcode to create unit/product from database and pass into functions 


public class AddItem implements BarcodeScannerObserver{
	//private BarcodedUnit barcodedUnit;
	//private BarcodedProduct barcodedProduct;
	private double totalWeight = 0;
	private BigDecimal totalPrice = BigDecimal.ZERO;
	protected SellableUnit unit;
	protected SelfCheckoutStation station;
	

	/**
	 * 
	 * 
	 * 
	 * @param staion - the station being used by the system
	 * @param currentUnit - Unit to be added (PLU or barcoded)
	 */
	public AddItem(SelfCheckoutStation station, SellableUnit currentUnit){
		
		this.station = station;
		this.unit = currentUnit; 

	}
		

	
	public BarcodedProduct getProductFromBarcode(Barcode barcode) {
		return ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
	}
	

	public PLUCodedProduct getProductFromPLU(PriceLookUpCode plu) {
		return ProductDatabases.PLU_PRODUCT_DATABASE.get(plu);
	}
	
	
	public void AddItemByScanning(SelfCheckoutStation station) throws Exception {
		BarcodedUnit copyOfUnit = (BarcodedUnit) this.unit;
		BarcodedProduct barcodedProduct = getProductFromBarcode(copyOfUnit.getBarcode());

		
		if(station.mainScanner.scan(copyOfUnit)) {
			//WeightDiscrepancy wd = new WeightDiscrepancy(station);
			
			//disable station
			addItemStationDisable();
			
			
			//get product/unit info
			double weightInGrams = copyOfUnit.getWeight();
			BigDecimal price = barcodedProduct.getPrice();
			
			//notify customer
			System.out.println("Please place your item in the bagging area");
			
			//add item to the baggingArea ElectronicScale
			station.baggingArea.add(copyOfUnit);
			
			//increment weight total
			this.totalWeight += weightInGrams;
			
			//boolean discrepancyCheck = wd.weightDiscrepancyOptions();
			
			//if (!discrepancyCheck) {
			//	throw new SimulationException("User added or removed item in repsonse");
			//}
			
			//increment price total
			totalPrice.add(price);
			
			//react to barcode scanned event (adds barcodedUnit to BillList)
			reactToBarcodeScannedEvent(station.mainScanner, copyOfUnit.getBarcode());
		}
	}

	
	public void AddItemByPLU (SelfCheckoutStation station) throws Exception {
		  PriceLookUpCodedUnit copyOfUnit = (PriceLookUpCodedUnit) this.unit;
		  PLUCodedProduct pluProduct = getProductFromPLU(copyOfUnit.getPLUCode());

		// *need alternative way to add PLU item, cant scan since no barcode* (cant implement the keyboard yet though)
        if (station.mainScanner.scan(copyOfUnit)) {
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
        }
    }

	
	
	
	private void addItemStationDisable() {
		station.mainScanner.disable();
		station.handheldScanner.disable();
		station.billInput.disable();
	}
	
	private void addItemStationEnable() {
		station.mainScanner.enable();
		station.handheldScanner.enable();
		station.billInput.enable();
	}
	
	@Override
	public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	// Reacts to scanner and adds item which is detected
	public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) throws SimulationException{

	}

}
