package com.autovend;

import com.autovend.products.BarcodedProduct;
import com.autovend.software.System;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/*
 * Members for Iteration 1:
 * Michelle Loi (30019557)
 * James Hayward (30149513)
 * Caleb Cavilla ()
 * Amandeep Kaur (30153923)
 * Ethan Oke (30142180)
 * Winjoy Tiop (30069663)
 */


/**
 * Test the print Receipt class in software
 */
public class printReceiptTest {

    // Create some products to register into the system for printing of receipt
    private BarcodedProduct DairyLand_Milk, Cinnamon_Toast_Crunch, Nature_Valley_Choc;
    private Barcode Bar_Milk, Bar_Cin, Bar_Nat;

    private System Station_1;

    @Before
    public void setup(){

        Bar_Milk = new Barcode(Numeral.zero, Numeral.one, Numeral.three, Numeral.five, Numeral.zero);
        Bar_Cin = new Barcode(Numeral.zero, Numeral.two, Numeral.nine, Numeral.seven, Numeral.zero);
        Bar_Nat = new Barcode(Numeral.zero, Numeral.three, Numeral.six, Numeral.four, Numeral.zero);

        DairyLand_Milk = new BarcodedProduct(Bar_Milk, "4L Jug of Milk from DairyLand",
                new BigDecimal("5.79"), 1);

        Cinnamon_Toast_Crunch = new BarcodedProduct(Bar_Cin, "Box of Cinnamon Toast Crunch",
                new BigDecimal("6.99"), 0.2);

        Nature_Valley_Choc = new BarcodedProduct(Bar_Nat, "Box of Nature Valley chocolate granola bars",
                new BigDecimal("6.49"), 0.5);

        //System.addBillList(DairyLand_Milk); Cannot access addBillList?

    }

    @After
    public void teardown(){

    }

    @Test
    public void test1(){

    }
}
