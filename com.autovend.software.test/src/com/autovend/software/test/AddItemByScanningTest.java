package com.autovend.software.test;

//Group Members Iteration 1:
//Amandeep Kaur: 30153923
//Winjoy Tiop: 30019557
//Michelle Loi: 30019557
//Ethan Oke: 30142180
//James Hayward: 30149513

import static org.junit.Assert.*;

import com.autovend.devices.SimulationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.autovend.Barcode;
import com.autovend.Numeral;
import com.autovend.devices.AbstractDevice;
import com.autovend.devices.BarcodeScanner;
import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.System;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BarcodeScannerObserver;
import com.autovend.products.BarcodedProduct;
import com.autovend.software.AddItemByScanning;

public class AddItemByScanningTest implements BarcodeScannerObserver{

    private BarcodedProduct Apple;
    private BarcodedProduct Orange;

    private AddItemByScanning Item;
    private AddItemByScanning Item2;

    private Barcode barcode;
    private Barcode barcode2;
    private BarcodeScanner Scanner;
    private SelfCheckoutStation Station;

    List<BarcodedProduct> Itemlist = new ArrayList<BarcodedProduct>();

    //This is a general setup for the test cases.
    @Before
    public void generalSetup() {
        this.barcode = new Barcode(Numeral.five, Numeral.three, Numeral.two, Numeral.eight);
        this.Apple = new BarcodedProduct(barcode, "Apple", new BigDecimal("10.6"), 15.0);

        this.barcode2 = new Barcode(Numeral.two, Numeral.six, Numeral.five, Numeral.nine);
        this.Orange = new BarcodedProduct(barcode2, "Orange", new BigDecimal("19.2"), 23.0);


        this.Scanner = new BarcodeScanner();

        int[] billDenominations = {5, 10 , 15, 20, 50, 100};
        int ScaleMaxWeight = 50;
        int ScaleSens = 10;
        BigDecimal fiveCent = new BigDecimal("0.05");
        BigDecimal tenCent = new BigDecimal("0.10");
        BigDecimal twentyFiveCent = new BigDecimal("0.25");
        BigDecimal loonie = new BigDecimal("1");
        BigDecimal toonie = new BigDecimal("2");
        BigDecimal[] coinDenominations = {fiveCent, tenCent, twentyFiveCent, loonie, toonie};

        this.Station = new SelfCheckoutStation(Currency.getInstance("CAD"), billDenominations, coinDenominations, ScaleMaxWeight, ScaleSens);

        this.Item = new AddItemByScanning(Apple, Station);
        this.Item2 = new AddItemByScanning(Orange, Station);

    }

    //This test checks if a single item has been scanned and added.
    @Test
    public void testAddItemByScanning()
    {
        Scanner.enable();
        AddItemByScanning addItem = new AddItemByScanning(Apple, Station);
        addItem.addBillList(Apple);
        assertEquals(1, addItem.getBillList().size());

    }

    //This test checks if multiple items can be scanned and added to the list.
    @Test
    public void testAddMultipleItems() {

        Scanner.enable();
        AddItemByScanning addItem = new AddItemByScanning(Apple, Station);

        addItem.addBillList(Apple);
        addItem.addBillList(Orange);
        addItem.addBillList(Apple);
        assertEquals(3, addItem.getBillList().size());
    }

    //This test checks if we are able to remove the Item from the bill list.
    @Test
    public void testRemoveItem() {
        Scanner.enable();
        AddItemByScanning addItem = new AddItemByScanning(Apple, Station);
        addItem.addBillList(Apple);
        addItem.addBillList(Orange);
        addItem.removeBillList(1);

        assertEquals(1, addItem.getBillList().size());
    }

    //This test throws an error when an item without a barcode is added to the station.
    @Test(expected = SimulationException.class)
    public void testItemWithNullBarcode() {
        Barcode nullBarcode = null;
        BarcodedProduct nullProduct = new BarcodedProduct(null, "Null Product", new BigDecimal("0"), 0.0);
        AddItemByScanning addItem = new AddItemByScanning(nullProduct, Station);
    }


    // This method doesn't do anything.
    @Test
    public void testreactToEnableEvent() {
        Item.reactToEnabledEvent(Scanner);
    }

    // This method doesn't do anything.
    @Test
    public void testreactToDisabledEvent() {
        Item.reactToDisabledEvent(Scanner);
    }

    // This method doesn't do anything.
    @Test
    public void testReactToBarcodeScannedEvent() {
        Item.reactToBarcodeScannedEvent(Scanner, barcode);
    }


    @Override
    public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reactToBarcodeScannedEvent(BarcodeScanner barcodeScanner, Barcode barcode) {
        // TODO Auto-generated method stub

    }
}

