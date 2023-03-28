
import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import com.autovend.devices.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Bill;
import com.autovend.software.PayWithCashController;
import com.autovend.software.System;


    /*
     * Members for Iteration 1:
     * Michelle Loi (30019557)
     * James Hayward (30149513)
     * Caleb Cavilla (30145972)
     * Amandeep Kaur (30153923)
     * Ethan Oke(30142180)
     * Winjoy Tiop (30069663)
     */


    //Test for the PayWithCash software
    public class PayWithCashTest {
        private PayWithCashController controller;
        private Currency currency;
        private Bill bill;
        private SelfCheckoutStation station;
        private System system;
        private BillDispenser dispenser;

        //Default setup: standard self checkout station and a 5$ bill
        @Before
        public void setup() {

            //Create a 5$ bill
            currency = Currency.getInstance(Locale.CANADA);
            bill = new Bill(5, currency);

            //Create self checkout station
            int[] billDenominations = {5, 10 , 15, 20, 50, 100};
            BigDecimal fiveCent = new BigDecimal("0.05");
            BigDecimal tenCent = new BigDecimal("0.10");
            BigDecimal twentyFiveCent = new BigDecimal("0.25");
            BigDecimal loonie = new BigDecimal("1");
            BigDecimal toonie = new BigDecimal("2");
            BigDecimal[] coinDenominations = {fiveCent, tenCent, twentyFiveCent, loonie, toonie};
            station = new SelfCheckoutStation(currency, billDenominations, coinDenominations,10,1);

            system = new System(station);

            dispenser = new BillDispenser(50);
        }

        //Reset the self checkout station and bill to null
        @After
        public void teardown() {
            station = null;
            bill = null;
            controller = null;
        }

        //Test creating a pay with cash controller with a null station
        @Test (expected = NullPointerException.class)
        public void testCreateWithNullStation() {
            new PayWithCashController(null,system);
        }

        //Test system's payWithCash() function
        @SuppressWarnings("deprecation")
        @Test
        public void testSystemPayWithCash() throws DisabledException,OverloadException {
            system.setAmountDue(10.00);
            system.payWithCash();
            station.billValidator.accept(bill);
            double expected = 5.00;
            double actual = system.getAmountDue();
            assertEquals(expected,actual,0.05);
        }

        //Test that deliver change works correctly within system
        @SuppressWarnings("deprecation")
        @Test
        public void testSystemDeliverChange() throws DisabledException,OverloadException {
            system.setAmountDue(7.99);
            system.payWithCash();
            station.billValidator.accept(bill);
            station.billValidator.accept(bill);
            double expected = -2.01;
            double actual = system.getAmountDue();
            assertEquals(expected,actual,0.05);
        }

        //Test for the bills that are dispensed by deliver change
        @SuppressWarnings("deprecation")
        @Test
        public void testSystemDispenseChange() throws DisabledException, OverloadException {
            system.setAmountDue(7.99);
            system.payWithCash();
            station.billValidator.accept(bill);
            station.billValidator.accept(bill);
            double expected = 5.0;
            double actual = system.getChangeDispensed();
            assertEquals(expected,actual,0.05);
        }

        @Test
        public void reactToEnabledEvent() {
            controller = new PayWithCashController(station,system);
            controller.reactToEnabledEvent(station.billInput);
        }

        @Test
        public void reactToDisabledEvent() {
            controller = new PayWithCashController(station,system);
            controller.reactToDisabledEvent(station.billInput);
        }

        @Test
        public void dummyReactToBillInsertedEvent() {
            controller = new PayWithCashController(station,system);
            controller.reactToBillInsertedEvent(station.billInput);
        }

        @Test
        public void dummyReactToBillEjectedEvent() {
            controller = new PayWithCashController(station,system);
            controller.reactToBillEjectedEvent(station.billInput);
        }

        @Test
        public void dummyReactToBillRemovedEvent() {
            controller = new PayWithCashController(station,system);
            controller.reactToBillRemovedEvent(station.billInput);
        }

        @Test
        public void dummyReactToInvalidBillDetectedEvent(){
            controller = new PayWithCashController(station,system);
            controller.reactToInvalidBillDetectedEvent(station.billValidator);
        }

        @Test
        public void dummyReactToBillsFullEvent(){
            controller = new PayWithCashController(station,system);
            controller.reactToBillsFullEvent(dispenser);
        }

        @Test
        public void dummyReactToBillsEmptyEvent(){
            controller = new PayWithCashController(station,system);
            controller.reactToBillsEmptyEvent(dispenser);
        }

        @Test
        public void dummyReactToBillAddedEvent(){
            controller = new PayWithCashController(station,system);
            controller.reactToBillAddedEvent(dispenser,bill);
        }

        @Test
        public void dummyReactToBillsLoadedEvent(){
            controller = new PayWithCashController(station,system);
            controller.reactToBillsLoadedEvent(dispenser,bill);
        }

        @Test
        public void dummyReactToBillsUnloadedEvent(){
            controller = new PayWithCashController(station,system);
            controller.reactToBillsUnloadedEvent(dispenser,bill);
        }

    }


