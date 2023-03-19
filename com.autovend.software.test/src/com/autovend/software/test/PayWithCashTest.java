package com.autovend.software.test;
import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Bill;
import com.autovend.devices.BillSlot;
import com.autovend.devices.BillValidator;
import com.autovend.devices.DisabledException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.devices.SimulationException;
import com.autovend.software.PayWithCashController;
import com.autovend.software.System;


    /*
     * Members for Iteration 1:
     * Michelle Loi (30019557)
     * James Hayward (30149513)
     * Caleb Cavilla (30145972)
     * Amandeep Kaur (30153923)
     * Ethan Oke (30142180)
     * Winjoy Tiop (30069663)
     */


    //Test for the PayWithCash software
    public class PayWithCashTest {
        private PayWithCashController controller;
        private Currency currency;
        private Bill bill;
        private SelfCheckoutStation station;
        private System system;

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
        }

        //Reset the self checkout station and bill to null
        @After
        public void teardown() {
            station = null;
            bill = null;
            controller = null;
        }

        //Test creating a pay with cash controller with a null station
        @Test (expected = SimulationException.class)
        public void testCreateWithNullStation() {
            new PayWithCashController(null);
        }

        //Test system's payWithCash() function
        @SuppressWarnings("deprecation")
        @Test
        public void testSystemPayWithCash() throws DisabledException {
            system.payWithCash();
            station.billValidator.accept(bill);
            double expected = 5.00;
            double actual = system.getAmountDue();
            assertEquals(expected,actual);
        }

        //Test that deliver change works correctly within system
        @SuppressWarnings("deprecation")
        @Test
        public void testSystemDeliverChange() throws DisabledException {
            system.setAmountDue(7.99);
            system.payWithCash();
            station.billValidator.accept(bill);
            station.billValidator.accept(bill);
            double expected = -2.01;
            double actual = system.getAmountDue();
            assertEquals(expected,actual);
        }

        @Test
        public void reactToEnabledEvent() {
            controller = new PayWithCashController(station);
            controller.reactToEnabledEvent(station.billInput);
        }

        @Test
        public void reactToDisabledEvent() {
            controller = new PayWithCashController(station);
            controller.reactToDisabledEvent(station.billInput);
        }

        @Test
        public void dummyReactToBillInsertedEvent() {
            controller = new PayWithCashController(station);
            controller.reactToBillInsertedEvent(station.billInput);
        }

        @Test
        public void dummyReactToBillEjectedEvent() {
            controller = new PayWithCashController(station);
            controller.reactToBillEjectedEvent(station.billInput);
        }

        @Test
        public void dummyReactToBillRemovedEvent() {
            controller = new PayWithCashController(station);
            controller.reactToBillRemovedEvent(station.billInput);
        }

        @Test
        public void dummyReactToInvalidBillDetectedEvent(){
            controller = new PayWithCashController(station);
            controller.reactToInvalidBillDetectedEvent(station.billValidator);
        }

    }


