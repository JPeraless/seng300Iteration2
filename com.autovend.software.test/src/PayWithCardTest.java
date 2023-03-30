import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.autovend.*;
import com.autovend.devices.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.software.PayWithCashController;
import com.autovend.software.SelfCheckoutSystemLogic;

public class PayWithCardTest {

    private Currency currency;

    private PayWithCard payWithCard;

    private SelfCheckoutStation station;

    private SelfCheckoutSystemLogic system;

    private CreditCard creditCard;

    private DebitCard debitCard;



    // Setup self-checkout station
    @Before
    public void setup() {
        // Set the currency to be used
        currency = Currency.getInstance(Locale.CANADA);


        // Create a self-checkout station
        int[] billDenominations = {5, 10 , 15, 20, 50, 100};
        BigDecimal fiveCent = new BigDecimal("0.05");
        BigDecimal tenCent = new BigDecimal("0.10");
        BigDecimal twentyFiveCent = new BigDecimal("0.25");
        BigDecimal loonie = new BigDecimal("1");
        BigDecimal toonie = new BigDecimal("2");
        BigDecimal[] coinDenominations = {fiveCent, tenCent, twentyFiveCent, loonie, toonie};
        station = new SelfCheckoutStation(currency, billDenominations, coinDenominations,10,1);

        system = new SelfCheckoutSystemLogic(station);


        station.mainScanner.enable();
        station.handheldScanner.enable();
        station.baggingArea.enable();

    }

    // Reset self-checkout station
    @After
    public void teardown() {
        station = null;
    }

    // Test inserting a debit card with sufficient funds and a valid pin
    @Test
    public void testDebitInsert() {

    }

    // Test tapping a debit card with sufficient funds and a valid pin
    @Test
    public void testDebitTap() {

    }

    // Test swiping a debit card with sufficient funds and a valid pin
    @Test
    public void testDebitSwipe() {

    }

    // Test inserting a debit card with sufficient funds and an invalid pin
    @Test
    public void testWrongPinInsertDebit() {

    }

    // Test inserting a debit card with insufficient funds and a valid pin
    @Test
    public void testInsufficientDebitInsert() {

    }

    // Test tapping a debit card with insufficient funds and a valid pin
    @Test
    public void testInsufficientDebitTap() {

    }

    // Test swiping a debit card with insufficient funds and a valid pin
    @Test
    public void testInsufficientDebitSwipe() {

    }

    // Test inserting a credit card with sufficient funds and a valid pin
    @Test
    public void testCreditInsert() {

    }

    // Test tapping a credit card with sufficient funds and a valid pin
    @Test
    public void testCreditTap() {

    }

    // Test swiping a credit card with sufficient funds and a valid pin
    @Test
    public void testCreditSwipe() {

    }

    // Test inserting a credit card with sufficient funds and an invalid pin
    @Test
    public void testWrongPinInsertCredit() {

    }

    // Test inserting a credit card with insufficient funds and a valid pin
    @Test
    public void testInsufficientCreditInsert() {

    }

    // Test tapping a credit card with insufficient funds and a valid pin
    @Test
    public void testInsufficientCreditTap() {

    }

    // Test swiping a credit card with insufficient funds and a valid pin
    @Test
    public void testInsufficientCreditSwipe() {

    }

}