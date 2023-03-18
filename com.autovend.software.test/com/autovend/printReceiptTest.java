package com.autovend;

import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.ReceiptPrinter;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import static com.autovend.software.PrintReceipt.*;


import org.junit.*;
import static org.junit.Assert.assertEquals;


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
    SelfCheckoutStation station_1;

    @Before
    public void setup() throws OverloadException {

        // Create barcodes for purchase items
        Barcode Bar_Milk = new Barcode(Numeral.zero, Numeral.one, Numeral.three, Numeral.five, Numeral.zero);
        Barcode Bar_Cin = new Barcode(Numeral.zero, Numeral.two, Numeral.nine, Numeral.seven, Numeral.zero);
        Barcode Bar_Nat = new Barcode(Numeral.zero, Numeral.three, Numeral.six, Numeral.four, Numeral.zero);

        // Create some products to register into the system for printing of receipt
        BarcodedProduct DairyLand_Milk, Cinnamon_Toast_Crunch, Nature_Valley_Choc;

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

    /**
     * Normal test where there is enough paper and ink to print a completed receipt
     * @throws EmptyException Printer has no more ink
     * @throws OverloadException If the character runs off the line
     */
    @Test
    public void testPrintingOfBillList() throws EmptyException, OverloadException {

        station_1.printer.addInk(5000); // fill up the receipt printer
        station_1.printer.addPaper(500); // Add paper to printer

        String expected = """
                4L DairyLand Milk______01350______$5.79
                Cinnamon Toast Crunch______02970______$6.99
                Nature Valley Chocolate Granola Bars______03640______$6.49
                                
                Total: $19.27""";

        String actual = print(station_1, billList);
        assertEquals(expected, actual);
    }

    /**
     * Tests when the receipt printer runs out of ink during printing
     */
    @Test(expected = EmptyException.class)
    public void testPrintingOutOfInk() throws OverloadException, EmptyException {
        station_1.printer.addInk(1);
        station_1.printer.addPaper(500); // Load with a lot of paper so this can't be what goes empty
        print(station_1, billList);
    }

    /**
     * Tests when the receipt printer runs out of paper during printing
     */
    @Test(expected = EmptyException.class)
    public void testPrintingOutOfPaper() throws OverloadException, EmptyException{
        station_1.printer.addInk(50000); // Fill with a lot of ink so this can't be what goes empty
        station_1.printer.addPaper(1);
        print(station_1, billList);
    }

}
