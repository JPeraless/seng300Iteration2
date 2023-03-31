package com.autovend.software.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AddItemByPLUTest.class, AddItemByScanningTest.class, AddOwnBagsTest.class, PayWithCardTest.class,
		PayWithCashTest.class, PurchaseBagsTest.class, testEnterMembershipNumber.class, testLowInkPaper.class,
		WeightDiscrepancyTest.class })
public class FullTestSuite {

}
