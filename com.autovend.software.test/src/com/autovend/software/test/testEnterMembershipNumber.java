package com.autovend.software.test;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Member;
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
import com.autovend.software.MemberDatabase;
import com.autovend.software.PrintReceipt;
import com.autovend.software.PrintReceiptObserver;
import com.autovend.software.SelfCheckoutSystemLogic;
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
public class testEnterMembershipNumber {

	SelfCheckoutStation scs;
	Currency c1 = Currency.getInstance(Locale.CANADA);
	Currency c2 = Currency.getInstance(Locale.ITALY);
	int[] billdenominations = {5, 10, 15, 20, 50};
//	BigDecimal[] coindenominations = {new BigDecimal("0"), new BigDecimal("1"), new BigDecimal("2")};
	BigDecimal[] coindenominations = {BigDecimal.ONE};
	
	AttendentStub attendantStub;
	CustomerIO customerStub;
	SelfCheckoutSystemLogic logic;
	
	ArrayList<String> membershipTrys;
	
	
	

	// Setup: AttendantStub and CustomerStub are created, and two products are added to the product database.
	// SelfCheckoutStation object is created.
	@Before
	public void Setup(){
		scs = new SelfCheckoutStation(c1, billdenominations, coindenominations, 20, 1);

		
		customerStub = new CustomerIO();
		logic = new SelfCheckoutSystemLogic(scs, customerStub, attendantStub);
		
		MemberDatabase.MEMBERSHIP_DATABASE.add("123456");
		MemberDatabase.MEMBERSHIP_DATABASE.add("234567");
		MemberDatabase.MEMBERSHIP_DATABASE.add("345678");
		MemberDatabase.MEMBERSHIP_DATABASE.add("456789");
		
		membershipTrys = new ArrayList<String>();
	}
	
	@Test
	public void testValidMembershipOneTry() {
		String number = "123456";
		boolean expected = true;
		boolean actual = logic.takeMembership(number);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testValidMembershipMultipleTries() {
		boolean cancel = false;
		boolean memberAdded = false;
		boolean actual = false;
		membershipTrys.add("654321");
		membershipTrys.add("123456");
		boolean expected = true;
		while (!cancel && !memberAdded) {
			for (String number: membershipTrys) {
				actual = logic.takeMembership(number);
				if (actual == true) {
					memberAdded = true;
					break;
				}
			}
			cancel = true;
		}

		assertEquals(expected, actual);
	}

	

	
	@Test
	public void testInvalidMembershipOneTry() {
		boolean cancel = false;
		boolean memberAdded = false;
		boolean actual = false;
		membershipTrys.add("654321");
		boolean expected = false;
		while (!cancel && !memberAdded) {
			for (String number: membershipTrys) {
				actual = logic.takeMembership(number);
				if (actual == true) {
					memberAdded = true;
					break;
				}
			}
			cancel = true;
		}

		assertEquals(expected, actual);
	}
	
	@Test
	public void testInvalidMembershipMultipleTries() {
		boolean cancel = false;
		boolean memberAdded = false;
		boolean actual = false;
		membershipTrys.add("654321");
		membershipTrys.add("765432");
		membershipTrys.add("876543");
		boolean expected = false;
		while (!cancel && !memberAdded) {
			for (String number: membershipTrys) {
				actual = logic.takeMembership(number);
				if (actual == true) {
					memberAdded = true;
					break;
				}
			}
			cancel = true;
		}

		assertEquals(expected, actual);
	}

}

