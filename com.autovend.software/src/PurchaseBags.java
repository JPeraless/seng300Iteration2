import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Numeral;
import com.autovend.devices.SelfCheckoutStation;


public class PurchaseBags {

public static final Barcode PURCHASEDBAGBARCODE = new Barcode(new Numeral[] {Numeral.six,Numeral.nine});
private int numOfBags; 
private SelfCheckoutStation station; 
	


//Constructor for Purchased bags 
	public PurchaseBags(SelfCheckoutStation station, int numOfBags) {
		this.station = station; 
		this.numOfBags = numOfBags; 
		
				
	}
	
	
	
	
	

// Stub for bag dispenser
private BarcodedUnit BagDispenser(){
	BarcodedUnit bag = new BarcodedUnit(PURCHASEDBAGBARCODE, 1f);
	return bag; 
		
	}
	





}
