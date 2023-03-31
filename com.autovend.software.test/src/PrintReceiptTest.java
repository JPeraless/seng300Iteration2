

import com.autovend.devices.EmptyException;
import com.autovend.devices.OverloadException;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.products.BarcodedProduct;
import com.autovend.Barcode;
import com.autovend.Numeral;

import static com.autovend.software.PrintReceipt.*;
import static org.junit.Assert.*;


import com.autovend.software.Attendant;
import com.autovend.software.CustomerIO;
import com.autovend.software.PrintReceipt;
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
 * Caleb Cavilla (30145972)
 * Amandeep Kaur (30153923)
 * Ethan Oke (30142180)
 * Winjoy Tiop (30069663)
 */


/**
 * Test the print Receipt class in software
 */
public class PrintReceiptTest {

    private static final int MAX_INK = 1048576;
    private static final int MAX_Paper = 1024;

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

        station_1.printer.addInk(MAX_INK); // fill up the receipt printer
        station_1.printer.addPaper(MAX_Paper); // Add paper to printer

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
        station_1.printer.addPaper(MAX_Paper); // Load with a lot of paper so this can't be what goes empty
        print(station_1, billList);
    }

    // Tests when the receipt printer runs out of paper during printing
    @Test(expected = EmptyException.class)
    public void testPrintingOutOfPaper() throws OverloadException, EmptyException{
        station_1.printer.addInk(MAX_INK); // Fill with a lot of ink so this can't be what goes empty
        station_1.printer.addPaper(1);
        print(billList);
    }

    // Test that a new line is written if the line is too long and the remainder goes to the next line
    // There is a bug where charactersOnCurrentLine does not seem to be incrementing unless there is a \n
    // So the very long line counts as 1 character, an overload exception is never thrown, and it never
    // makes new lines
    @Test
    public void testLongLine() throws OverloadException, EmptyException {
        station_1.printer.addInk(MAX_INK); // fill up the receipt printer
        station_1.printer.addPaper(MAX_Paper); // Add paper to printer

        Barcode bar = new Barcode(Numeral.zero, Numeral.one, Numeral.three, Numeral.five, Numeral.zero);
        BarcodedProduct longLine = new BarcodedProduct(bar,
                "LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_" +
                        "LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_" +
                        "LINE_LONG_LINE_LONG_LINE_LONG_LINE",
                new BigDecimal("9999.99"), 25);

        billList.add(longLine);
        print(station_1, billList);

        String expected = """
                4L DairyLand Milk      01350      $5.79
                Cinnamon Toast Crunch      02970      $6.99
                Nature Valley Chocolate Granola Bars      03640      $6.49
                LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_
                LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_
                LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE_LONG_LINE""";

       assertEquals(expected, station_1.printer.removeReceipt());
    }

    // Tests if attendant gets flagged when out of ink
    @Test
    public void flagAttendantInk() throws OverloadException {
        CustomerIO CIO = new CustomerIO();
        Attendant AT = new Attendant();

        station_1.printer.addInk(0); // Out of ink
        station_1.printer.addPaper(MAX_Paper);
        System sys = new System(station_1, AT, CIO);
        sys.setPrinting(true); // Signal system to start printing
        sys.addBillList(DairyLand_Milk); sys.addBillList(Cinnamon_Toast_Crunch); sys.addBillList(Nature_Valley_Choc);
        sys.startPrinting();

        assertTrue(AT.isInformed()); // Attendant should be informed of out of ink event
    }

    // Tests if attendant gets flagged when out of paper
    @Test
    public void flagAttendantPaper() throws OverloadException {
        CustomerIO CIO = new CustomerIO();
        Attendant AT = new Attendant();

        station_1.printer.addInk(MAX_INK); // Fill with a lot of ink so this can't be what goes empty
        station_1.printer.addPaper(0);
        System sys = new System(station_1, AT, CIO);
        sys.setPrinting(true); // Signal system to start printing
        sys.addBillList(DairyLand_Milk); sys.addBillList(Cinnamon_Toast_Crunch); sys.addBillList(Nature_Valley_Choc);
        sys.startPrinting();

        assertTrue(AT.isInformed()); // Attendant should be informed of out of paper event
    }

    // Tests if the station is unblocked once ink is filled
    @Test
    public void fillInk() throws OverloadException {
        CustomerIO CIO = new CustomerIO();
        Attendant AT = new Attendant();

        station_1.printer.addInk(0); // Out of ink
        station_1.printer.addPaper(MAX_Paper);
        System sys = new System(station_1, AT, CIO);
        sys.setPrinting(true);
        sys.addBillList(DairyLand_Milk); sys.addBillList(Cinnamon_Toast_Crunch); sys.addBillList(Nature_Valley_Choc);
        sys.startPrinting();
        if(AT.isInformed()){
            AT.fillInk(sys.getStation().printer, MAX_INK);
        }

        assertFalse(AT.isInformed()); // Attendant should no longer be informed after they fill the ink
    }

    // Tests if the station is unblocked once paper is filled
    @Test
    public void fillPaper() throws OverloadException{
        CustomerIO CIO = new CustomerIO();
        Attendant AT = new Attendant();

        station_1.printer.addInk(MAX_INK); // Out of ink
        station_1.printer.addPaper(0);
        System sys = new System(station_1, AT, CIO);
        sys.setPrinting(true);
        sys.addBillList(DairyLand_Milk); sys.addBillList(Cinnamon_Toast_Crunch); sys.addBillList(Nature_Valley_Choc);
        sys.startPrinting();
        if(AT.isInformed()){
            AT.fillPaper(sys.getStation().printer, MAX_Paper);
        }

        assertFalse(AT.isInformed()); // Attendant should no longer be informed after they fill the ink
    }

    // Test flagging of customer
    @Test
    public void testThankCustomer() throws OverloadException {
        CustomerIO CIO = new CustomerIO();
        Attendant AT = new Attendant();
        station_1.printer.addInk(MAX_INK); // fill up the receipt printer
        station_1.printer.addPaper(MAX_Paper); // Add paper to printer
        System sys = new System(station_1, AT, CIO);
        sys.setPrinting(true);
        sys.addBillList(DairyLand_Milk); sys.addBillList(Cinnamon_Toast_Crunch); sys.addBillList(Nature_Valley_Choc);
        sys.startPrinting();

        assertTrue(CIO.getThanks()); // Customer should have been thanked
        assertEquals("Thank you for shopping with us today!", CIO.thankCustomer());
    }

    // Test if customer is not flagged then, nothing should be printed to the screen
    @Test
    public void dontThankCustomer() {
        CustomerIO CIO = new CustomerIO();
        Attendant AT = new Attendant();
        System sys = new System(station_1, AT, CIO);
        // Don't fill system so that empty exception occurs, receipt not printer and customer
        // will not be thanked

        sys.setPrinting(true);
        sys.addBillList(DairyLand_Milk); sys.addBillList(Cinnamon_Toast_Crunch); sys.addBillList(Nature_Valley_Choc);
        sys.startPrinting();

        assertFalse(CIO.getThanks());
        assertEquals("", CIO.thankCustomer()); // Nothing displayed to customer
    }

    // Test attendant once flagged will print a new receipt
    @Test
    public void attendantPrints() throws OverloadException {
        // Make empty system
        CustomerIO CIO = new CustomerIO();
        Attendant AT = new Attendant();
        System sys = new System(station_1, AT, CIO);

        // Try to print should notify attendant
        sys.setPrinting(true);
        sys.addBillList(DairyLand_Milk); sys.addBillList(Cinnamon_Toast_Crunch); sys.addBillList(Nature_Valley_Choc);
        sys.startPrinting();

        // AT fills printer
        if(AT.isInformed()){
            AT.fillPaper(sys.getStation().printer, MAX_Paper);
            AT.fillInk(sys.getStation().printer, MAX_INK);
            AT.invokeReceiptPrinter(station_1, billList);
        }

        String expected = """
                4L DairyLand Milk      01350      $5.79
                Cinnamon Toast Crunch      02970      $6.99
                Nature Valley Chocolate Granola Bars      03640      $6.49
                                
                Total: $19.27""";

        assertEquals(expected, sys.getStation().printer.removeReceipt()); // Attendant successfully fixes receipt printing
        assertFalse(AT.isInformed()); // Attendant no longer informed
    }

    // Attendant tries and fails to print receipt should still be informed
    @Test
    public void attendantCantPrint() throws OverloadException {
        // Make empty system
        CustomerIO CIO = new CustomerIO();
        Attendant AT = new Attendant();
        System sys = new System(station_1, AT, CIO);

        // Try to print should notify attendant
        sys.setPrinting(true);
        sys.addBillList(DairyLand_Milk); sys.addBillList(Cinnamon_Toast_Crunch); sys.addBillList(Nature_Valley_Choc);
        sys.startPrinting();

        if(AT.isInformed()){ // because attendant usually should be informed before they invoke printing receipt
            AT.invokeReceiptPrinter(station_1, billList); // do not fill so another error can occur
        }

        assertNull(sys.getStation().printer.removeReceipt()); // There should be no receipt because of exception
        assertTrue(AT.isInformed()); // Attendant should be informed, so they can continue to try and fix
    }


    // Tests setReady setter in CustomerIO
    @Test
    public void testSetReady() {
        CustomerIO CIO = new CustomerIO();
        // Setter should have set ready field to true
        CIO.setReady(true);
        assertTrue(CIO.getReady());
    }

    // Dummy Tests for Events to see if they can be triggered
    @Test
    public void dummyReactEnabled(){
        PrintReceipt pr = new PrintReceipt();
        pr.reactToEnabledEvent(station_1.printer);
    }

    @Test
    public void dummyReactDisabled(){
        PrintReceipt pr = new PrintReceipt();
        pr.reactToDisabledEvent(station_1.printer);
    }

    @Test
    public void dummyReactOutPaper(){
        PrintReceipt pr = new PrintReceipt();
        pr.reactToOutOfPaperEvent(station_1.printer);
    }

    @Test
    public void dummyReactOutInk(){
        PrintReceipt pr = new PrintReceipt();
        pr.reactToOutOfInkEvent(station_1.printer);
    }


    @Test
    public void dummyReactPaperAdd(){
        PrintReceipt pr = new PrintReceipt();
        pr.reactToPaperAddedEvent(station_1.printer);
    }

    @Test
    public void dummyReactInkAdd(){
        PrintReceipt pr = new PrintReceipt();
        pr.reactToInkAddedEvent(station_1.printer);
    }
}
