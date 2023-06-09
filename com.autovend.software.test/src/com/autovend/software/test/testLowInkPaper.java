/*
 * Anna Lee: 30137463
 * Caleb Cavilla: 30145972
 * Desmond O'Brien: 30064340
 * Jose Perales: 30143354
 * Matthew Cusanelli: 30145324
 * Muhtadi Alam: 30150910
 * Omar Tejada: 31052626
 * Saadman Rahman: 30153482
 * Sahaj Malhotra: 30144405
 * Sean Tan: 30094560
 * Tanvir Ahamed Himel: 30148868
 * Victor campos: 30106934
 */
package com.autovend.software.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.BarcodedUnit;
import com.autovend.Bill;
import com.autovend.Numeral;
import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.external.ProductDatabases;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.AttendentStub;
import com.autovend.software.CustomerIO;
import com.autovend.software.PrintReceipt;
import com.autovend.software.PrintReceiptObserver;
import com.autovend.software.SelfCheckoutSystemLogic;

public class testLowInkPaper {

	SelfCheckoutStation scs;
	Currency c1 = Currency.getInstance(Locale.CANADA);
	Currency c2 = Currency.getInstance(Locale.ITALY);
	int[] billdenominations = {5, 10, 15, 20, 50};
//	BigDecimal[] coindenominations = {new BigDecimal("0"), new BigDecimal("1"), new BigDecimal("2")};
	BigDecimal[] coindenominations = {BigDecimal.ONE};
	
	Barcode barcode = new Barcode(Numeral.eight,Numeral.one,Numeral.two,Numeral.three);
	BarcodedProduct product = new BarcodedProduct(barcode, "Milk", new BigDecimal("20"), 2.5);
	BarcodedUnit unit = new BarcodedUnit(barcode, 2.5);
	
	Barcode barcode2 = new Barcode(Numeral.nine,Numeral.one,Numeral.two,Numeral.three);
	BarcodedProduct product2 = new BarcodedProduct(barcode, "Eggs", new BigDecimal("5"), 2.5);
	BarcodedUnit unit2 = new BarcodedUnit(barcode2, 2.5);
	
	ArrayList<BarcodedProduct> shoppingCart;
	
	Bill bill5 = new Bill(5, c1);
	Bill bill20 = new Bill(20, c1);
	Bill bill50 = new Bill(50, c1);
	Bill bill5DiffCurrency = new Bill(5, c2);

	AttendentStub attendantStub;
	CustomerIO customerStub;
	SelfCheckoutSystemLogic logic;
	
	PrintReceipt printercontroller;

	// Setup: AttendantStub and CustomerStub are created, and two products are added to the product database.
	// SelfCheckoutStation object is created.
	@Before
	public void Setup(){
		scs = new SelfCheckoutStation(c1, billdenominations, coindenominations, 20, 1);

		printercontroller = new PrintReceipt(scs);
		
		
//		printercontroller.registerObserver(controllerObserver1);
//		printercontroller.registerObserver(controllerObserver2);
//		printercontroller.registerObserver(controllerObserver3);
		
		attendantStub = new AttendentStub(printercontroller);
		customerStub = new CustomerIO();
		logic = new SelfCheckoutSystemLogic(scs, customerStub, attendantStub);

		printercontroller.registerAttendent(attendantStub);
		printercontroller.registerCustomerIO(customerStub);
		

		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode, product);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, product2);
		
		shoppingCart = new ArrayList<BarcodedProduct>();
		shoppingCart.add(product);
		shoppingCart.add(product2);

	}
	
	@Test
	public void testValidReceipt() throws EmptyException, OverloadException {
		scs.printer.addInk(1<<9);
		scs.printer.addPaper(1<<9);
		
		boolean expected = true;
		boolean actual = logic.startPrinting(shoppingCart);
		assertEquals("this test case should pass", expected, actual);
	}
	
	@Test
	public void testlowpaper() throws EmptyException, OverloadException {
		scs.printer.addInk(1<<9);
		
		boolean expected = false;
		boolean actual = logic.startPrinting(shoppingCart);
		assertEquals("this test case should pass", expected, actual);
	}
	
	
	@Test
	public void testLowInk() throws EmptyException, OverloadException {
		
	    scs.printer.addPaper(1<<9);
		
		boolean expected = false;
		boolean actual = logic.startPrinting(shoppingCart);
		assertEquals("this test case should pass", expected, actual);
	}

}

