
import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import com.autovend.devices.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Bill;
import com.autovend.Coin;
import com.autovend.software.PayWithCashController;
import com.autovend.software.SelfCheckoutSystemLogic;


    //Test for the PayWithCash software
    public class PayWithCashTest {
        private PayWithCashController controller;
        private Currency currency;
        private Bill bill_5;
        private Bill bill_10;
        private SelfCheckoutStation station;
        private SelfCheckoutSystemLogic system;
        private BillDispenser dispenser;
		private Coin coin_2;
		private Coin coin_025;
		private Coin coin_005;

        //Default setup: standard self checkout station and a 5$ bill
        @Before
        public void setup() throws SimulationException, OverloadException {

        	currency = Currency.getInstance(Locale.CANADA);
        	
            //Create self checkout station
            int[] billDenominations = {5, 10 , 15, 20, 50, 100};
            BigDecimal fiveCent = new BigDecimal("0.05");
            BigDecimal tenCent = new BigDecimal("0.10");
            BigDecimal twentyFiveCent = new BigDecimal("0.25");
            BigDecimal loonie = new BigDecimal("1");
            BigDecimal toonie = new BigDecimal("2");
            BigDecimal[] coinDenominations = {fiveCent, tenCent, twentyFiveCent, loonie, toonie};
            station = new SelfCheckoutStation(currency, billDenominations, coinDenominations,10,1);

            system = new SelfCheckoutSystemLogic(station);

            dispenser = new BillDispenser(50);
            
            //Create a 5$ bill
            bill_5 = new Bill(5, currency);
            bill_10 = new Bill(10, currency);
            //Create a toonie 
            coin_2 = new Coin(toonie, currency);
            coin_025 = new Coin(twentyFiveCent, currency);
            coin_005 = new Coin(fiveCent, currency);
            
            for(int x : station.billDispensers.keySet()) {
				Bill bill = new Bill(x, Currency.getInstance("CAD"));
				station.billDispensers.get(x).load(bill, bill, bill, bill, bill);
			}
            
            for(BigDecimal x : station.coinDispensers.keySet()) {
				Coin coin = new Coin(x, Currency.getInstance("CAD"));
				station.coinDispensers.get(x).load(coin, coin, coin, coin, coin);
			}
        }

        //Reset the self checkout station and bill to null
        @After
        public void teardown() {
            station = null;
            bill_5 = null;
            controller = null;
        }

        //Test creating a pay with cash controller with a null station
        @Test (expected = NullPointerException.class)
        public void testCreateWithNullStation() {
            new PayWithCashController(null,system);
        }

        //Test system's payWithCash() function with a standard whole number dollar amount, no cents involved
        @SuppressWarnings("deprecation")
        @Test
        public void SystemCalculateChangeWholeNumber() throws DisabledException,OverloadException {
        	system.setAmountDue(22);
            system.payWithCash();
            station.billValidator.accept(bill_10);
            station.billValidator.accept(bill_10);
            station.billValidator.accept(bill_5);
            double expected = -3;
            double actual = system.getAmountDue();
            assertEquals(expected,actual,0.05);
        }

        //Test that deliver change works correctly within system
        @SuppressWarnings("deprecation")
        @Test
        public void testSystemCalculateChangeDecimalNoRounding() throws DisabledException,OverloadException {
            system.setAmountDue(22);
            system.payWithCash();
            station.billValidator.accept(bill_10);
            station.billValidator.accept(bill_10);
            station.billValidator.accept(bill_5);
            double expected = 3;
            double actual = system.getAmountDue();
            assertEquals(expected,actual,0.05);
        }

        //Test for the bills that are dispensed by deliver change
        @SuppressWarnings("deprecation")
        @Test
        public void testSystemDispenseChange() throws DisabledException, OverloadException {
            system.setAmountDue(7.94);
            system.payWithCash();
            station.billValidator.accept(bill_5);
            station.billValidator.accept(bill_5);
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
            controller.reactToBillAddedEvent(dispenser,bill_5);
        }

        @Test
        public void dummyReactToBillsLoadedEvent(){
            controller = new PayWithCashController(station,system);
            controller.reactToBillsLoadedEvent(dispenser,bill_5);
        }

        @Test
        public void dummyReactToBillsUnloadedEvent(){
            controller = new PayWithCashController(station,system);
            controller.reactToBillsUnloadedEvent(dispenser,bill_5);
        }

    }


