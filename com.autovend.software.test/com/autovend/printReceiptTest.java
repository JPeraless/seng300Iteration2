package com.autovend;

import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import static com.autovend.software.PrintReceipt.*;
import static org.junit.Assert.*;


import com.autovend.software.Attendant;
import com.autovend.software.CustomerIO;
import com.autovend.software.System;
import org.junit.*;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

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
    private List<BarcodedProduct> billList;
    private SelfCheckoutStation station_1;

    // Create some products to register into the system for printing of receipt
    private BarcodedProduct DairyLand_Milk, Cinnamon_Toast_Crunch, Nature_Valley_Choc;


    @Before
    public void setup() throws OverloadException {

        // Create barcodes for purchase items
        Barcode Bar_Milk = new Barcode(Numeral.zero, Numeral.one, Numeral.three, Numeral.five, Numeral.zero);
        Barcode Bar_Cin = new Barcode(Numeral.zero, Numeral.two, Numeral.nine, Numeral.seven, Numeral.zero);
        Barcode Bar_Nat = new Barcode(Numeral.zero, Numeral.three, Numeral.six, Numeral.four, Numeral.zero);

        // Create purchase items
        DairyLand_Milk = new BarcodedProduct(Bar_Milk, "4L DairyLand Milk",
                new BigDecimal("5.79"), 1);

        Cinnamon_Toast_Crunch = new BarcodedProduct(Bar_Cin, "Cinnamon Toast Crunch",
                new BigDecimal("6.99"), 0.2);

        Nature_Valley_Choc = new BarcodedProduct(Bar_Nat, "Nature Valley Chocolate Granola Bars",
                new BigDecimal("6.49"), 0.5);

        // Initialize bill list
        billList = new ArrayList<>();
        // Add item into bill list
        billList.add(DairyLand_Milk);
        billList.add(Cinnamon_Toast_Crunch);
        billList.add(Nature_Valley_Choc);

        // Setup and create station
        int[] billDenominations = {5, 10 , 15, 20, 50, 100};
        BigDecimal fiveCent = new BigDecimal("0.05");
        BigDecimal tenCent = new BigDecimal("0.10");
        BigDecimal twentyFiveCent = new BigDecimal("0.25");
        BigDecimal loonie = new BigDecimal("1");
        BigDecimal toonie = new BigDecimal("2");
        BigDecimal[] coinDenominations = {fiveCent, tenCent, twentyFiveCent, loonie, toonie};
        int scaleMaximumWeight = 50;
        int scaleSensitivity = 10;
        station_1 = new SelfCheckoutStation(Currency.getInstance("CAD"),
                billDenominations, coinDenominations, scaleMaximumWeight, scaleSensitivity);
    }

    @After
    public void teardown(){
        billList = null;
        station_1 = null;
    }

    //Normal test where there is enough paper and ink to print a completed receipt
    @Test
    public void testPrintingOfBillList() throws EmptyException, OverloadException {

        station_1.printer.addInk(5000); // fill up the receipt printer
        station_1.printer.addPaper(500); // Add paper to printer

        String expected = """
                4L DairyLand Milk      01350      $5.79
                Cinnamon Toast Crunch      02970      $6.99
                Nature Valley Chocolate Granola Bars      03640      $6.49
                                
                Total: $19.27""";

        print(station_1, billList); // print the receipt
        String actual = station_1.printer.removeReceipt(); // Simulate customer removing receipt
        assertEquals(expected, actual);
    }

    //Tests when the receipt printer runs out of ink during printing
    @Test(expected = EmptyException.class)
    public void testPrintingOutOfInk() throws OverloadException, EmptyException {
        station_1.printer.addInk(1);
        station_1.printer.addPaper(500); // Load with a lot of paper so this can't be what goes empty
        print(station_1, billList);
    }

    // Tests when the receipt printer runs out of paper during printing
    @Test(expected = EmptyException.class)
    public void testPrintingOutOfPaper() throws OverloadException, EmptyException{
        station_1.printer.addInk(5000); // Fill with a lot of ink so this can't be what goes empty
        station_1.printer.addPaper(1);
        print(station_1, billList);
    }

    // Test that a new line is written if the line is too long and the remainder goes to the next line
    @Test
    public void testLongLine() throws OverloadException, EmptyException {
        station_1.printer.addInk(5000); // fill up the receipt printer
        station_1.printer.addPaper(500); // Add paper to printer

        Barcode bar = new Barcode(Numeral.zero, Numeral.one, Numeral.three, Numeral.five, Numeral.zero);
        BarcodedProduct longLine = new BarcodedProduct(bar,
                "LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_" +
                        "LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE",
                new BigDecimal("9999.99"), 25);

        billList.add(longLine);
        print(station_1, billList);

       assertEquals(station_1.printer.removeReceipt(), "");

    }

    // Tests if attendant gets flagged when out of ink
    @Test
    public void flagAttendantInk() throws OverloadException {
        CustomerIO CIO = new CustomerIO();
        Attendant AT = new Attendant();

        station_1.printer.addInk(1); // Out of ink
        station_1.printer.addPaper(50);
        System sys = new System(station_1, AT, CIO);
        sys.setPrinting(true);
        sys.addBillList(DairyLand_Milk); sys.addBillList(Cinnamon_Toast_Crunch); sys.addBillList(Nature_Valley_Choc);
        sys.startPrinting();

        assertTrue(AT.isInformed());
    }

    // Tests if attendant gets flagged when out of paper
    @Test
    public void flagAttendantPaper() throws OverloadException {
        CustomerIO CIO = new CustomerIO();
        Attendant AT = new Attendant();

        station_1.printer.addInk(5000); // Fill with a lot of ink so this can't be what goes empty
        station_1.printer.addPaper(1);
        System sys = new System(station_1, AT, CIO);
        sys.setPrinting(true);
        sys.addBillList(DairyLand_Milk); sys.addBillList(Cinnamon_Toast_Crunch); sys.addBillList(Nature_Valley_Choc);
        sys.startPrinting();

        assertTrue(AT.isInformed());
    }

    // Tests once attendant gets flagged they fill ink and paper
    @Test
    public void fillInkOrPaper() throws OverloadException {
        CustomerIO CIO = new CustomerIO();
        Attendant AT = new Attendant();

        station_1.printer.addInk(1); // Out of ink
        station_1.printer.addPaper(50);
        System sys = new System(station_1, AT, CIO);
        sys.setPrinting(true);
        sys.addBillList(DairyLand_Milk); sys.addBillList(Cinnamon_Toast_Crunch); sys.addBillList(Nature_Valley_Choc);
        sys.startPrinting();
        if(AT.isInformed()){
            AT.fill(sys.getStation().printer);
        }

        assertFalse(AT.isInformed());
    }

    @Test
    public void testThankCustomer() throws OverloadException {
        CustomerIO CIO = new CustomerIO();
        Attendant AT = new Attendant();
        station_1.printer.addInk(5000); // fill up the receipt printer
        station_1.printer.addPaper(500); // Add paper to printer
        System sys = new System(station_1, AT, CIO);
        sys.setPrinting(true);
        sys.addBillList(DairyLand_Milk); sys.addBillList(Cinnamon_Toast_Crunch); sys.addBillList(Nature_Valley_Choc);
        sys.startPrinting();

        assertTrue(CIO.getThanks());;

    }

}
