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




import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;


import java.util.Currency;
import java.util.Locale;



import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;

public class AddItemByScanning extends AddItem {

	public AddItemByScanning(SelfCheckoutStation station, SellableUnit currentUnit) {
		super(station, currentUnit);
	
	}

	
	
}
