import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import com.autovend.*;
import com.autovend.devices.*;
import com.autovend.external.CardIssuer;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.CustomerIO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.autovend.software.PayWithCashController;
import com.autovend.software.SelfCheckoutSystemLogic;

public class PayWithCardTest {

    private Currency currency;

    private SelfCheckoutStation station;

    private SelfCheckoutSystemLogic system;

    private PayWithCardObserver controller;

    private CustomerIO customer;

    CardIssuer bank = new CardIssuer("RBC");

    Calendar expiry = new GregorianCalendar(2027, Calendar.JUNE, 25);

    private BarcodedUnit barcodedUnit;

    private BarcodedProduct barcodedProduct;

    Numeral[] numerals = new Numeral[]{Numeral.three, Numeral.one, Numeral.two};

    Barcode barcode = new Barcode(numerals);

    Pay pay;



    // Setup self-checkout station
    @Before
    public void setup() {
        // Set the currency to be used
        currency = Currency.getInstance(Locale.CANADA);


        // Initialize a self-checkout station
        int[] billDenominations = {5, 10 , 15, 20, 50, 100};
        BigDecimal fiveCent = new BigDecimal("0.05");
        BigDecimal tenCent = new BigDecimal("0.10");
        BigDecimal twentyFiveCent = new BigDecimal("0.25");
        BigDecimal loonie = new BigDecimal("1");
        BigDecimal toonie = new BigDecimal("2");
        BigDecimal[] coinDenominations = {fiveCent, tenCent, twentyFiveCent, loonie, toonie};
        station = new SelfCheckoutStation(currency, billDenominations, coinDenominations,10,1);

        // Initialize the station's system
        system = new SelfCheckoutSystemLogic(station);



        controller = new PayWithCardObserver(station, pay);



        // set a product's description
        String description = "Description for item";

        // set a product's weight
        double weight = 13.0;

        // set a product's price
        BigDecimal price = new BigDecimal("5.50");

        // initialize a barcoded unit
        barcodedUnit = new BarcodedUnit(barcode, weight);

        // initialize a barcoded product
        barcodedProduct = new BarcodedProduct(barcode, description, price, weight);

        // add product to product's databases for accessing
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, barcodedProduct);


        // TODO: register observers

        station.mainScanner.enable();
        station.handheldScanner.enable();

        customer = new CustomerIO();

    }


    // Reset self-checkout station and card
    @After
    public void teardown() {
        station = null;
    }


    // Test inserting a debit card with sufficient funds and a valid pin
    @Test
    public void testInsertDebit() throws IOException {
        DebitCard insertDebit = new DebitCard("DEBIT", "283745869032", "debit", "432", "3030", true, true);

        station.cardReader.register(controller);
        station.mainScanner.scan(barcodedUnit);
        station.baggingArea.add(barcodedUnit);

        PayWithCard payWithCard = new PayWithCard(station, controller, insertDebit, customer);
        bank.addCardData("283745869032", "debit", expiry, "432", BigDecimal.valueOf(200));

        try {
            station.cardReader.insert(insertDebit, "3030");
        }
        catch (SimulationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(new BigDecimal("5.50"), BigDecimal.valueOf(Pay.getAmountPaid()));
    }

    // Test tapping a debit card with sufficient funds and a valid pin
    @Test
    public void testTapDebit() throws IOException {
        DebitCard tapDebit = new DebitCard("DEBIT", "283745869032", "debit", "432", "3030", true, true);

        PayWithCard payWithCard = new PayWithCard(station, controller, tapDebit, customer);
        bank.addCardData("283745869032", "debit", expiry, "432", BigDecimal.valueOf(200));

        station.cardReader.register(controller);
        station.mainScanner.scan(barcodedUnit);
        station.baggingArea.add(barcodedUnit);

        try {
            station.cardReader.tap(tapDebit);
        }
        catch (SimulationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(new BigDecimal("5.50"), Pay.getAmountPaid());
    }

    // Test swiping a debit card with sufficient funds and a valid pin
    @Test
    public void testSwipeDebit() throws IOException {
        DebitCard swipeDebit = new DebitCard("DEBIT", "283745869032", "debit", "432", "3030", true, true);

        PayWithCard payWithCard = new PayWithCard(station, controller, swipeDebit, customer);
        bank.addCardData("283745869032", "debit", expiry, "432", BigDecimal.valueOf(200));

        station.cardReader.register(controller);
        station.mainScanner.scan(barcodedUnit);
        station.baggingArea.add(barcodedUnit);

        try {
            station.cardReader.swipe(swipeDebit, null);
        }
        catch (SimulationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(new BigDecimal("5.50"), Pay.getAmountPaid());
    }

    // Test inserting a debit card with sufficient funds and an invalid pin
    @Test (expected = InvalidPINException.class)
    public void testWrongPinInsertDebit() throws IOException{
        DebitCard insertDebit = new DebitCard("DEBIT", "283745869032", "debit", "432", "3030", true, true);

        PayWithCard payWithCard = new PayWithCard(station, controller, insertDebit, customer);
        bank.addCardData("283745869032", "debit", expiry, "432", BigDecimal.valueOf(200));

        station.cardReader.register(controller);
        station.mainScanner.scan(barcodedUnit);
        station.baggingArea.add(barcodedUnit);

        station.cardReader.insert(insertDebit, "0000");
    }

    // Test inserting a debit card with insufficient funds and a valid pin
    @Test
    public void testInsufficientDebitInsert() throws IOException{
        DebitCard insertInsufficientDebit = new DebitCard("DEBIT", "283745869032", "debit", "432", "3030", true, true);

        PayWithCard payWithCard = new PayWithCard(station, controller, insertInsufficientDebit, customer);
        bank.addCardData("283745869032", "debit", expiry, "432", BigDecimal.valueOf(200));

        station.cardReader.register(controller);
        station.mainScanner.scan(barcodedUnit);
        station.baggingArea.add(barcodedUnit);

        try {
            station.cardReader.insert(insertInsufficientDebit, "3030");
        }
        catch (SimulationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(new BigDecimal(0), Pay.getAmountPaid());
        Assert.assertEquals(new BigDecimal("5.50"), Pay.getTotalAmountDue() - Pay.getAmountPaid());
    }

    // Test tapping a debit card with insufficient funds and a valid pin
    @Test
    public void testInsufficientDebitTap() throws IOException{
        DebitCard tapInsufficientDebit = new DebitCard("DEBIT", "283745869032", "debit", "432", "3030", true, true);

        PayWithCard payWithCard = new PayWithCard(station, controller, tapInsufficientDebit, customer);
        bank.addCardData("283745869032", "debit", expiry, "432", BigDecimal.valueOf(200));

        station.cardReader.register(controller);
        station.mainScanner.scan(barcodedUnit);
        station.baggingArea.add(barcodedUnit);

        try {
            station.cardReader.tap(tapInsufficientDebit);
        }
        catch (SimulationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(new BigDecimal(0), Pay.getAmountPaid());
        Assert.assertEquals(new BigDecimal("5.50"), Pay.getTotalAmountDue() - Pay.getAmountPaid());
    }

    // Test swiping a debit card with insufficient funds and a valid pin
    @Test
    public void testInsufficientDebitSwipe() throws IOException{
        DebitCard swipeInsufficientDebit = new DebitCard("DEBIT", "283745869032", "debit", "432", "3030", true, true);

        PayWithCard payWithCard = new PayWithCard(station, controller, swipeInsufficientDebit, customer);
        bank.addCardData("283745869032", "debit", expiry, "432", BigDecimal.valueOf(200));

        station.cardReader.register(controller);
        station.mainScanner.scan(barcodedUnit);
        station.baggingArea.add(barcodedUnit);

        try {
            station.cardReader.swipe(swipeInsufficientDebit, null);
        }
        catch (SimulationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(new BigDecimal(0), Pay.getAmountPaid());
        Assert.assertEquals(new BigDecimal("5.50"), Pay.getTotalAmountDue() - Pay.getAmountPaid());
    }

    // Test inserting a credit card with sufficient funds and a valid pin
    @Test
    public void testInsertCredit() throws IOException{
        CreditCard insertCredit = new CreditCard("CREDIT", "283745869032", "credit", "432", "3030", true, true);

        PayWithCard payWithCard = new PayWithCard(station, controller, insertCredit, customer);
        bank.addCardData("283745869032", "credit", expiry, "432", BigDecimal.valueOf(200));

        station.cardReader.register(controller);
        station.mainScanner.scan(barcodedUnit);
        station.baggingArea.add(barcodedUnit);

        try {
            station.cardReader.insert(insertCredit, "3030");
        }
        catch (SimulationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(new BigDecimal("5.50"), Pay.getAmountPaid());
    }

    // Test tapping a credit card with sufficient funds and a valid pin
    @Test
    public void testTapCredit() throws IOException {
        CreditCard tapCredit = new CreditCard("CREDIT", "283745869032", "credit", "432", "3030", true, true);

        PayWithCard payWithCard = new PayWithCard(station, controller, tapCredit, customer);
        bank.addCardData("283745869032", "credit", expiry, "432", BigDecimal.valueOf(200));

        station.cardReader.register(controller);
        station.mainScanner.scan(barcodedUnit);
        station.baggingArea.add(barcodedUnit);

        try {
            station.cardReader.tap(tapCredit);
        }
        catch (SimulationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(new BigDecimal("5.50"), Pay.getAmountPaid());
    }

    // Test swiping a credit card with sufficient funds and a valid pin
    @Test
    public void testSwipeCredit() throws IOException {
        CreditCard swipeCredit = new CreditCard("CREDIT", "283745869032", "credit", "432", "3030", true, true);

        PayWithCard payWithCard = new PayWithCard(station, controller, swipeCredit, customer);
        bank.addCardData("283745869032", "credit", expiry, "432", BigDecimal.valueOf(200));

        station.cardReader.register(controller);
        station.mainScanner.scan(barcodedUnit);
        station.baggingArea.add(barcodedUnit);

        try {
            station.cardReader.swipe(swipeCredit, null);
        }
        catch (SimulationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(new BigDecimal("5.50"), Pay.getAmountPaid());
    }

    // Test inserting a credit card with sufficient funds and an invalid pin
    @Test (expected = InvalidPINException.class)
    public void testWrongPinInsertCredit() throws IOException {
        CreditCard insertCredit = new CreditCard("CREDIT", "283745869032", "credit", "432", "3030", true, true);

        PayWithCard payWithCard = new PayWithCard(station, controller, insertCredit, customer);
        bank.addCardData("283745869032", "credit", expiry, "432", BigDecimal.valueOf(200));

        station.cardReader.register(controller);
        station.mainScanner.scan(barcodedUnit);
        station.baggingArea.add(barcodedUnit);

        station.cardReader.insert(insertCredit, "0000");
    }

    // Test inserting a credit card with insufficient funds and a valid pin
    @Test
    public void testInsufficientCreditInsert() throws IOException {
        CreditCard insertInsufficientCredit = new CreditCard("CREDIT", "283745869032", "credit", "432", "3030", true, true);

        PayWithCard payWithCard = new PayWithCard(station, controller, insertInsufficientCredit, customer);
        bank.addCardData("283745869032", "credit", expiry, "432", BigDecimal.valueOf(200));

        station.cardReader.register(controller);
        station.mainScanner.scan(barcodedUnit);
        station.baggingArea.add(barcodedUnit);

        try {
            station.cardReader.insert(insertInsufficientCredit, "3030");
        }
        catch (SimulationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(new BigDecimal(0), Pay.getAmountPaid());
        Assert.assertEquals(new BigDecimal("5.50"), Pay.getTotalAmountDue() - Pay.getAmountPaid());
    }

    // Test tapping a credit card with insufficient funds and a valid pin
    @Test
    public void testInsufficientCreditTap() throws IOException {
        CreditCard tapInsufficientCredit = new CreditCard("CREDIT", "283745869032", "credit", "432", "3030", true, true);

        PayWithCard payWithCard = new PayWithCard(station, controller, tapInsufficientCredit, customer);
        bank.addCardData("283745869032", "credit", expiry, "432", BigDecimal.valueOf(200));

        station.cardReader.register(controller);
        station.mainScanner.scan(barcodedUnit);
        station.baggingArea.add(barcodedUnit);

        try {
            station.cardReader.tap(tapInsufficientCredit);
        }
        catch (SimulationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(new BigDecimal(0), Pay.getAmountPaid());
        Assert.assertEquals(new BigDecimal("5.50"), Pay.getTotalAmountDue() - Pay.getAmountPaid());
    }

    // Test swiping a credit card with insufficient funds and a valid pin
    @Test
    public void testInsufficientCreditSwipe() throws IOException {
        CreditCard swipeInsufficientCredit = new CreditCard("CREDIT", "283745869032", "credit", "432", "3030", true, true);

        PayWithCard payWithCard = new PayWithCard(station, controller, swipeInsufficientCredit, customer);
        bank.addCardData("283745869032", "credit", expiry, "432", BigDecimal.valueOf(200));

        station.cardReader.register(controller);
        station.mainScanner.scan(barcodedUnit);
        station.baggingArea.add(barcodedUnit);

        try {
            station.cardReader.swipe(swipeInsufficientCredit, null);
        }
        catch (SimulationException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(new BigDecimal(0), Pay.getAmountPaid());
        Assert.assertEquals(new BigDecimal("5.50"), Pay.getTotalAmountDue() - Pay.getAmountPaid());
    }

}