package com.autovend.software.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
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
@RunWith(Suite.class)
@SuiteClasses({ AddItemByPLUTest.class, AddItemByScanningTest.class, AddOwnBagsTest.class, PayWithCardTest.class, 
		PayWithCashTest.class, PurchaseBagsTest.class, testEnterMembershipNumber.class, testLowInkPaper.class,
		WeightDiscrepancyTest.class })
public class FullTestSuite {

}
