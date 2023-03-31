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
	
		//tests a valid membership attempt
	@Test
	public void testValidMembershipOneTry() {
		String number = "123456";
		boolean expected = true;
		boolean actual = logic.takeMembership(number);
		assertEquals(expected, actual);
	}
	
	//tests a valid membership attempt after multiple incorrect attempts
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

	

	//tests an invalid attempt in one try
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
	
	//tests an invalid attempt for multiple tries, then cancel
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

